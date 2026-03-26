package com.PJDM.mapper;

import com.PJDM.dto.AccompanistQueryDTO;
import com.PJDM.pojo.UserAccompanist;
import com.PJDM.vo.AccompanistListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 陪诊师资质表 Mapper
 *
 * @author Alanine
 * @since 2026-03-17
 */
@Mapper
public interface UserAccompanistMapper extends BaseMapper<UserAccompanist> {

    /**
     * 分页查询陪诊师列表（LEFT JOIN user_user 获取账号状态/用户名）
     */
    IPage<AccompanistListVO> selectAccompanistListPage(@Param("page") Page<AccompanistListVO> page,
                                                     @Param("q") AccompanistQueryDTO query);
}
