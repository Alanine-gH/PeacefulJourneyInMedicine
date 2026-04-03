package com.PJDM.controller;

import com.PJDM.common.R;
import com.PJDM.dto.OrderUpdateDTO;
import com.PJDM.dto.OrderQueryDTO;
import com.PJDM.pojo.UserUser;
import com.PJDM.service.IOrderOrderService;
import com.PJDM.service.IUserUserService;
import com.PJDM.vo.OrderDetailVO;
import com.PJDM.vo.OrderListVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 患者端 - 订单接口（患者视角路径别名）
 */
@Slf4j
@RestController
@Tag(name = "患者端-订单", description = "患者端订单创建、查询、取消")
public class PatientOrderController {

    @Autowired private IOrderOrderService orderService;
    @Autowired private IUserUserService userService;

    /**
     * 患者创建订单  POST /patient/orders
     */
    @PostMapping("/patient/orders")
    @Operation(summary = "患者创建订单")
    public R<String> createOrder(@RequestBody OrderUpdateDTO dto) {
        Long userId = dto.getUserId();
        System.out.println("dto = " + dto);
        log.info("[患者订单] 创建 userId={}", userId);

        // 通过userId查询用户对象
        UserUser user = userService.getById(userId);
        if (user != null) {
            // 将用户信息赋值到dto
            dto.setPatientName(user.getRealName());
            dto.setPatientGender(user.getGender());
            dto.setPatientAge(user.getAge());
            dto.setUserName(user.getUsername());
            dto.setUserPhone(user.getPhone());
        }

        orderService.addOrder(dto);
        return R.success("创建成功");
    }

    /**
     * 患者获取订单列表  GET /patient/orders  (alias for /order/list)
     */
    @GetMapping("/patient/orders")
    @Operation(summary = "患者获取订单列表（别名）")
    public R<IPage<OrderListVO>> getOrders(OrderQueryDTO query) {
        return R.success(orderService.getOrderListPage(query));
    }

    /**
     * 患者获取订单详情（通过订单号）  GET /patient/orders/{orderNo}
     */
    @GetMapping("/patient/orders/{orderNo}")
    @Operation(summary = "患者获取订单详情")
    public R<OrderDetailVO> getOrderDetail(@PathVariable String orderNo) {
        return R.success(orderService.getDetailByOrderNo(orderNo));
    }
}
