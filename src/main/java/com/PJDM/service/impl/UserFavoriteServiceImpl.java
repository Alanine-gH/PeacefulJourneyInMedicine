package com.PJDM.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.PJDM.dto.PatientFavoriteDTO;
import com.PJDM.exception.BusinessException;
import com.PJDM.mapper.UserFavoriteMapper;
import com.PJDM.pojo.UserFavorite;
import com.PJDM.service.IUserFavoriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;

@Slf4j
@Service
public class UserFavoriteServiceImpl extends ServiceImpl<UserFavoriteMapper, UserFavorite>
        implements IUserFavoriteService {

    @Value("${user-module.page.default-size:10}")
    private int defaultPageSize;
    @Value("${user-module.page.max-size:100}")
    private int maxPageSize;

    @Override
    public IPage<UserFavorite> getFavoritePage(Long userId, String targetType, Integer pageNum, Integer pageSize) {
        int size = Math.min(pageSize == null ? defaultPageSize : pageSize, maxPageSize);
        LambdaQueryWrapper<UserFavorite> wrapper = new LambdaQueryWrapper<UserFavorite>()
                .eq(UserFavorite::getUserId, userId)
                .eq(StringUtils.hasText(targetType), UserFavorite::getTargetType, targetType)
                .orderByDesc(UserFavorite::getCreateTime);
        return page(new Page<>(pageNum == null ? 1 : pageNum, size), wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addFavorite(Long userId, PatientFavoriteDTO dto) {
        if (!StringUtils.hasText(dto.getTargetType())) throw new BusinessException("收藏类型不能为空");
        if (dto.getTargetId() == null) throw new BusinessException("收藏目标ID不能为空");
        int exists = baseMapper.existsFavorite(userId, dto.getTargetType(), dto.getTargetId());
        if (exists > 0) throw new BusinessException("已收藏，请勿重复操作");
        UserFavorite fav = new UserFavorite();
        fav.setUserId(userId);
        fav.setTargetType(dto.getTargetType());
        fav.setTargetId(dto.getTargetId());
        fav.setCreateTime(LocalDateTime.now());
        save(fav);
        log.info("[收藏] 添加 userId={} type={} targetId={}", userId, dto.getTargetType(), dto.getTargetId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeFavorite(Long userId, PatientFavoriteDTO dto) {
        remove(new LambdaQueryWrapper<UserFavorite>()
                .eq(UserFavorite::getUserId, userId)
                .eq(UserFavorite::getTargetType, dto.getTargetType())
                .eq(UserFavorite::getTargetId, dto.getTargetId()));
        log.info("[收藏] 取消 userId={} type={} targetId={}", userId, dto.getTargetType(), dto.getTargetId());
    }
}
