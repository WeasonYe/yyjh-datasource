package com.sxp.yyjhservice.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sxp.tools.JDBCTools;
import com.sxp.tools.YYJHTools;
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

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/mysql")
public class MysqlSourceController {
    @Autowired
    private TDatasourceService tDatasourceService;

    /**
     * MYSQL数据源配置
     * 必须的字段:url,username,password,encode
     * */
    @RequestMapping("/mysqlUpload")
    @RequiresRoles(value = {"super_admin","data_manager"},logical = Logical.OR)
    @RequiresPermissions(value = {"add"})
    private Object mysqlUpload(@RequestParam String mysql_conf_str) throws IOException {
        boolean flag = true;
        ArrayNode mysql_confs = new ObjectMapper().readValue(mysql_conf_str, ArrayNode.class);
        ControllerResult result = new ControllerResult();
        result.setCode(DatasourceEnum.FAIL.getCode());
        result.setMsg(DatasourceEnum.FAIL.getMsg());
        for (int i = 0; i < mysql_confs.size(); i++){
            JsonNode mysql_conf = mysql_confs.get(i);
            if (mysql_conf.size() > 0){
                String databaseName = mysql_conf.get("database_name").asText();
                String driver = "com.mysql.jdbc.Driver";
                String url = mysql_conf.get("url").asText();
                String description = mysql_conf.get("description").asText();
                Integer port = mysql_conf.get("port").asInt();
                String username = mysql_conf.get("username").asText();
                String password = mysql_conf.get("password").asText();
                String encode = mysql_conf.get("encode").asText();
                TDatasource td = new TDatasource();
                td.setType("mysql");
                td.setDatabaseName(databaseName);
                td.setPort(port);
                td.setDriver(driver);
                td.setUrl(url);
                td.setDescription(description);
                td.setUsername(username);
                td.setPassword(password);
                td.setEncode(encode);
                flag = tDatasourceService.addTDataSource(td) && flag ;
            }
        }
        if (flag){
            result.setCode(DatasourceEnum.SUCCESS.getCode());
            result.setMsg(DatasourceEnum.SUCCESS.getMsg());
        }
        return result;
    }

    @RequestMapping("/mysqlTablesPreview")
    @RequiresRoles(value = {"super_admin","data_manager"},logical = Logical.OR)
    @RequiresPermissions(value = {"add"})
    public Object mysqlPreview(@RequestParam("mysql_id") Integer id) throws SQLException {
        ControllerResult result = new ControllerResult();
        result.setCode(DatasourceEnum.FAIL.getCode());
        result.setMsg(DatasourceEnum.FAIL.getMsg());
        TDatasource td = tDatasourceService.findTDataSourceById(id);
        String url = "jdbc:mysql://" + td.getUrl() + ":" + td.getPort() + "/" + td.getDatabaseName() + "?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true";
        String username = td.getUsername();
        String password = td.getPassword();
        String thisurl = "jdbc:mysql://127.0.0.1:3306/yyjh_datasource?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true";
        List<String> tables = JDBCTools.getTables(url,username,password);
        List<String> thistables = JDBCTools.getTables(thisurl,username,password);
        for (int i = 0; i < tables.size(); i++){//查找是否已入库
            String table = tables.get(i);
            for (String thistable: thistables){
                if (thistable.equals(table)){
                    tables.remove(i);
                    i--;
                    break;
                }
            }
        }
        if (tables.size() > 0){
            result.setPayload(tables);
            result.setCode(DatasourceEnum.SUCCESS.getCode());
            result.setMsg(DatasourceEnum.SUCCESS.getMsg());
        }
        return result;
    }

    @RequestMapping("/mysqlSave")
    @RequiresRoles(value = {"super_admin","data_manager"},logical = Logical.OR)
    @RequiresPermissions(value = {"add"})
    public Object mysqlSave(@RequestParam("tables_name") String tablesName) throws IOException, SQLException {
        ControllerResult result = new ControllerResult();
        result.setCode(DatasourceEnum.FAIL.getCode());
        result.setMsg(DatasourceEnum.FAIL.getMsg());
        JsonNode tables = new ObjectMapper().readValue(tablesName, JsonNode.class);
        int id = tables.get("ds_id").asInt();
        ArrayNode names_json = (ArrayNode) tables.get("tables_name");
        List<String> names = new ArrayList<String>();
        for (int i = 0; i < names_json.size(); i++){
            names.add(names_json.get(i).asText());
        }
        TDatasource td = tDatasourceService.findTDataSourceById(id);
        String url = "jdbc:mysql://" + td.getUrl() + ":" + td.getPort() + "/" + td.getDatabaseName() + "?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true";
        String username = td.getUsername();
        String password = td.getPassword();
        ObjectMapper om = new ObjectMapper();
        ObjectNode on = om.createObjectNode();
        on.put("url",url);
        on.put("username",username);
        on.put("password",password);
        boolean flag = JDBCTools.copyTables(on,names);
        if (flag){
            result.setCode(DatasourceEnum.SUCCESS.getCode());
            result.setMsg(DatasourceEnum.SUCCESS.getMsg());
        }
        return result;
    }
}
