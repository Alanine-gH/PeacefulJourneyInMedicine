package com.PJDM.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.PJDM.common.FkValidator;
import com.PJDM.dto.AccompanistQueryDTO;
import com.PJDM.dto.AccompanistUpdateDTO;
import com.PJDM.dto.AuditDTO;
import com.PJDM.exception.BusinessException;
import com.PJDM.exception.DuplicateEntryException;
import com.PJDM.exception.ResourceNotFoundException;
import com.PJDM.exception.ValidationException;
import com.PJDM.mapper.OrderOrderMapper;
import com.PJDM.mapper.UserAccompanistMapper;
import com.PJDM.mapper.UserUserMapper;
import com.PJDM.pojo.OrderOrder;
import com.PJDM.pojo.UserAccompanist;
import com.PJDM.pojo.UserUser;
import com.PJDM.service.IUserAccompanistService;
import com.PJDM.vo.AccompanistListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * 陪诊师资质 Service 实现
 *
 * @author Alanine
 */
@Slf4j
@Service
public class UserAccompanistServiceImpl extends ServiceImpl<UserAccompanistMapper, UserAccompanist>
        implements IUserAccompanistService {

    @Value("${user-module.page.default-size:10}")
    private int defaultPageSize;
    @Value("${user-module.page.max-size:100}")
    private int maxPageSize;

    @Autowired
    private FkValidator fkValidator;
    @Autowired
    private UserUserMapper userUserMapper;
    @Autowired
    private OrderOrderMapper orderOrderMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserAccompanistMapper userAccompanistMapper;

    @Override
    public IPage<AccompanistListVO> getAccompanistListPage(AccompanistQueryDTO query) {
        int size = Math.min(query.getPageSize() == null ? defaultPageSize : query.getPageSize(), maxPageSize);
        return baseMapper.selectAccompanistListPage(
                new Page<>(query.getPageNum() == null ? 1 : query.getPageNum(), size), query);
    }

    @Override
    public UserAccompanist getAccompanistDetail(Long id) {
        UserAccompanist userAccompanist = userAccompanistMapper.selectById(id);
//        UserAccompanist acc = getById(id);
        System.out.println(userAccompanist);
        if (userAccompanist == null) throw new ResourceNotFoundException("陪诊师", id);
        return userAccompanist;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addAccompanist(AccompanistUpdateDTO dto) {
        Long userId = dto.getUserId();

        if (userId == null) {
            // 没有 userId，自动创建 user_user 账号
            if (!StringUtils.hasText(dto.getUsername()))
                throw new ValidationException("username", "新增陪诊师时必须填写用户名或提供已有用户ID");
            if (!StringUtils.hasText(dto.getPassword()))
                throw new ValidationException("password", "新增陪诊师时必须填写密码");
            if (userUserMapper.selectCount(new LambdaQueryWrapper<UserUser>()
                    .eq(UserUser::getUsername, dto.getUsername())) > 0) {
                throw new DuplicateEntryException("username", dto.getUsername());
            }
            UserUser user = new UserUser();
            user.setUsername(dto.getUsername());
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            user.setNickName(StringUtils.hasText(dto.getRealName()) ? dto.getRealName() : dto.getUsername());
            user.setRealName(dto.getRealName());
            user.setPhone(dto.getPhone());
            user.setGender(dto.getGender());
            user.setAge(dto.getAge());
            user.setUserType((byte) 2);
            user.setStatus((byte) 1);
            user.setCreateBy("admin");
            user.setCreateTime(LocalDateTime.now());
            userUserMapper.insert(user);
            userId = user.getId();
            log.info("自动创建陪诊师账号 username=[{}] userId=[{}]", dto.getUsername(), userId);
        } else {
            // 提供了 userId，校验用户存在且类型正确
            fkValidator.requireExistsNotNull(userUserMapper, userId, "关联用户");
            UserUser user = userUserMapper.selectById(userId);
            if (user.getUserType() == null || user.getUserType() != 2) {
                // 自动更正 user_type 为陪诊师
                UserUser update = new UserUser();
                update.setId(userId);
                update.setNickName(StringUtils.hasText(dto.getRealName()) ? dto.getRealName() : dto.getUsername());
                update.setRealName(dto.getRealName());
                update.setPhone(dto.getPhone());
                update.setGender(dto.getGender());
                update.setAge(dto.getAge());
                update.setUserType((byte) 2);
                userUserMapper.updateById(update);
            }
        }

        // 唯一性：每个用户只能有一条陪诊师记录
        if (count(new LambdaQueryWrapper<UserAccompanist>()
                .eq(UserAccompanist::getUserId, userId)) > 0) {
            throw new DuplicateEntryException("userId", String.valueOf(userId));
        }

        UserAccompanist acc = new UserAccompanist();
        acc.setUserId(userId);
        acc.setRealName(dto.getRealName());
        acc.setPhone(dto.getPhone());
        acc.setGender(dto.getGender());
        acc.setAge(dto.getAge());
        acc.setProfessionalTitle(dto.getProfessionalTitle());
        acc.setMedicalBackground(dto.getMedicalBackground());
        acc.setCertificateNumber(dto.getCertificateNumber());
        acc.setLanguageAbility(dto.getLanguageAbility());
        acc.setSpecialties(dto.getSpecialties());
        acc.setAccompanyStatus(dto.getAccompanyStatus() != null ? dto.getAccompanyStatus() : (byte) 1);
        acc.setAuditStatus((byte) 1);
        acc.setServiceCount(0);
        acc.setComplaintCount(0);
        acc.setCertificatePhoto(dto.getCertificatePhoto());
        acc.setRemark(dto.getRemark());
        save(acc);
        log.info("新增陪诊师资质 userId=[{}] 成功", userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAccompanist(Long id, AccompanistUpdateDTO dto) {
        if (getById(id) == null) throw new ResourceNotFoundException("陪诊师", id);
        UserAccompanist update = new UserAccompanist();
        update.setId(id);
        update.setRealName(dto.getRealName());
        update.setPhone(dto.getPhone());
        update.setGender(dto.getGender());
        update.setAge(dto.getAge());
        update.setProfessionalTitle(dto.getProfessionalTitle());
        update.setMedicalBackground(dto.getMedicalBackground());
        update.setCertificateNumber(dto.getCertificateNumber());
        update.setLanguageAbility(dto.getLanguageAbility());
        update.setSpecialties(dto.getSpecialties());
        update.setAccompanyStatus(dto.getAccompanyStatus());
        update.setCertificatePhoto(dto.getCertificatePhoto());
        update.setRemark(dto.getRemark());
        updateById(update);
        // 同步更新 user_user 的姓名/手机/性别/年龄
        UserAccompanist acc = getById(id);
        if (acc != null && acc.getUserId() != null) {
            UserUser userUpdate = new UserUser();
            userUpdate.setId(acc.getUserId());
            userUpdate.setRealName(dto.getRealName());
            userUpdate.setPhone(dto.getPhone());
            userUpdate.setGender(dto.getGender());
            userUpdate.setAge(dto.getAge());
            userUserMapper.updateById(userUpdate);
        }
        log.info("修改陪诊师 [{}] 成功", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAccompanist(Long id) {
        UserAccompanist acc = getById(id);
        if (acc == null) throw new ResourceNotFoundException("陪诊师", id);

        long activeOrders = orderOrderMapper.selectCount(
                new LambdaQueryWrapper<OrderOrder>()
                        .eq(OrderOrder::getAccompanistId, id)
                        .in(OrderOrder::getOrderStatus, 1, 2, 3, 4));
        fkValidator.restrictDelete(activeOrders, "陪诊师", "进行中的订单");

        removeById(id);
        log.info("删除陪诊师 [{}] 成功", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditAccompanist(Long id, AuditDTO dto) {
        if (dto.getStatus() == null) throw new ValidationException("status", "审核状态不能为空");
        if (dto.getStatus() == 3 && !StringUtils.hasText(dto.getFailReason()))
            throw new ValidationException("failReason", "拒绝时必须填写原因");

        UserAccompanist exist = getById(id);
        if (exist == null) throw new ResourceNotFoundException("陪诊师", id);

        if (exist.getAuditStatus() != 1) {
            throw new BusinessException(String.format(
                    "陪诊师 [%d] 当前审核状态为 %s，无法重复审核",
                    id, auditStatusDesc(exist.getAuditStatus())));
        }

        UserAccompanist update = new UserAccompanist();
        update.setId(id);
        update.setAuditStatus(dto.getStatus());
        if (dto.getStatus() == 2) update.setAuditTime(LocalDateTime.now());
        updateById(update);
        log.info("审核陪诊师 [{}] 结果: {}", id, dto.getStatus() == 2 ? "通过" : "拒绝");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAccompanyStatus(Long id, Byte status) {
        if (status == null) throw new ValidationException("status", "状态不能为空");
        UserAccompanist exist = getById(id);
        if (exist == null) throw new ResourceNotFoundException("陪诊师", id);

        if (status == 1 && exist.getAuditStatus() != 2) {
            throw new BusinessException("陪诊师尚未通过审核，无法设为空闲状态");
        }
        UserAccompanist update = new UserAccompanist();
        update.setId(id);
        update.setAccompanyStatus(status);
        updateById(update);
        log.info("陪诊师 [{}] 陪诊状态变更为 {}", id, status);
    }

    private String auditStatusDesc(byte status) {
        return switch (status) {
            case 1 -> "待审核";
            case 2 -> "已通过";
            case 3 -> "已拒绝";
            default -> "未知(" + status + ")";
        };
    }
}
