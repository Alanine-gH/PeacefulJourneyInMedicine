package com.PJDM.service.impl;

import com.PJDM.pojo.UserIdentityAuth;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.PJDM.dto.UserQueryDTO;
import com.PJDM.dto.UserUpdateDTO;
import com.PJDM.mapper.UserUserMapper;
import com.PJDM.pojo.UserUser;
import com.PJDM.service.IUserUserService;
import com.PJDM.vo.UserDetailVO;
import com.PJDM.vo.UserListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.hasText;

/**
 * 用户主表 Service 实现
 *
 * @author Alanine
 */
@Slf4j
@Service
public class UserUserServiceImpl extends ServiceImpl<UserUserMapper, UserUser> implements IUserUserService {

    @Value("${user-module.page.default-size:10}")
    private int defaultPageSize;

    @Value("${user-module.page.max-size:100}")
    private int maxPageSize;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public IPage<UserListVO> getUserListPage(UserQueryDTO query) {
        int size = Math.min(query.getPageSize() == null ? defaultPageSize : query.getPageSize(), maxPageSize);
        int pageNum = query.getPageNum() == null ? 1 : query.getPageNum();
        // 直接用 MP 内置 page() 方法查 user_user 表，分页完全由 MP 处理，不涉及复杂 JOIN
        LambdaQueryWrapper<UserUser> lqw = new LambdaQueryWrapper<>();//下面是lqw的判断
        if (hasText(query.getUsername())) {
            lqw.and(w -> w.like(UserUser::getUsername, query.getUsername())
                          .or().like(UserUser::getNickName, query.getUsername()));
        }
        if (hasText(query.getPhone())) {
            lqw.eq(UserUser::getPhone, query.getPhone());
        }
        if (query.getUserType() != null) {
            lqw.eq(UserUser::getUserType, query.getUserType());
        }
        if (query.getStatus() != null) {
            lqw.eq(UserUser::getStatus, query.getStatus());
        }
//        lqw.orderByDesc(UserUser::getCreateTime);
        //构建分页查询
        Page<UserUser> pageParam = new Page<>(pageNum, size);
        IPage<UserUser> userPage = page(pageParam, lqw);
        // 转换为 UserListVO
        Page<UserListVO> result = new Page<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        result.setRecords(userPage.getRecords().stream().map(u -> {
            UserListVO vo = new UserListVO();
            vo.setId(u.getId());
            vo.setUsername(u.getUsername());
            vo.setNickName(u.getNickName());
            vo.setRealName(u.getRealName());
            vo.setPhone(u.getPhone());
            vo.setEmail(u.getEmail());
            vo.setAvatarUrl(u.getAvatarUrl());
            vo.setAge(u.getAge());
            vo.setGender(u.getGender());
            vo.setUserType(u.getUserType());
            vo.setStatus(u.getStatus());
            vo.setLastLoginTime(u.getLastLoginTime());
            vo.setLastLoginIp(u.getLastLoginIp());
            vo.setCreateTime(u.getCreateTime());
            // 认证状态单独查
            UserIdentityAuth auth = baseMapper.selectLatestAuth(u.getId());
            if (auth != null) {
                vo.setAuthStatus(auth.getAuthStatus());
                vo.setAuthType(auth.getAuthType());
            }
            return vo;
        }).collect(Collectors.toList()));
        return result;
    }

    @Override
    public UserDetailVO getUserDetail(Long id) {
        UserDetailVO vo = baseMapper.selectUserDetail(id);
        if (vo == null) {
            throw new RuntimeException("用户不存在");
        }
        List<UserDetailVO.RoleSimpleVO> roles = baseMapper.selectRolesByUserId(id);
        vo.setRoles(roles);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUser(UserUpdateDTO dto) {
        if (!hasText(dto.getUsername())) {
            throw new RuntimeException("用户名不能为空");
        }
        if (!hasText(dto.getPassword()) || dto.getPassword().length() < 8) {
            throw new RuntimeException("密码不能少于8位");
        }
        if (count(new LambdaQueryWrapper<UserUser>().eq(UserUser::getUsername, dto.getUsername())) > 0) {
            throw new RuntimeException("用户名已存在");
        }
        if (hasText(dto.getPhone()) &&
                count(new LambdaQueryWrapper<UserUser>().eq(UserUser::getPhone, dto.getPhone())) > 0) {
            throw new RuntimeException("手机号已被使用");
        }
        if (hasText(dto.getEmail()) &&
                count(new LambdaQueryWrapper<UserUser>().eq(UserUser::getEmail, dto.getEmail())) > 0) {
            throw new RuntimeException("邮箱已被使用");
        }
        UserUser user = new UserUser();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setNickName(hasText(dto.getNickName()) ? dto.getNickName() : dto.getUsername());
        user.setRealName(dto.getRealName());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setUserType(dto.getUserType() != null ? dto.getUserType() : (byte) 1);
        user.setStatus(dto.getStatus() != null ? dto.getStatus() : (byte) 1);
        user.setRemark(dto.getRemark());
        user.setAvatarUrl(dto.getAvatarUrl());
        user.setAge(dto.getAge());
        user.setGender(dto.getGender());
        save(user);
        log.info("新增用户 [{}] 成功", user.getUsername());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(Long id, UserUpdateDTO dto) {
        UserUser exist = getById(id);
        if (exist == null) {
            throw new RuntimeException("用户不存在");
        }
        if (hasText(dto.getPhone()) && !dto.getPhone().equals(exist.getPhone()) &&
                count(new LambdaQueryWrapper<UserUser>().eq(UserUser::getPhone, dto.getPhone())) > 0) {
            throw new RuntimeException("手机号已被使用");
        }
        if (hasText(dto.getEmail()) && !dto.getEmail().equals(exist.getEmail()) &&
                count(new LambdaQueryWrapper<UserUser>().eq(UserUser::getEmail, dto.getEmail())) > 0) {
            throw new RuntimeException("邮箱已被使用");
        }
        UserUser update = new UserUser();
        update.setId(id);
        update.setNickName(dto.getNickName());
        update.setRealName(dto.getRealName());
        update.setPhone(dto.getPhone());
        update.setEmail(dto.getEmail());
        update.setUserType(dto.getUserType());
        update.setStatus(dto.getStatus());
        update.setRemark(dto.getRemark());
        update.setAvatarUrl(dto.getAvatarUrl());
        update.setAge(dto.getAge());
        update.setGender(dto.getGender());
        updateById(update);
        log.info("修改用户 [{}] 成功", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long id) {
        if (getById(id) == null) {
            throw new RuntimeException("用户不存在");
        }
        removeById(id);
        log.info("删除用户 [{}] 成功", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long id, Byte status) {
        if (getById(id) == null) {
            throw new RuntimeException("用户不存在");
        }
        UserUser update = new UserUser();
        update.setId(id);
        update.setStatus(status);
        updateById(update);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(Long id, String newPassword) {
        if (!hasText(newPassword) || newPassword.length() < 8) {
            throw new RuntimeException("新密码不能少于8位");
        }
        if (getById(id) == null) {
            throw new RuntimeException("用户不存在");
        }
        UserUser update = new UserUser();
        update.setId(id);
        update.setPassword(passwordEncoder.encode(newPassword));
        updateById(update);
        log.info("重置用户 [{}] 密码成功", id);
    }
}
