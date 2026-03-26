package com.PJDM.mapper;

import com.PJDM.dto.AppointmentQueryDTO;
import com.PJDM.pojo.MedicalAppointment;
import com.PJDM.vo.AppointmentListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MedicalAppointmentMapper extends BaseMapper<MedicalAppointment> {
    IPage<AppointmentListVO> selectAppointmentListPage(@Param("page") Page<AppointmentListVO> page,
                                                    @Param("q") AppointmentQueryDTO query);
}
