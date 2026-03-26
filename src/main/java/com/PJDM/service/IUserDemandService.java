package com.PJDM.service;

import com.PJDM.dto.DemandPublishDTO;
import com.PJDM.dto.DemandQueryDTO;
import com.PJDM.pojo.UserDemand;
import com.PJDM.vo.DemandListVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 需求发布 Service 接口
 */
public interface IUserDemandService extends IService<UserDemand> {

    IPage<DemandListVO> getDemandPage(DemandQueryDTO query);

    UserDemand getDemandDetail(Long demandId);

    void publishDemand(Long userId, DemandPublishDTO dto);

    void editDemand(Long demandId, DemandPublishDTO dto);

    void cancelDemand(Long demandId, String reason);

    void deleteDemand(Long demandId);

    void acceptDemand(Long demandId, Long accompanistId);

    void rejectDemand(Long demandId, String reason);

    void refreshDemand(Long demandId);

    Object getDemandStatistics(Long userId);
}
