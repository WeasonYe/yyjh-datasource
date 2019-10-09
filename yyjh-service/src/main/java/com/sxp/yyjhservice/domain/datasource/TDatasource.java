package com.sxp.yyjhservice.domain.datasource;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxp.yyjhservice.domain.page.Page;
import lombok.Data;

import java.util.Date;

@Data
public class TDatasource extends Page {
    private Integer id;

    private String userId;

    private String type;

    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createtime;

    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updatetime;

    private String databaseName;

    private String alias;

    private String driver;

    private String url;

    private Integer port;

    private String auth;

    private String username;

    private String password;

    private String description;

    private String encode;
}