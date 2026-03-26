package com.PJDM.mapper;

import com.PJDM.dto.ExpertQueryDTO;
import com.PJDM.pojo.MedicalExpert;
import com.PJDM.vo.ExpertListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MedicalExpertMapper extends BaseMapper<MedicalExpert> {
    IPage<ExpertListVO> selectExpertListPage(@Param("page") Page<ExpertListVO> page,
                                           @Param("q") ExpertQueryDTO query);
}
