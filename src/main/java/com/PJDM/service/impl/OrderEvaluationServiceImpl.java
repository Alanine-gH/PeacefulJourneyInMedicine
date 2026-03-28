package com.PJDM.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.PJDM.dto.EvaluationQueryDTO;
import com.PJDM.dto.EvaluationSubmitDTO;
import com.PJDM.mapper.OrderOrderMapper;
import com.PJDM.mapper.OrderEvaluationMapper;
import com.PJDM.pojo.OrderEvaluation;
import com.PJDM.pojo.OrderOrder;
import com.PJDM.service.IOrderEvaluationService;
import com.PJDM.vo.EvaluationListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * 订单评价表 Service 实现
 *
 * @author Alanine
 */
@Slf4j
@Service
public class OrderEvaluationServiceImpl extends ServiceImpl<OrderEvaluationMapper, OrderEvaluation>
        implements IOrderEvaluationService {

    @Autowired
    private com.PJDM.mapper.OrderOrderMapper orderOrderMapper;

    @Value("${user-module.page.default-size:10}")
    private int defaultPageSize;

    @Value("${user-module.page.max-size:100}")
    private int maxPageSize;

    @Override
    public IPage<EvaluationListVO> getEvaluationListPage(EvaluationQueryDTO query) {
        int size = Math.min(query.getPageSize() == null ? defaultPageSize : query.getPageSize(), maxPageSize);
        Page<EvaluationListVO> page = new Page<>(query.getPageNum() == null ? 1 : query.getPageNum(), size);
        return baseMapper.selectEvaluationListPage(page, query);
    }

    @Override
    public OrderEvaluation getEvaluationDetail(Long id) {
        OrderEvaluation ev = getById(id);
        if (ev == null) throw new RuntimeException("评价记录不存在");
        return ev;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteEvaluation(Long id) {
        if (getById(id) == null) throw new RuntimeException("评价记录不存在");
        removeById(id);
        log.info("删除评价记录 [{}] 成功", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitEvaluation(com.PJDM.dto.EvaluationSubmitDTO dto) {
        if (dto.getOrderNo() == null || dto.getOrderNo().isBlank())
            throw new RuntimeException("订单号不能为空");
        if (dto.getRatingScore() == null)
            throw new RuntimeException("评分不能为空");
        com.PJDM.pojo.OrderOrder order = orderOrderMapper.selectOne(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<com.PJDM.pojo.OrderOrder>()
                .eq(com.PJDM.pojo.OrderOrder::getOrderNo, dto.getOrderNo()));
        if (order == null) throw new RuntimeException("订单不存在");
        if (order.getOrderStatus() == null || order.getOrderStatus() != 5)
            throw new RuntimeException("只有已完成的订单才能评价");
        long count = count(new LambdaQueryWrapper<OrderEvaluation>()
            .eq(OrderEvaluation::getOrderNo, dto.getOrderNo()));
        if (count > 0) throw new RuntimeException("该订单已评价，请勿重复提交");
        OrderEvaluation ev = new OrderEvaluation();
        ev.setOrderId(order.getId());
        ev.setOrderNo(dto.getOrderNo());
        ev.setUserId(order.getUserId());
        ev.setAccompanistId(order.getAccompanistId());
        ev.setRatingScore(dto.getRatingScore());
        ev.setEvaluationContent(dto.getEvaluationContent());
        ev.setRatingLabels(dto.getRatingLabels());
        ev.setCreateTime(java.time.LocalDateTime.now());
        ev.setCreateBy(Boolean.TRUE.equals(dto.getAnonymous()) ? "匿名" : String.valueOf(order.getUserId()));
        save(ev);
        log.info("订单 [{}] 评价提交成功，评分={}", dto.getOrderNo(), dto.getRatingScore());
    }
}
