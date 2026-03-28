package com.PJDM.mapper;

import com.PJDM.dto.UserQueryDTO;
import com.PJDM.pojo.UserIdentityAuth;
import com.PJDM.pojo.UserUser;
import com.PJDM.vo.UserDetailVO;
import com.PJDM.vo.UserListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


/**
 * 用户主表 Mapper
 *
 * @author Alanine
 * @since 2026-03-17
 */
@Mapper
public interface UserUserMapper extends BaseMapper<UserUser> {
//
//    /**
//     * 分页查询用户列表（LEFT JOIN user_identity_auth 获取认证状态）
//     */
//    IPage<UserListVO> selectUserListPage(@Param("page") Page<UserListVO> page,
//                                        @Param("q") UserQueryDTO query);

    /**
     * 用户列表分页 COUNT（避免分页插件自动优化标量子查询导致偏移错误）
     */
    Long selectUserListPageCount(@Param("q") UserQueryDTO query);

    /**
     * 查询用户详情
     */
    UserDetailVO selectUserDetail(@Param("id") Long id);

    /**
     * 查询用户最新一条认证记录
     */
    @Select("SELECT * FROM user_identity_auth WHERE user_id = #{userId} ORDER BY id LIMIT 1")
    UserIdentityAuth selectLatestAuth(@Param("userId") Long userId);
}
