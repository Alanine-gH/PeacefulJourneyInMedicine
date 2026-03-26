package com.PJDM.mapper;

import com.PJDM.dto.CheckinQueryDTO;
import com.PJDM.pojo.ServiceCheckin;
import com.PJDM.vo.CheckinListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ServiceCheckinMapper extends BaseMapper<ServiceCheckin> {
    IPage<CheckinListVO> selectCheckinListPage(@Param("page") Page<CheckinListVO> page,
                                             @Param("q") CheckinQueryDTO query);
}
