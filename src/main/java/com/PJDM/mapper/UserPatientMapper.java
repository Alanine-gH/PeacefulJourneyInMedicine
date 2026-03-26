package com.PJDM.mapper;

import com.PJDM.dto.PatientQueryDTO;
import com.PJDM.pojo.UserPatient;
import com.PJDM.vo.PatientDetailVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * 就诊人 Mapper
 */
@Mapper
public interface UserPatientMapper extends BaseMapper<UserPatient> {

    /**
     * 分页查询就诊人列表
     */
    IPage<PatientDetailVO> selectPatientPage(
            @Param("page") Page<PatientDetailVO> page,
            @Param("q") PatientQueryDTO query);

    /**
     * 查询就诊人详情（含就诊卡）
     */
    @Select("SELECT * FROM user_patient WHERE id = #{id} AND status = 1")
    UserPatient selectActiveById(@Param("id") Long id);
}
