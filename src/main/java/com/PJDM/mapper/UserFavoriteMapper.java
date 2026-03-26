package com.PJDM.mapper;

import com.PJDM.pojo.UserFavorite;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 收藏 Mapper
 */
@Mapper
public interface UserFavoriteMapper extends BaseMapper<UserFavorite> {

    @Select("SELECT COUNT(*) FROM user_favorite WHERE user_id=#{userId} AND target_type=#{targetType} AND target_id=#{targetId}")
    int existsFavorite(@Param("userId") Long userId,
                       @Param("targetType") String targetType,
                       @Param("targetId") Long targetId);
}
