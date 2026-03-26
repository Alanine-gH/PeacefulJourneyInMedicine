package com.PJDM.controller;

import com.PJDM.common.R;
import com.PJDM.dto.PackageQueryDTO;
import com.PJDM.dto.PackageUpdateDTO;
import com.PJDM.exception.ValidationException;
import com.PJDM.pojo.ServicePackage;
import com.PJDM.service.IServicePackageService;
import com.PJDM.vo.PackageListVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/service/package")
@Tag(name = "服务套餐管理", description = "套餐增删改查及上下架")
public class ServicePackageController {

    @Autowired private IServicePackageService packageService;

    @GetMapping("/list")
    @Operation(summary = "分页查询套餐列表")
    public R<IPage<PackageListVO>> list(PackageQueryDTO query) {
        log.info("[套餐] 列表查询 query={}", query);
        return R.success(packageService.getPackageListPage(query));
    }

    @GetMapping("/{id}")
    @Operation(summary = "套餐详情")
    public R<ServicePackage> detail(@PathVariable Long id) {
        log.info("[套餐] 详情 id={}", id);
        return R.success(packageService.getPackageDetail(id));
    }

    @PostMapping
    @Operation(summary = "新增套餐")
    public R<String> add(@RequestBody PackageUpdateDTO dto) {
        log.info("[套餐] 新增 name={}", dto.getPackageName());
        packageService.addPackage(dto);
        return R.success("新增成功");
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改套餐")
    public R<String> update(@PathVariable Long id, @RequestBody PackageUpdateDTO dto) {
        log.info("[套餐] 修改 id={}", id);
        packageService.updatePackage(id, dto);
        return R.success("修改成功");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除套餐")
    public R<String> delete(@PathVariable Long id) {
        log.info("[套餐] 删除 id={}", id);
        packageService.deletePackage(id);
        return R.success("删除成功");
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "修改套餐状态（1上架 2下架 3停售）")
    public R<String> updateStatus(@PathVariable Long id, @RequestBody Map<String, Byte> body) {
        Byte status = body.get("status");
        if (status == null) throw new ValidationException("status", "不能为空");
        log.info("[套餐] 状态变更 id={} status={}", id, status);
        packageService.updateStatus(id, status);
        return R.success("修改成功");
    }
}
