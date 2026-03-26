package com.PJDM.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.PJDM.dto.DispatchQueryDTO;
import com.PJDM.exception.ResourceNotFoundException;
import com.PJDM.exception.ValidationException;
import com.PJDM.mapper.OrderOrderMapper;
import com.PJDM.mapper.ServiceDispatchMapper;
import com.PJDM.mapper.UserAccompanistMapper;
import com.PJDM.pojo.OrderOrder;
import com.PJDM.pojo.ServiceDispatch;
import com.PJDM.pojo.UserAccompanist;
import com.PJDM.service.IServiceDispatchService;
import com.PJDM.vo.DispatchListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Service
public class ServiceDispatchServiceImpl extends ServiceImpl<ServiceDispatchMapper, ServiceDispatch>
        implements IServiceDispatchService {

    @Value("${user-module.page.default-size:10}")
    private int defaultPageSize;
    @Value("${user-module.page.max-size:100}")
    private int maxPageSize;

    @Autowired
    private UserAccompanistMapper accompanistMapper;
    @Autowired
    private OrderOrderMapper orderOrderMapper;

    @Override
    public IPage<DispatchListVO> getDispatchListPage(DispatchQueryDTO query) {
        int size = Math.min(query.getPageSize() == null ? defaultPageSize : query.getPageSize(), maxPageSize);
        return baseMapper.selectDispatchListPage(
                new Page<>(query.getPageNum() == null ? 1 : query.getPageNum(), size), query);
    }

    @Override
    public ServiceDispatch getDispatchDetail(Long id) {
        ServiceDispatch d = getById(id);
        if (d == null) throw new ResourceNotFoundException("派单记录", id);
        return d;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDispatch(Long orderId, Long accompanistId, String remark) {
        if (orderId == null) throw new ValidationException("orderId", "订单ID不能为空");
        if (accompanistId == null) throw new ValidationException("accompanistId", "陪诊师ID不能为空");

        // 校验陪诊师是否存在，并读取姓名/手机用于冗余存储
        UserAccompanist accompanist = accompanistMapper.selectById(accompanistId);
        if (accompanist == null) throw new ResourceNotFoundException("陪诊师", accompanistId);

        // 校验订单是否存在，读取订单号
        OrderOrder order = orderOrderMapper.selectById(orderId);
        if (order == null) throw new ResourceNotFoundException("订单", orderId);

        ServiceDispatch d = new ServiceDispatch();
        d.setDispatchNo(genDispatchNo());
        d.setOrderId(orderId);
        d.setOrderNo(order.getOrderNo());
        d.setAccompanistId(accompanistId);
        // 冗余陪诊师姓名和手机，供列表展示
        d.setAccompanistName(accompanist.getRealName());
        d.setAccompanistPhone(accompanist.getPhone());
        d.setDispatchType((byte) 2);  // 手动派单
        d.setDispatchStatus((byte) 1); // 待接单
        d.setPushTime(LocalDateTime.now());
        if (StringUtils.hasText(remark)) d.setRemark(remark);
        save(d);
        log.info("手动派单 [{}] orderId={} accompanistId={} accompanistName={}",
                d.getDispatchNo(), orderId, accompanistId, accompanist.getRealName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long id, Byte status, String rejectReason) {
        ServiceDispatch exist = getById(id);
        if (exist == null) throw new ResourceNotFoundException("派单记录", id);
        ServiceDispatch d = new ServiceDispatch();
        d.setId(id);
        d.setDispatchStatus(status);
        if (status == 2) d.setReceiveTime(LocalDateTime.now());
        if (status == 3) {
            d.setRejectTime(LocalDateTime.now());
            if (StringUtils.hasText(rejectReason)) d.setRejectReason(rejectReason);
        }
        updateById(d);
        log.info("派单 [{}] 状态变更为 {}", id, status);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDispatch(Long id) {
        if (getById(id) == null) throw new ResourceNotFoundException("派单记录", id);
        removeById(id);
    }

    private String genDispatchNo() {
        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "DSP" + ts + UUID.randomUUID().toString().replace("-", "").substring(0, 4).toUpperCase();
    }
}
