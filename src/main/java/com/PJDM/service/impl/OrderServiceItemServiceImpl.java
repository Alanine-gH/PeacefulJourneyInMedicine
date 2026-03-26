package com.PJDM.service.impl;

import com.PJDM.pojo.OrderServiceItem;
import com.PJDM.mapper.OrderServiceItemMapper;
import com.PJDM.service.IOrderServiceItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单服务项目表 服务实现类
 * </p>
 *
 * @author Alanine
 * @since 2026-03-17
 */
@Service
public class OrderServiceItemServiceImpl extends ServiceImpl<OrderServiceItemMapper, OrderServiceItem> implements IOrderServiceItemService {

}
