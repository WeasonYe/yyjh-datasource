package com.sxp.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

import java.io.IOException;
import java.sql.*;
import java.util.*;

public class JDBCTools {
    private static String url = "yyjh_datasource";
    private static String username = "root";
    private static String password = "123456";
    private static Connection conn = null;

    /**
     * 建表方法
     * 没有字段类型，默认全是varchar，
     * */
    public static boolean createTable(String tableName, List<String> keys, ArrayNode datas,String primary_key) throws SQLException {
        conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/"+url+"?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true",username,password);
        int count = -1;
        if (conn != null){
            //拼接建表语句
            String tableSql = "create table t_" + tableName + " (";
            for (String key : keys){
                tableSql += key + " varchar(50),";
            }
            tableSql += "PRIMARY KEY (" + primary_key + "));\n";
            //拼接insert语句
            tableSql += "insert into t_"+ tableName + " (";
            for (String key: keys){
                tableSql += key + ",";
            }
            tableSql = tableSql.substring(0,tableSql.length()-1);
            tableSql += ") values(";
            for (int i = 0; i < datas.size(); i++){
                JsonNode jn = datas.get(i);
                for (int j = 0; j < keys.size(); j++) {
                    try {
                        String value = jn.get(keys.get(j)).asText();
                        tableSql += "'" + jn.get(keys.get(j)).asText() + "',";
                    }catch (NullPointerException e){
                        tableSql += "null,";
                    }
                }
                tableSql = tableSql.substring(0, tableSql.length() - 1);
                tableSql += "),(";
            }
            tableSql = tableSql.substring(0,tableSql.length()-2);
            tableSql += ";";
            Statement smt = conn.createStatement();
            count = smt.executeUpdate(tableSql);
        }
        if (count==0){
            return true;
        }else
            return false;
    }

    /**
     * 建表方法
     * 没有字段类型，默认全是varchar，
     * */
    public static boolean createTable(String tableName, List<String> keys, ArrayNode datas) throws SQLException {
        conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/"+url+"?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true",username,password);
        int count = -1;
        if (conn != null){
            //拼接建表语句
            String tableSql = "create table t_" + tableName + " (id int(10) auto_increment primary key,";
            for (String key : keys){
                tableSql += key + " varchar(50),";
            }
            tableSql = tableSql.substring(0,tableSql.length()-1);
            tableSql += ");\n";
            //拼接insert语句
            tableSql += "insert into t_"+ tableName + " (";
            for (String key: keys){
                tableSql += key + ",";
            }
            tableSql = tableSql.substring(0,tableSql.length()-1);
            tableSql += ") values(";
            for (int i = 0; i < datas.size(); i++){
                JsonNode jn = datas.get(i);
                for (int j = 0; j < keys.size(); j++) {
                    try {
                        String value = jn.get(keys.get(j)).asText();
                        tableSql += "'" + jn.get(keys.get(j)).asText() + "',";
                    }catch (NullPointerException e){
                        tableSql += "null,";
                    }
                }
                tableSql = tableSql.substring(0, tableSql.length() - 1);
                tableSql += "),(";
            }
            tableSql = tableSql.substring(0,tableSql.length()-2);
            tableSql += ";";
            Statement smt = conn.createStatement();

            System.out.println(tableSql);

            count = smt.executeUpdate(tableSql);
        }
        if (count==0){
            return true;
        }else
            return false;
    }

    /**
     * 获取某数据库的所有表名的方法
     * */
    public static List<String> getTables(String url,String username,String password) throws SQLException {
        conn = DriverManager.getConnection(url,username,password);
        List<String> result = new ArrayList<String>();
        DatabaseMetaData dmd = conn.getMetaData();
        ResultSet rs = dmd.getTables(null, "%","%",new String[]{"TABLE"});
        while (rs.next()) {
            result.add(rs.getString("TABLE_NAME"));
        }
        return result;
    }

    /**
     * 复制一个库的某些表复制入本地库
     * */
    public static boolean copyTables(ObjectNode db_conf,List<String> tables) throws SQLException, IOException {
        String fromurl = db_conf.get("url").asText();
        String fromusername = db_conf.get("username").asText();
        String frompassword = db_conf.get("password").asText();
        conn = DriverManager.getConnection(fromurl,fromusername,frompassword);
        ObjectMapper om = new ObjectMapper();
        List<Map<String,Object>> tablesdatas = new ArrayList<>();
        if (conn != null){//取出要存入的表的数据
            DatabaseMetaData md = conn.getMetaData();
            String catalog = conn.getCatalog();
            for (int i = 0; i < tables.size(); i++){//遍历需要入库的表
                Map<String,Object> tabledata = new LinkedHashMap<>();
                String tablename = tables.get(i);
                tabledata.put("tablename",tablename);
                ResultSet priset = md.getPrimaryKeys(catalog,null,tablename);
                String primarykey = "";
                while (priset.next()){
                    primarykey = priset.getString("COLUMN_NAME");
                }
                ResultSet colRet = md.getColumns(null,"%", tablename,"%");
                List<String> create_sqls = new ArrayList<>();
                List<String> cols = new ArrayList<>();
                List<String> colTypes = new ArrayList<>();
                while (colRet.next()){//遍历表的列信息
                        String columnName = colRet.getString("COLUMN_NAME");
                        String columnType = colRet.getString("TYPE_NAME");
                        int datasize = colRet.getInt("COLUMN_SIZE");
                        int digits = colRet.getInt("DECIMAL_DIGITS");
                        int nullable = colRet.getInt("NULLABLE");
                        cols.add(columnName);
                        if (columnType.equals("INT UNSIGNED"))
                            columnType = "INT";
                        if (columnType.equals("DATETIME"))
                            datasize = 6;
                        colTypes.add(columnType);
                        String create_sql = columnName + " " + columnType + "(" + datasize;
                        if (0 == digits){
                            create_sql += ") ";
                        }else {
                            create_sql += "," + digits + ") ";
                        }

                        if (nullable == 0){
                            create_sql += "not null,";
                        }else {
                            create_sql += ",";
                        }

                        create_sqls.add(create_sql);
                }
                create_sqls.add("PRIMARY KEY (" + primarykey + ")");
                tabledata.put("create_sqls",create_sqls);
                tabledata.put("cols",cols);
                tabledata.put("colsType",colTypes);

                //获取表内数据
                Statement smt = conn.createStatement();
                ResultSet rs = smt.executeQuery("SELECT * FROM " + tablename);
                List<Map<String,Object>> rows = new ArrayList<>();
                int rownum = 0;
                while (rs.next()){//获取行数
                    rownum++;
                }
                for (int j = 0; j < rownum; j++){//代表第几行
                    Map<String,Object> row = new LinkedHashMap<>();
                    rows.add(row);
                }
                rs = smt.executeQuery("SELECT * FROM " + tablename);
                int index = 0;
                while (rs.next()){
                    for (int colnum = 0; colnum < cols.size(); colnum++){

                        switch (colTypes.get(colnum)){
                            case "INT" : rows.get(index).put(cols.get(colnum),rs.getInt(cols.get(colnum)));break;
                            case "VARCHAR" : rows.get(index).put(cols.get(colnum),rs.getString(cols.get(colnum)));break;
                            case "CHAR" : rows.get(index).put(cols.get(colnum),rs.getString(cols.get(colnum)));break;
                            case "DOUBLE" : rows.get(index).put(cols.get(colnum),rs.getDouble(cols.get(colnum)));break;
                            case "DATETIME" : rows.get(index).put(cols.get(colnum),rs.getString(cols.get(colnum)).substring(0,rs.getString(cols.get(colnum)).length()-2));break;
                        }
                    }
                    index++;
                }
                tabledata.put("datas",rows);
                tablesdatas.add(tabledata);
            }
        }

        conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/"+url+"?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true",username,password);
        if (conn != null){//连接本地数据库,把表存入
            String ts_datas_str = om.writeValueAsString(tablesdatas);
            ArrayNode ts_datas = om.readValue(ts_datas_str,ArrayNode.class);
            for (int i = 0; i < ts_datas.size(); i++){//遍历每一张表
                JsonNode table = ts_datas.get(i);
                String tablename = table.get("tablename").asText();
                ArrayNode crete_sqls_json = (ArrayNode) table.get("create_sqls");
                String sql = "create table " + tablename + "(";
                for (int j = 0; j < crete_sqls_json.size(); j++){//遍历形成建表语句
                    sql += crete_sqls_json.get(j).asText();
                }
                sql += ");\n";

                ArrayNode cols = (ArrayNode) table.get("cols");
                ArrayNode table_datas = (ArrayNode) table.get("datas");
                ArrayNode colsTypes = (ArrayNode) table.get("colsType");
                sql += "insert into " + tablename + "(";
                for (int j = 0; j < cols.size(); j++){
                    sql += cols.get(j).asText() + ",";
                }
                sql = sql.substring(0,sql.length()-1) + ")values(";
                for (int j = 0; j < table_datas.size(); j++){//遍历每一行的数据
                    JsonNode row_datas = table_datas.get(j);
                    for (int k = 0; k < cols.size(); k++){
                        switch (colsTypes.get(k).asText()){
                            case "INT" : sql += row_datas.get(cols.get(k).asText()).asInt() + ",";break;
                            case "VARCHAR" : sql += "\"" + row_datas.get(cols.get(k).asText()).asText() + "\",";break;
                            case "CHAR" : sql += "'" + row_datas.get(cols.get(k).asText()).asText() + "',";break;
                            case "DOUBLE" : sql += row_datas.get(cols.get(k).asText()).asDouble() + ",";break;
                            case "DATETIME" : sql += "\"" + row_datas.get(cols.get(k).asText()).asText() + "\",";break;
                        }
                    }
                    sql = sql.substring(0,sql.length()-1) + "),(";
                }
                sql = sql.substring(0,sql.length()-2) + ";";

                Statement smt = conn.createStatement();
                if (smt.executeUpdate(sql) == 0)
                    return true;
            }
        }
        return false;
    }
}
