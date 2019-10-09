package com.sxp.yyjhservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/testcsv")
    public String testcsv(){
        return "datasource/datasource";
    }

    @RequestMapping("/index")
    public String index(){
        return "datasource/index";
    }
}
