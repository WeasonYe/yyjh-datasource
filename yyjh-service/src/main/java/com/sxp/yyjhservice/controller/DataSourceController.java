package com.sxp.yyjhservice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.sxp.yyjhservice.domain.datasource.TDatasource;
import com.sxp.yyjhservice.domain.page.PageHelper;
import com.sxp.yyjhservice.enumeration.DatasourceEnum;
import com.sxp.yyjhservice.service.datasource.TDatasourceService;
import com.sxp.yyjhservice.vo.ControllerResult;
import org.apache.poi.ss.formula.functions.T;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/datasource")
public class DataSourceController {

    @Autowired
    private TDatasourceService tDatasourceService;

    @RequestMapping("/delTDataSourceById")
    @RequiresRoles(value = {"super_admin","data_manager"},logical = Logical.OR)
    @RequiresPermissions(value = {"delete"})
    public Object delTDataSourceById(@RequestParam Integer id) {
        System.out.println(id);
        boolean flag = tDatasourceService.delTDataSourceById(id);
        Map<String, Object> result = new HashMap<String, Object>();
        System.out.println(flag);
        if(flag){
            result.put("code", 0);//成功
        }else{
            result.put("code", -1);//失败
        }
        System.out.println(result);
        return result;
    }

    @RequestMapping("/addTDataSource")
    @RequiresRoles(value = {"super_admin","data_manager"},logical = Logical.OR)
    @RequiresPermissions(value = {"add"})
    public Object addTDataSource(@RequestParam String json) throws Exception{
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            TDatasource tDatasource = new ObjectMapper().readValue(json, TDatasource.class);
            boolean flag = tDatasourceService.addTDataSource(tDatasource);
            if(flag){
                result.put("code", 0);//成功
            }else{
                result.put("code", -1);
            }
        } catch (Exception e) {
            result.put("code", -1);
        }
        System.out.println(result);
        return result;
    }

    @RequestMapping("/findTDataSourceById")
    @RequiresRoles(value = {"super_admin","data_manager"},logical = Logical.OR)
    @RequiresPermissions(value = {"select"})
    public Object findTDataSourceById(@RequestParam Integer id) throws Exception{
        TDatasource tDatasource = new TDatasource();
        tDatasource = tDatasourceService.findTDataSourceById(id);
        Map<String, Object> result = new HashMap<String, Object>();
        if(tDatasource != null){
            result.put("code", 0);//成功
            result.put("data", tDatasource);
        }else{
            result.put("code", -1);
        }
        return result;
    }

    @PostMapping("/updTDataSourceById")
    @RequiresRoles(value = {"super_admin","data_manager"},logical = Logical.OR)
    @RequiresPermissions(value = {"select"})
    public Object updTDataSourceById(@RequestParam String json,@RequestParam String excel_interpret) throws Exception{
        ControllerResult result = new ControllerResult();
        result.setCode(DatasourceEnum.NOFILE.getCode());
        result.setMsg(DatasourceEnum.NOFILE.getMsg());
        TDatasource tDatasource = new ObjectMapper().readValue(json, TDatasource.class);
        Date date = new Date();
        tDatasource.setUpdatetime(date);
        ObjectMapper om = new ObjectMapper();
        List<Map<String,Object>> excel_interprets = om.readValue(excel_interpret,new TypeReference<List<Map<String,Object>>>(){});
        for (Map<String, Object> excel_in:excel_interprets){
            if (tDatasource.getAlias().equals(excel_in.get("filename").toString())){
                tDatasource.setEncode(om.writeValueAsString(excel_in));
                break;
            }
        }
        boolean flag = tDatasourceService.updTDataSourceById(tDatasource);
        if (flag){
            result.setCode(DatasourceEnum.SUCCESS.getCode());
            result.setMsg(DatasourceEnum.SUCCESS.getMsg());
        }else {
            result.setCode(DatasourceEnum.FAIL.getCode());
            result.setMsg(DatasourceEnum.FAIL.getMsg());
        }
        System.out.println(result);
        return result;
    }

    @RequestMapping("/getAll")
    @RequiresRoles(value = {"super_admin","data_manager"},logical = Logical.OR)
    @RequiresPermissions(value = {"select"})
    public Object getAll() {
        List<TDatasource> tDatasources;
        tDatasources = tDatasourceService.getAll();
        Map<String, Object> result = new HashMap<>();
        if(tDatasources != null){
            result.put("code", 0);//成功
            result.put("data", tDatasources);
        }else{
            result.put("code", -1);
        }
        return result;
    }


    @RequestMapping("/getTDatasourceListPage")
    @RequiresRoles(value = {"super_admin","data_manager"},logical = Logical.OR)
    @RequiresPermissions(value = {"select"})
    public PageHelper<TDatasource> getTDatasourceListPage(TDatasource tDatasource, HttpServletRequest request) {
        System.out.println(tDatasource);
        PageHelper<TDatasource> pageHelper = new PageHelper<>();
        // 统计总记录数
        Integer total = tDatasourceService.getTotal();
        pageHelper.setTotal(total);

        // 查询当前页实体对象
        List<TDatasource> list = tDatasourceService.getTDatasourceListPage(tDatasource);
        pageHelper.setRows(list);

        return pageHelper;
    }

//    批量删除
    @RequestMapping("/batchDeleteById")
    @RequiresRoles(value = {"super_admin","data_manager"},logical = Logical.OR)
    @RequiresPermissions(value = {"delete"})
    public Object batchDeleteById(@RequestParam String ids) throws IOException {
        ArrayNode idsarry = new ObjectMapper().readValue(ids,ArrayNode.class);
        List<Integer> idsList = new ArrayList<>();
        for (int i = 0; i < idsarry.size();i++){
            idsList.add(idsarry.get(i).asInt());
        }
        System.out.println(ids);
        boolean flag = tDatasourceService.batchDeleteById(idsList);
        Map<String, Object> result = new HashMap<String, Object>();
        System.out.println(flag);
        if(flag){
            result.put("code", 0);//成功
        }else{
            result.put("code", -1);//失败
        }
        System.out.println(result);
        return result;
    }

}
