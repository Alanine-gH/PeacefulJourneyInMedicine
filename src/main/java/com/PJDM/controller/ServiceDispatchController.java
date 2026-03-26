package com.PJDM.controller;

import com.PJDM.common.R;
import com.PJDM.dto.DispatchQueryDTO;
import com.PJDM.exception.ValidationException;
import com.PJDM.pojo.ServiceDispatch;
import com.PJDM.service.IServiceDispatchService;
import com.PJDM.vo.DispatchListVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/service/dispatch")
@Tag(name = "调度派单管理", description = "派单查询、手动派单、状态变更")
public class ServiceDispatchController {

    @Autowired private IServiceDispatchService dispatchService;

    @GetMapping("/list")
    @Operation(summary = "分页查询派单列表")
    public R<IPage<DispatchListVO>> list(DispatchQueryDTO query) {
        log.info("[派单] 列表查询 query={}", query);
        return R.success(dispatchService.getDispatchListPage(query));
    }

    @GetMapping("/{id}")
    @Operation(summary = "派单详情")
    public R<ServiceDispatch> detail(@PathVariable Long id) {
        log.info("[派单] 详情 id={}", id);
        return R.success(dispatchService.getDispatchDetail(id));
    }

    @PostMapping
    @Operation(summary = "手动派单（orderId + accompanistId）")
    public R<String> add(@RequestBody Map<String, Object> body) {
        Long orderId = body.get("orderId") != null ? Long.valueOf(body.get("orderId").toString()) : null;
        Long accompanistId = body.get("accompanistId") != null ? Long.valueOf(body.get("accompanistId").toString()) : null;
        String remark = body.get("remark") != null ? body.get("remark").toString() : null;
        log.info("[派单] 手动派单 orderId={} accompanistId={}", orderId, accompanistId);
        dispatchService.addDispatch(orderId, accompanistId, remark);
        return R.success("派单成功");
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "变更派单状态（2已接单 3已拒绝 4已取消）")
    public R<String> updateStatus(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Byte status = body.get("status") != null ? Byte.valueOf(body.get("status").toString()) : null;
        if (status == null) throw new ValidationException("status", "不能为空");
        String rejectReason = body.get("rejectReason") != null ? body.get("rejectReason").toString() : null;
        log.info("[派单] 状态变更 id={} status={}", id, status);
        dispatchService.updateStatus(id, status, rejectReason);
        return R.success("状态变更成功");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除派单")
    public R<String> delete(@PathVariable Long id) {
        log.info("[派单] 删除 id={}", id);
        dispatchService.deleteDispatch(id);
        return R.success("删除成功");
    }
}
