package com.PJDM.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.PJDM.dto.CheckinQueryDTO;
import com.PJDM.mapper.ServiceCheckinMapper;
import com.PJDM.pojo.ServiceCheckin;
import com.PJDM.service.IServiceCheckinService;
import com.PJDM.vo.CheckinListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ServiceCheckinServiceImpl extends ServiceImpl<ServiceCheckinMapper, ServiceCheckin>
        implements IServiceCheckinService {

    @Value("${user-module.page.default-size:10}")
    private int defaultPageSize;
    @Value("${user-module.page.max-size:100}")
    private int maxPageSize;

    @Override
    public IPage<CheckinListVO> getCheckinListPage(CheckinQueryDTO query) {
        int size = Math.min(query.getPageSize() == null ? defaultPageSize : query.getPageSize(), maxPageSize);
        return baseMapper.selectCheckinListPage(new Page<>(query.getPageNum() == null ? 1 : query.getPageNum(), size), query);
    }

    @Override
    public ServiceCheckin getCheckinDetail(Long id) {
        ServiceCheckin c = getById(id);
        if (c == null) throw new RuntimeException("打卡记录不存在");
        return c;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCheckin(Long id) {
        if (getById(id) == null) throw new RuntimeException("打卡记录不存在");
        removeById(id);
        log.info("删除打卡记录 [{}]", id);
    }
}
