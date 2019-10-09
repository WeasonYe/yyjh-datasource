package com.sxp.tools;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.*;

public class ExcelAnalysis {

    public static List<Map<String, Object>> translateExcels(MultipartFile[] param_files) throws IOException {
        List<Map<String, Object>> file_datas = new ArrayList<Map<String, Object>>();
        for (MultipartFile f : param_files) {
            Map<String, Object> f_data = new HashMap<>();
            Workbook wb = null;//Excel文档对象
            Sheet sheet = null;//工作表
            // 选择2003/2007，构造Excel文档对象
            String fileName = f.getOriginalFilename();
            InputStream f_in = f.getInputStream();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            if (suffix.equalsIgnoreCase("xls")) {
                wb = new HSSFWorkbook(new NPOIFSFileSystem(f_in).getRoot(), true);
            } else if (suffix.equalsIgnoreCase("xlsx")) {
                try {
                    wb = new XSSFWorkbook(f_in);
                } catch (Exception ex) {
                    wb = new HSSFWorkbook(f_in);
                }
            }

            //获取Excel中的工作表 getSheet(String name)根据sheet名称来获取
            for (int in = 0; in < wb.getNumberOfSheets(); in++) {
                sheet = wb.getSheetAt(in);//根据下标获取sheet
                Map<String, Object> datas = new LinkedHashMap<>();
                List<Map<String, Object>> sheet_datas = new ArrayList<>();
                //循环取行对象
                for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                    //每行当做一个Map来存储
                    Row row = sheet.getRow(i);
                    if (null == row)
                        continue;
                    Map<String, Object> line = new LinkedHashMap<>();
                    //循环取列对象
                    for (int index = 0; index < row.getLastCellNum(); index++) {
                        //获取列对象
                        Cell cell = row.getCell(index);
                        Object value = getCellValue(cell);
                        line.put("col" + (index + 1), value);
                    }
                    //存储行getCellValue(Cell cell)
                    sheet_datas.add(line);
                }
                datas.put("sheet_datas", sheet_datas);
                file_datas.add(datas);
            }
        }
        return file_datas;
    }

    //判断类型
    private static Object getCellValue(Cell cell) {
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
