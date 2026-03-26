package com.PJDM.mapper;

import com.PJDM.pojo.UserMedicalCard;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * 就诊卡 Mapper
 */
@Mapper
public interface UserMedicalCardMapper extends BaseMapper<UserMedicalCard> {

    @Select("SELECT * FROM user_medical_card WHERE patient_id = #{patientId} AND status = 1 ORDER BY id")
    List<UserMedicalCard> selectByPatientId(@Param("patientId") Long patientId);
}
