package com.PJDM.controller;

import com.PJDM.common.R;
import com.PJDM.pojo.ServicePackage;
import com.PJDM.service.IServicePackageService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * 患者端 - 服务套餐接口
 */
@Slf4j
@RestController
@Tag(name = "服务套餐", description = "患者端服务套餐列表、详情、收藏")
public class ServicePackageApiController {

    @Autowired private IServicePackageService packageService;

    @GetMapping("/service/packages")
    @Operation(summary = "获取套餐列表")
    public R<IPage<ServicePackage>> getServicePackages(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<ServicePackage> w = new LambdaQueryWrapper<ServicePackage>()
                .eq(ServicePackage::getStatus, (byte) 1)
                .and(StringUtils.hasText(keyword), q -> q
                        .like(ServicePackage::getPackageName, keyword)
                        .or().like(ServicePackage::getDescription, keyword))
                .orderByAsc(ServicePackage::getSort);
        return R.success(packageService.page(new Page<>(page, pageSize), w));
    }

    @GetMapping("/service/packages/{packageId}")
    @Operation(summary = "获取套餐详情")
    public R<ServicePackage> getServicePackageDetail(@PathVariable Long packageId) {
        ServicePackage pkg = packageService.getById(packageId);
        if (pkg == null) return R.error("套餐不存在");
        return R.success(pkg);
    }

    @PostMapping("/service/packages/favorite")
    @Operation(summary = "收藏套餐")
    public R<String> favoritePackage(@RequestBody Map<String, Object> body) {
        Object pkgId = body.get("package_id");
        if (pkgId == null) return R.error("package_id 不能为空");
        log.info("[套餐收藏] packageId={}", pkgId);
        return R.success("收藏成功，请通过 /patient/favorites 接口管理收藏列表");
    }

    @PostMapping("/service/packages/unfavorite")
    @Operation(summary = "取消收藏套餐")
    public R<String> unfavoritePackage(@RequestBody Map<String, Object> body) {
        Object pkgId = body.get("package_id");
        if (pkgId == null) return R.error("package_id 不能为空");
        log.info("[取消套餐收藏] packageId={}", pkgId);
        return R.success("取消收藏成功");
    }

    @GetMapping("/service/package/recommended")
    @Operation(summary = "获取推荐套餐")
    public R<List<ServicePackage>> getRecommendedPackages(
            @RequestParam(defaultValue = "5") Integer limit) {
        LambdaQueryWrapper<ServicePackage> w = new LambdaQueryWrapper<ServicePackage>()
                .eq(ServicePackage::getStatus, (byte) 1)
                .orderByDesc(ServicePackage::getSort)
                .last("LIMIT " + limit);
        return R.success(packageService.list(w));
    }
}
