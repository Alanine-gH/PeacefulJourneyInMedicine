package com.PJDM.controller;

import com.PJDM.common.R;
import com.PJDM.dto.EvaluationQueryDTO;
import com.PJDM.pojo.OrderEvaluation;
import com.PJDM.service.IOrderEvaluationService;
import com.PJDM.vo.EvaluationListVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订单评价 Controller
 *
 * @author Alanine
 */
@Slf4j
@RestController
@RequestMapping("/order/evaluation")
@Tag(name = "订单评价管理", description = "评价查询与删除")
public class OrderEvaluationController {

    @Autowired
    private IOrderEvaluationService evaluationService;

    @GetMapping("/list")
    @Operation(summary = "分页查询评价列表（LEFT JOIN user_user、user_accompanist）")
    public R<IPage<EvaluationListVO>> list(EvaluationQueryDTO query) {
        return R.success(evaluationService.getEvaluationListPage(query));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取评价详情")
    public R<OrderEvaluation> detail(@PathVariable Long id) {
        try {
            return R.success(evaluationService.getEvaluationDetail(id));
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除评价")
    public R<String> delete(@PathVariable Long id) {
        try {
            evaluationService.deleteEvaluation(id);
            return R.success("删除成功");
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }
}
