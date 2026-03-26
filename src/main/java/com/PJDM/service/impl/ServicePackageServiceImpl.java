package com.PJDM.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.PJDM.dto.PackageQueryDTO;
import com.PJDM.dto.PackageUpdateDTO;
import com.PJDM.exception.DuplicateEntryException;
import com.PJDM.exception.ResourceNotFoundException;
import com.PJDM.exception.ValidationException;
import com.PJDM.mapper.ServicePackageMapper;
import com.PJDM.pojo.ServicePackage;
import com.PJDM.service.IServicePackageService;
import com.PJDM.vo.PackageListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class ServicePackageServiceImpl extends ServiceImpl<ServicePackageMapper, ServicePackage>
        implements IServicePackageService {

    @Value("${user-module.page.default-size:10}")
    private int defaultPageSize;
    @Value("${user-module.page.max-size:100}")
    private int maxPageSize;

    @Override
    public IPage<PackageListVO> getPackageListPage(PackageQueryDTO query) {
        int size = Math.min(query.getPageSize() == null ? defaultPageSize : query.getPageSize(), maxPageSize);
        return baseMapper.selectPackageListPage(new Page<>(query.getPageNum() == null ? 1 : query.getPageNum(), size), query);
    }

    @Override
    public ServicePackage getPackageDetail(Long id) {
        ServicePackage p = getById(id);
        if (p == null) throw new ResourceNotFoundException("套餐", id);
        log.info("[套餐] 查询详情 id={}", id);
        return p;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addPackage(PackageUpdateDTO dto) {
        if (!StringUtils.hasText(dto.getPackageName())) throw new ValidationException("packageName", "不能为空");
        if (StringUtils.hasText(dto.getPackageCode()) &&
                count(new LambdaQueryWrapper<ServicePackage>().eq(ServicePackage::getPackageCode, dto.getPackageCode())) > 0)
            throw new DuplicateEntryException("packageCode", dto.getPackageCode());
        ServicePackage p = new ServicePackage();
        copyDto(p, dto);
        if (p.getCurrency() == null) p.setCurrency("USD");
        if (p.getMaxUsageCount() == null) p.setMaxUsageCount(-1);
        if (p.getStatus() == null) p.setStatus((byte) 1);
        save(p);
        log.info("新增套餐 [{}]", p.getPackageName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePackage(Long id, PackageUpdateDTO dto) {
        if (getById(id) == null) throw new ResourceNotFoundException("套餐", id);
        log.info("[套餐] 修改 id={}", id);
        ServicePackage p = new ServicePackage();
        p.setId(id);
        copyDto(p, dto);
        updateById(p);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePackage(Long id) {
        ServicePackage p = getById(id);
        if (p == null) throw new ResourceNotFoundException("套餐", id);
        removeById(id);
        log.info("[套餐] 删除 id={} name={}", id, p.getPackageName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long id, Byte status) {
        if (getById(id) == null) throw new ResourceNotFoundException("套餐", id);
        ServicePackage p = new ServicePackage();
        p.setId(id);
        p.setStatus(status);
        updateById(p);
        log.info("[套餐] 状态变更 id={} status={}", id, status);
    }

    private void copyDto(ServicePackage p, PackageUpdateDTO dto) {
        if (StringUtils.hasText(dto.getPackageCode())) p.setPackageCode(dto.getPackageCode());
        if (StringUtils.hasText(dto.getPackageName())) p.setPackageName(dto.getPackageName());
        if (StringUtils.hasText(dto.getPackageNameEn())) p.setPackageNameEn(dto.getPackageNameEn());
        if (dto.getPackageType() != null) p.setPackageType(dto.getPackageType());
        if (dto.getServiceType() != null) p.setServiceType(dto.getServiceType());
        if (StringUtils.hasText(dto.getDescription())) p.setDescription(dto.getDescription());
        if (StringUtils.hasText(dto.getServiceContent())) p.setServiceContent(dto.getServiceContent());
        if (dto.getPrice() != null) p.setPrice(dto.getPrice());
        if (StringUtils.hasText(dto.getCurrency())) p.setCurrency(dto.getCurrency());
        if (StringUtils.hasText(dto.getUnit())) p.setUnit(dto.getUnit());
        if (dto.getDuration() != null) p.setDuration(dto.getDuration());
        if (dto.getMaxUsageCount() != null) p.setMaxUsageCount(dto.getMaxUsageCount());
        if (dto.getValidDays() != null) p.setValidDays(dto.getValidDays());
        if (StringUtils.hasText(dto.getCoverImageUrl())) p.setCoverImageUrl(dto.getCoverImageUrl());
        if (StringUtils.hasText(dto.getIncludesItems())) p.setIncludesItems(dto.getIncludesItems());
        if (StringUtils.hasText(dto.getExcludeItems())) p.setExcludeItems(dto.getExcludeItems());
        if (StringUtils.hasText(dto.getUsageNotice())) p.setUsageNotice(dto.getUsageNotice());
        if (dto.getSort() != null) p.setSort(dto.getSort());
        if (dto.getStatus() != null) p.setStatus(dto.getStatus());
        if (StringUtils.hasText(dto.getRemark())) p.setRemark(dto.getRemark());
    }
}
