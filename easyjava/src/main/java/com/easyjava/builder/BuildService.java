package com.easyjava.builder;

import com.easyjava.bean.Constants;
import com.easyjava.bean.FieldInfo;
import com.easyjava.bean.TableInfo;
import com.easyjava.utils.DateUtils;
import com.easyjava.utils.JsonUtils;
import com.easyjava.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BuildService {
    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.PATH_SERVICE);
        if (!folder.exists()) {
            // 递归创建整个不存在路径
            folder.mkdirs();
        }
        String beanName = StringUtils.uperCaseFirstLetter(tableInfo.getBeanName());
        String className = beanName + "Service";
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
            bw.write("package " + Constants.PACKAGE_SERVICE + ";\n");
            bw.write("import " + Constants.PACKAGE_PO + "." + beanName +";\n");
            bw.write("import " + Constants.PACKAGE_QUERY + "." + beanName + "Query;\n");
            bw.write("import " + Constants.PACKAGE_VO + ".PaginationResultVO;\n");
            bw.write("import java.util.List;");
            bw.newLine();
            bw.newLine();
            BuildComments.createClassComment(bw, tableInfo.getComment() + "Service");
            bw.write("public interface " + className + " {\n");
            bw.newLine();

            BuildComments.createFieldComment(bw, "根据条件查询列表");
            bw.write("\tList<" + beanName + "> findListByParam(" +  StringUtils.uperCaseFirstLetter(tableInfo.getBeanParaName())  + " query);\n\n");

            BuildComments.createFieldComment(bw, "根据条件查询数量");
            bw.write("\tInteger findCountByParam(" + StringUtils.uperCaseFirstLetter(tableInfo.getBeanParaName())  + " query);\n\n");

            BuildComments.createFieldComment(bw, "分页查询");
            bw.write("\tPaginationResultVO<" + StringUtils.uperCaseFirstLetter(tableInfo.getBeanName()) + "> findListByPage(" + StringUtils.uperCaseFirstLetter(tableInfo.getBeanParaName())  + " query);\n\n");

            BuildComments.createFieldComment(bw, "新增");
            bw.write("\tInteger add(" + StringUtils.uperCaseFirstLetter(tableInfo.getBeanName())  + " bean);\n\n");

            BuildComments.createFieldComment(bw, "批量新增");
            bw.write("\tInteger addBatch(List<" + StringUtils.uperCaseFirstLetter(tableInfo.getBeanName())  + "> listBean);\n\n");

            BuildComments.createFieldComment(bw, "批量新增或修改");
            bw.write("\tInteger insertOrUpdateBatch(List<" + StringUtils.uperCaseFirstLetter(tableInfo.getBeanName())  + "> listBean);\n\n");

            Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();
            for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()) {
                List<FieldInfo> keyFieldInfo = entry.getValue();
                StringBuilder methodName = new StringBuilder();
                ArrayList<FieldInfo> paramList = new ArrayList<>();
                Integer index = 0;
                StringBuilder sb = new StringBuilder();
                StringBuilder sbParam = new StringBuilder();
                Iterator<FieldInfo> iterator = keyFieldInfo.iterator();
                while (iterator.hasNext()) {
                    FieldInfo fieldInfo = iterator.next();
                    index++;
                    methodName.append(StringUtils.uperCaseFirstLetter(fieldInfo.getPropertyName()));
                    if (index < keyFieldInfo.size()) {
                        methodName.append("And");
                    }
                    paramList.add(fieldInfo);
                    sb.append(fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName());
                    sbParam.append(StringUtils.uperCaseFirstLetter(fieldInfo.getPropertyName()));
                    if (iterator.hasNext()) {
                        sb.append(", ");
                        sbParam.append("And");
                    }
                }
                BuildComments.createFieldComment(bw, "根据 " + methodName + " 查询");
                bw.write("\t"+ beanName + " get" + beanName + "By" + sbParam + "(" + sb + ");\n\n");

                BuildComments.createFieldComment(bw, "根据 " + methodName + " 更新");
                bw.write("\tInteger "  + "update" + beanName + "By" + sbParam + "(" + beanName + " bean, " + sb +");\n\n");

                BuildComments.createFieldComment(bw, "根据 " + methodName + " 删除");
                bw.write("\tInteger " + "delete" + beanName + "By" + sbParam + "(" + sb + ");\n\n");

            }

            bw.write("}\n");
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bw != null) {
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
                    e.printStackTrace();
                }
            }

            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
