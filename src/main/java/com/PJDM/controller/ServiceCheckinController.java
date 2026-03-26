package com.PJDM.controller;

import com.PJDM.common.R;
import com.PJDM.dto.CheckinQueryDTO;
import com.PJDM.pojo.ServiceCheckin;
import com.PJDM.service.IServiceCheckinService;
import com.PJDM.vo.CheckinListVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/service/checkin")
@Tag(name = "打卡记录管理", description = "陪诊师服务过程打卡查询")
public class ServiceCheckinController {

    @Autowired private IServiceCheckinService checkinService;

    @GetMapping("/list")
    @Operation(summary = "分页查询打卡记录（LEFT JOIN user_accompanist, order_order）")
    public R<IPage<CheckinListVO>> list(CheckinQueryDTO query) {
        return R.success(checkinService.getCheckinListPage(query));
    }

    @GetMapping("/{id}")
    @Operation(summary = "打卡记录详情")
    public R<ServiceCheckin> detail(@PathVariable Long id) {
        try { return R.success(checkinService.getCheckinDetail(id)); }
        catch (RuntimeException e) { return R.error(e.getMessage()); }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除打卡记录")
    public R<String> delete(@PathVariable Long id) {
        try { checkinService.deleteCheckin(id); return R.success("删除成功"); }
        catch (RuntimeException e) { return R.error(e.getMessage()); }
    }
}
