package com.PJDM.service;

import com.PJDM.dto.TriageQueryDTO;
import com.PJDM.pojo.MedicalTriageRecord;
import com.PJDM.vo.TriageListVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IMedicalTriageService extends IService<MedicalTriageRecord> {
    IPage<TriageListVO> getTriageListPage(TriageQueryDTO query);

    MedicalTriageRecord getTriageDetail(Long id);

    void updateTriageStatus(Long id, Byte status);

    void deleteTriage(Long id);
}
