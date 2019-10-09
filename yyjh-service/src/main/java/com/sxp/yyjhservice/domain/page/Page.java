package com.sxp.yyjhservice.domain.page;

import lombok.Data;

@Data
public class Page {
    //每页显示数量
    private int limit;
    //页码
    private int page;
    //sql语句起始索引
    private int offset;

    private String content;
}