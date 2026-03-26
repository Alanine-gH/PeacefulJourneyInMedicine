package com.PJDM.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 患者个人信息 VO
 */
@Data
public class PatientProfileVO {
    private Long id;
    private String username;
    private String nickname;
    private String realName;
    private String phone;
    private String email;
    private String avatarUrl;
    private Integer age;
    private Byte gender;
    private String language;
    /** 账号状态（1：活跃；2：停用；3：禁用） */
    private Byte status;
    /** 实名认证状态（0：未认证；1：待审核；2：已通过；3：已拒绝） */
    private Byte authStatus;
    private LocalDateTime lastLoginTime;
    private LocalDateTime createTime;
}
