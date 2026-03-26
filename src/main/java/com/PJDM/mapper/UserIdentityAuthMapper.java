package com.PJDM.mapper;

import com.PJDM.dto.IdentityQueryDTO;
import com.PJDM.pojo.UserIdentityAuth;
import com.PJDM.vo.IdentityListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 实名认证表 Mapper
 *
 * @author Alanine
 * @since 2026-03-17
 */
@Mapper
public interface UserIdentityAuthMapper extends BaseMapper<UserIdentityAuth> {

    /**
     * 分页查询实名认证列表（LEFT JOIN user_user 获取用户名/手机号）
     */
    IPage<IdentityListVO> selectIdentityListPage(@Param("page") Page<IdentityListVO> page,
                                                @Param("q") IdentityQueryDTO query);
}
