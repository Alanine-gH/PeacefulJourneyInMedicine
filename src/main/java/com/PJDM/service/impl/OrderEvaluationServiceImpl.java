package com.PJDM.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.PJDM.dto.EvaluationQueryDTO;
import com.PJDM.mapper.OrderEvaluationMapper;
import com.PJDM.pojo.OrderEvaluation;
import com.PJDM.service.IOrderEvaluationService;
import com.PJDM.vo.EvaluationListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 订单评价表 Service 实现
 *
 * @author Alanine
 */
@Slf4j
@Service
public class OrderEvaluationServiceImpl extends ServiceImpl<OrderEvaluationMapper, OrderEvaluation>
        implements IOrderEvaluationService {

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
}
