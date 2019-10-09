package com.sxp.yyjhservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/router")
public class RouterController {
    @RequestMapping(value = "/datasource")
    public String datasource(){
        return "datasource/datasource";
    }
    @RequestMapping("/login")
    public String login(){return "login/index";}
    @RequestMapping("/logout")
    public String logout(){ return "login/index";}//方法体不执行
    @RequestMapping("/errorpage")
    public String error(){return "login/errorpage";}
    @RequestMapping("/redis")
    public String redis(){return "login/redis";}
}
