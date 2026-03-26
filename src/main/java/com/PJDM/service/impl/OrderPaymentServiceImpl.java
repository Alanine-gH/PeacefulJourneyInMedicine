package com.PJDM.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.PJDM.dto.PaymentQueryDTO;
import com.PJDM.mapper.OrderPaymentMapper;
import com.PJDM.pojo.OrderPayment;
import com.PJDM.service.IOrderPaymentService;
import com.PJDM.vo.PaymentListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 支付记录表 Service 实现
 *
 * @author Alanine
 */
@Slf4j
@Service
public class OrderPaymentServiceImpl extends ServiceImpl<OrderPaymentMapper, OrderPayment>
        implements IOrderPaymentService {

    @Value("${user-module.page.default-size:10}")
    private int defaultPageSize;

    @Value("${user-module.page.max-size:100}")
    private int maxPageSize;

    @Override
    public IPage<PaymentListVO> getPaymentListPage(PaymentQueryDTO query) {
        int size = Math.min(query.getPageSize() == null ? defaultPageSize : query.getPageSize(), maxPageSize);
        Page<PaymentListVO> page = new Page<>(query.getPageNum() == null ? 1 : query.getPageNum(), size);
        return baseMapper.selectPaymentListPage(page, query);
    }

    @Override
    public OrderPayment getPaymentDetail(Long id) {
        OrderPayment pay = getById(id);
        if (pay == null) throw new RuntimeException("支付记录不存在");
        return pay;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePayment(Long id) {
        if (getById(id) == null) throw new RuntimeException("支付记录不存在");
        removeById(id);
        log.info("删除支付记录 [{}] 成功", id);
    }
}
