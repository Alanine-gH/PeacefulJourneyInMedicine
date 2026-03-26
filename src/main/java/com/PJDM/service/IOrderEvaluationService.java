package com.PJDM.service;

import com.PJDM.dto.EvaluationQueryDTO;
import com.PJDM.pojo.OrderEvaluation;
import com.PJDM.vo.EvaluationListVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 订单评价表 Service
 *
 * @author Alanine
 */
public interface IOrderEvaluationService extends IService<OrderEvaluation> {

    /** 分页查询评价列表（多表联查） */
    IPage<EvaluationListVO> getEvaluationListPage(EvaluationQueryDTO query);

    /** 获取评价详情 */
    OrderEvaluation getEvaluationDetail(Long id);

    /** 删除评价 */
    void deleteEvaluation(Long id);
}
