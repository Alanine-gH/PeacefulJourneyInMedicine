package com.PJDM.mapper;

import com.PJDM.dto.MessageQueryDTO;
import com.PJDM.pojo.SysMessage;
import com.PJDM.vo.MessageListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 系统消息 Mapper
 */
@Mapper
public interface SysMessageMapper extends BaseMapper<SysMessage> {

    /**
     * 分页查询消息列表
     */
    IPage<MessageListVO> selectMessagePage(
            @Param("page") Page<MessageListVO> page,
            @Param("q") MessageQueryDTO query);

    /**
     * 统计未读消息数量
     */
    @Select("SELECT COUNT(*) FROM sys_message WHERE receiver_id = #{userId} AND status = 1")
    Long countUnread(@Param("userId") Long userId);
}
