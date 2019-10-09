package com.sxp.yyjhservice.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRoleVO implements Serializable {
    private Integer id;
    private Integer user_id;
    private Integer role_id;
    private String loginid;
    private String rolename;
}
