package com.PJDM.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.PJDM.dto.PatientMedicalCardDTO;
import com.PJDM.dto.PatientQueryDTO;
import com.PJDM.dto.PatientUpdateDTO;
import com.PJDM.exception.BusinessException;
import com.PJDM.exception.ResourceNotFoundException;
import com.PJDM.exception.ValidationException;
import com.PJDM.mapper.UserMedicalCardMapper;
import com.PJDM.mapper.UserPatientMapper;
import com.PJDM.pojo.UserMedicalCard;
import com.PJDM.pojo.UserPatient;
import com.PJDM.service.IUserPatientService;
import com.PJDM.vo.PatientDetailVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 就诊人 Service 实现
 */
@Slf4j
@Service
public class UserPatientServiceImpl extends ServiceImpl<UserPatientMapper, UserPatient>
        implements IUserPatientService {

    @Value("${user-module.page.default-size:10}")
    private int defaultPageSize;
    @Value("${user-module.page.max-size:100}")
    private int maxPageSize;

    @Autowired
    private UserMedicalCardMapper medicalCardMapper;

    @Override
    public IPage<PatientDetailVO> getPatientPage(PatientQueryDTO query) {
        int size = Math.min(query.getPageSize() == null ? defaultPageSize : query.getPageSize(), maxPageSize);
        return baseMapper.selectPatientPage(
                new Page<>(query.getPage() == null ? 1 : query.getPage(), size), query);
    }

    @Override
    public PatientDetailVO getPatientDetail(Long patientId) {
        UserPatient p = getById(patientId);
        if (p == null) throw new ResourceNotFoundException("就诊人", patientId);
        PatientDetailVO vo = toDetailVO(p);
        List<UserMedicalCard> cards = medicalCardMapper.selectByPatientId(patientId);
        vo.setMedicalCards(cards.stream().map(c -> {
            PatientDetailVO.MedicalCardVO cv = new PatientDetailVO.MedicalCardVO();
            cv.setId(c.getId());
            cv.setHospitalId(c.getHospitalId());
            cv.setHospitalName(c.getHospitalName());
            cv.setCardNo(c.getCardNo());
            cv.setCreateTime(c.getCreateTime());
            return cv;
        }).collect(Collectors.toList()));
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addPatient(PatientUpdateDTO dto) {
        if (dto.getUserId() == null) throw new ValidationException("userId", "不能为空");
        if (!StringUtils.hasText(dto.getName())) throw new ValidationException("name", "姓名不能为空");
        UserPatient p = new UserPatient();
        p.setUserId(dto.getUserId());
        copyDto(p, dto);
        p.setIsDefault((byte) 2);
        p.setStatus((byte) 1);
        save(p);
        log.info("[就诊人] 新增 userId={} name={}", dto.getUserId(), dto.getName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePatient(Long patientId, PatientUpdateDTO dto) {
        UserPatient p = getById(patientId);
        if (p == null) throw new ResourceNotFoundException("就诊人", patientId);
        copyDto(p, dto);
        p.setUpdateTime(LocalDateTime.now());
        updateById(p);
        log.info("[就诊人] 更新 id={}", patientId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePatient(Long patientId) {
        UserPatient p = getById(patientId);
        if (p == null) throw new ResourceNotFoundException("就诊人", patientId);
        // 软删除
        p.setStatus((byte) 2);
        p.setUpdateTime(LocalDateTime.now());
        updateById(p);
        log.info("[就诊人] 删除 id={}", patientId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setDefaultPatient(Long userId, Long patientId) {
        UserPatient p = getById(patientId);
        if (p == null || !p.getUserId().equals(userId))
            throw new BusinessException("就诊人不存在或不属于当前用户");
        // 清除当前默认
        UserPatient clear = new UserPatient();
        clear.setIsDefault((byte) 2);
        update(clear, new LambdaQueryWrapper<UserPatient>()
                .eq(UserPatient::getUserId, userId)
                .eq(UserPatient::getIsDefault, (byte) 1));
        // 设置新默认
        p.setIsDefault((byte) 1);
        updateById(p);
        log.info("[就诊人] 设置默认 userId={} patientId={}", userId, patientId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addMedicalCard(Long patientId, PatientMedicalCardDTO dto) {
        if (getById(patientId) == null) throw new ResourceNotFoundException("就诊人", patientId);
        if (!StringUtils.hasText(dto.getCardNo())) throw new ValidationException("cardNo", "就诊卡号不能为空");
        UserMedicalCard card = new UserMedicalCard();
        card.setPatientId(patientId);
        card.setHospitalId(dto.getHospitalId());
        card.setHospitalName(dto.getHospitalName());
        card.setCardNo(dto.getCardNo());
        card.setStatus((byte) 1);
        card.setCreateTime(LocalDateTime.now());
        medicalCardMapper.insert(card);
        log.info("[就诊卡] 新增 patientId={} cardNo={}", patientId, dto.getCardNo());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMedicalCard(Long patientId, Long cardId) {
        UserMedicalCard card = medicalCardMapper.selectById(cardId);
        if (card == null || !card.getPatientId().equals(patientId))
            throw new BusinessException("就诊卡不存在或不属于该就诊人");
        medicalCardMapper.deleteById(cardId);
        log.info("[就诊卡] 删除 id={}", cardId);
    }

    private PatientDetailVO toDetailVO(UserPatient p) {
        PatientDetailVO vo = new PatientDetailVO();
        vo.setId(p.getId());
        vo.setUserId(p.getUserId());
        vo.setName(p.getName());
        vo.setPhone(p.getPhone());
        vo.setIdCard(p.getIdCard());
        vo.setGender(p.getGender());
        vo.setBirthday(p.getBirthday());
        vo.setRelation(p.getRelation());
        vo.setAddress(p.getAddress());
        vo.setIsDefault(p.getIsDefault());
        vo.setCreateTime(p.getCreateTime());
        return vo;
    }

    private void copyDto(UserPatient p, PatientUpdateDTO dto) {
        if (StringUtils.hasText(dto.getName())) p.setName(dto.getName());
        if (StringUtils.hasText(dto.getPhone())) p.setPhone(dto.getPhone());
        if (StringUtils.hasText(dto.getIdCard())) p.setIdCard(dto.getIdCard());
        if (dto.getGender() != null) p.setGender(dto.getGender());
        if (dto.getBirthday() != null) p.setBirthday(dto.getBirthday());
        if (StringUtils.hasText(dto.getRelation())) p.setRelation(dto.getRelation());
        if (StringUtils.hasText(dto.getAddress())) p.setAddress(dto.getAddress());
        if (StringUtils.hasText(dto.getRemark())) p.setRemark(dto.getRemark());
    }
}
