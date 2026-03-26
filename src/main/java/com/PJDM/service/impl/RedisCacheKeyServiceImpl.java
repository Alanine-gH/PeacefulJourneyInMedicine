package com.PJDM.service.impl;

import com.PJDM.pojo.RedisCacheKey;
import com.PJDM.mapper.RedisCacheKeyMapper;
import com.PJDM.service.IRedisCacheKeyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * Redis缓存Key设计表 服务实现类
 * </p>
 *
 * @author Alanine
 * @since 2026-03-17
 */
@Service
public class RedisCacheKeyServiceImpl extends ServiceImpl<RedisCacheKeyMapper, RedisCacheKey> implements IRedisCacheKeyService {

}
