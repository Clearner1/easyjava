package com.easyjava.builder;

import com.easyjava.bean.Constants;
import com.easyjava.bean.FieldInfo;
import com.easyjava.bean.TableInfo;
import com.easyjava.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.Map;

public class BuildMapper {
    private static final Logger logger = LoggerFactory.getLogger(BuildBase.class);

    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.PATH_MAPPER);
        if (!folder.exists()) {
            // 递归创建整个不存在路径
            folder.mkdirs();
        }
        String className = tableInfo.getBeanName() + Constants.SUFFIX_BEAN_MAPPER;
        File poFile = new File(folder, className + ".java");
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
            bw.write("package " + Constants.PACKAGE_MAPPER + ";");
            bw.newLine();
            bw.newLine();
            bw.write(Constants.PACKAGE_PARAM);
            bw.newLine();
            bw.newLine();

            // 构建类的注释
            BuildComments.createClassComment(bw, tableInfo.getComment() + "Mapper查询对象");
            bw.write("public interface " + StringUtils.uperCaseFirstLetter(tableInfo.getBeanName()) + Constants.SUFFIX_BEAN_MAPPER + "<T, P>" + " extends" + " BaseMapper" + " {");
            bw.newLine();
            Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();

            for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()) {
                List<FieldInfo> keyFieldInfo = entry.getValue();
                StringBuilder methodName = new StringBuilder();
                StringBuilder paraName = new StringBuilder();
                Integer index = 0;
                for (FieldInfo fieldInfo : keyFieldInfo) {
                    index++;
                    methodName.append(StringUtils.uperCaseFirstLetter(fieldInfo.getPropertyName()));
                    if (index < keyFieldInfo.size()) {
                        methodName.append("And");
                    }
                    paraName.append("@Param(" + "\"(" + fieldInfo.getPropertyName() + "\") " + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName());
                    if (index < keyFieldInfo.size()) {
                        paraName.append(", ");
                    }
                }
                bw.newLine();
                BuildComments.createFieldComment(bw, "根据" + methodName + "查询");
                bw.write("\tpublic T selectBy" + methodName + "(" + paraName + ");");
                bw.newLine();
                bw.newLine();
                BuildComments.createFieldComment(bw, "根据" + methodName + "更新");
                bw.write("\tpublic T updateBy" + methodName + "(@Param(\"bean\") T t, " + paraName + ");");
                bw.newLine();
                bw.newLine();
                BuildComments.createFieldComment(bw, "根据" + methodName + "删除");
                bw.write("\tpublic T deleteBy" + methodName + "(" + paraName + ");");
                bw.newLine();
                bw.newLine();


            }
//            for () {
//                // 构建字段的注释
//                BuildComments.createFieldComment(bw, field.getComment());
//            }
            bw.newLine();
            bw.write("}");
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (outw != null) {
                try {
                    outw.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
