package com.PJDM.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.PJDM.dto.PaymentCreateDTO;
import com.PJDM.exception.BusinessException;
import com.PJDM.exception.ResourceNotFoundException;
import com.PJDM.exception.ValidationException;
import com.PJDM.mapper.PaymentRecordMapper;
import com.PJDM.pojo.PaymentRecord;
import com.PJDM.service.IPaymentRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Service
public class PaymentRecordServiceImpl extends ServiceImpl<PaymentRecordMapper, PaymentRecord>
        implements IPaymentRecordService {

    @Value("${user-module.page.default-size:10}")
    private int defaultPageSize;
    @Value("${user-module.page.max-size:100}")
    private int maxPageSize;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentRecord createPayment(Long userId, PaymentCreateDTO dto) {
        if (!StringUtils.hasText(dto.getOrderNo())) throw new ValidationException("orderNo", "订单号不能为空");
        if (dto.getAmount() == null || dto.getAmount().compareTo(BigDecimal.ZERO) <= 0)
            throw new ValidationException("amount", "支付金额必须大于0");
        PaymentRecord rec = new PaymentRecord();
        rec.setPaymentNo(generatePaymentNo());
        rec.setOrderNo(dto.getOrderNo());
        rec.setUserId(userId);
        rec.setPaymentType(dto.getPaymentType());
        rec.setAmount(dto.getAmount());
        rec.setCurrency(StringUtils.hasText(dto.getCurrency()) ? dto.getCurrency() : "CNY");
        rec.setDescription(dto.getDescription());
        rec.setStatus((byte) 1);
        rec.setCreateTime(LocalDateTime.now());
        save(rec);
        log.info("[支付] 创建 paymentNo={} orderNo={} amount={}", rec.getPaymentNo(), dto.getOrderNo(), dto.getAmount());
        return rec;
    }

    @Override
    public PaymentRecord getByPaymentNo(String paymentNo) {
        PaymentRecord rec = baseMapper.selectByPaymentNo(paymentNo);
        if (rec == null) throw new ResourceNotFoundException("支付记录", paymentNo);
        return rec;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmPayment(String paymentNo, String paymentType, String transactionId) {
        PaymentRecord rec = getByPaymentNo(paymentNo);
        if (rec.getStatus() != 1) throw new BusinessException("当前支付状态不允许确认");
        rec.setStatus((byte) 2);
        rec.setPaymentType(paymentType);
        rec.setTransactionId(transactionId);
        rec.setPayTime(LocalDateTime.now());
        rec.setUpdateTime(LocalDateTime.now());
        updateById(rec);
        log.info("[支付] 确认 paymentNo={} transactionId={}", paymentNo, transactionId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelPayment(String paymentNo) {
        PaymentRecord rec = getByPaymentNo(paymentNo);
        if (rec.getStatus() != 1) throw new BusinessException("仅待支付状态可取消");
        rec.setStatus((byte) 3);
        rec.setUpdateTime(LocalDateTime.now());
        updateById(rec);
        log.info("[支付] 取消 paymentNo={}", paymentNo);
    }

    @Override
    public IPage<PaymentRecord> getPaymentPage(Long userId, Byte status, Integer pageNum, Integer pageSize) {
        int size = Math.min(pageSize == null ? defaultPageSize : pageSize, maxPageSize);
        return baseMapper.selectPaymentPage(
                new Page<>(pageNum == null ? 1 : pageNum, size), userId, status);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void applyRefund(String paymentNo, BigDecimal amount, String reason) {
        PaymentRecord rec = getByPaymentNo(paymentNo);
        if (rec.getStatus() != 2) throw new BusinessException("仅已支付状态可申请退款");
        if (amount != null && amount.compareTo(rec.getAmount()) > 0)
            throw new BusinessException("退款金额不能超过支付金额");
        rec.setStatus((byte) 4);
        rec.setRefundAmount(amount != null ? amount : rec.getAmount());
        rec.setRefundReason(reason);
        rec.setUpdateTime(LocalDateTime.now());
        updateById(rec);
        log.info("[支付] 申请退款 paymentNo={} amount={}", paymentNo, amount);
    }

    private String generatePaymentNo() {
        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String rand = UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
        return "PAY" + ts + rand;
    }
}
