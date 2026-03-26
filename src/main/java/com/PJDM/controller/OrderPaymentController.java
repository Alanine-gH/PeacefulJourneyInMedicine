package com.PJDM.controller;

import com.PJDM.common.R;
import com.PJDM.dto.PaymentQueryDTO;
import com.PJDM.pojo.OrderPayment;
import com.PJDM.service.IOrderPaymentService;
import com.PJDM.vo.PaymentListVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 支付记录 Controller
 *
 * @author Alanine
 */
@Slf4j
@RestController
@RequestMapping("/order/payment")
@Tag(name = "支付记录管理", description = "支付记录查询与删除")
public class OrderPaymentController {

    @Autowired
    private IOrderPaymentService paymentService;

    @GetMapping("/list")
    @Operation(summary = "分页查询支付记录")
    public R<IPage<PaymentListVO>> list(PaymentQueryDTO query) {
        return R.success(paymentService.getPaymentListPage(query));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取支付详情")
    public R<OrderPayment> detail(@PathVariable Long id) {
        try {
            return R.success(paymentService.getPaymentDetail(id));
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除支付记录")
    public R<String> delete(@PathVariable Long id) {
        try {
            paymentService.deletePayment(id);
            return R.success("删除成功");
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }
}
