package com.PJDM.controller;

import com.PJDM.common.R;
import com.PJDM.dto.OrderEvaluationDTO;
import com.PJDM.dto.OrderQueryDTO;
import com.PJDM.dto.OrderStatusDTO;
import com.PJDM.dto.OrderUpdateDTO;
import com.PJDM.service.IOrderOrderService;
import com.PJDM.vo.OrderDetailVO;
import com.PJDM.vo.OrderListVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订单管理 Controller
 *
 * @author Alanine
 */
@Slf4j
@RestController
@RequestMapping("/order")
@Tag(name = "订单管理", description = "订单增删改查及状态管理")
public class OrderOrderController {

    @Autowired
    private IOrderOrderService orderOrderService;

    @GetMapping("/stat/count")
    @Operation(summary = "查询订单总数")
    public R<Long> count() {
        return R.success(orderOrderService.count());
    }

    @GetMapping("/list")
    @Operation(summary = "分页查询订单列表")
    public R<IPage<OrderListVO>> list(OrderQueryDTO query) {
        log.info("[订单] 列表查询 query={}", query);
        return R.success(orderOrderService.getOrderListPage(query));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取订单详情（含服务项目列表）")
    public R<OrderDetailVO> detail(@PathVariable Long id) {
        log.info("[订单] 详情 id={}", id);
        return R.success(orderOrderService.getOrderDetail(id));
    }

    @PostMapping("/create")
    @Operation(summary = "新增订单")
    public R<String> add(@RequestBody OrderUpdateDTO dto) {
        log.info("[订单] 新增 userId={} patientName={}", dto.getUserId(), dto.getPatientName());
        orderOrderService.addOrder(dto);
        return R.success("新增成功");
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改订单")
    public R<String> update(@PathVariable Long id, @RequestBody OrderUpdateDTO dto) {
        log.info("[订单] 修改 id={}", id);
        orderOrderService.updateOrder(id, dto);
        return R.success("修改成功");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除订单")
    public R<String> delete(@PathVariable Long id) {
        log.info("[订单] 删除 id={}", id);
        orderOrderService.deleteOrder(id);
        return R.success("删除成功");
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "变更订单状态")
    public R<String> updateStatus(@PathVariable Long id, @RequestBody OrderStatusDTO dto) {
        log.info("[订单] 状态变更 id={} status={}", id, dto.getOrderStatus());
        orderOrderService.updateOrderStatus(id, dto);
        return R.success("状态变更成功");
    }

    /**
     * 患者端API - 根据订单号查询订单详情
     */
    @GetMapping("/order-no/{orderNo}")
    @Operation(summary = "根据订单号查询订单")
    public R<OrderDetailVO> getByOrderNo(@PathVariable String orderNo) {
        log.info("[订单] 根据订单号查询 orderNo={}", orderNo);
        return R.success(orderOrderService.getDetailByOrderNo(orderNo));
    }

    /**
     * 患者端API - 评价订单
     */
    @PostMapping("/{id}/evaluate")
    @Operation(summary = "订单评价")
    public R<String> evaluateOrder(@PathVariable Long id, @RequestBody OrderEvaluationDTO dto) {
        log.info("[订单] 评价订单 orderId={} rating={}", id, dto.getRatingScore());
        // TODO: 实现评价逻辑
        return R.error("评价功能暂未实现");
    }
}
