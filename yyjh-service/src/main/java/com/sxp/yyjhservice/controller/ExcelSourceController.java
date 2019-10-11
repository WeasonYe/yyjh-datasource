package com.sxp.yyjhservice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.sxp.tools.*;
import com.sxp.yyjhservice.domain.datasource.TDatasource;
import com.sxp.yyjhservice.enumeration.DatasourceEnum;
import com.sxp.yyjhservice.service.datasource.TDatasourceService;
import com.sxp.yyjhservice.vo.ControllerResult;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.FileUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/excel")
public class ExcelSourceController {
    @Value("${file.upload.path.datasource.excel}")
    private String excel_path;

    //file转MultipartFile方法
    public static MultipartFile convertFileToMulti(String path) throws IOException {
        File file = new File(path);
        FileItemFactory fileItemFactory = new DiskFileItemFactory(16, null);
        FileItem item = fileItemFactory.createItem(file.getName(), "text/plain", true, file.getName());
        int bytesRead;
        byte[] buffer = new byte[8192];

        FileInputStream fileInputStream = new FileInputStream(path);
        OutputStream outputStream = item.getOutputStream();
        while ((bytesRead = fileInputStream.read(buffer, 0, 8192)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        fileInputStream.close();
        outputStream.close();

        return new CommonsMultipartFile(item);
    }


    @Autowired
    private TDatasourceService tDatasourceService;

    //上传
    @RequestMapping("/excelUpload")
    @RequiresRoles(value = {"super_admin","data_manager"},logical = Logical.OR)
    @RequiresPermissions(value = {"add"})
    public Object excelUpload(@RequestParam("excel_upload")MultipartFile[] files,
                              @RequestParam("excel_interpret")String excel_interpret) throws IOException {
        ControllerResult result = new ControllerResult();
        result.setCode(DatasourceEnum.NOFILE.getCode());
        result.setMsg(DatasourceEnum.NOFILE.getMsg());
        if (null!=files&&files.length!=0){
            //创建目录
            File directory=new File(excel_path);
            if(!directory.exists()){
                directory.mkdirs();
            }
            //遍历
            for (MultipartFile file:files){
                String original =file.getOriginalFilename();
                String suffix=original.substring(original.lastIndexOf("."));
                if (!(".xlsx".equals(suffix.trim()) || ".xls".equals(suffix.trim())))
                    continue;
                //生成UUID
                String uuid= YYJHTools.get32UUID();
                String real_path=excel_path+"/"+uuid+suffix;
                File excel=new File(real_path);
                //存库，存文件
                FileUtils.copyInputStreamToFile(file.getInputStream(),excel);

                TDatasource excel_data=new TDatasource();
                excel_data.setDatabaseName(uuid);
                excel_data.setType("excel");
                excel_data.setUrl(real_path);
                excel_data.setAlias(original);
                ObjectMapper om = new ObjectMapper();
                List<Map<String,Object>> excel_interprets = om.readValue(excel_interpret,new TypeReference<List<Map<String,Object>>>(){});
                for (Map<String, Object> excel_in:excel_interprets){
                    if (original.equals(excel_in.get("filename").toString())){
                        excel_data.setEncode(om.writeValueAsString(excel_in));
                        break;
                    }
                }
                boolean flag=tDatasourceService.addTDataSource(excel_data);
                if (flag){
                    result.setCode(DatasourceEnum.SUCCESS.getCode());
                    result.setMsg(DatasourceEnum.SUCCESS.getMsg());
                }else {
                    result.setCode(DatasourceEnum.FAIL.getCode());
                    result.setMsg(DatasourceEnum.FAIL.getMsg());
                }
            }
        }
        return result;
    }

    //预览
    @RequestMapping("/excelPreview")
    @RequiresRoles(value = {"super_admin","data_manager"},logical = Logical.OR)
    @RequiresPermissions(value = {"add"})
    public Object excelPreview(@RequestParam("excel_upload") MultipartFile[] param_files) throws IOException {
        ControllerResult result = new ControllerResult();
        result.setCode(DatasourceEnum.NOFILE.getCode());
        result.setMsg(DatasourceEnum.NOFILE.getMsg());
        if (null!=param_files&&param_files.length!=0){
            List<Map<String,Object>> datas = ExcelOper.translateExcels(param_files);
            //返回数据
            result.setCode(DatasourceEnum.SUCCESS.getCode());
            result.setMsg(DatasourceEnum.SUCCESS.getMsg());
            result.setPayload(datas);
        }
        return result;
    }

    //入库
    @RequestMapping("/excelWarehouse")
    @RequiresRoles(value = {"super_admin","data_manager"},logical = Logical.OR)
    @RequiresPermissions(value = {"add"})
    public Object excelWarehouse(@RequestParam Integer id) throws Exception {
        ControllerResult result = new ControllerResult();
        TDatasource tDatasource=tDatasourceService.findTDataSourceById(id);
        String tablename=tDatasource.getDatabaseName();
        //获取文件路径
        String path=tDatasource.getUrl();
        //解析interpret
        String excel_interpret=tDatasource.getEncode();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(excel_interpret);
        System.out.println(rootNode);
        boolean isfilter=rootNode.path("isfilter").asBoolean();
        System.out.println(isfilter);
        if (!isfilter) {
            try {
                MultipartFile[] files = new MultipartFile[1];
                files[0] = convertFileToMulti(path);
                List<Map<String, Object>> datas = ExcelAnalysis.translateExcels(files);
                System.out.println("数据：" + datas);
                int keylength = 0;
                for (int i = 0; i < datas.size(); i++) {
                    List<String> keys = new ArrayList<String>();
                    List<Map<String, Object>> sheet_datas = (List<Map<String, Object>>) datas.get(i).get("sheet_datas");
                    System.out.println(sheet_datas);
                    for (int j = 0; j < sheet_datas.size(); j++) {
                        if (keylength < sheet_datas.get(j).size()) {
                            keylength = sheet_datas.get(j).size();
                        }
                    }
                    for (int j = 1; j < keylength + 1; j++) {
                        keys.add("col" + j);
                    }
                    System.out.println(keys);
                    String stringWareHouseDatas = mapper.writeValueAsString(sheet_datas);
                    ArrayNode wareHouseDatas = mapper.readValue(stringWareHouseDatas, ArrayNode.class);
                    boolean flag = JDBCTools.createTable(tablename + "_sheet" + i, keys, wareHouseDatas);
                    System.out.println(flag);
                }
                result.setCode(0);
            } catch (Exception e) {
                result.setCode(-1);
            }
        } else {
//            try {
                JsonNode interpret_filter=rootNode.get("interpret_filter");
                System.out.println(interpret_filter);
                String start_line=interpret_filter.path("start_line").asText();
                String end_line=interpret_filter.path("end_line").asText();
                String index_logic=interpret_filter.path("index_logic").asText();
                String json=interpret_filter.path("index_string").toString();
                List<Map<String,Object>> index_string = new ObjectMapper().readValue(json, new TypeReference<List<Map<String,Object>>>(){});
                System.out.println(index_string);
                File file=new File(path);
                List<Map<String, Object>> datas = ExcelScreen.findDate(file,start_line,end_line,index_string,index_logic);
                System.out.println("数据：" + datas+","+datas.size());
                int keylength = 0;
                for (int i = 0; i < datas.size(); i++) {
                    List<String> keys = new ArrayList<String>();
                    List<Map<String, Object>> sheet_datas = (List<Map<String, Object>>) datas.get(i).get("sheet_datas");
                    System.out.println(sheet_datas);
                    for (int j = 0; j < sheet_datas.size(); j++) {
                        if (keylength < sheet_datas.get(j).size()) {
                            keylength = sheet_datas.get(j).size();
                        }
                    }
                    for (int j = 1; j < keylength + 1; j++) {
                        keys.add("col" + j);
                    }
                    System.out.println(keys);
                    String stringWareHouseDatas = mapper.writeValueAsString(sheet_datas);
                    ArrayNode wareHouseDatas = mapper.readValue(stringWareHouseDatas, ArrayNode.class);
                    System.out.println(wareHouseDatas);
                    boolean flag = JDBCTools.createTable(tablename + "_sheet" + i, keys, wareHouseDatas);
                    System.out.println(flag);
                }
                result.setCode(0);
//            } catch (Exception e) {
//                result.setCode(-1);
//            }

        }
        return result;
    }

}
