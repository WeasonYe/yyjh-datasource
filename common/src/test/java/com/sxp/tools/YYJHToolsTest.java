package com.sxp.tools;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;

import static org.junit.Assert.*;

public class YYJHToolsTest {

    @Test
    public void parseStrToDate() {
        try {
            System.out.println(YYJHTools.parseStrToDate("2019-9-11 15:49:20",""));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void formatDateToStr() {
        Date date = new Date();
        String d = YYJHTools.formatDateToStr(date,"");
        System.out.println(d);
    }

    @Test
    public void formatURL() {
        URL url = null;
        try {
            url = YYJHTools.formatURL("127.0.0.1:3306");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        System.out.println(url.getPath());
    }

    @Test
    public void isEmail() {
        boolean flag = YYJHTools.isEmail("456131894@qq.com");
        System.out.println(flag);
    }

    @Test
    public void filterInnerBlank() {
        System.out.println(YYJHTools.filterInnerBlank("ffa grg we"));
    }
}