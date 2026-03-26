package com.PJDM.mapper;

import com.PJDM.dto.DemandQueryDTO;
import com.PJDM.pojo.UserDemand;
import com.PJDM.vo.DemandListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 需求发布 Mapper
 */
@Mapper
public interface UserDemandMapper extends BaseMapper<UserDemand> {

    /**
     * 分页查询需求列表（关联用户、医院、科室名称）
     */
    IPage<DemandListVO> selectDemandPage(
            @Param("page") Page<DemandListVO> page,
            @Param("q") DemandQueryDTO query);
}
