package com.PJDM.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.PJDM.common.FkValidator;
import com.PJDM.dto.AuditDTO;
import com.PJDM.dto.IdentityQueryDTO;
import com.PJDM.dto.RealNameAuthDTO;
import com.PJDM.exception.BusinessException;
import com.PJDM.exception.ResourceNotFoundException;
import com.PJDM.exception.ValidationException;
import com.PJDM.mapper.UserIdentityAuthMapper;
import com.PJDM.mapper.UserUserMapper;
import com.PJDM.pojo.UserIdentityAuth;
import com.PJDM.service.IUserIdentityAuthService;
import com.PJDM.untils.RedisCache;
import com.PJDM.vo.IdentityListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * 实名认证 Service 实现
 *
 * @author Alanine
 */
@Slf4j
@Service
public class UserIdentityAuthServiceImpl extends ServiceImpl<UserIdentityAuthMapper, UserIdentityAuth>
        implements IUserIdentityAuthService {

    @Value("${user-module.page.default-size:10}")
    private int defaultPageSize;
    @Value("${user-module.page.max-size:100}")
    private int maxPageSize;
    @Value("${user-module.security.id-card-mask-start:3}")
    private int maskStart;
    @Value("${user-module.security.id-card-mask-end:3}")
    private int maskEnd;

    @Autowired
    private FkValidator fkValidator;
    @Autowired
    private UserUserMapper userUserMapper;
    @Autowired
    private RedisCache redisCache;

    /**
     * 患者端提交实名认证
     * 1. 校验短信验证码（与 Redis key "sms:code:{phone}" 比对）
     * 2. 单表插入 user_identity_auth
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitRealNameAuth(RealNameAuthDTO dto) {
        // 基础参数校验
        if (!StringUtils.hasText(dto.getRealName()))
            throw new ValidationException("realName", "真实姓名不能为空");
        if (!StringUtils.hasText(dto.getPhone()))
            throw new ValidationException("phone", "手机号不能为空");
        if (!StringUtils.hasText(dto.getVerificationCode()))
            throw new ValidationException("verificationCode", "验证码不能为空");
        if (dto.getAuthType() == null)
            throw new ValidationException("authType", "认证类型不能为空");
        if (dto.getUserId() == null)
            throw new ValidationException("userId", "用户ID不能为空");

        // 校验验证码（key 与 AuthController.sendCode 存入 Redis 时保持一致）
        String redisKey = "sms:code:" + dto.getPhone();
        String cachedCode = redisCache.getCacheObject(redisKey);
        if (!StringUtils.hasText(cachedCode)) {
            throw new BusinessException("验证码已过期，请重新获取");
        }
        if (!cachedCode.trim().equals(dto.getVerificationCode().trim())) {
            throw new BusinessException("验证码错误");
        }
        // 验证通过后删除，防止重复使用
        redisCache.deleteObject(redisKey);

        // 检查是否已有待审核或已通过的认证
        long existCount = count(new LambdaQueryWrapper<UserIdentityAuth>()
                .eq(UserIdentityAuth::getUserId, dto.getUserId())
                .in(UserIdentityAuth::getAuthStatus, (byte) 1, (byte) 2));
        if (existCount > 0) {
            throw new BusinessException("您已提交过实名认证，请勿重复提交");
        }

        // 构建实体并单表插入
        UserIdentityAuth auth = new UserIdentityAuth();
        auth.setUserId(dto.getUserId());
        auth.setAuthType(dto.getAuthType());
        auth.setRealName(dto.getRealName());
        auth.setAuthStatus((byte) 1); // 待审核
        auth.setCreateTime(LocalDateTime.now());
        auth.setCreateBy(String.valueOf(dto.getUserId()));

        if (dto.getAuthType() != null && dto.getAuthType() == 2) {
            // 身份证
            auth.setIdCardNumber(dto.getIdCardNumber());
            auth.setIdCardPhotoFront(dto.getPhotoFront());
            auth.setIdCardPhotoBack(dto.getPhotoBack());
        } else {
            // 护照
            auth.setPassportNumber(dto.getPassportNumber());
            auth.setPassportPhotoFront(dto.getPhotoFront());
            auth.setPassportPhotoBack(dto.getPhotoBack());
        }

        // 补充信息存入 remark
        auth.setRemark(buildRemark(dto));

        save(auth);
        log.info("[实名认证] 用户 {} 提交认证成功，authType={}", dto.getUserId(), dto.getAuthType());
    }

    /** 将 step1 补充字段序列化到 remark */
    private String buildRemark(RealNameAuthDTO dto) {
        return String.format(
                "gender=%s;birthDate=%s;address=%s;phone=%s;bankCard=%s;bankType=%s;emergencyContact=%s;emergencyPhone=%s",
                nvl(dto.getGender()), nvl(dto.getBirthDate()), nvl(dto.getAddress()),
                nvl(dto.getPhone()), nvl(dto.getBankCard()), nvl(dto.getBankType()),
                nvl(dto.getEmergencyContact()), nvl(dto.getEmergencyPhone()));
    }

    private String nvl(String s) {
        return s == null ? "" : s;
    }

    @Override
    public IPage<IdentityListVO> getIdentityListPage(IdentityQueryDTO query) {
        int size = Math.min(query.getPageSize() == null ? defaultPageSize : query.getPageSize(), maxPageSize);
        IPage<IdentityListVO> result = baseMapper.selectIdentityListPage(
                new Page<>(query.getPageNum() == null ? 1 : query.getPageNum(), size), query);
        // 列表脱敏
        result.getRecords().forEach(vo -> {
            vo.setIdCardNumber(maskSensitive(vo.getIdCardNumber()));
            vo.setPassportNumber(maskSensitive(vo.getPassportNumber()));
        });
        return result;
    }

    @Override
    public UserIdentityAuth getIdentityDetail(Long id) {
        UserIdentityAuth auth = getById(id);
        if (auth == null) throw new ResourceNotFoundException("认证记录", id);
        return auth;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditIdentity(Long id, AuditDTO dto) {
        if (dto.getStatus() == null)
            throw new ValidationException("status", "审核状态不能为空");
        if (dto.getStatus() == 3 && !StringUtils.hasText(dto.getFailReason()))
            throw new ValidationException("failReason", "拒绝时必须填写原因");

        UserIdentityAuth exist = getById(id);
        if (exist == null) throw new ResourceNotFoundException("认证记录", id);

        fkValidator.requireExists(userUserMapper, exist.getUserId(), "关联用户");

        if (exist.getAuthStatus() != 1) {
            throw new BusinessException(String.format(
                    "认证记录 [%d] 当前状态为 %s，不可重复审核",
                    id, authStatusDesc(exist.getAuthStatus())));
        }
        if (dto.getStatus() != 2 && dto.getStatus() != 3) {
            throw new ValidationException("status", "审核结果只能是 2(通过) 或 3(拒绝)");
        }

        UserIdentityAuth update = new UserIdentityAuth();
        update.setId(id);
        update.setAuthStatus(dto.getStatus());
        update.setAuthFailReason(dto.getFailReason());
        if (dto.getStatus() == 2) update.setAuthTime(LocalDateTime.now());
        updateById(update);
        log.info("审核实名认证 [{}] userId={} 结果: {}",
                id, exist.getUserId(), dto.getStatus() == 2 ? "通过" : "拒绝");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteIdentity(Long id) {
        UserIdentityAuth exist = getById(id);
        if (exist == null) throw new ResourceNotFoundException("认证记录", id);

        if (exist.getAuthStatus() == 2) {
            throw new BusinessException("已通过的实名认证不允许直接删除，请联系管理员");
        }
        removeById(id);
        log.info("删除认证记录 [{}] userId={} 成功", id, exist.getUserId());
    }

    private String authStatusDesc(byte status) {
        return switch (status) {
            case 1 -> "待审核";
            case 2 -> "已通过";
            case 3 -> "已拒绝";
            default -> "未知(" + status + ")";
        };
    }

    private String maskSensitive(String value) {
        if (!StringUtils.hasText(value)) return value;
        int len = value.length();
        if (len <= maskStart + maskEnd) return "***";
        return value.substring(0, maskStart) + "***" + value.substring(len - maskEnd);
    }
}
