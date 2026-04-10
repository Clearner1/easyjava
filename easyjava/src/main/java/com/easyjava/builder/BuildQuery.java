package com.easyjava.builder;

import com.easyjava.bean.Constants;
import com.easyjava.bean.FieldInfo;
import com.easyjava.bean.TableInfo;
import com.easyjava.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建一个类，字段与数据库表的列一一对应
 */
public class BuildQuery {
    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.PATH_Query);
        if (!folder.exists()) {
            // 递归创建整个不存在路径
            folder.mkdirs();
        }
        String fileName = StringUtils.uperCaseFirstLetter(tableInfo.getBeanName()) + Constants.SUFFIX_BEAN_QUERY;
        File poFile = new File(folder,  fileName  + ".java");
        if (!poFile.exists()) {
            try {
                poFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 字节流写数据
        OutputStream out = null;
        // 字符->字节
        OutputStreamWriter outw = null;
        // 缓冲写入
        BufferedWriter bw = null;
        // BufferedWriter -> OutputStreamWriter -> OutputStream
        try {
            out = new FileOutputStream(poFile);
            outw = new OutputStreamWriter(out, "utf8");
            bw = new BufferedWriter(outw);
            // 导包
            bw.write("package " + Constants.PACKAGE_Query + ";");
            bw.newLine();
            bw.newLine();
            bw.write("import java.io.Serializable;");
            bw.newLine();
            if (tableInfo.getHaveDateTime() || tableInfo.getHaveDate()) {
                bw.write("import java.util.Date;");
                bw.newLine();
                bw.write(Constants.BEAN_DATE_FORMAT_CLASS + ";");
                bw.newLine();
                bw.write(Constants.BEAN_DATE_UNFORMAT_CLASS + ";");
                bw.newLine();
                bw.write("import " + Constants.PACKAGE_UTILS + "." + "DateUtils" + ";");
                bw.newLine();
                bw.write("import " + Constants.PACKAGE_ENUMS + "." + "DateTimePatternEnum" + ";");
                bw.newLine();
            }
            if (tableInfo.getHaveBigDecimal()) {
                bw.write("import java.math.BigDecimal;");
                bw.newLine();
            }
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                if (ArrayUtils.contains(Constants.IGNORE_BEAN_TOJSON_FIELD, fieldInfo.getPropertyName())) {
                    bw.write(Constants.IGNORE_BEAN_TOJSON_CLASS + ";");
                    bw.newLine();
                    break;
                }
            }
            bw.newLine();
            // 构建类的注释
            BuildComments.createClassComment(bw, tableInfo.getComment() + "查询对象");
            bw.write("public class " + StringUtils.uperCaseFirstLetter(tableInfo.getBeanName()) + Constants.SUFFIX_BEAN_QUERY + " {");
            bw.newLine();
            // 添加额外列表
            ArrayList<FieldInfo> extendedList = new ArrayList<>();
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                // 构建字段的注释
                BuildComments.createFieldComment(bw, fieldInfo.getComment());
                bw.write("\tprivate " + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + ";");
                bw.newLine();

                // String类型的字段转换模糊搜索
                // 将其添加到tableInfo.getFieldList()
                if (ArrayUtils.contains(Constants.SQL_STRING_TYPE, fieldInfo.getSqlType())) {
                    bw.newLine();
                    bw.write("\tprivate " + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_FUZZY + ";");
                    FieldInfo fuzzy = new FieldInfo();
                    fuzzy.setJavaType(fieldInfo.getJavaType());
                    fuzzy.setPropertyName(fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_FUZZY);
                    extendedList.add(fuzzy);
                }

                // 时间的起止搜索
                if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, fieldInfo.getSqlType()) || ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, fieldInfo.getSqlType())) {
                    bw.newLine();
                    bw.write("\tprivate String " + fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_TIME_START + ";");
                    bw.newLine();
                    bw.newLine();
                    bw.write("\tprivate String " + fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_TIME_END + ";");
                    FieldInfo timeStart = new FieldInfo();
                    timeStart.setJavaType("String");
                    timeStart.setPropertyName(fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_TIME_START);
                    extendedList.add(timeStart);

                    FieldInfo timeEnd = new FieldInfo();
                    timeEnd.setJavaType("String");
                    timeEnd.setPropertyName(fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_TIME_END);
                    extendedList.add(timeEnd);
                }
                bw.newLine();
                bw.newLine();
            }
            List<FieldInfo> fieldList = tableInfo.getFieldList();
            fieldList.addAll(extendedList);

            for (FieldInfo fieldInfo : fieldList) {
                String tempField = StringUtils.uperCaseFirstLetter(fieldInfo.getPropertyName());
                // Set方法
                bw.write("\tpublic void set" + tempField + "(" + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + ") {" );
                bw.newLine();
                bw.write("\t\tthis." + fieldInfo.getPropertyName() + " = " + fieldInfo.getPropertyName() + ";");
                bw.newLine();
                bw.write("\t}");
                bw.newLine();

                bw.newLine();
                // Get方法
                bw.write("\tpublic " + fieldInfo.getJavaType() + " get" + tempField + "() {");
                bw.newLine();
                bw.write("\t\treturn this." + fieldInfo.getPropertyName() + ";");
                bw.newLine();
                bw.write("\t}");
                bw.newLine();
                bw.newLine();
            }

            bw.write("}");
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
