package com.PJDM.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.PJDM.common.FkValidator;
import com.PJDM.dto.AuditDTO;
import com.PJDM.dto.IdentityQueryDTO;
import com.PJDM.exception.BusinessException;
import com.PJDM.exception.ResourceNotFoundException;
import com.PJDM.exception.ValidationException;
import com.PJDM.mapper.UserIdentityAuthMapper;
import com.PJDM.mapper.UserUserMapper;
import com.PJDM.pojo.UserIdentityAuth;
import com.PJDM.service.IUserIdentityAuthService;
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
 * 外键约束由 FkValidator 保障：
 * - user_id → user_user.id（唯一，每用户只能一条）
 * - 审核状态流转合法性
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

        // FK: 校验关联用户还存在
        fkValidator.requireExists(userUserMapper, exist.getUserId(), "关联用户");

        // 审核状态流转：只有待审核(1)才能审核，已通过/已拒绝不可重复审核
        if (exist.getAuthStatus() != 1) {
            throw new BusinessException(String.format(
                    "认证记录 [%d] 当前状态为 %s，不可重复审核",
                    id, authStatusDesc(exist.getAuthStatus())));
        }
        // 审核结果只能是：通过(2) 或 拒绝(3)
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

        // 已通过的认证不允许直接删除，需先撤销
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
