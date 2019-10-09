package com.sxp.tools;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.*;

//筛选文件内容
public class ExcelScreen {
    public static List<Map<String, Object>> findDate(File f, String start_line, String end_line, List<Map<String,Object>> index_string,String index_logic) throws Exception{
        List<Map<String, Object>> file_datas = new ArrayList<Map<String, Object>>();
        Workbook wb = null;//Excel文档对象
        Sheet sheet = null;//工作表
        // 选择2003/2007，构造Excel文档对象
        String fileName = f.getName();
        //获取开始行数
        int start;
        if (start_line == null) {
            start = 0;
        }else { start = Integer.parseInt(start_line);
        }
        //获取结束行数
        int end;
        if (end_line == null) {
            end = sheet.getLastRowNum();
        }else {
            end = Integer.parseInt(end_line);
        }
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (suffix.equalsIgnoreCase("xls")) {
            wb = new HSSFWorkbook(new NPOIFSFileSystem(f).getRoot(), true);
        } else if (suffix.equalsIgnoreCase("xlsx")) {
            try {
                wb = new XSSFWorkbook(f);
            } catch (Exception ex) {
                wb = new HSSFWorkbook(new FileInputStream(f));
            }
        }

        //获取Excel中的工作表
        for (int h = 0; h <wb.getNumberOfSheets(); h++) {
            sheet = wb.getSheetAt(h);//根据下标获取sheet
            Map<String, Object> datas = new LinkedHashMap<>();
            List<Map<String, Object>> sheet_datas = new ArrayList<>();
            //循环取行对象
            for(int i=start;i<=end;i++){
                //每行当做一个Map来存储
                Row row = sheet.getRow(i);
                Map<String,Object> line = new LinkedHashMap<String,Object>();

                boolean flag2=false;

                //循环取列对象
                for(int index=0;index<row.getLastCellNum();index++){
                    //获取列对象
                    Cell cell = row.getCell(index);
                    Object value = getCellValue(cell);
                    String test=value.toString();
                    boolean flag1=false;

                    if (index_logic.equals("or")){
                        for (int k=0;k<index_string.size();k++){
                            if (test.startsWith((String) index_string.get(k).get("start_string"))&&
                                    test.endsWith((String) index_string.get(k).get("end_string"))&&
                                    test.contains((String) index_string.get(k).get("content_string"))){
                                flag1=!flag1;
                                flag2=!flag2;
                                break;
                            }
                        }
                    }else {
                        int num=0;
                        for (int k=0;k<index_string.size();k++){
                            if (test.startsWith((String) index_string.get(k).get("start_string"))&&
                                    test.endsWith((String) index_string.get(k).get("end_string"))&&
                                    test.contains((String) index_string.get(k).get("content_string"))){
                                num++;
                            }
                        }
                        if (num==index_string.size()){
                            flag1=!flag1;
                            flag2=!flag2;
                        }
                    }

                    if (flag1) {
                        line.put("cel" + (index + 1), value);
                    }else {
                        line.put("cel" + (index + 1), "");
                    }
                }
                //存储行getCellValue(Cell cell)
                if(flag2){
                    sheet_datas.add(line);
                }

            }
            datas.put("sheet_datas", sheet_datas);
            file_datas.add(datas);
        }

        return file_datas;
    }

    //判断类型
    private static Object getCellValue(Cell cell){
        Object result = "";
        //判断列类型
        if (null != cell) {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC://判断是否数值
                    result = getCellByDate(cell);
                    break;
                case Cell.CELL_TYPE_BOOLEAN://判断是布尔型
                    result = cell.getBooleanCellValue();
                    break;
                case Cell.CELL_TYPE_STRING://判断是字符串型
                    result = cell.getStringCellValue();
                    break;
                case Cell.CELL_TYPE_FORMULA://判断是数字
                    result = cell.getNumericCellValue();
                    break;
                default:
                    result = "";
                    break;
            }
        }

        return result;
    }


    private static Object getCellByDate(Cell cell) {
        Object obj = null;
        if (HSSFDateUtil.isCellDateFormatted(cell)) {
            Date date = cell.getDateCellValue();
            obj = date;
        } else {
            DecimalFormat df = new DecimalFormat("0");
            // 如果是纯数字
            double value = cell.getNumericCellValue();
            int i = (int) value;
            // 判断是整数还是小数
            if (i == value) {
                obj = i;
            } else {
                obj = df.format(cell.getNumericCellValue());
            }
        }
        return obj;
    }


}
