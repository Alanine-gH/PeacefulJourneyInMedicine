package com.PJDM.service;

import com.PJDM.dto.PaymentQueryDTO;
import com.PJDM.pojo.OrderPayment;
import com.PJDM.vo.PaymentListVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 支付记录表 Service
 *
 * @author Alanine
 */
public interface IOrderPaymentService extends IService<OrderPayment> {

    /** 分页查询支付记录 */
    IPage<PaymentListVO> getPaymentListPage(PaymentQueryDTO query);

    /** 获取支付详情 */
    OrderPayment getPaymentDetail(Long id);

    /** 删除支付记录 */
    void deletePayment(Long id);
}
