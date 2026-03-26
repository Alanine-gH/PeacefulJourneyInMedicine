package com.PJDM.service;

import com.PJDM.dto.DispatchQueryDTO;
import com.PJDM.pojo.ServiceDispatch;
import com.PJDM.vo.DispatchListVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IServiceDispatchService extends IService<ServiceDispatch> {
    IPage<DispatchListVO> getDispatchListPage(DispatchQueryDTO query);

    ServiceDispatch getDispatchDetail(Long id);

    void addDispatch(Long orderId, Long accompanistId, String remark);

    void updateStatus(Long id, Byte status, String rejectReason);

    void deleteDispatch(Long id);
}
