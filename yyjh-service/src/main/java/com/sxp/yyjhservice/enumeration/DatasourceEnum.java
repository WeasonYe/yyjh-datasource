package com.sxp.yyjhservice.enumeration;

public enum DatasourceEnum {
    SUCCESS(0,"success"), FAIL(-1,"erro"),EXCEPTION(-2,"exception"),
    NOFILE(-3,"nofiles");

    private final Integer code;
    private String msg ="";

    DatasourceEnum(Integer code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode(){
        return this.code;
    }

    public String getMsg(){
        return this.msg;
    }
}
