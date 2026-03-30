package com.easyjava.builder;

import com.easyjava.bean.Constants;
import com.easyjava.bean.FieldInfo;
import com.easyjava.bean.TableInfo;
import com.easyjava.utils.JsonUtils;
import com.easyjava.utils.PropertiesUtils;
import com.easyjava.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

public class BuilderTable {
    private static final Logger logger = LoggerFactory.getLogger(BuilderTable.class);
    private static Connection conn = null;

    private static String SQL_SHOW_TABLE_STATUS = "show table status";
    // %s -> TableInfo.getTableName -> show full fields from tb_product_info;
    private static String SQL_SHOW_TABLE_FIELDS = "show full fields from %s";

    private static String SQL_SHOW_TABLE_INDEXES = "show index from %s";

    static {
        String driverName = PropertiesUtils.getString("db.driver.name");
        String urlName = PropertiesUtils.getString("db.url");
        String username = PropertiesUtils.getString("db.username");
        String pwd = PropertiesUtils.getString("db.password");

        try {
            Class.forName(driverName);
            // 建立连接
            conn = DriverManager.getConnection(urlName, username, pwd);
        } catch (Exception e) {
            logger.error("数据库连接失败", e);
        }

    }

    public static List<TableInfo> getTables() {
        // 需要执行的SQL
        PreparedStatement ps = null;
        // 执行SQL后的结果
        ResultSet tableResult = null;

        List<TableInfo> tableInfoList = new ArrayList<>();
        try {
            // 输入SQL语句
            ps = conn.prepareStatement(SQL_SHOW_TABLE_STATUS);
            // 执行SQL语句
            tableResult = ps.executeQuery();
            // 一行一行地遍历（每一行代表一张表）
            while (tableResult.next()) {
                // 这里的Name content是Mysql自带的
                String tableName = tableResult.getString("name");
                String comment = tableResult.getString("comment");
                // logger.info("tableName:{},comment:{}", tableName, comment);
                // 填充表的各个属性 表名字是表的名字，JavaBean需要对表名进行转换
                // 类似于 IGNORE_TABLE_PREFIX = true - > tb_product_info -> ProductInfo
                // tb_product_info -> TbProductInfo
                String beanName = tableName;
                // 去掉前缀
                if (Constants.IGNORE_TABLE_PREFIX) {
                    // beanName是一个类的名字 product_info
                    // beanName = tableName.substring(beanName.indexOf("_") + 1);
                    // 类的名字：product_info -> ProductInfo
                    // 去到tb_product_info这个的第一个_，然后从_的后一个字符开始到最后 -> product_info
                    beanName = tableName.substring(beanName.indexOf("_") + 1);
                }
                // processField -> product_info -> ProductInfo
                beanName = BuilderTable.processField(beanName, false);
                // 获取ProductInfo类的名字之后，开始填充表的元信息
                TableInfo tableInfo = new TableInfo();
                tableInfo.setTableName(tableName);
                tableInfo.setBeanName(beanName);
                tableInfo.setComment(comment);
                tableInfo.setBeanParaName(beanName + Constants.SUFFIX_BEAN_PARAM);
                // 获取表所有字段信息
                List<FieldInfo> fieldInfoList = readFieldInfo(tableInfo);
                tableInfo.setFieldList(fieldInfoList);
                tableInfoList.add(readIndexInfo(tableInfo));
            }
        } catch (Exception e) {
            logger.error("读取表信息失败", e);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            if (tableResult != null) {
                try {
                    tableResult.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            return tableInfoList;
        }
    }

    // tb_product_info -> ProductInfo
    // 表 product_info -> ProductInfo
    // 先分开 [product, info] -> Product, info -> ProductInfo
    // 字段 product_name -> productName
    private static String processField(String field, Boolean upperCaseFirstLetter) {
        StringBuffer sb = new StringBuffer();
        String[] fields = field.split("_");
        String temp = StringUtils.uperCaseFirstLetter(fields[0]);
        sb.append(upperCaseFirstLetter ? temp : fields[0]);
        for (int i = 1, len = fields.length; i < len; i++) {
            sb.append(StringUtils.uperCaseFirstLetter(fields[i]));
        }
        return sb.toString();
    }

    private static TableInfo readIndexInfo(TableInfo tableInfo) {
        // 需要执行的SQL
        PreparedStatement ps = null;
        // 执行SQL后的结果
        ResultSet indexResult = null;
        try {
            // 输入SQL语句
            ps = conn.prepareStatement(String.format(SQL_SHOW_TABLE_INDEXES, tableInfo.getTableName()));
            // 执行SQL语句
            indexResult = ps.executeQuery();
            HashMap<String, FieldInfo> map = new HashMap<>();
            // 先将tableInfo中的List取出来，把每一个filedName对应一个fieldInfo
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                map.put(fieldInfo.getFieldName(), fieldInfo);
            }
            // 一行一行地遍历（每一行代表每一个字段）
            while (indexResult.next()) {
                String indexName = indexResult.getString("Key_name");
                String columnName = indexResult.getString("Column_name");
                String nonUnique = indexResult.getString("non_Unique");
                List<FieldInfo> fieldList = tableInfo.getFieldList();
                Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();
                List<FieldInfo> addFieldInfo = keyIndexMap.get(indexName);
                if ("1".equals(nonUnique)) {
                    continue;
                }
                if (addFieldInfo == null) {
                    addFieldInfo = new ArrayList<>();
                    keyIndexMap.put(indexName, addFieldInfo);
                }
//                for (FieldInfo fieldInfo : fieldList) {
//                    if (columnName.equals(fieldInfo.getFieldName())) {
//                        addFieldInfo.add(fieldInfo);
//                    }
//                }
                addFieldInfo.add(map.get(columnName));
            }
        } catch (Exception e) {
            logger.error("读取表信息失败", e);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            if (indexResult != null) {
                try {
                    indexResult.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return tableInfo;
    }

    private static List<FieldInfo> readFieldInfo(TableInfo tableInfo) {
        // 需要执行的SQL
        PreparedStatement ps = null;
        // 执行SQL后的结果
        ResultSet fieldResult = null;
        List<FieldInfo> fieldList = tableInfo.getFieldList();
        try {
            // 输入SQL语句
            ps = conn.prepareStatement(String.format(SQL_SHOW_TABLE_FIELDS, tableInfo.getTableName()));
            // 执行SQL语句
            fieldResult = ps.executeQuery();
            // 在循环外先初始化为 false，循环内只设 true，避免后续字段覆盖
            tableInfo.setHaveDateTime(false);
            tableInfo.setHaveDate(false);
            tableInfo.setHaveBigDecimal(false);
            // 一行一行地遍历（每一行代表每一个字段）
            while (fieldResult.next()) {
                String fieldName = fieldResult.getString("Field");
                /*
                 * tinyint、int -> int
                 * varchar -> String
                 * decimal -> double
                 */
                String type = fieldResult.getString("Type");
                String extra = fieldResult.getString("Extra");
                String comment = fieldResult.getString("Comment");
                if (type.indexOf("(") > 0) {
                    // 如果有(的位置，直接从0定位到(的位置
                    type = type.substring(0, type.indexOf("("));
                }
                // company_id -> companyId 转换为Java可接受的字段名
                String propertyName = processField(fieldName, false);
                FieldInfo fieldInfo = new FieldInfo();
                fieldList.add(fieldInfo);
                fieldInfo.setIsAutoIncrement("auto_increment".equals(extra));
                fieldInfo.setFieldName(fieldName);
                fieldInfo.setComment(comment);
                fieldInfo.setSqlType(type);
                fieldInfo.setPropertyName(propertyName);
                // logger.info("fieldName:{},type:{},extra:{},comment:{}", fieldName, type,
                // extra, comment);
                fieldInfo.setJavaType(processJavaType(type));
                /**
                 * 如果 haveDateTime || haveDate 为 true，才加这行
                 * import java.util.Date;
                 * 如果 haveBigDecimal 为 true，才加这行
                 * import java.math.BigDecimal;
                 */
                if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, type)) {
                    tableInfo.setHaveDateTime(true);
                }
                if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, type)) {
                    tableInfo.setHaveDate(true);
                }
                if (ArrayUtils.contains(Constants.SQL_DECIMAL_TYPES, type)) {
                    tableInfo.setHaveBigDecimal(true);
                }
            }
        } catch (Exception e) {
            logger.error("读取表信息失败", e);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            if (fieldResult != null) {
                try {
                    fieldResult.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return fieldList;
    }

    private static String processJavaType(String type) {
        if (ArrayUtils.contains(Constants.SQL_INTEGER_TYPE, type)) {
            return "Integer";
            // SQL_DATE_TYPES和SQL_DATE_TIME_TYPES的区别在于时间，是否带有时分秒
        } else if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, type)
                || ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, type)) {
            return "Date";
        } else if (ArrayUtils.contains(Constants.SQL_DECIMAL_TYPES, type)) {
            return "BigDecimal";
        } else if (ArrayUtils.contains(Constants.SQL_LONG_TYPE, type)) {
            return "Long";
        } else if (ArrayUtils.contains(Constants.SQL_STRING_TYPE, type)) {
            return "String";
        } else {
            throw new RuntimeException("无法识别的类型: " + type);
        }
    }

    public static void main(String[] args) {
        System.out.println(BuilderTable.processField("product_info", true));
    }
}
