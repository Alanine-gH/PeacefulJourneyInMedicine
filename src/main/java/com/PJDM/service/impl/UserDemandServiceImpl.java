package com.PJDM.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.PJDM.dto.DemandPublishDTO;
import com.PJDM.dto.DemandQueryDTO;
import com.PJDM.exception.BusinessException;
import com.PJDM.exception.ResourceNotFoundException;
import com.PJDM.exception.ValidationException;
import com.PJDM.mapper.UserDemandMapper;
import com.PJDM.pojo.UserDemand;
import com.PJDM.service.IUserDemandService;
import com.PJDM.vo.DemandListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class UserDemandServiceImpl extends ServiceImpl<UserDemandMapper, UserDemand>
        implements IUserDemandService {

    @Value("${user-module.page.default-size:10}")
    private int defaultPageSize;
    @Value("${user-module.page.max-size:100}")
    private int maxPageSize;

    @Override
    public IPage<DemandListVO> getDemandPage(DemandQueryDTO query) {
        int size = Math.min(query.getPageSize() == null ? defaultPageSize : query.getPageSize(), maxPageSize);
        return baseMapper.selectDemandPage(
                new Page<>(query.getPageNum() == null ? 1 : query.getPageNum(), size), query);
    }

    @Override
    public UserDemand getDemandDetail(Long demandId) {
        UserDemand d = getById(demandId);
        if (d == null) throw new ResourceNotFoundException("需求", demandId);
        return d;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publishDemand(Long userId, DemandPublishDTO dto) {
        if (!StringUtils.hasText(dto.getTitle())) throw new ValidationException("title", "需求标题不能为空");
        if (!StringUtils.hasText(dto.getType())) throw new ValidationException("type", "需求类型不能为空");
        UserDemand d = new UserDemand();
        d.setUserId(userId);
        copyDto(d, dto);
        d.setStatus((byte) 1);
        d.setRefreshTime(LocalDateTime.now());
        d.setCreateTime(LocalDateTime.now());
        save(d);
        log.info("[需求] 发布 userId={} title={}", userId, dto.getTitle());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editDemand(Long demandId, DemandPublishDTO dto) {
        UserDemand d = getById(demandId);
        if (d == null) throw new ResourceNotFoundException("需求", demandId);
        if (d.getStatus() != 1) throw new BusinessException("仅待接单状态的需求可以编辑");
        copyDto(d, dto);
        d.setUpdateTime(LocalDateTime.now());
        updateById(d);
        log.info("[需求] 编辑 id={}", demandId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelDemand(Long demandId, String reason) {
        UserDemand d = getById(demandId);
        if (d == null) throw new ResourceNotFoundException("需求", demandId);
        if (d.getStatus() != 1) throw new BusinessException("仅待接单状态的需求可以取消");
        d.setStatus((byte) 4);
        d.setCancelReason(reason);
        d.setUpdateTime(LocalDateTime.now());
        updateById(d);
        log.info("[需求] 取消 id={}", demandId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDemand(Long demandId) {
        UserDemand d = getById(demandId);
        if (d == null) throw new ResourceNotFoundException("需求", demandId);
        if (d.getStatus() == 2) throw new BusinessException("已接单需求不可删除");
        removeById(demandId);
        log.info("[需求] 删除 id={}", demandId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void acceptDemand(Long demandId, Long accompanistId) {
        UserDemand d = getById(demandId);
        if (d == null) throw new ResourceNotFoundException("需求", demandId);
        if (d.getStatus() != 1) throw new BusinessException("该需求已被接单或已结束");
        d.setStatus((byte) 2);
        d.setAcceptAccompanistId(accompanistId);
        d.setAcceptTime(LocalDateTime.now());
        d.setUpdateTime(LocalDateTime.now());
        updateById(d);
        log.info("[需求] 接单 id={} accompanistId={}", demandId, accompanistId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rejectDemand(Long demandId, String reason) {
        UserDemand d = getById(demandId);
        if (d == null) throw new ResourceNotFoundException("需求", demandId);
        // 拒绝后回到待接单状态
        d.setStatus((byte) 1);
        d.setAcceptAccompanistId(null);
        d.setAcceptTime(null);
        d.setUpdateTime(LocalDateTime.now());
        updateById(d);
        log.info("[需求] 拒绝接单 id={} reason={}", demandId, reason);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refreshDemand(Long demandId) {
        UserDemand d = getById(demandId);
        if (d == null) throw new ResourceNotFoundException("需求", demandId);
        if (d.getStatus() != 1) throw new BusinessException("仅待接单需求可刷新");
        d.setRefreshTime(LocalDateTime.now());
        d.setUpdateTime(LocalDateTime.now());
        updateById(d);
        log.info("[需求] 刷新 id={}", demandId);
    }

    @Override
    public Object getDemandStatistics(Long userId) {
        Map<String, Long> stat = new HashMap<>();
        stat.put("pending",   count(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<UserDemand>().eq(UserDemand::getUserId, userId).eq(UserDemand::getStatus, 1)));
        stat.put("accepted",  count(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<UserDemand>().eq(UserDemand::getUserId, userId).eq(UserDemand::getStatus, 2)));
        stat.put("completed", count(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<UserDemand>().eq(UserDemand::getUserId, userId).eq(UserDemand::getStatus, 3)));
        stat.put("cancelled", count(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<UserDemand>().eq(UserDemand::getUserId, userId).eq(UserDemand::getStatus, 4)));
        return stat;
    }

    private void copyDto(UserDemand d, DemandPublishDTO dto) {
        if (StringUtils.hasText(dto.getTitle())) d.setTitle(dto.getTitle());
        if (StringUtils.hasText(dto.getDescription())) d.setDescription(dto.getDescription());
        if (StringUtils.hasText(dto.getType())) d.setType(dto.getType());
        if (dto.getHospitalId() != null) d.setHospitalId(dto.getHospitalId());
        if (dto.getDepartmentId() != null) d.setDepartmentId(dto.getDepartmentId());
        if (dto.getPatientId() != null) d.setPatientId(dto.getPatientId());
        if (dto.getStartDate() != null) d.setStartDate(dto.getStartDate());
        if (dto.getEndDate() != null) d.setEndDate(dto.getEndDate());
        if (dto.getBudget() != null) d.setBudget(dto.getBudget());
        if (StringUtils.hasText(dto.getContactName())) d.setContactName(dto.getContactName());
        if (StringUtils.hasText(dto.getContactPhone())) d.setContactPhone(dto.getContactPhone());
        if (StringUtils.hasText(dto.getRequirements())) d.setRequirements(dto.getRequirements());
        if (StringUtils.hasText(dto.getCity())) d.setCity(dto.getCity());
        if (StringUtils.hasText(dto.getRemark())) d.setRemark(dto.getRemark());
    }
}
