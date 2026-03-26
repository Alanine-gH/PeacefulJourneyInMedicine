package com.PJDM.mapper;

import com.PJDM.dto.OrderQueryDTO;
import com.PJDM.pojo.OrderOrder;
import com.PJDM.vo.OrderDetailVO;
import com.PJDM.vo.OrderListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单主表 Mapper
 *
 * @author Alanine
 * @since 2026-03-17
 */
@Mapper
public interface OrderOrderMapper extends BaseMapper<OrderOrder> {

    /**
     * 分页查询订单列表（LEFT JOIN order_evaluation 获取评分）
     */
    IPage<OrderListVO> selectOrderListPage(@Param("page") Page<OrderListVO> page,
                                          @Param("q") OrderQueryDTO query);

    /**
     * 查询订单详情（含服务项目列表）
     */
    OrderDetailVO selectOrderDetail(@Param("id") Long id);

    /**
     * 查询订单下的服务项目
     */
    List<OrderDetailVO.ServiceItemVO> selectServiceItems(@Param("orderId") Long orderId);
}
