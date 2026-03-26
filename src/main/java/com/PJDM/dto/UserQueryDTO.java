package com.PJDM.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户列表查询参数
 *
 * @author Alanine
 */
@Data
@Schema(description = "用户查询参数")
public class UserQueryDTO {

    @Schema(description = "用户名（模糊）")
    private String username;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "用户类型 1患者 2陪诊师 3客服 4管理员")
    private Byte userType;

    @Schema(description = "账号状态 1活跃 2停用 3禁用")
    private Byte status;

    @Schema(description = "页码", example = "1")
    private Integer pageNum = 1;

    @Schema(description = "每页条数", example = "10")
    private Integer pageSize = 10;
}
