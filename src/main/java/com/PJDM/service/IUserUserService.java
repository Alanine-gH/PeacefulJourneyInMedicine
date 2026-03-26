package com.PJDM.service;

import com.PJDM.dto.UserQueryDTO;
import com.PJDM.dto.UserUpdateDTO;
import com.PJDM.pojo.UserUser;
import com.PJDM.vo.UserDetailVO;
import com.PJDM.vo.UserListVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 用户主表 Service
 *
 * @author Alanine
 */
public interface IUserUserService extends IService<UserUser> {

    /** 分页查询用户列表（多表联查） */
    IPage<UserListVO> getUserListPage(UserQueryDTO query);

    /** 获取用户详情（含角色） */
    UserDetailVO getUserDetail(Long id);

    /** 新增用户 */
    void addUser(UserUpdateDTO dto);

    /** 修改用户 */
    void updateUser(Long id, UserUpdateDTO dto);

    /** 删除用户 */
    void deleteUser(Long id);

    /** 修改账号状态 */
    void updateStatus(Long id, Byte status);

    /** 重置密码 */
    void resetPassword(Long id, String newPassword);
}
