package com.PJDM.controller;

import com.PJDM.common.R;
import com.PJDM.dto.ValueAddedSelectDTO;
import com.PJDM.pojo.ServicePackage;
import com.PJDM.pojo.ValueAddedOrder;
import com.PJDM.service.IServicePackageService;
import com.PJDM.service.IValueAddedOrderService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 患者端 - 增值服务接口
 */
@Slf4j
@RestController
@Tag(name = "患者端-增值服务", description = "增值服务查询与管理")
public class ValueAddedController {

    @Autowired private IServicePackageService packageService;
    @Autowired private IValueAddedOrderService valueAddedOrderService;

    @GetMapping("/value-added/services")
    @Operation(summary = "获取增值服务列表")
    public R<IPage<ServicePackage>> getServices(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<ServicePackage> w = new LambdaQueryWrapper<ServicePackage>()
                .eq(ServicePackage::getStatus, (byte) 1)
                .eq(ServicePackage::getPackageType, (byte) 2)  // 增值套餐
                .orderByAsc(ServicePackage::getSort);
        return R.success(packageService.page(new Page<>(page, pageSize), w));
    }

    @GetMapping("/value-added/service/{serviceId}")
    @Operation(summary = "获取增值服务详情")
    public R<ServicePackage> getServiceDetail(@PathVariable Long serviceId) {
        return R.success(packageService.getById(serviceId));
    }

    @PostMapping("/value-added/select")
    @Operation(summary = "选择增值服务")
    public R<String> selectService(@RequestBody ValueAddedSelectDTO dto) {
        log.info("[增值服务] 选择 serviceId={} orderNo={}", dto.getServiceId(), dto.getOrderNo());
        ServicePackage pkg = packageService.getById(dto.getServiceId());
        if (pkg == null) return R.error("增值服务不存在");
        ValueAddedOrder order = new ValueAddedOrder();
        order.setOrderNo(dto.getOrderNo());
        order.setServiceFee(pkg.getPrice() != null && dto.getQuantity() != null
                ? pkg.getPrice().multiply(BigDecimal.valueOf(dto.getQuantity()))
                : pkg.getPrice());
        order.setCurrency(pkg.getCurrency());
        order.setServiceContent(pkg.getDescription());
        order.setOrderStatus((byte) 1);
        order.setCreateTime(LocalDateTime.now());
        valueAddedOrderService.save(order);
        return R.success("选择成功");
    }

    @PostMapping("/value-added/cancel/{selectionId}")
    @Operation(summary = "取消增值服务")
    public R<String> cancelService(@PathVariable Long selectionId) {
        ValueAddedOrder order = valueAddedOrderService.getById(selectionId);
        if (order == null) return R.error("记录不存在");
        order.setOrderStatus((byte) 5);
        order.setUpdateTime(LocalDateTime.now());
        valueAddedOrderService.updateById(order);
        return R.success("取消成功");
    }

    @GetMapping("/value-added/selected/{orderNo}")
    @Operation(summary = "获取订单已选增值服务")
    public R<List<ValueAddedOrder>> getSelected(@PathVariable String orderNo) {
        LambdaQueryWrapper<ValueAddedOrder> w = new LambdaQueryWrapper<ValueAddedOrder>()
                .eq(ValueAddedOrder::getOrderNo, orderNo)
                .ne(ValueAddedOrder::getOrderStatus, (byte) 5)
                .orderByDesc(ValueAddedOrder::getCreateTime);
        return R.success(valueAddedOrderService.list(w));
    }

    @GetMapping("/value-added/categories")
    @Operation(summary = "获取增值服务分类")
    public R<List<String>> getCategories() {
        List<String> cats = packageService.listObjs(
                new LambdaQueryWrapper<ServicePackage>()
                        .select(ServicePackage::getPackageType)
                        .eq(ServicePackage::getStatus, (byte) 1)
                        .groupBy(ServicePackage::getPackageType),
                obj -> String.valueOf(obj));
        return R.success(cats);
    }

    @GetMapping("/value-added/popular")
    @Operation(summary = "获取热门增值服务")
    public R<List<ServicePackage>> getPopular(
            @RequestParam(defaultValue = "5") Integer limit) {
        LambdaQueryWrapper<ServicePackage> w = new LambdaQueryWrapper<ServicePackage>()
                .eq(ServicePackage::getStatus, (byte) 1)
                .eq(ServicePackage::getPackageType, (byte) 2)
                .orderByAsc(ServicePackage::getSort)
                .last("LIMIT " + limit);
        return R.success(packageService.list(w));
    }

    @PostMapping("/value-added/calculate")
    @Operation(summary = "计算增值服务总价")
    public R<BigDecimal> calculateTotal(@RequestBody Map<String, Object> body) {
        @SuppressWarnings("unchecked")
        List<Integer> ids = (List<Integer>) body.get("service_ids");
        if (ids == null || ids.isEmpty()) return R.success(BigDecimal.ZERO);
        BigDecimal total = ids.stream()
                .map(id -> packageService.getById(Long.valueOf(id)))
                .filter(p -> p != null && p.getPrice() != null)
                .map(ServicePackage::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return R.success(total);
    }

    @GetMapping("/value-added/recommended/{orderNo}")
    @Operation(summary = "获取推荐增值服务")
    public R<List<ServicePackage>> getRecommended(@PathVariable String orderNo) {
        LambdaQueryWrapper<ServicePackage> w = new LambdaQueryWrapper<ServicePackage>()
                .eq(ServicePackage::getStatus, (byte) 1)
                .eq(ServicePackage::getPackageType, (byte) 2)
                .orderByAsc(ServicePackage::getSort)
                .last("LIMIT 5");
        return R.success(packageService.list(w));
    }
}
