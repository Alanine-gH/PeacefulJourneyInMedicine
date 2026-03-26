package com.PJDM.service;

import com.PJDM.dto.PatientFavoriteDTO;
import com.PJDM.pojo.UserFavorite;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 收藏 Service 接口
 */
public interface IUserFavoriteService extends IService<UserFavorite> {

    IPage<UserFavorite> getFavoritePage(Long userId, String targetType, Integer pageNum, Integer pageSize);

    void addFavorite(Long userId, PatientFavoriteDTO dto);

    void removeFavorite(Long userId, PatientFavoriteDTO dto);
}
