package com.sxp.yyjhservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.sxp.tools.JDBCTools;
import com.sxp.yyjhservice.config.jedis.JedisUtil;
import com.sxp.yyjhservice.domain.datasource.TDatasource;
import com.sxp.yyjhservice.enumeration.DatasourceEnum;
import com.sxp.yyjhservice.service.datasource.TDatasourceService;
import com.sxp.yyjhservice.vo.ControllerResult;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@RestController
@RequestMapping("/redis")
public class RedisConfController {
    @Autowired
    TDatasourceService tDatasourceService;
    @Autowired
    JedisUtil jedisUtil;

    @RequestMapping("/redisConfig")
    @RequiresRoles(value = {"super_admin","data_manager"},logical = Logical.OR)
    @RequiresPermissions(value = {"add"})
    public Object redisConfig(@RequestParam("r_ip")String ip,@RequestParam("r_port")Integer port,@RequestParam("r_pwd")String pwd){
        ControllerResult controllerResult = new ControllerResult();
        controllerResult.setCode(DatasourceEnum.FAIL.getCode());
        controllerResult.setMsg(DatasourceEnum.FAIL.getMsg());
        TDatasource datasource= new TDatasource();
        if (ip!=null&&port!=null&&pwd!=null){
            datasource.setUrl(ip);
            datasource.setPort(port);
            datasource.setPassword(pwd);
            datasource.setType("redis");
            tDatasourceService.addTDataSource(datasource);

            controllerResult.setCode(DatasourceEnum.SUCCESS.getCode());
            controllerResult.setMsg(DatasourceEnum.SUCCESS.getMsg());
        }
        return controllerResult;
    }

    @RequestMapping("/configTest")
    public Object configTest(@RequestParam("r_ip")String ip,@RequestParam("r_port")Integer port,@RequestParam("r_pwd")String pwd){
        ControllerResult controllerResult = new ControllerResult();
        controllerResult.setCode(DatasourceEnum.FAIL.getCode());
        controllerResult.setMsg(DatasourceEnum.FAIL.getMsg());
        try {
            Jedis jedis = new Jedis(ip,port);
            if(jedis.auth(pwd).equals("OK")){
                controllerResult.setCode(DatasourceEnum.SUCCESS.getCode());
                controllerResult.setMsg(DatasourceEnum.SUCCESS.getMsg());
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return controllerResult;
    }

    @RequestMapping("/getRedisDb")
    @RequiresRoles(value = {"super_admin","data_manager"},logical = Logical.OR)
    @RequiresPermissions(value = {"add"})
    public Object getRedisDb(){
        ControllerResult controllerResult = new ControllerResult();
        controllerResult.setCode(DatasourceEnum.FAIL.getCode());
        controllerResult.setMsg(DatasourceEnum.FAIL.getMsg());

        List<String> dbs = jedisUtil.getDBS();
        if (dbs.size()>0){
            controllerResult.setCode(DatasourceEnum.SUCCESS.getCode());
            controllerResult.setMsg(DatasourceEnum.SUCCESS.getMsg());
            controllerResult.setPayload(dbs);
        }
        return controllerResult;
    }

    @RequestMapping("/redisInDb")
    @RequiresRoles(value = {"super_admin","data_manager"},logical = Logical.OR)
    @RequiresPermissions(value = {"add"})
    public Object redisInDb(@RequestParam("db_nums")String db_nums) throws IOException, SQLException {
        ControllerResult controllerResult = new ControllerResult();
        controllerResult.setCode(DatasourceEnum.FAIL.getCode());
        controllerResult.setMsg(DatasourceEnum.FAIL.getMsg());

        ArrayNode dbs = new ObjectMapper().readValue(db_nums,ArrayNode.class);
        if (dbs.size()!=0){
            List<String> colName = new ArrayList<>();//redis库的列名
            colName.add("r_key");colName.add("r_value");
            for (int i = 0; i < dbs.size(); i++){
                int db = dbs.get(i).asInt();//入库的redis库号
                Iterator<String> keys = jedisUtil.getDBKeys(db).iterator();
                List<Map<String,Object>> dataList = new ArrayList<>();//一个redis库建一张表
                while (keys.hasNext()){
                    String key = String.valueOf(keys.next());
                    Map<String,Object> data = new LinkedHashMap<>();//一个map一条key value记录
                    data.put("r_key",key);
                    data.put("r_value",jedisUtil.get(key,db));
                    dataList.add(data);
                }
                String jsonstr = new ObjectMapper().writeValueAsString(dataList);
                ArrayNode datas = new ObjectMapper().readValue(jsonstr,ArrayNode.class);
                JDBCTools.createTable("redisDB"+db,colName,datas,colName.get(0));
            }
            controllerResult.setCode(DatasourceEnum.SUCCESS.getCode());
            controllerResult.setMsg(DatasourceEnum.SUCCESS.getMsg());
        }
        return controllerResult;
    }
}
