package com.PJDM.mapper;

import com.PJDM.dto.PaymentQueryDTO;
import com.PJDM.pojo.OrderPayment;
import com.PJDM.vo.PaymentListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 支付记录表 Mapper
 *
 * @author Alanine
 * @since 2026-03-17
 */
@Mapper
public interface OrderPaymentMapper extends BaseMapper<OrderPayment> {

    /**
     * 分页查询支付记录
     */
    IPage<PaymentListVO> selectPaymentListPage(@Param("page") Page<PaymentListVO> page,
                                              @Param("q") PaymentQueryDTO query);
}
