package com.PJDM.mapper;

import com.PJDM.dto.HospitalQueryDTO;
import com.PJDM.pojo.MedicalHospital;
import com.PJDM.vo.HospitalListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MedicalHospitalMapper extends BaseMapper<MedicalHospital> {
    IPage<HospitalListVO> selectHospitalListPage(@Param("page") Page<HospitalListVO> page,
                                               @Param("q") HospitalQueryDTO query);
}
