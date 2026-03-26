package com.PJDM.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.PJDM.dto.TriageQueryDTO;
import com.PJDM.mapper.MedicalTriageRecordMapper;
import com.PJDM.pojo.MedicalTriageRecord;
import com.PJDM.service.IMedicalTriageService;
import com.PJDM.vo.TriageListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class MedicalTriageServiceImpl extends ServiceImpl<MedicalTriageRecordMapper, MedicalTriageRecord>
        implements IMedicalTriageService {

    @Value("${user-module.page.default-size:10}")
    private int defaultPageSize;
    @Value("${user-module.page.max-size:100}")
    private int maxPageSize;

    @Override
    public IPage<TriageListVO> getTriageListPage(TriageQueryDTO query) {
        int size = Math.min(query.getPageSize() == null ? defaultPageSize : query.getPageSize(), maxPageSize);
        return baseMapper.selectTriageListPage(
                new Page<>(query.getPageNum() == null ? 1 : query.getPageNum(), size), query);
    }

    @Override
    public MedicalTriageRecord getTriageDetail(Long id) {
        MedicalTriageRecord t = getById(id);
        if (t == null) throw new RuntimeException("分诊记录不存在");
        return t;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTriageStatus(Long id, Byte status) {
        if (status == null) throw new RuntimeException("状态不能为空");
        if (getById(id) == null) throw new RuntimeException("分诊记录不存在");
        MedicalTriageRecord t = new MedicalTriageRecord();
        t.setId(id);
        t.setTriageStatus(status);
        if (status == 5) t.setHumanReviewTime(java.time.LocalDateTime.now());
        updateById(t);
        log.info("分诊 [{}] 状态变更为 {}", id, status);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTriage(Long id) {
        if (getById(id) == null) throw new RuntimeException("分诊记录不存在");
        removeById(id);
        log.info("删除分诊记录 [{}]", id);
    }
}
