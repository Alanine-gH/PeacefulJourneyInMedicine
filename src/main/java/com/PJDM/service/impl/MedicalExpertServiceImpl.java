package com.PJDM.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.PJDM.common.FkValidator;
import com.PJDM.dto.ExpertQueryDTO;
import com.PJDM.dto.ExpertUpdateDTO;
import com.PJDM.exception.DuplicateEntryException;
import com.PJDM.exception.ResourceNotFoundException;
import com.PJDM.exception.ValidationException;
import com.PJDM.mapper.MedicalAppointmentMapper;
import com.PJDM.mapper.MedicalDepartmentMapper;
import com.PJDM.mapper.MedicalExpertMapper;
import com.PJDM.mapper.MedicalHospitalMapper;
import com.PJDM.mapper.UserUserMapper;
import com.PJDM.pojo.MedicalAppointment;
import com.PJDM.pojo.MedicalExpert;
import com.PJDM.pojo.UserUser;
import com.PJDM.service.IMedicalExpertService;
import com.PJDM.vo.ExpertListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * 专家 Service 实现
 * 外键约束由 FkValidator 保障：
 * - hospital_id   → medical_hospital.id
 * - department_id → medical_department.id
 * - 删除专家前校验是否有关联预约
 *
 * @author Alanine
 */
@Slf4j
@Service
public class MedicalExpertServiceImpl extends ServiceImpl<MedicalExpertMapper, MedicalExpert>
        implements IMedicalExpertService {

    @Value("${user-module.page.default-size:10}")
    private int defaultPageSize;
    @Value("${user-module.page.max-size:100}")
    private int maxPageSize;

    @Autowired
    private FkValidator fkValidator;
    @Autowired
    private MedicalHospitalMapper hospitalMapper;
    @Autowired
    private MedicalDepartmentMapper departmentMapper;
    @Autowired
    private MedicalAppointmentMapper appointmentMapper;
    @Autowired
    private UserUserMapper userUserMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public IPage<ExpertListVO> getExpertListPage(ExpertQueryDTO query) {
        int size = Math.min(query.getPageSize() == null ? defaultPageSize : query.getPageSize(), maxPageSize);
        return baseMapper.selectExpertListPage(
                new Page<>(query.getPageNum() == null ? 1 : query.getPageNum(), size), query);
    }

    @Override
    public MedicalExpert getExpertDetail(Long id) {
        MedicalExpert e = getById(id);
        if (e == null) throw new ResourceNotFoundException("专家", id);
        log.info("[专家] 查询详情 id={}", id);
        return e;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addExpert(ExpertUpdateDTO dto) {
        if (!StringUtils.hasText(dto.getExpertName()))
            throw new ValidationException("expertName", "不能为空");
        if (!StringUtils.hasText(dto.getUsername()))
            throw new ValidationException("username", "新增专家必须填写用户名");
        if (!StringUtils.hasText(dto.getPassword()))
            throw new ValidationException("password", "新增专家必须填写密码");

        // FK: hospital_id → medical_hospital.id
        fkValidator.requireExistsNotNull(hospitalMapper, dto.getHospitalId(), "医院");
        // FK: department_id → medical_department.id
        fkValidator.requireExistsNotNull(departmentMapper, dto.getDepartmentId(), "科室");

        // 唯一性：expertCode 不能重复
        if (StringUtils.hasText(dto.getExpertCode()) &&
                count(new LambdaQueryWrapper<MedicalExpert>()
                        .eq(MedicalExpert::getExpertCode, dto.getExpertCode())) > 0) {
            throw new DuplicateEntryException("expertCode", dto.getExpertCode());
        }

        // 唯一性：用户名不能重复
        if (userUserMapper.selectCount(new LambdaQueryWrapper<UserUser>()
                .eq(UserUser::getUsername, dto.getUsername())) > 0) {
            throw new DuplicateEntryException("username", dto.getUsername());
        }

        // 1. 创建 user_user 记录，user_type=5（专家）
        UserUser user = new UserUser();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setNickName(dto.getExpertName());
        user.setRealName(dto.getExpertName());
        user.setAvatarUrl(dto.getPhotoUrl());
        user.setUserType((byte) 5);
        user.setStatus((byte) 1);
        user.setCreateBy("admin");
        user.setCreateTime(LocalDateTime.now());
        userUserMapper.insert(user);

        // 2. 创建 medical_expert 记录，关联 userId
        MedicalExpert e = new MedicalExpert();
        e.setUserId(user.getId());
        copyDto(e, dto);
        save(e);
        log.info("新增专家 [{}] userId={} hospitalId={} departmentId={}",
                e.getExpertName(), user.getId(), dto.getHospitalId(), dto.getDepartmentId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateExpert(Long id, ExpertUpdateDTO dto) {
        MedicalExpert exist = getById(id);
        if (exist == null) throw new ResourceNotFoundException("专家", id);

        // FK 校验（传了就校验）
        fkValidator.requireExists(hospitalMapper, dto.getHospitalId(), "医院");
        fkValidator.requireExists(departmentMapper, dto.getDepartmentId(), "科室");

        log.info("[专家] 修改 id={}", id);
        MedicalExpert e = new MedicalExpert();
        e.setId(id);
        copyDto(e, dto);
        updateById(e);

        // 同步更新 user_user 的头像
        if (exist.getUserId() != null && StringUtils.hasText(dto.getPhotoUrl())) {
            UserUser userUpdate = new UserUser();
            userUpdate.setId(exist.getUserId());
            userUpdate.setAvatarUrl(dto.getPhotoUrl());
            userUpdate.setNickName(dto.getExpertName());
            userUpdate.setRealName(dto.getExpertName());
            userUserMapper.updateById(userUpdate);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteExpert(Long id) {
        MedicalExpert e = getById(id);
        if (e == null) throw new ResourceNotFoundException("专家", id);

        // 级联保护：存在未完成的预约时禁止删除
        long activeAppt = appointmentMapper.selectCount(
                new LambdaQueryWrapper<MedicalAppointment>()
                        .eq(MedicalAppointment::getExpertId, id)
                        .in(MedicalAppointment::getAppointmentStatus, 1, 2));
        fkValidator.restrictDelete(activeAppt, "专家", "未完成预约");

        removeById(id);
        // 同步删除 user_user 记录
        if (e.getUserId() != null) {
            userUserMapper.deleteById(e.getUserId());
        }
        log.info("[专家] 删除 id={} name={}", id, e.getExpertName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long id, Byte status) {
        if (status == null) throw new ValidationException("status", "状态不能为空");
        MedicalExpert exist = getById(id);
        if (exist == null) throw new ResourceNotFoundException("专家", id);
        MedicalExpert e = new MedicalExpert();
        e.setId(id);
        e.setStatus(status);
        updateById(e);
        // 同步更新 user_user 状态
        if (exist.getUserId() != null) {
            UserUser userUpdate = new UserUser();
            userUpdate.setId(exist.getUserId());
            userUpdate.setStatus(status);
            userUserMapper.updateById(userUpdate);
        }
        log.info("[专家] 状态变更 id={} status={}", id, status);
    }

    private void copyDto(MedicalExpert e, ExpertUpdateDTO dto) {
        if (StringUtils.hasText(dto.getExpertCode())) e.setExpertCode(dto.getExpertCode());
        if (StringUtils.hasText(dto.getExpertName())) e.setExpertName(dto.getExpertName());
        if (StringUtils.hasText(dto.getExpertNameEn())) e.setExpertNameEn(dto.getExpertNameEn());
        if (dto.getHospitalId() != null) e.setHospitalId(dto.getHospitalId());
        if (StringUtils.hasText(dto.getHospitalName())) e.setHospitalName(dto.getHospitalName());
        if (dto.getDepartmentId() != null) e.setDepartmentId(dto.getDepartmentId());
        if (StringUtils.hasText(dto.getDepartmentName())) e.setDepartmentName(dto.getDepartmentName());
        if (StringUtils.hasText(dto.getProfessionalTitle())) e.setProfessionalTitle(dto.getProfessionalTitle());
        if (StringUtils.hasText(dto.getSpecialty())) e.setSpecialty(dto.getSpecialty());
        if (StringUtils.hasText(dto.getLanguageAbility())) e.setLanguageAbility(dto.getLanguageAbility());
        if (dto.getGender() != null) e.setGender(dto.getGender());
        if (dto.getAge() != null) e.setAge(dto.getAge());
        if (StringUtils.hasText(dto.getPhotoUrl())) e.setPhotoUrl(dto.getPhotoUrl());
        if (StringUtils.hasText(dto.getIntroduction())) e.setIntroduction(dto.getIntroduction());
        if (dto.getExperienceYears() != null) e.setExperienceYears(dto.getExperienceYears());
        if (StringUtils.hasText(dto.getEducationBackground())) e.setEducationBackground(dto.getEducationBackground());
        if (dto.getAppointmentFee() != null) e.setAppointmentFee(dto.getAppointmentFee());
        if (dto.getIsAcceptInternational() != null) e.setIsAcceptInternational(dto.getIsAcceptInternational());
        if (dto.getIsRecommend() != null) e.setIsRecommend(dto.getIsRecommend());
        if (dto.getStatus() != null) e.setStatus(dto.getStatus());
        if (dto.getSort() != null) e.setSort(dto.getSort());
        if (StringUtils.hasText(dto.getRemark())) e.setRemark(dto.getRemark());
    }
}
