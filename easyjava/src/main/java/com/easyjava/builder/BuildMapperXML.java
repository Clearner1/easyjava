package com.easyjava.builder;

import com.easyjava.bean.Constants;
import com.easyjava.bean.FieldInfo;
import com.easyjava.bean.TableInfo;
import com.easyjava.utils.JsonUtils;
import com.easyjava.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BuildMapperXML {
    private static final Logger logger = LoggerFactory.getLogger(BuildBase.class);

    public static void execute(TableInfo tableInfo) throws IOException {
        File folder = new File(Constants.PATH_Mapper_XML);
        if (!folder.exists()) {
            // 递归创建整个不存在路径
            folder.mkdirs();
        }
        String className = tableInfo.getBeanName() + Constants.SUFFIX_BEAN_MAPPER;
        File poFile = new File(folder, className + ".xml");
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

            /***********************************************************
             * MapperXml 文件头头部
             ***********************************************************/
            bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            bw.write("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"\n");
            bw.write("\t\t\"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n\n");
            bw.write("<mapper namespace=\"" + Constants.PACKAGE_MAPPER + "." + StringUtils.uperCaseFirstLetter(className) + "\">\n");
            FieldInfo idField = null;

            bw.newLine();
            bw.write("\t<!--实体映射-->");
            bw.newLine();
            String poPath = Constants.PACKAGE_PO + "." + StringUtils.uperCaseFirstLetter(tableInfo.getBeanName());
            bw.write("\t<resultMap id=\"base_result_map\" type=\"" + poPath + "\">");
            bw.newLine();


            // String 是索引名称，FieldInfo是字段详细信息
            Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();
            FieldInfo primaryKey = new FieldInfo();
            for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()) {
                if ("PRIMARY".equals(entry.getKey())) {
                    // 主键只有一个字段，字段只能在第一个
                    primaryKey = entry.getValue().getFirst();
                }
            }
            // 开始遍历字段
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                // 字段注释
                bw.write("\t\t<!--" + fieldInfo.getComment() + "-->");
                bw.newLine();
                if (fieldInfo.getPropertyName().equals(primaryKey.getPropertyName())) {
                    bw.write("\t\t<id column=\"id\" property=\"id\"/>");
                } else {
                    bw.write("\t\t<result column=\"" + fieldInfo.getFieldName() + "\" property=\"" + fieldInfo.getPropertyName() + "\"/>");
                }
                bw.newLine();
            }
            bw.write("\t</resultMap>");
            bw.newLine();

            // 通用查询结果列
            bw.newLine();
            bw.write("\t\t<!-- 通用查询结果列 -->");
            bw.newLine();
            String base_column_list = "base_column_list";
            bw.write("\t\t<sql id=\"" + base_column_list +  "\">");
            bw.newLine();
            StringBuilder sb = new StringBuilder();
            Iterator<FieldInfo> iterator = tableInfo.getFieldList().iterator();
            while (iterator.hasNext()) {
                FieldInfo fieldInfo = iterator.next();
                sb.append(fieldInfo.getFieldName());
                if (iterator.hasNext()) {
                    sb.append(",");
                }
            }
            bw.write("\t\t" + sb.toString());
            bw.newLine();
            bw.write("\t\t</sql>");
            bw.newLine();


            // <!-- 基础查询条件 -->
            bw.write("\t\t<!-- 基础查询条件 -->");
            String base_query_condition = "base_query_condition";
            bw.newLine();
            bw.write("\t\t<sql id=\"" + base_query_condition + "\">");
            bw.newLine();

            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                if (!Objects.equals(fieldInfo.getJavaType(), "String")) {
                    bw.write("\t\t\t<if test=\"query." + fieldInfo.getPropertyName() + " != null\">");
                } else {
                    bw.write("\t\t\t<if test=\"query." + fieldInfo.getPropertyName() + " != null and query." + fieldInfo.getPropertyName() + "!= ''\">");
                }
                bw.newLine();
                bw.write("\t\t\t\tand " + fieldInfo.getFieldName() + "= #{query." + fieldInfo.getPropertyName() + "}");
                bw.newLine();
                bw.write("\t\t\t</if>");
                bw.newLine();
            }

            bw.write("\t\t</sql>\n");
            bw.newLine();
            // <!-- 扩展查询条件 -->
            bw.write("\t\t<!-- 扩展查询条件 -->");
            bw.newLine();
            String base_query_condition_extend = "base_query_condition_extend";
            bw.write("\t\t<sql id=\"" + base_query_condition_extend + "\">");
            bw.newLine();

            //System.out.println(JsonUtils.convertObj2Json(tableInfo.getExtendedfieldList()));
            for (FieldInfo fieldInfo : tableInfo.getExtendedfieldList()) {
                // TODO 代码冗余
                if (ArrayUtils.contains(Constants.SQL_STRING_TYPE, fieldInfo.getSqlType())) {
                    bw.write("\t\t\t<if test=\"query." + fieldInfo.getPropertyName() + " != null" + " and query." + fieldInfo.getPropertyName() + " != '' " +"\">");
                    bw.newLine();
                    bw.write("\t\t\t\tand " + fieldInfo.getFieldName() + " like concat {'%', #{query." + fieldInfo.getPropertyName() + "}, '%'}");
                } else {
                    bw.write("\t\t\t<if test=\"query." + fieldInfo.getPropertyName() + " != null and query." + fieldInfo.getPropertyName() + " != ''\">");
                    bw.newLine();
                    if (fieldInfo.getPropertyName().endsWith(Constants.SUFFIX_BEAN_QUERY_TIME_START)){
                        bw.write("\t\t\t\t<![CDATA[ and " + fieldInfo.getFieldName() + " >= str_to_date(#{query." + fieldInfo.getPropertyName() + "}, '%Y-%m-%d') ]]>");
                    } else if (fieldInfo.getPropertyName().endsWith(Constants.SUFFIX_BEAN_QUERY_TIME_END)) {
                        bw.write("\t\t\t\t<![CDATA[ and " + fieldInfo.getFieldName() + " < date_sub(str_to_date(#{query." + fieldInfo.getPropertyName() + "}, '%Y-%m-%d')" + ", interval -1 day) " +  "]]>");
                    }
                }
                bw.newLine();
                bw.write("\t\t\t</if>");
                bw.newLine();
            }
            bw.write("\t\t</sql>\n");
            bw.newLine();

            // <!-- 通用查询条件 -->
            String query_condition = "query_condition";
            bw.write("\t<sql id=\"" + query_condition + "\">");
            bw.newLine();
            bw.write("\t\t<where>");
            bw.newLine();
            bw.write("\t\t\t<include refid=\""+ base_query_condition +"\"/>");
            bw.newLine();
            bw.write("\t\t\t<include refid=\"" + base_query_condition_extend +"\"/>");
            bw.newLine();
            bw.write("\t\t</where>");
            bw.newLine();
            bw.write("\t</sql>");
            bw.newLine();
            bw.newLine();
            // <!-- 查询列表 -->
            bw.write("\t<!-- 查询列表 -->");
            bw.newLine();
            bw.write("\t<select id=\"selectList\" resultMap=\"base_result_map\">");
            bw.newLine();
            bw.write("\t\tSELECT <include refid=\"" + base_column_list +"\"/> FROM " + tableInfo.getTableName() + " <include refid=\"" + query_condition + "\"/>");
            bw.newLine();
            bw.write("\t\t<if test=\"query.orderBy != null\"> order by ${query.orderBy} </if>");
            bw.newLine();
            bw.write("\t\t<if test=\"query.simplePage != null\"> limit ${query.simplePage.start}, ${query.simplePage.end} </if>");
            bw.newLine();
            bw.write("\t</select>");
            bw.newLine();
            bw.newLine();

            // <!-- 查询数量 -->
            bw.write("\t<!-- 查询数量 -->");
            bw.newLine();
            bw.write("\t<select id=\"selectCount\" resultType=\"java.lang.Integer\">");
            bw.newLine();
            bw.write("\t\tSELECT COUNT(1) FROM " + tableInfo.getTableName());
            bw.newLine();
            bw.write("\t\t<include refid=\"query_condition\"/>");
            bw.newLine();
            bw.write("\t</select>");
            bw.newLine();

            // <!-- 单条插入 (匹配有值的字段) -->
            //bw.write("\t<!-- 单条插入 (匹配有值的字段) -->");
            bw.newLine();
            //com.easyjava.entity.po.ProductInfo
            //bw.write("<insert id=\"insert\" parameterType=\"" + Constants.PACKAGE_PO +"\">");
            bw.write("</mapper>");
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
