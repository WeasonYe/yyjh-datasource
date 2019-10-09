package com.sxp.yyjhservice.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sxp.yyjhservice.config.jedis.JedisUtil;
import com.sxp.yyjhservice.domain.user.TUser;
import com.sxp.yyjhservice.enumeration.DatasourceEnum;
import com.sxp.yyjhservice.service.user.TUserService;
import com.sxp.yyjhservice.vo.ControllerResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/index")
public class LoginController {
    @Autowired
    TUserService tUserService;
    @Autowired
    JedisUtil jedisUtil;

    @RequestMapping("/login")
    public Object login(@RequestParam("log_id")Integer id,@RequestParam("log_email")String email,
                      @RequestParam("log_tel")String tel,@RequestParam("log_pwd")String pwd){
        ControllerResult controllerResult = new ControllerResult();
        controllerResult.setCode(DatasourceEnum.FAIL.getCode());
        controllerResult.setMsg(DatasourceEnum.FAIL.getMsg());
        TUser user = tUserService.findTUserById(id);
        if (user !=null){
            if (user.getEmail().equals(email)&&user.getTel().equals(tel)&&
                user.getPassword().equals(pwd)){
                controllerResult.setCode(DatasourceEnum.SUCCESS.getCode());
                controllerResult.setMsg(DatasourceEnum.SUCCESS.getMsg());
            }
        }
        return controllerResult;
    }

    @RequestMapping("/logout")
    public Object login(@RequestParam("log_id")String log_id){
        ControllerResult controllerResult = new ControllerResult();
        controllerResult.setCode(DatasourceEnum.FAIL.getCode());
        controllerResult.setMsg(DatasourceEnum.FAIL.getMsg());
        if (jedisUtil.get(log_id,0)!=null){
            jedisUtil.del(log_id,0);
            controllerResult.setCode(DatasourceEnum.SUCCESS.getCode());
            controllerResult.setMsg(DatasourceEnum.SUCCESS.getMsg());
        }
        return controllerResult;
    }

    @RequestMapping("/signup")
    public Object signup(@RequestParam("signup_datas") String datas) throws IOException {
        Map<String, Object> result = new HashMap<>();
        JsonNode jn = new ObjectMapper().readValue(datas,JsonNode.class);
        TUser user = new TUser();
        String username = jn.get("username").asText();
        String password = jn.get("pwd").asText();
        String tel = jn.get("tel").asText();
        String email = jn.get("email").asText();
        String nickname = jn.get("nick").asText();
        user.setLoginid(username);
        user.setPassword(password);
        user.setTel(tel);
        user.setEmail(email);
        user.setNickname(nickname);
        user.setCreateTime(new Date());
        user.setState("1");

        boolean flag = tUserService.addTUser(user);
        if (flag){
            result.put("code",0);
            result.put("msg","success");
        }else {
            result.put("code",-1);
            result.put("msg","fail");
        }
        return  result;
    }

    @RequestMapping("/updpassword")
    public Object updpassword(@RequestParam("u_id") Integer id,@RequestParam("u_pwd") String pwd){
        Map<String, Object> result = new HashMap<>();
        TUser u = tUserService.findTUserById(id);
        u.setPassword(pwd);
        boolean flag = tUserService.updTUser(u);
        if (flag){
            result.put("code",0);
            result.put("msg","success");
        }else {
            result.put("code",-1);
            result.put("msg","fail");
        }
        return result;
    }
}
