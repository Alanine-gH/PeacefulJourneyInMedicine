package com.PJDM.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.PJDM.common.FkValidator;
import com.PJDM.dto.AppointmentQueryDTO;
import com.PJDM.dto.AppointmentUpdateDTO;
import com.PJDM.exception.BusinessException;
import com.PJDM.exception.ResourceNotFoundException;
import com.PJDM.exception.ValidationException;
import com.PJDM.mapper.MedicalAppointmentMapper;
import com.PJDM.mapper.MedicalDepartmentMapper;
import com.PJDM.mapper.MedicalExpertMapper;
import com.PJDM.mapper.MedicalHospitalMapper;
import com.PJDM.mapper.UserUserMapper;
import com.PJDM.pojo.MedicalAppointment;
import com.PJDM.service.IMedicalAppointmentService;
import com.PJDM.vo.AppointmentListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 预约 Service 实现
 * 外键约束由 FkValidator 保障：
 * - user_id       → user_user.id
 * - hospital_id   → medical_hospital.id
 * - department_id → medical_department.id
 * - expert_id     → medical_expert.id（可选）
 * - 预约状态流转合法性校验
 *
 * @author Alanine
 */
@Slf4j
@Service
public class MedicalAppointmentServiceImpl extends ServiceImpl<MedicalAppointmentMapper, MedicalAppointment>
        implements IMedicalAppointmentService {

    @Value("${user-module.page.default-size:10}")
    private int defaultPageSize;
    @Value("${user-module.page.max-size:100}")
    private int maxPageSize;

    @Autowired
    private FkValidator fkValidator;
    @Autowired
    private UserUserMapper userUserMapper;
    @Autowired
    private MedicalHospitalMapper hospitalMapper;
    @Autowired
    private MedicalDepartmentMapper departmentMapper;
    @Autowired
    private MedicalExpertMapper expertMapper;

    @Override
    public IPage<AppointmentListVO> getAppointmentListPage(AppointmentQueryDTO query) {
        int size = Math.min(query.getPageSize() == null ? defaultPageSize : query.getPageSize(), maxPageSize);
        return baseMapper.selectAppointmentListPage(
                new Page<>(query.getPageNum() == null ? 1 : query.getPageNum(), size), query);
    }

    @Override
    public MedicalAppointment getAppointmentDetail(Long id) {
        MedicalAppointment a = getById(id);
        if (a == null) throw new ResourceNotFoundException("预约记录", id);
        return a;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addAppointment(AppointmentUpdateDTO dto) {
        // FK: user_id → user_user.id
        fkValidator.requireExistsNotNull(userUserMapper, dto.getUserId(), "患者用户");
        // FK: hospital_id → medical_hospital.id
        fkValidator.requireExistsNotNull(hospitalMapper, dto.getHospitalId(), "医院");
        // FK: department_id → medical_department.id
        fkValidator.requireExistsNotNull(departmentMapper, dto.getDepartmentId(), "科室");
        // FK: expert_id → medical_expert.id（可选）
        fkValidator.requireExists(expertMapper, dto.getExpertId(), "专家");

        if (dto.getAppointmentDate() == null) throw new ValidationException("appointmentDate", "预约日期不能为空");

        MedicalAppointment a = new MedicalAppointment();
        a.setAppointmentNo(generateApptNo());
        copyDto(a, dto);
        a.setAppointmentStatus((byte) 1); // 待确认
        save(a);
        log.info("新增预约 [{}] userId={} hospitalId={}",
                a.getAppointmentNo(), dto.getUserId(), dto.getHospitalId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAppointment(Long id, AppointmentUpdateDTO dto) {
        MedicalAppointment exist = getById(id);
        if (exist == null) throw new ResourceNotFoundException("预约记录", id);

        // 已完成/已取消/已过期的预约不允许修改
        byte status = exist.getAppointmentStatus();
        if (status == 3 || status == 4 || status == 5) {
            throw new BusinessException(String.format("预约 [%s] 当前状态为 %s，不允许修改", exist.getAppointmentNo(),
                    appointmentStatusDesc(status))
            );
        }

        // FK 校验（传了就校验）
        fkValidator.requireExists(hospitalMapper, dto.getHospitalId(), "医院");
        fkValidator.requireExists(departmentMapper, dto.getDepartmentId(), "科室");
        fkValidator.requireExists(expertMapper, dto.getExpertId(), "专家");

        MedicalAppointment a = new MedicalAppointment();
        a.setId(id);
        copyDto(a, dto);
        updateById(a);
        log.info("修改预约 [{}] 成功", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAppointment(Long id) {
        MedicalAppointment exist = getById(id);
        if (exist == null) throw new ResourceNotFoundException("预约记录", id);

        // 已确认/已完成的预约禁止直接删除
        byte status = exist.getAppointmentStatus();
        if (status == 2 || status == 4) {
            throw new BusinessException(String.format(
                    "预约 [%s] 当前状态为 %s，请先取消后再删除",
                    exist.getAppointmentNo(), appointmentStatusDesc(status)));
        }
        removeById(id);
        log.info("删除预约 [{}] 成功", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long id, Byte status) {
        if (status == null) throw new ValidationException("status", "状态不能为空");
        MedicalAppointment exist = getById(id);
        if (exist == null) throw new ResourceNotFoundException("预约记录", id);

        // 状态流转校验
        validateStatusTransition(exist.getAppointmentNo(), exist.getAppointmentStatus(), status);

        MedicalAppointment a = new MedicalAppointment();
        a.setId(id);
        a.setAppointmentStatus(status);
        // 已完成时记录签到时间
        if (status == 4) a.setCheckInTime(LocalDateTime.now());
        updateById(a);
        log.info("预约 [{}] 状态 {} → {}",
                exist.getAppointmentNo(),
                appointmentStatusDesc(exist.getAppointmentStatus()),
                appointmentStatusDesc(status));
    }

    // ==================== 私有方法 ====================

    /**
     * 预约状态流转校验
     * 1:待确认 2:已确认 3:已取消 4:已完成 5:已过期
     */
    private void validateStatusTransition(String apptNo, byte current, byte next) {
        boolean valid = switch (current) {
            case 1 -> next == 2 || next == 3 || next == 5; // 待确认 → 已确认|已取消|已过期
            case 2 -> next == 3 || next == 4;              // 已确认 → 已取消|已完成
            case 3 -> false;                               // 已取消 → 不可变
            case 4 -> false;                               // 已完成 → 不可变
            case 5 -> false;                               // 已过期 → 不可变
            default -> false;
        };
        if (!valid) {
            throw new BusinessException(String.format(
                    "预约 [%s] 状态流转不合法：%s → %s",
                    apptNo, appointmentStatusDesc(current), appointmentStatusDesc(next)));
        }
    }

    private String appointmentStatusDesc(byte status) {
        return switch (status) {
            case 1 -> "待确认";
            case 2 -> "已确认";
            case 3 -> "已取消";
            case 4 -> "已完成";
            case 5 -> "已过期";
            default -> "未知(" + status + ")";
        };
    }

    private void copyDto(MedicalAppointment a, AppointmentUpdateDTO dto) {
        if (dto.getUserId() != null) a.setUserId(dto.getUserId());
        if (dto.getHospitalId() != null) a.setHospitalId(dto.getHospitalId());
        if (StringUtils.hasText(dto.getHospitalName())) a.setHospitalName(dto.getHospitalName());
        if (dto.getDepartmentId() != null) a.setDepartmentId(dto.getDepartmentId());
        if (StringUtils.hasText(dto.getDepartmentName())) a.setDepartmentName(dto.getDepartmentName());
        if (dto.getExpertId() != null) a.setExpertId(dto.getExpertId());
        if (StringUtils.hasText(dto.getExpertName())) a.setExpertName(dto.getExpertName());
        if (dto.getAppointmentDate() != null) a.setAppointmentDate(dto.getAppointmentDate());
        if (dto.getAppointmentTime() != null) a.setAppointmentTime(dto.getAppointmentTime());
        if (dto.getAppointmentType() != null) a.setAppointmentType(dto.getAppointmentType());
        if (StringUtils.hasText(dto.getDiseaseDescription())) a.setDiseaseDescription(dto.getDiseaseDescription());
        if (StringUtils.hasText(dto.getVisitCardNo())) a.setVisitCardNo(dto.getVisitCardNo());
        if (StringUtils.hasText(dto.getRemark())) a.setRemark(dto.getRemark());
    }

    private String generateApptNo() {
        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "APT" + ts + UUID.randomUUID().toString().replace("-", "").substring(0, 4).toUpperCase();
    }
}
