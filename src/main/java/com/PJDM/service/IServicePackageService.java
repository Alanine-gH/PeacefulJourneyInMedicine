package com.PJDM.service;

import com.PJDM.dto.PackageQueryDTO;
import com.PJDM.dto.PackageUpdateDTO;
import com.PJDM.pojo.ServicePackage;
import com.PJDM.vo.PackageListVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IServicePackageService extends IService<ServicePackage> {
    IPage<PackageListVO> getPackageListPage(PackageQueryDTO query);

    ServicePackage getPackageDetail(Long id);

    void addPackage(PackageUpdateDTO dto);

    void updatePackage(Long id, PackageUpdateDTO dto);

    void deletePackage(Long id);

    void updateStatus(Long id, Byte status);
}
