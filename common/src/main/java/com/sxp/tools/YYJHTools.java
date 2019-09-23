package com.sxp.tools;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;

public class YYJHTools {
    public static final String DEFAULT_FORMAT_PARAM = "yyyy-MM-dd' 'HH:mm:ss";

    /**
     * 根据格式化参数来格式化日期字符串
     * @param date
     * @param param
     * @return Date
     * */
    public static Date parseStrToDate(String date, String param) throws ParseException {
        if (null == param || "".equals(param.trim()))
            param = DEFAULT_FORMAT_PARAM;
        DateFormat df = new SimpleDateFormat(param);
        return df.parse(date);
    }

    /**
     * 根据格式化参数来格式化日期字符串
     * @param date
     * @param param
     * @return String
     * */
    public static String formatDateToStr(Date date, String param){
        if (null == param || "".equals(param.trim()))
            param = DEFAULT_FORMAT_PARAM;
        DateFormat df = new SimpleDateFormat(param);
        return df.format(date);
    }

    public static URL formatURL(String url) throws MalformedURLException {
        return new URL(url);
    }

    public static boolean isEmail(String email){
        return email.matches("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
    }
    /**
     * 取出所有空格，包括字符间的空格
     * */
    public static String filterInnerBlank(String str){
        return str.replaceAll(" ","");
    }

    /**
     * 生成UUID
     */
    public static String get32UUID(){
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-","");
        return uuid;
    }

}
