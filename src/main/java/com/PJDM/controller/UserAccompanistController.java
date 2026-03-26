package com.PJDM.controller;

import com.PJDM.common.R;
import com.PJDM.dto.AccompanistQueryDTO;
import com.PJDM.dto.AccompanistUpdateDTO;
import com.PJDM.dto.AuditDTO;
import com.PJDM.pojo.UserAccompanist;
import com.PJDM.service.IUserAccompanistService;
import com.PJDM.vo.AccompanistListVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 陪诊师管理 Controller
 *
 * @author Alanine
 */
@Slf4j
@RestController
@RequestMapping("/user/accompanist")
@Tag(name = "陪诊师管理", description = "陪诊师资质增删改查及审核")
public class UserAccompanistController {

    @Autowired
    private IUserAccompanistService accompanistService;

    @GetMapping("/stat/count")
    @Operation(summary = "查询陪诊师总数")
    public R<Long> count() {
        return R.success(accompanistService.count());
    }

    @GetMapping("/list")
    @Operation(summary = "分页查询陪诊师列表（user_accompanist JOIN user_user）")
    public R<IPage<AccompanistListVO>> list(AccompanistQueryDTO query) {
        return R.success(accompanistService.getAccompanistListPage(query));
    }

    @GetMapping("/by-user/{userId}")
    @Operation(summary = "按 userId 获取陪诊师详情")
    public R<UserAccompanist> detailByUserId(@PathVariable Long userId) {
        try {
            UserAccompanist a = accompanistService.getOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<UserAccompanist>()
                    .eq(UserAccompanist::getUserId, userId)
            );
            if (a == null) return R.error("未找到对应陪诊师记录");
            return R.success(a);
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取陪诊师详情")
    public R<UserAccompanist> detail(@PathVariable Long id) {
        try {
            return R.success(accompanistService.getAccompanistDetail(id));
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @PostMapping
    @Operation(summary = "新增陪诊师资质")
    public R<String> add(@RequestBody AccompanistUpdateDTO dto) {
        try {
            accompanistService.addAccompanist(dto);
            return R.success("新增成功");
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改陪诊师信息")
    public R<String> update(@PathVariable Long id, @RequestBody AccompanistUpdateDTO dto) {
        try {
            accompanistService.updateAccompanist(id, dto);
            return R.success("修改成功");
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除陪诊师")
    public R<String> delete(@PathVariable Long id) {
        try {
            accompanistService.deleteAccompanist(id);
            return R.success("删除成功");
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @PutMapping("/{id}/audit")
    @Operation(summary = "审核陪诊师（status: 2通过 3拒绝）")
    public R<String> audit(@PathVariable Long id, @RequestBody AuditDTO dto) {
        try {
            accompanistService.auditAccompanist(id, dto);
            return R.success("审核成功");
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "修改陪诊状态（1空闲 2服务中 3休假 4停用）")
    public R<String> updateStatus(@PathVariable Long id, @RequestBody java.util.Map<String, Byte> body) {
        try {
            Byte status = body.get("status");
            if (status == null) return R.error("status 不能为空");
            accompanistService.updateAccompanyStatus(id, status);
            return R.success("状态修改成功");
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }
}
