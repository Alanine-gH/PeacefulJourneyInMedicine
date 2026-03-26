package com.PJDM.mapper;

import com.PJDM.dto.EvaluationQueryDTO;
import com.PJDM.pojo.OrderEvaluation;
import com.PJDM.vo.EvaluationListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 订单评价表 Mapper
 *
 * @author Alanine
 * @since 2026-03-17
 */
@Mapper
public interface OrderEvaluationMapper extends BaseMapper<OrderEvaluation> {

    /**
     * 分页查询评价列表（LEFT JOIN user_user、user_accompanist）
     */
    IPage<EvaluationListVO> selectEvaluationListPage(@Param("page") Page<EvaluationListVO> page,
                                                   @Param("q") EvaluationQueryDTO query);
}
