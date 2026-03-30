package com.PJDM.service;

import com.PJDM.dto.HospitalQueryDTO;
import com.PJDM.dto.HospitalUpdateDTO;
import com.PJDM.pojo.MedicalHospital;
import com.PJDM.vo.HospitalListVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IMedicalHospitalService extends IService<MedicalHospital> {
    IPage<HospitalListVO> getHospitalListPage(HospitalQueryDTO query);

    MedicalHospital getHospitalDetail(Long id);

    void addHospital(HospitalUpdateDTO dto);

    void updateHospital(Long id, HospitalUpdateDTO dto);

    void deleteHospital(Long id);

    void updateStatus(Long id, Byte status);

    IPage<HospitalListVO> searchByRegion(HospitalQueryDTO query);
}
