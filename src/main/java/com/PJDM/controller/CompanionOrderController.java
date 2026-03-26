package com.PJDM.controller;

import com.PJDM.common.R;
import com.PJDM.dto.CompanionCertificationDTO;
import com.PJDM.pojo.OrderOrder;
import com.PJDM.pojo.UserAccompanist;
import com.PJDM.service.IOrderOrderService;
import com.PJDM.service.IUserAccompanistService;
import com.PJDM.vo.CompanionWorkInfoVO;
import com.PJDM.vo.OrderDetailVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 陪诊师端 - 个人中心 / 订单 / 资格认证
 */
@Slf4j
@RestController
@RequestMapping("/companion")
@Tag(name = "陪诊师端-订单认证", description = "陪诊师个人中心、订单管理、资格认证")
public class CompanionOrderController {

    @Autowired
    private IUserAccompanistService accompanistService;
    @Autowired
    private IOrderOrderService orderService;

    // ==================== 个人中心 ====================

    @GetMapping("/profile")
    @Operation(summary = "获取陪诊师个人信息")
    public R<UserAccompanist> getProfile(@RequestParam Long accompanistId) {
        UserAccompanist a = accompanistService.getById(accompanistId);
        if (a == null) return R.error("陪诊师不存在");
        return R.success(a);
    }

    @PutMapping("/profile")
    @Operation(summary = "更新陪诊师个人信息")
    public R<String> updateProfile(@RequestParam Long accompanistId,
                                   @RequestBody Map<String, Object> body) {
        UserAccompanist a = accompanistService.getById(accompanistId);
        if (a == null) return R.error("陪诊师不存在");
        if (body.get("introduction") != null) a.setRemark(body.get("introduction").toString());
        if (body.get("language_ability") != null) a.setLanguageAbility(body.get("language_ability").toString());
        if (body.get("specialties") != null) a.setSpecialties(body.get("specialties").toString());
        a.setUpdateTime(LocalDateTime.now());
        accompanistService.updateById(a);
        return R.success("更新成功");
    }

    @GetMapping("/work-info")
    @Operation(summary = "获取工作统计信息")
    public R<CompanionWorkInfoVO> getWorkInfo(@RequestParam Long accompanistId) {
        UserAccompanist a = accompanistService.getById(accompanistId);
        if (a == null) return R.error("陪诊师不存在");
        CompanionWorkInfoVO vo = new CompanionWorkInfoVO();
        vo.setAccompanistId(a.getId());
        vo.setRealName(a.getRealName());
        vo.setAccompanyStatus(a.getAccompanyStatus());
        vo.setAuditStatus(a.getAuditStatus());
        vo.setServiceCount(a.getServiceCount());
        vo.setRatingScore(a.getRatingScore());
        vo.setOnTimeRate(a.getOnTimeRate());
        vo.setComplaintCount(a.getComplaintCount());
        long active = orderService.count(new LambdaQueryWrapper<OrderOrder>()
                .eq(OrderOrder::getAccompanistId, accompanistId)
                .eq(OrderOrder::getOrderStatus, (byte) 4));
        vo.setActiveOrderCount(active);
        long pending = orderService.count(new LambdaQueryWrapper<OrderOrder>()
                .eq(OrderOrder::getOrderStatus, (byte) 2)
                .isNull(OrderOrder::getAccompanistId));
        vo.setPendingOrderCount(pending);
        return R.success(vo);
    }

    // ==================== 订单管理 ====================

    @GetMapping("/orders/available")
    @Operation(summary = "获取待接订单列表")
    public R<IPage<OrderOrder>> getAvailableOrders(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<OrderOrder> w = new LambdaQueryWrapper<OrderOrder>()
                .eq(OrderOrder::getOrderStatus, (byte) 2)
                .isNull(OrderOrder::getAccompanistId)
                .orderByDesc(OrderOrder::getCreateTime);
        return R.success(orderService.page(new Page<>(page, pageSize), w));
    }

    @GetMapping("/orders")
    @Operation(summary = "获取陪诊师的订单列表")
    public R<IPage<OrderOrder>> getCompanionOrders(
            @RequestParam Long accompanistId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Byte statusByte = null;
        if (status != null && !status.isBlank() && !status.equalsIgnoreCase("null")) {
            try {
                statusByte = Byte.parseByte(status);
            } catch (NumberFormatException ignored) {
            }
        }
        final Byte finalStatus = statusByte;
        LambdaQueryWrapper<OrderOrder> w = new LambdaQueryWrapper<OrderOrder>()
                .eq(OrderOrder::getAccompanistId, accompanistId)
                .eq(finalStatus != null, OrderOrder::getOrderStatus, finalStatus)
                .orderByDesc(OrderOrder::getCreateTime);
        return R.success(orderService.page(new Page<>(page, pageSize), w));
    }

    @GetMapping("/orders/{orderNo}")
    @Operation(summary = "获取订单详情")
    public R<OrderDetailVO> getOrderDetail(@PathVariable String orderNo) {
        return R.success(orderService.getDetailByOrderNo(orderNo));
    }

    @PostMapping("/orders/accept")
    @Operation(summary = "接单")
    public R<String> acceptOrder(@RequestParam Long accompanistId,
                                 @RequestBody Map<String, String> body) {
        String orderNo = body.get("order_no");
        if (!StringUtils.hasText(orderNo)) return R.error("order_no 不能为空");
        OrderOrder order = orderService.getOne(new LambdaQueryWrapper<OrderOrder>()
                .eq(OrderOrder::getOrderNo, orderNo));
        if (order == null) return R.error("订单不存在");
        if (order.getOrderStatus() != 2) return R.error("该订单当前状态不可接单");
        if (order.getAccompanistId() != null) return R.error("该订单已被其他陪诊师接单");
        order.setAccompanistId(accompanistId);
        order.setOrderStatus((byte) 3);
        order.setUpdateTime(LocalDateTime.now());
        orderService.updateById(order);
        log.info("[接单] accompanistId={} orderNo={}", accompanistId, orderNo);
        return R.success("接单成功");
    }

    // ==================== 资格认证 ====================

    @PostMapping("/certification")
    @Operation(summary = "提交资格认证")
    public R<String> submitCertification(@RequestParam Long accompanistId,
                                         @RequestBody CompanionCertificationDTO dto) {
        UserAccompanist a = accompanistService.getById(accompanistId);
        if (a == null) {
            a = new UserAccompanist();
            a.setId(accompanistId);
        }
        if (StringUtils.hasText(dto.getRealName())) a.setRealName(dto.getRealName());
        if (StringUtils.hasText(dto.getPhone())) a.setPhone(dto.getPhone());
        if (dto.getGender() != null) a.setGender(dto.getGender());
        if (dto.getAge() != null) a.setAge(dto.getAge());
        if (StringUtils.hasText(dto.getProfessionalTitle())) a.setProfessionalTitle(dto.getProfessionalTitle());
        if (dto.getMedicalBackground() != null) a.setMedicalBackground(dto.getMedicalBackground());
        if (StringUtils.hasText(dto.getCertificateNumber())) a.setCertificateNumber(dto.getCertificateNumber());
        if (StringUtils.hasText(dto.getCertificatePhoto())) a.setCertificatePhoto(dto.getCertificatePhoto());
        if (StringUtils.hasText(dto.getCriminalRecordCert())) a.setCriminalRecordCert(dto.getCriminalRecordCert());
        if (StringUtils.hasText(dto.getBusinessLicense())) a.setBusinessLicense(dto.getBusinessLicense());
        if (StringUtils.hasText(dto.getLanguageAbility())) a.setLanguageAbility(dto.getLanguageAbility());
        if (StringUtils.hasText(dto.getSpecialties())) a.setSpecialties(dto.getSpecialties());
        a.setAuditStatus((byte) 1);
        a.setUpdateTime(LocalDateTime.now());
        accompanistService.saveOrUpdate(a);
        log.info("[陪诊师认证] 提交 accompanistId={}", accompanistId);
        return R.success("认证申请已提交，请等待审核");
    }

    @GetMapping("/certification/status")
    @Operation(summary = "查询认证状态")
    public R<Map<String, Object>> getCertificationStatus(@RequestParam Long accompanistId) {
        UserAccompanist a = accompanistService.getById(accompanistId);
        if (a == null) return R.success(Map.of("auditStatus", 0, "message", "未提交认证"));
        return R.success(Map.of(
                "auditStatus", a.getAuditStatus() != null ? a.getAuditStatus() : 0,
                "accompanyStatus", a.getAccompanyStatus() != null ? a.getAccompanyStatus() : 0,
                "auditTime", a.getAuditTime() != null ? a.getAuditTime().toString() : ""
        ));
    }
}
