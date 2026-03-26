package com.PJDM.service;

import com.PJDM.dto.CheckinQueryDTO;
import com.PJDM.pojo.ServiceCheckin;
import com.PJDM.vo.CheckinListVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IServiceCheckinService extends IService<ServiceCheckin> {
    IPage<CheckinListVO> getCheckinListPage(CheckinQueryDTO query);

    ServiceCheckin getCheckinDetail(Long id);

    void deleteCheckin(Long id);
}
