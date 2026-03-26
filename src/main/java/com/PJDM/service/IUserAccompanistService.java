package com.PJDM.service;

import com.PJDM.dto.AccompanistQueryDTO;
import com.PJDM.dto.AccompanistUpdateDTO;
import com.PJDM.dto.AuditDTO;
import com.PJDM.pojo.UserAccompanist;
import com.PJDM.vo.AccompanistListVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 陪诊师资质表 Service
 *
 * @author Alanine
 */
public interface IUserAccompanistService extends IService<UserAccompanist> {

    /** 分页查询陪诊师列表（多表联查） */
    IPage<AccompanistListVO> getAccompanistListPage(AccompanistQueryDTO query);

    /** 获取陪诊师详情 */
    UserAccompanist getAccompanistDetail(Long id);

    /** 新增陪诊师资质 */
    void addAccompanist(AccompanistUpdateDTO dto);

    /** 修改陪诊师信息 */
    void updateAccompanist(Long id, AccompanistUpdateDTO dto);

    /** 删除陪诊师 */
    void deleteAccompanist(Long id);

    /** 审核陪诊师（通过/拒绝） */
    void auditAccompanist(Long id, AuditDTO dto);

    /** 修改陪诊状态 */
    void updateAccompanyStatus(Long id, Byte status);
}
