package com.sxp.yyjhservice.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class RolePerVO implements Serializable {
    private Integer id;
    private Integer role_id;
    private Integer permission_id;
    private String rolename;
    private String permission;
}
