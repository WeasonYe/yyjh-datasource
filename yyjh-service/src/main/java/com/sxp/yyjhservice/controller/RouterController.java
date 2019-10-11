package com.sxp.yyjhservice.controller;

import com.sxp.yyjhservice.config.jedis.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/router")
public class RouterController {
    @RequestMapping(value = "/datasource")
    public String datasource(){
        return "datasource/datasource_control";
    }
    @RequestMapping("/login")
    public String login(){return "login/index";}
    @RequestMapping("/logout")
    public String logout(){ return "login/index"; }
    @RequestMapping("/errorpage")
    public String error(){return "login/errorpage";}
    @RequestMapping("/redis")
    public String redis(){return "login/redis";}
}
