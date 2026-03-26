package com.PJDM.controller;

import com.PJDM.common.R;
import com.PJDM.dto.PaymentCreateDTO;
import com.PJDM.pojo.PaymentRecord;
import com.PJDM.service.IPaymentRecordService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 支付接口
 */
@Slf4j
@RestController
@Tag(name = "支付管理", description = "支付创建、查询、确认、退款")
public class PaymentApiController {

    @Autowired private IPaymentRecordService paymentService;

    @PostMapping("/payment/create")
    @Operation(summary = "创建支付订单")
    public R<PaymentRecord> createPayment(
            @RequestParam Long userId,
            @RequestBody PaymentCreateDTO dto) {
        return R.success(paymentService.createPayment(userId, dto));
    }

    @GetMapping("/payment/status/{paymentNo}")
    @Operation(summary = "获取支付状态")
    public R<PaymentRecord> getPaymentStatus(@PathVariable String paymentNo) {
        return R.success(paymentService.getByPaymentNo(paymentNo));
    }

    @PostMapping("/payment/confirm/{paymentNo}")
    @Operation(summary = "确认支付")
    public R<String> confirmPayment(
            @PathVariable String paymentNo,
            @RequestBody Map<String, String> body) {
        paymentService.confirmPayment(
                paymentNo,
                body.get("payment_type"),
                body.get("transaction_id"));
        return R.success("支付确认成功");
    }

    @PostMapping("/payment/cancel/{paymentNo}")
    @Operation(summary = "取消支付")
    public R<String> cancelPayment(@PathVariable String paymentNo) {
        paymentService.cancelPayment(paymentNo);
        return R.success("取消成功");
    }

    @GetMapping("/payment/methods")
    @Operation(summary = "获取支付方式列表")
    public R<List<Map<String, String>>> getPaymentMethods() {
        return R.success(List.of(
                Map.of("type", "wechat", "name", "微信支付"),
                Map.of("type", "alipay", "name", "支付宝"),
                Map.of("type", "cash", "name", "现金")
        ));
    }

    @GetMapping("/payment/records")
    @Operation(summary = "获取支付记录列表")
    public R<IPage<PaymentRecord>> getPaymentRecords(
            @RequestParam Long userId,
            @RequestParam(required = false) Byte status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return R.success(paymentService.getPaymentPage(userId, status, page, pageSize));
    }

    @GetMapping("/payment/records/{paymentNo}")
    @Operation(summary = "获取支付记录详情")
    public R<PaymentRecord> getPaymentRecordDetail(@PathVariable String paymentNo) {
        return R.success(paymentService.getByPaymentNo(paymentNo));
    }

    @PostMapping("/payment/refund/{paymentNo}")
    @Operation(summary = "申请退款")
    public R<String> applyRefund(
            @PathVariable String paymentNo,
            @RequestBody Map<String, Object> body) {
        BigDecimal amount = body.get("amount") != null
                ? new BigDecimal(body.get("amount").toString()) : null;
        String reason = (String) body.get("reason");
        paymentService.applyRefund(paymentNo, amount, reason);
        return R.success("退款申请成功");
    }

    @GetMapping("/payment/refund-records")
    @Operation(summary = "获取退款记录")
    public R<IPage<PaymentRecord>> getRefundRecords(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        // 状态4=退款中，5=已退款
        return R.success(paymentService.getPaymentPage(userId, (byte) 4, page, pageSize));
    }

    // ==================== 支付授权（简化实现） ====================

    @GetMapping("/payment/auth/info")
    @Operation(summary = "获取支付授权信息")
    public R<Map<String, Object>> getAuthInfo(@RequestParam Long userId) {
        return R.success(Map.of("userId", userId, "hasPassword", false, "boundMethods", List.of()));
    }

    @PostMapping("/payment/auth/password")
    @Operation(summary = "设置支付密码")
    public R<String> setPaymentPassword(@RequestBody Map<String, String> body) {
        return R.success("设置成功");
    }

    @PostMapping("/payment/auth/verify")
    @Operation(summary = "验证支付密码")
    public R<Boolean> verifyPaymentPassword(@RequestBody Map<String, String> body) {
        return R.success(true);
    }

    @PostMapping("/payment/auth/bind")
    @Operation(summary = "绑定支付方式")
    public R<String> bindPaymentMethod(@RequestBody Map<String, String> body) {
        return R.success("绑定成功");
    }

    @PostMapping("/payment/auth/unbind/{type}")
    @Operation(summary = "解绑支付方式")
    public R<String> unbindPaymentMethod(@PathVariable String type) {
        return R.success("解绑成功");
    }
}
