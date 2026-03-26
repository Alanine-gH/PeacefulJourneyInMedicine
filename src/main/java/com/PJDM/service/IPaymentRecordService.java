package com.PJDM.service;

import com.PJDM.dto.PaymentCreateDTO;
import com.PJDM.pojo.PaymentRecord;
import com.PJDM.vo.PaymentRecordVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 支付记录 Service 接口
 */
public interface IPaymentRecordService extends IService<PaymentRecord> {

    PaymentRecord createPayment(Long userId, PaymentCreateDTO dto);

    PaymentRecord getByPaymentNo(String paymentNo);

    void confirmPayment(String paymentNo, String paymentType, String transactionId);

    void cancelPayment(String paymentNo);

    IPage<PaymentRecord> getPaymentPage(Long userId, Byte status, Integer pageNum, Integer pageSize);

    void applyRefund(String paymentNo, java.math.BigDecimal amount, String reason);
}
