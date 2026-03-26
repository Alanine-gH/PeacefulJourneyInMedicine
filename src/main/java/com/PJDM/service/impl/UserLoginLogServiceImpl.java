package com.PJDM.service.impl;

import com.PJDM.pojo.UserLoginLog;
import com.PJDM.mapper.UserLoginLogMapper;
import com.PJDM.service.IUserLoginLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 登录日志表 服务实现类
 * </p>
 *
 * @author Alanine
 * @since 2026-03-17
 */
@Service
public class UserLoginLogServiceImpl extends ServiceImpl<UserLoginLogMapper, UserLoginLog> implements IUserLoginLogService {

}
