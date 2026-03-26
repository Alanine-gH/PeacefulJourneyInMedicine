package com.PJDM.service;

import com.PJDM.dto.PatientMedicalCardDTO;
import com.PJDM.dto.PatientQueryDTO;
import com.PJDM.dto.PatientUpdateDTO;
import com.PJDM.pojo.UserPatient;
import com.PJDM.vo.PatientDetailVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 就诊人 Service 接口
 */
public interface IUserPatientService extends IService<UserPatient> {

    IPage<PatientDetailVO> getPatientPage(PatientQueryDTO query);

    PatientDetailVO getPatientDetail(Long patientId);

    void addPatient(PatientUpdateDTO dto);

    void updatePatient(Long patientId, PatientUpdateDTO dto);

    void deletePatient(Long patientId);

    void setDefaultPatient(Long userId, Long patientId);

    void addMedicalCard(Long patientId, PatientMedicalCardDTO dto);

    void deleteMedicalCard(Long patientId, Long cardId);
}
