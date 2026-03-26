package com.PJDM.service;

import com.PJDM.dto.AppointmentQueryDTO;
import com.PJDM.dto.AppointmentUpdateDTO;
import com.PJDM.pojo.MedicalAppointment;
import com.PJDM.vo.AppointmentListVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IMedicalAppointmentService extends IService<MedicalAppointment> {
    IPage<AppointmentListVO> getAppointmentListPage(AppointmentQueryDTO query);

    MedicalAppointment getAppointmentDetail(Long id);

    void addAppointment(AppointmentUpdateDTO dto);

    void updateAppointment(Long id, AppointmentUpdateDTO dto);

    void deleteAppointment(Long id);

    void updateStatus(Long id, Byte status);
}
