package com.easyjava.builder;

import com.easyjava.bean.Constants;
import com.easyjava.bean.FieldInfo;
import com.easyjava.bean.TableInfo;
import com.easyjava.utils.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BuildServiceImpl {
    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.PATH_SERVICE_IMPL);
        if (!folder.exists()) {
            // 递归创建整个不存在路径
            folder.mkdirs();
        }
        String beanName = StringUtils.uperCaseFirstLetter(tableInfo.getBeanName());
        String className = beanName + "ServiceImpl";
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
            bw.write("package " + Constants.PACKAGE_SERVICE_IMPL + ";\n");
            bw.write("import " + Constants.PACKAGE_PO + "." + beanName +";\n");
            bw.write("import " + Constants.PACKAGE_QUERY + "." + beanName + "Query;\n");
            bw.write("import " + Constants.PACKAGE_QUERY + "." + "SimplePage;\n");
            bw.write("import " + Constants.PACKAGE_VO + ".PaginationResultVO;\n");
            bw.write("import " + "org.springframework.stereotype.Service;\n");
            bw.write("import " + "org.springframework.beans.factory.annotation.Autowired;\n");
            bw.write("import " + Constants.PACKAGE_SERVICE + "." + beanName  + "Service;\n");
            bw.write("import " + Constants.PACKAGE_MAPPER +"." + beanName  + Constants.SUFFIX_BEAN_MAPPER + ";\n");
            bw.write("import " + Constants.PACKAGE_ENUMS +"." + "PageSize;\n");
            bw.write("import java.util.List;");
            bw.newLine();
            bw.newLine();
            BuildComments.createClassComment(bw, tableInfo.getComment() + "Service");
            bw.newLine();
            String mapperName = tableInfo.getBeanName() + Constants.SUFFIX_BEAN_MAPPER;
            bw.write("@Service(\"" + beanName + "Service" + "\")\n");
            bw.write("public class " + className + " implements " + beanName + "Service{\n");
            bw.newLine();
            bw.write("\t@Autowired\n");
            bw.write("\tprivate " + StringUtils.uperCaseFirstLetter(mapperName) +"<" + beanName + ", " + beanName + Constants.SUFFIX_BEAN_QUERY +"> " + mapperName + ";\n\n");

            BuildComments.createFieldComment(bw, "根据条件查询列表");
            bw.write("\t@Override\n");
            bw.write("\tpublic List<" + beanName + "> findListByParam(" +  StringUtils.uperCaseFirstLetter(tableInfo.getBeanParaName())  + " query) {\n");
            bw.write("\t\treturn this." + mapperName + ".selectList(query);\n");
            bw.write("\t}\n\n");

            BuildComments.createFieldComment(bw, "根据条件查询数量");
            bw.write("\t@Override\n");
            bw.write("\tpublic Integer findCountByParam(" + StringUtils.uperCaseFirstLetter(tableInfo.getBeanParaName())  + " query) {\n");
            bw.write("\t\treturn this." + mapperName + ".selectCount(query);\n");
            bw.write("\t}\n\n");


            BuildComments.createFieldComment(bw, "分页查询");
            bw.write("\t@Override\n");
            bw.write("\tpublic PaginationResultVO<" + StringUtils.uperCaseFirstLetter(tableInfo.getBeanName()) + "> findListByPage(" + StringUtils.uperCaseFirstLetter(tableInfo.getBeanParaName())  + " query) {\n");
            bw.write("\t\tInteger count = this.findCountByParam(query);\n");
            bw.write("\t\tInteger pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();\n");
            bw.write("\t\tSimplePage page = new SimplePage(query.getPageNo(), count, pageSize);\n");
            bw.write("\t\tquery.setSimplePage(page);\n");
            bw.write("\t\tList<ProductInfo> list = this.findListByParam(query);\n");
            bw.write("\t\tPaginationResultVO<ProductInfo> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);\n");
            bw.write("\t\treturn result;\n");
            bw.write("\t}\n\n");

            BuildComments.createFieldComment(bw, "新增");
            bw.write("\t@Override\n");
            bw.write("\tpublic Integer add(" + StringUtils.uperCaseFirstLetter(tableInfo.getBeanName())  + " bean) {\n");
            bw.write("\t\treturn this." + mapperName + ".insert(bean);\n");
            bw.write("\t}\n\n");

            BuildComments.createFieldComment(bw, "批量新增");
            bw.write("\t@Override\n");
            bw.write("\tpublic Integer addBatch(List<" + StringUtils.uperCaseFirstLetter(tableInfo.getBeanName())  + "> listBean) {\n");
            bw.write("\t\t if ((listBean == null) || listBean.isEmpty()) {\n");
            bw.write("\t\t\treturn 0;\n");
            bw.write("\t\t}\n");
            bw.write("\t\t\treturn this." + mapperName + ".insertBatch(listBean);\n");
            bw.write("\t}\n\n");

            BuildComments.createFieldComment(bw, "批量新增或修改");
            bw.write("\t@Override\n");
            bw.write("\tpublic Integer insertOrUpdateBatch(List<" + StringUtils.uperCaseFirstLetter(tableInfo.getBeanName())  + "> listBean){\n");
            bw.write("\t\t if ((listBean == null) || listBean.isEmpty()) {\n");
            bw.write("\t\t\treturn 0;\n");
            bw.write("\t\t}\n");
            bw.write("\t\t\treturn this." + mapperName + ".insertOrUpdateBatch(listBean);\n");
            bw.write("\t}\n\n");

            Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();
            for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()) {
                List<FieldInfo> keyFieldInfo = entry.getValue();
                StringBuilder methodName = new StringBuilder();
                ArrayList<FieldInfo> paramList = new ArrayList<>();
                Integer index = 0;
                StringBuilder sb = new StringBuilder();
                StringBuilder sbParam = new StringBuilder();
                StringBuilder param = new StringBuilder();
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
                    param.append(StringUtils.lowerCaseFirstLetter(fieldInfo.getPropertyName()));
                    if (iterator.hasNext()) {
                        sb.append(", ");
                        sbParam.append("And");
                        param.append(", ");
                    }
                }
                BuildComments.createFieldComment(bw, "根据 " + methodName + " 查询");
                bw.write("\t@Override\n");
                bw.write("\tpublic "+ beanName + " get" + beanName + "By" + sbParam + "(" + sb + ") {\n");
                bw.write("\t\treturn this." + mapperName + ".selectBy" + sbParam + "(" + param +");\n");
                bw.write("\t}\n\n");

                BuildComments.createFieldComment(bw, "根据 " + methodName + " 更新");
                bw.write("\t@Override\n");
                bw.write("\tpublic Integer "  + "update" + beanName + "By" + sbParam + "(" + beanName + " bean, " + sb +") {\n");
                bw.write("\t\treturn this." + mapperName + ".updateBy" + sbParam + "(bean, " + param +");\n");
                bw.write("\t}\n\n");


                BuildComments.createFieldComment(bw, "根据 " + methodName + " 删除");
                bw.write("\t@Override\n");
                bw.write("\tpublic Integer " + "delete" + beanName + "By" + sbParam + "(" + sb + ") {\n");
                bw.write("\t\treturn this." + mapperName + ".deleteBy" + sbParam  + "(" + param +");\n");
                bw.write("\t}\n\n");
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
