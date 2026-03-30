package com.easyjava.builder;

import com.easyjava.bean.Constants;
import com.easyjava.bean.FieldInfo;
import com.easyjava.bean.TableInfo;

import java.io.*;

/**
 * 创建一个类，字段与数据库表的列一一对应
 */
public class BuildPo {
    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.PATH_PO);
        if (!folder.exists()) {
            // 递归创建整个不存在路径
            folder.mkdirs();
        }
        File poFile = new File(folder, tableInfo.getBeanName() + ".java");
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
            bw.write("package " + Constants.IMPORT_PACKAGE + ";");
            bw.newLine();
            bw.write("import java.io.Serializable;");
            bw.newLine();
            if (tableInfo.getHaveDateTime() || tableInfo.getHaveDate()) {
                bw.write("import java.util.Date;");
                bw.newLine();
            }
            if (tableInfo.getHaveBigDecimal()) {
                bw.write("import java.math.BigDecimal;");
                bw.newLine();
            }
            bw.newLine();
            // 构建类的注释
            BuildComments.createClassComment(bw, tableInfo.getComment());
            bw.write("public class " + tableInfo.getBeanName() + " implements Serializable {");
            bw.newLine();
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                // 构建字段的注释
                BuildComments.createFieldComment(bw, fieldInfo.getComment());
                bw.write("\tprivate " + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + ";");
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
