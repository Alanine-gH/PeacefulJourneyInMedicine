package com.PJDM.mapper;

import com.PJDM.dto.TriageQueryDTO;
import com.PJDM.pojo.MedicalTriageRecord;
import com.PJDM.vo.TriageListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MedicalTriageRecordMapper extends BaseMapper<MedicalTriageRecord> {
    IPage<TriageListVO> selectTriageListPage(@Param("page") Page<TriageListVO> page,
                                           @Param("q") TriageQueryDTO query);
}
