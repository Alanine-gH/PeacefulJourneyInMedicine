package com.PJDM.controller;

import com.PJDM.common.R;
import com.PJDM.dto.DemandPublishDTO;
import com.PJDM.dto.DemandQueryDTO;
import com.PJDM.pojo.UserDemand;
import com.PJDM.service.IUserDemandService;
import com.PJDM.vo.DemandListVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * 需求发布接口
 */
@Slf4j
@RestController
@Tag(name = "需求发布", description = "患者/陪诊师需求发布与管理")
public class DemandController {

    @Autowired private IUserDemandService demandService;

    @PostMapping("/demand/publish")
    @Operation(summary = "发布需求")
    public R<String> publishDemand(@RequestParam Long userId, @RequestBody DemandPublishDTO dto) {
        demandService.publishDemand(userId, dto);
        return R.success("发布成功");
    }

    @GetMapping("/demand/my")
    @Operation(summary = "获取我的需求列表")
    public R<IPage<DemandListVO>> getMyDemands(
            @RequestParam Long userId,
            @RequestParam(required = false) Byte status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        DemandQueryDTO q = new DemandQueryDTO();
        q.setUserId(userId); q.setStatus(status);
        q.setPageNum(page); q.setPageSize(pageSize);
        return R.success(demandService.getDemandPage(q));
    }

    @GetMapping("/demand/list")
    @Operation(summary = "获取需求列表（陪诊师端）")
    public R<IPage<DemandListVO>> getDemandList(DemandQueryDTO query) {
        if (query.getStatus() == null) query.setStatus((byte) 1);
        return R.success(demandService.getDemandPage(query));
    }

    @GetMapping("/demand/detail/{demandId}")
    @Operation(summary = "获取需求详情")
    public R<UserDemand> getDemandDetail(@PathVariable Long demandId) {
        return R.success(demandService.getDemandDetail(demandId));
    }

    @PutMapping("/demand/edit/{demandId}")
    @Operation(summary = "编辑需求")
    public R<String> editDemand(@PathVariable Long demandId, @RequestBody DemandPublishDTO dto) {
        demandService.editDemand(demandId, dto);
        return R.success("编辑成功");
    }

    @PostMapping("/demand/cancel/{demandId}")
    @Operation(summary = "取消需求")
    public R<String> cancelDemand(@PathVariable Long demandId,
                                  @RequestBody(required = false) Map<String, String> body) {
        demandService.cancelDemand(demandId, body != null ? body.get("reason") : null);
        return R.success("取消成功");
    }

    @DeleteMapping("/demand/delete/{demandId}")
    @Operation(summary = "删除需求")
    public R<String> deleteDemand(@PathVariable Long demandId) {
        demandService.deleteDemand(demandId);
        return R.success("删除成功");
    }

    @PostMapping("/demand/accept/{demandId}")
    @Operation(summary = "接单")
    public R<String> acceptDemand(@PathVariable Long demandId, @RequestParam Long accompanistId) {
        demandService.acceptDemand(demandId, accompanistId);
        return R.success("接单成功");
    }

    @PostMapping("/demand/reject/{demandId}")
    @Operation(summary = "拒绝接单")
    public R<String> rejectDemand(@PathVariable Long demandId,
                                  @RequestBody(required = false) Map<String, String> body) {
        demandService.rejectDemand(demandId, body != null ? body.get("reason") : null);
        return R.success("拒绝成功");
    }

    @GetMapping("/demand/recommended")
    @Operation(summary = "获取推荐需求")
    public R<IPage<DemandListVO>> getRecommended(@RequestParam(defaultValue = "10") Integer limit) {
        DemandQueryDTO q = new DemandQueryDTO();
        q.setStatus((byte) 1); q.setPageNum(1); q.setPageSize(limit);
        return R.success(demandService.getDemandPage(q));
    }

    @PostMapping("/demand/refresh/{demandId}")
    @Operation(summary = "刷新需求")
    public R<String> refreshDemand(@PathVariable Long demandId) {
        demandService.refreshDemand(demandId);
        return R.success("刷新成功");
    }

    @GetMapping("/demand/statistics")
    @Operation(summary = "获取需求统计")
    public R<Object> getDemandStatistics(@RequestParam Long userId) {
        return R.success(demandService.getDemandStatistics(userId));
    }
}
