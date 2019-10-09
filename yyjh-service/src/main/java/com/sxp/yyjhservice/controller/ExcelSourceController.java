package com.sxp.yyjhservice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sxp.tools.ExcelOper;
import com.sxp.tools.YYJHTools;
import com.sxp.yyjhservice.domain.datasource.TDatasource;
import com.sxp.yyjhservice.enumeration.DatasourceEnum;
import com.sxp.yyjhservice.service.datasource.TDatasourceService;
import com.sxp.yyjhservice.vo.ControllerResult;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/excel")
public class ExcelSourceController {
    @Value("${file.upload.path.datasource.excel}")
    private String excel_path;
    @Autowired
    private TDatasourceService tDatasourceService;

    //上传
    @RequestMapping("/excelUpload")
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
}
