package com.PJDM.service;

import com.PJDM.dto.OrderQueryDTO;
import com.PJDM.dto.OrderStatusDTO;
import com.PJDM.dto.OrderUpdateDTO;
import com.PJDM.pojo.OrderOrder;
import com.PJDM.vo.OrderDetailVO;
import com.PJDM.vo.OrderListVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 订单主表 Service
 *
 * @author Alanine
 */
public interface IOrderOrderService extends IService<OrderOrder> {

    /** 分页查询订单列表（多表联查） */
    IPage<OrderListVO> getOrderListPage(OrderQueryDTO query);

    /** 获取订单详情（含服务项目） */
    OrderDetailVO getOrderDetail(Long id);

    /** 新增订单 */
    void addOrder(OrderUpdateDTO dto);

    /** 修改订单 */
    void updateOrder(Long id, OrderUpdateDTO dto);

    /** 删除订单 */
    void deleteOrder(Long id);

    /** 变更订单状态（取消/退款等） */
    void updateOrderStatus(Long id, OrderStatusDTO dto);

    OrderDetailVO getDetailByOrderNo(String orderNo);
}
