package com.PJDM.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.PJDM.dto.HospitalQueryDTO;
import com.PJDM.dto.HospitalUpdateDTO;
import com.PJDM.exception.DuplicateEntryException;
import com.PJDM.exception.ResourceNotFoundException;
import com.PJDM.exception.ValidationException;
import com.PJDM.mapper.MedicalHospitalMapper;
import com.PJDM.pojo.MedicalHospital;
import com.PJDM.service.IMedicalHospitalService;
import com.PJDM.untils.RegexUtils;
import com.PJDM.vo.HospitalListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class MedicalHospitalServiceImpl extends ServiceImpl<MedicalHospitalMapper, MedicalHospital>
        implements IMedicalHospitalService {

    @Value("${user-module.page.default-size:10}")
    private int defaultPageSize;
    @Value("${user-module.page.max-size:100}")
    private int maxPageSize;

    @Override
    public IPage<HospitalListVO> getHospitalListPage(HospitalQueryDTO query) {
        int size = Math.min(query.getPageSize() == null ? defaultPageSize : query.getPageSize(), maxPageSize);
        return baseMapper.selectHospitalListPage(new Page<>(query.getPageNum() == null ? 1 : query.getPageNum(), size), query);
    }

    @Override
    public IPage<HospitalListVO> searchByRegion(HospitalQueryDTO query) {
        int size = Math.min(query.getPageSize() == null ? defaultPageSize : query.getPageSize(), maxPageSize);
        return baseMapper.selectHospitalListPage(new Page<>(query.getPageNum() == null ? 1 : query.getPageNum(), size), query);
    }

    @Override
    public MedicalHospital getHospitalDetail(Long id) {
        MedicalHospital h = getById(id);
        if (h == null) throw new ResourceNotFoundException("医院", id);
        log.info("[医院] 查询详情 id={}", id);
        return h;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addHospital(HospitalUpdateDTO dto) {
        if (!StringUtils.hasText(dto.getHospitalName())) throw new ValidationException("hospitalName", "不能为空");
        if (StringUtils.hasText(dto.getPhone()) && !RegexUtils.isPhoneNumber(dto.getPhone()))
            throw new ValidationException("phone", "手机号格式不正确");
        if (StringUtils.hasText(dto.getHospitalCode()) &&
                count(new LambdaQueryWrapper<MedicalHospital>().eq(MedicalHospital::getHospitalCode, dto.getHospitalCode())) > 0)
            throw new DuplicateEntryException("hospitalCode", dto.getHospitalCode());
        MedicalHospital h = new MedicalHospital();
        copyDto(h, dto);
        save(h);
        log.info("新增医院 [{}]", h.getHospitalName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateHospital(Long id, HospitalUpdateDTO dto) {
        if (getById(id) == null) throw new ResourceNotFoundException("医院", id);
        log.info("[医院] 修改 id={}", id);
        MedicalHospital h = new MedicalHospital();
        h.setId(id);
        copyDto(h, dto);
        updateById(h);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteHospital(Long id) {
        MedicalHospital h = getById(id);
        if (h == null) throw new ResourceNotFoundException("医院", id);
        removeById(id);
        log.info("[医院] 删除 id={} name={}", id, h.getHospitalName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long id, Byte status) {
        if (getById(id) == null) throw new ResourceNotFoundException("医院", id);
        MedicalHospital h = new MedicalHospital();
        h.setId(id);
        h.setStatus(status);
        updateById(h);
        log.info("[医院] 状态变更 id={} status={}", id, status);
    }

    private void copyDto(MedicalHospital h, HospitalUpdateDTO dto) {
        if (StringUtils.hasText(dto.getHospitalCode())) h.setHospitalCode(dto.getHospitalCode());
        if (StringUtils.hasText(dto.getHospitalName())) h.setHospitalName(dto.getHospitalName());
        if (StringUtils.hasText(dto.getHospitalNameEn())) h.setHospitalNameEn(dto.getHospitalNameEn());
        if (dto.getHospitalType() != null) h.setHospitalType(dto.getHospitalType());
        if (StringUtils.hasText(dto.getHospitalLevel())) h.setHospitalLevel(dto.getHospitalLevel());
        if (StringUtils.hasText(dto.getProvince())) h.setProvince(dto.getProvince());
        if (StringUtils.hasText(dto.getCity())) h.setCity(dto.getCity());
        if (StringUtils.hasText(dto.getDistrict())) h.setDistrict(dto.getDistrict());
        if (StringUtils.hasText(dto.getAddress())) h.setAddress(dto.getAddress());
        if (StringUtils.hasText(dto.getPhone())) h.setPhone(dto.getPhone());
        if (StringUtils.hasText(dto.getInternationalPhone())) h.setInternationalPhone(dto.getInternationalPhone());
        if (StringUtils.hasText(dto.getWebsite())) h.setWebsite(dto.getWebsite());
        if (StringUtils.hasText(dto.getIntroduction())) h.setIntroduction(dto.getIntroduction());
        if (StringUtils.hasText(dto.getFeaturedDepartments())) h.setFeaturedDepartments(dto.getFeaturedDepartments());
        if (StringUtils.hasText(dto.getAppointmentNotes())) h.setAppointmentNotes(dto.getAppointmentNotes());
        if (dto.getHasInternationalDept() != null) h.setHasInternationalDept(dto.getHasInternationalDept());
        if (dto.getIsCooperation() != null) h.setIsCooperation(dto.getIsCooperation());
        if (dto.getCooperationStartDate() != null) h.setCooperationStartDate(dto.getCooperationStartDate());
        if (StringUtils.hasText(dto.getLogoUrl())) h.setLogoUrl(dto.getLogoUrl());
        if (StringUtils.hasText(dto.getCoverImageUrl())) h.setCoverImageUrl(dto.getCoverImageUrl());
        if (dto.getStatus() != null) h.setStatus(dto.getStatus());
        if (dto.getSort() != null) h.setSort(dto.getSort());
        if (StringUtils.hasText(dto.getRemark())) h.setRemark(dto.getRemark());
    }
}
