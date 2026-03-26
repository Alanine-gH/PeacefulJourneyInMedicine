package com.PJDM.service;

import com.PJDM.dto.ExpertQueryDTO;
import com.PJDM.dto.ExpertUpdateDTO;
import com.PJDM.pojo.MedicalExpert;
import com.PJDM.vo.ExpertListVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IMedicalExpertService extends IService<MedicalExpert> {
    IPage<ExpertListVO> getExpertListPage(ExpertQueryDTO query);

    MedicalExpert getExpertDetail(Long id);

    void addExpert(ExpertUpdateDTO dto);

    void updateExpert(Long id, ExpertUpdateDTO dto);

    void deleteExpert(Long id);

    void updateStatus(Long id, Byte status);
}
