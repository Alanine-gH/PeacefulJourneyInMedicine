package com.PJDM.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.PJDM.common.FkValidator;
import com.PJDM.dto.OrderQueryDTO;
import com.PJDM.dto.OrderStatusDTO;
import com.PJDM.dto.OrderUpdateDTO;
import com.PJDM.exception.BusinessException;
import com.PJDM.exception.ResourceNotFoundException;
import com.PJDM.exception.ValidationException;
import com.PJDM.mapper.OrderOrderMapper;
import com.PJDM.mapper.OrderServiceItemMapper;
import com.PJDM.mapper.UserAccompanistMapper;
import com.PJDM.mapper.UserUserMapper;
import com.PJDM.pojo.OrderOrder;
import com.PJDM.service.IOrderOrderService;
import com.PJDM.vo.OrderDetailVO;
import com.PJDM.vo.OrderListVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * 订单主表 Service 实现
 * 外键约束由 FkValidator 在业务层保障：
 * - order_order.user_id        → user_user.id
 * - order_order.accompanist_id → user_accompanist.id
 * - 删除订单前校验子表（order_service_item）
 * - 状态流转合法性校验
 *
 * @author Alanine
 */
@Slf4j
@Service
public class OrderOrderServiceImpl extends ServiceImpl<OrderOrderMapper, OrderOrder>
        implements IOrderOrderService {

    @Value("${user-module.page.default-size:10}")
    private int defaultPageSize;

    @Value("${user-module.page.max-size:100}")
    private int maxPageSize;

    @Autowired
    private FkValidator fkValidator;

    @Autowired
    private UserUserMapper userUserMapper;

    @Autowired
    private UserAccompanistMapper userAccompanistMapper;

    @Autowired
    private OrderServiceItemMapper orderServiceItemMapper;

    // ==================== 查询 ====================

    @Override
    public IPage<OrderListVO> getOrderListPage(OrderQueryDTO query) {
        int size = Math.min(query.getPageSize() == null ? defaultPageSize : query.getPageSize(), maxPageSize);
        Page<OrderListVO> page = new Page<>(query.getPageNum() == null ? 1 : query.getPageNum(), size);
        return baseMapper.selectOrderListPage(page, query);
    }

    @Override
    public OrderDetailVO getOrderDetail(Long id) {
        OrderDetailVO vo = baseMapper.selectOrderDetail(id);
        if (vo == null) throw new ResourceNotFoundException("订单", id);
        log.info("[订单] 查询详情 id={}", id);
        List<OrderDetailVO.ServiceItemVO> items = baseMapper.selectServiceItems(id);
        vo.setServiceItems(items);
        return vo;
    }

    // ==================== 新增 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addOrder(OrderUpdateDTO dto) {
        // FK: user_id → user_user.id（必填）
        if (dto.getUserId() == null) throw new ValidationException("userId", "不能为空");
        fkValidator.requireExists(userUserMapper, dto.getUserId(), "用户");

        // FK: accompanist_id → user_accompanist.id（可选）
        fkValidator.requireExists(userAccompanistMapper, dto.getAccompanistId(), "陪诊师");

        OrderOrder order = new OrderOrder();
        order.setOrderNo(generateOrderNo());
        order.setUserId(dto.getUserId());
        order.setAccompanistId(dto.getAccompanistId());
        order.setOrderStatus((byte) 1);  // 待付款
        order.setServiceStartTime(dto.getServiceStartTime());
        order.setServiceEndTime(dto.getServiceEndTime());
        order.setServiceDays(dto.getServiceDays());
        order.setServiceHours(dto.getServiceHours());
        order.setPatientName(dto.getPatientName());
        order.setPatientGender(dto.getPatientGender());
        order.setPatientAge(dto.getPatientAge());
        order.setPatientPassport(dto.getPatientPassport());
        order.setDiseaseDescription(dto.getDiseaseDescription());
        order.setAppointmentHospital(dto.getAppointmentHospital());
        order.setAppointmentDepartment(dto.getAppointmentDepartment());
        order.setAppointmentDate(dto.getAppointmentDate());
        order.setTotalAmount(dto.getTotalAmount());
        order.setDepositAmount(dto.getDepositAmount());
        order.setBalanceAmount(dto.getBalanceAmount());
        order.setCurrency(StringUtils.hasText(dto.getCurrency()) ? dto.getCurrency() : "USD");
        order.setFlightNumber(dto.getFlightNumber());
        order.setFlightArrivalTime(dto.getFlightArrivalTime());
        order.setHotelName(dto.getHotelName());
        order.setHotelAddress(dto.getHotelAddress());
        order.setPickUpAddress(dto.getPickUpAddress());
        order.setDropOffAddress(dto.getDropOffAddress());
        order.setRemark(dto.getRemark());
        save(order);
        log.info("新增订单 [{}] 成功 userId={}", order.getOrderNo(), dto.getUserId());
    }

    // ==================== 修改 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrder(Long id, OrderUpdateDTO dto) {
        // 订单必须存在
        if (getById(id) == null) throw new ResourceNotFoundException("订单", id);

        // FK: accompanist_id → user_accompanist.id（可选，传了就校验）
        fkValidator.requireExists(userAccompanistMapper, dto.getAccompanistId(), "陪诊师");

        log.info("[订单] 修改 id={}", id);
        OrderOrder update = new OrderOrder();
        update.setId(id);
        update.setAccompanistId(dto.getAccompanistId());
        update.setServiceStartTime(dto.getServiceStartTime());
        update.setServiceEndTime(dto.getServiceEndTime());
        update.setServiceDays(dto.getServiceDays());
        update.setServiceHours(dto.getServiceHours());
        update.setPatientName(dto.getPatientName());
        update.setPatientGender(dto.getPatientGender());
        update.setPatientAge(dto.getPatientAge());
        update.setPatientPassport(dto.getPatientPassport());
        update.setDiseaseDescription(dto.getDiseaseDescription());
        update.setAppointmentHospital(dto.getAppointmentHospital());
        update.setAppointmentDepartment(dto.getAppointmentDepartment());
        update.setAppointmentDate(dto.getAppointmentDate());
        update.setTotalAmount(dto.getTotalAmount());
        update.setDepositAmount(dto.getDepositAmount());
        update.setBalanceAmount(dto.getBalanceAmount());
        if (StringUtils.hasText(dto.getCurrency())) update.setCurrency(dto.getCurrency());
        update.setFlightNumber(dto.getFlightNumber());
        update.setFlightArrivalTime(dto.getFlightArrivalTime());
        update.setHotelName(dto.getHotelName());
        update.setHotelAddress(dto.getHotelAddress());
        update.setPickUpAddress(dto.getPickUpAddress());
        update.setDropOffAddress(dto.getDropOffAddress());
        update.setRemark(dto.getRemark());
        updateById(update);
        log.info("修改订单 [{}] 成功", id);
    }

    // ==================== 删除 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteOrder(Long id) {
        OrderOrder exist = getById(id);
        if (exist == null) throw new ResourceNotFoundException("订单", id);

        // 级联保护：已付款/服务中/已完成的订单不允许直接删除
        byte status = exist.getOrderStatus();
        if (status == 2 || status == 3 || status == 4) {
            throw new BusinessException(
                    String.format("订单 [%s] 当前状态为 %s，不允许删除，请先取消订单",
                            exist.getOrderNo(), orderStatusDesc(status)));
        }

        // 级联保护：存在服务项时提示（实际可级联删除，此处选择提示）
        long itemCount = orderServiceItemMapper.selectCount(
                new LambdaQueryWrapper<com.PJDM.pojo.OrderServiceItem>().eq(com.PJDM.pojo.OrderServiceItem::getOrderId, id));
        if (itemCount > 0) {
            log.info("[订单删除] 同步删除 {} 条服务项 orderId={}", itemCount, id);
            orderServiceItemMapper.delete(
                    new LambdaQueryWrapper<com.PJDM.pojo.OrderServiceItem>().eq(com.PJDM.pojo.OrderServiceItem::getOrderId, id));
        }

        removeById(id);
        log.info("删除订单 [{}] 成功", id);
    }

    // ==================== 状态流转 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderStatus(Long id, OrderStatusDTO dto) {
        if (dto.getOrderStatus() == null) throw new ValidationException("orderStatus", "不能为空");
        OrderOrder exist = getById(id);
        if (exist == null) throw new ResourceNotFoundException("订单", id);

        byte current = exist.getOrderStatus();
        byte next = dto.getOrderStatus();

        // 状态流转合法性校验（替代数据库触发器）
        validateStatusTransition(exist.getOrderNo(), current, next);

        OrderOrder update = new OrderOrder();
        update.setId(id);
        update.setOrderStatus(dto.getOrderStatus());

        // 取消：必须填写原因
        if (next == 6) {
            if (!StringUtils.hasText(dto.getReason())) throw new ValidationException("reason", "取消原因不能为空");
            update.setCancelReason(dto.getReason());
            update.setCancelTime(LocalDateTime.now());
        }
        // 退款中：记录原因
        if (next == 7) {
            if (!StringUtils.hasText(dto.getReason())) throw new ValidationException("reason", "退款原因不能为空");
            update.setRefundReason(dto.getReason());
        }
        // 已退款：记录退款时间和金额
        if (next == 8) {
            update.setRefundTime(LocalDateTime.now());
        }

        updateById(update);
        log.info("订单 [{}] 状态 {} → {}", exist.getOrderNo(),
                orderStatusDesc(current), orderStatusDesc(next));
    }

    @Override
    public OrderDetailVO getDetailByOrderNo(String orderNo) {
        LambdaQueryWrapper<OrderOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderOrder::getOrderNo, orderNo);
        OrderOrder order = getOne(wrapper);
        if (order == null) {
            throw new ResourceNotFoundException("订单", orderNo);
        }
        return getOrderDetail(order.getId());
    }

    // ==================== 私有方法 ====================

    /**
     * 状态流转合法性校验
     * 订单状态：1待付款 2已付款 3已确认 4服务中 5已完成 6已取消 7退款中 8已退款
     */
    private void validateStatusTransition(String orderNo, byte current, byte next) {
        boolean valid = switch (current) {
            case 1 -> next == 2 || next == 6;          // 待付款 → 已付款 | 取消
            case 2 -> next == 3 || next == 6 || next == 7; // 已付款 → 已确认 | 取消 | 退款中
            case 3 -> next == 4 || next == 6;          // 已确认 → 服务中 | 取消
            case 4 -> next == 5;                        // 服务中 → 已完成
            case 5 -> false;                            // 已完成 → 不可变更
            case 6 -> false;                            // 已取消 → 不可变更
            case 7 -> next == 8 || next == 2;          // 退款中 → 已退款 | 退款失败回已付款
            case 8 -> false;                            // 已退款 → 不可变更
            default -> false;
        };
        if (!valid) {
            throw new BusinessException(String.format(
                    "订单 [%s] 状态流转不合法：%s → %s",
                    orderNo, orderStatusDesc(current), orderStatusDesc(next)));
        }
    }

    private String orderStatusDesc(byte status) {
        return switch (status) {
            case 1 -> "待付款";
            case 2 -> "已付款";
            case 3 -> "已确认";
            case 4 -> "服务中";
            case 5 -> "已完成";
            case 6 -> "已取消";
            case 7 -> "退款中";
            case 8 -> "已退款";
            default -> "未知(" + status + ")";
        };
    }

    /**
     * 生成订单号：ORD + yyyyMMddHHmmss + 6位随机
     */
    private String generateOrderNo() {
        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String rand = UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
        return "ORD" + ts + rand;
    }
}
