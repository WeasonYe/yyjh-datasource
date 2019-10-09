package com.sxp.yyjhservice.domain.page;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageHelper <T>{
    //实体类集合
    private List<T> rows = new ArrayList<T>();
    //数据总条数
    private int total;

}
