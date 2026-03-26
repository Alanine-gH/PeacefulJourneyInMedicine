package com.PJDM.mapper;

import com.PJDM.dto.DispatchQueryDTO;
import com.PJDM.pojo.ServiceDispatch;
import com.PJDM.vo.DispatchListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ServiceDispatchMapper extends BaseMapper<ServiceDispatch> {
    IPage<DispatchListVO> selectDispatchListPage(@Param("page") Page<DispatchListVO> page,
                                               @Param("q") DispatchQueryDTO query);
}
