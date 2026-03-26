package com.PJDM.mapper;

import com.PJDM.pojo.PaymentRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 支付记录 Mapper
 */
@Mapper
public interface PaymentRecordMapper extends BaseMapper<PaymentRecord> {

    @Select("SELECT * FROM payment_record WHERE payment_no = #{paymentNo} LIMIT 1")
    PaymentRecord selectByPaymentNo(@Param("paymentNo") String paymentNo);

    @Select("SELECT * FROM payment_record WHERE order_no = #{orderNo} ORDER BY id DESC LIMIT 1")
    PaymentRecord selectLatestByOrderNo(@Param("orderNo") String orderNo);

    IPage<PaymentRecord> selectPaymentPage(
            @Param("page") Page<PaymentRecord> page,
            @Param("userId") Long userId,
            @Param("status") Byte status);
}
