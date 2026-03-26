package com.PJDM.mapper;

import com.PJDM.dto.PackageQueryDTO;
import com.PJDM.pojo.ServicePackage;
import com.PJDM.vo.PackageListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ServicePackageMapper extends BaseMapper<ServicePackage> {
    IPage<PackageListVO> selectPackageListPage(@Param("page") Page<PackageListVO> page,
                                             @Param("q") PackageQueryDTO query);
}
