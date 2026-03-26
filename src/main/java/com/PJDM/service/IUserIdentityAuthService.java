package com.PJDM.service;

import com.PJDM.dto.AuditDTO;
import com.PJDM.dto.IdentityQueryDTO;
import com.PJDM.pojo.UserIdentityAuth;
import com.PJDM.vo.IdentityListVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 实名认证表 Service
 *
 * @author Alanine
 */
public interface IUserIdentityAuthService extends IService<UserIdentityAuth> {

    /** 分页查询实名认证列表（多表联查） */
    IPage<IdentityListVO> getIdentityListPage(IdentityQueryDTO query);

    /** 获取认证详情 */
    UserIdentityAuth getIdentityDetail(Long id);

    /** 审核实名认证 */
    void auditIdentity(Long id, AuditDTO dto);

    /** 删除认证记录 */
    void deleteIdentity(Long id);
}
