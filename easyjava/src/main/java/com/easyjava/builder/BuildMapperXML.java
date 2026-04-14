package com.easyjava.builder;

import com.easyjava.bean.Constants;
import com.easyjava.bean.FieldInfo;
import com.easyjava.bean.TableInfo;
import com.easyjava.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

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
                    bw.write("\t\t\t\tand " + fieldInfo.getFieldName() + " like concat ('%', #{query." + fieldInfo.getPropertyName() + "}, '%')");
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
            bw.write("\t<!-- 单条插入 (匹配有值的字段) -->");
            bw.newLine();
            //com.easyjava.entity.po.ProductInfo
            bw.write("\t<insert id=\"insert\" parameterType=\"" + poPath +"\">");
            bw.newLine();
            bw.write("\t\t<selectKey keyProperty=\"bean.id\" resultType=\"Integer\" order=\"AFTER\">");
            bw.newLine();
            bw.write("\t\t\tSELECT LAST_INSERT_ID()");
            bw.newLine();
            bw.write("\t\t</selectKey>");
            bw.newLine();
            bw.write("\t\tINSERT INTO " + tableInfo.getTableName());
            bw.newLine();
            bw.write("\t\t<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
            bw.newLine();
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() +" != null\">");
                bw.newLine();
                bw.write("\t\t\t\t" + fieldInfo.getFieldName() + ",");
                bw.newLine();
                bw.write("\t\t\t</if>");
                bw.newLine();
            }
            bw.write("\t\t</trim>");
            bw.newLine();
            bw.write("\t\t<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">");
            bw.newLine();
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() +" != null\">");
                bw.newLine();
                bw.write("\t\t\t\t#{bean." + fieldInfo.getPropertyName() + "},");
                bw.newLine();
                bw.write("\t\t\t</if>");
                bw.newLine();
            }

            bw.write("\t\t</trim>");
            bw.newLine();
            bw.write("\t</insert>");
            bw.newLine();
            bw.newLine();
            // <!-- 插入或者更新（匹配有值的字段） -->
            bw.write("\t<!-- 插入或者更新（匹配有值的字段） -->");
            bw.newLine();
            bw.write("\t\t<insert id=\"insertOrUpdate\" parameterType=\""+ poPath + "\">");
            bw.newLine();
            bw.write("\t\tINSERT INTO " + tableInfo.getTableName());
            bw.newLine();
            bw.write("\t\t<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
            bw.newLine();
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() +" != null\">");
                bw.newLine();
                bw.write("\t\t\t\t" + fieldInfo.getFieldName() + ",");
                bw.newLine();
                bw.write("\t\t\t</if>");
                bw.newLine();
            }
            bw.write("\t\t</trim>");
            bw.newLine();
            bw.write("\t\t<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">");
            bw.newLine();
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() +" != null\">");
                bw.newLine();
                bw.write("\t\t\t\t#{bean." + fieldInfo.getPropertyName() + "},");
                bw.newLine();
                bw.write("\t\t\t</if>");
                bw.newLine();
            }

            bw.write("\t\t</trim>");
            bw.newLine();
            bw.write("\t\t\tON DUPLICATE KEY UPDATE");
            bw.newLine();
            bw.write("\t\t<trim prefix=\"\" suffix=\"\" suffixOverrides=\",\">");
            bw.newLine();
            ArrayList<String> keyList = new ArrayList<>();

            // 存储所有索引的name
            for (Map.Entry<String, List<FieldInfo>> entry : tableInfo.getKeyIndexMap().entrySet()) {
                List<FieldInfo> fieldInfoList = entry.getValue();
                for (FieldInfo fieldInfo : fieldInfoList) {
                    keyList.add(fieldInfo.getFieldName());
                }
            }

            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                if (!keyList.contains(fieldInfo.getFieldName())) {
                    bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() +" != null\">");
                    bw.newLine();
                    bw.write("\t\t\t\t" + fieldInfo.getFieldName() + " = " + "VALUES(" + fieldInfo.getFieldName() + ")" + ",");
                    bw.newLine();
                    bw.write("\t\t\t</if>");
                    bw.newLine();
                }
            }
            bw.write("\t\t</trim>");
            bw.newLine();

            bw.write("\t</insert>");
            bw.newLine();

            StringBuilder fieldSb = new StringBuilder();
            Iterator fieldIt = tableInfo.getFieldList().iterator();
            while (fieldIt.hasNext()) {
                FieldInfo fieldInfo = (FieldInfo) fieldIt.next();
                fieldSb.append(fieldInfo.getFieldName());
                if (fieldIt.hasNext()) {
                    fieldSb.append(", ");
                }
            }
            // <!-- 添加（批量添加） -->
            bw.write("\t\t<!-- 添加（批量添加） -->");
            bw.newLine();
            bw.write("\t\t<insert id=\"insertBatch\" parameterType=\"" +  poPath +"\" >");
            bw.newLine();
            bw.write("\t\t\tINSERT INTO " + tableInfo.getTableName() + "(" + fieldSb + ") values\n");
            bw.write("\t\t\t<foreach collection=\"list\" item=\"item\" separator=\",\">\n");
            bw.write("\t\t\t\t(");
            Iterator fieldIt2 = tableInfo.getFieldList().iterator();
            while (fieldIt2.hasNext()) {
                FieldInfo fieldInfo = (FieldInfo) fieldIt2.next();
                bw.write("#{item." + fieldInfo.getPropertyName() +"}");
                if (fieldIt2.hasNext()) {
                    bw.write(", ");
                }
            }
            bw.write(")\n");
            bw.write("\t\t\t</foreach>");
            bw.newLine();
            bw.write("\t\t</insert>");
            bw.newLine();
            bw.newLine();

            // <!-- 批量新增修改（批量插入） -->
            bw.write("\t\t<!-- 批量新增修改（批量插入） -->");
            bw.newLine();
            bw.write("\t\t<insert id=\"insertOrUpdateBatch\" parameterType=\"" +  poPath + "\" >");
            bw.newLine();
            bw.write("\t\t\tINSERT INTO " + tableInfo.getTableName() + "(" + fieldSb + ") values\n");
            bw.write("\t\t\t<foreach collection=\"list\" item=\"item\" separator=\",\">\n");
            Iterator fieldIt3 = tableInfo.getFieldList().iterator();
            bw.write("\t\t\t\t(");
            while (fieldIt3.hasNext()) {
                FieldInfo fieldInfo = (FieldInfo) fieldIt3.next();
                bw.write("#{item." + fieldInfo.getPropertyName() +"}");
                if (fieldIt3.hasNext()) {
                    bw.write(", ");
                }
            }
            bw.write(")");
            bw.newLine();
            bw.write("\t\t\t</foreach>");
            bw.newLine();
            bw.write("\t\t\tON DUPLICATE KEY UPDATE");
            bw.newLine();
            Iterator<FieldInfo> iterator1 = tableInfo.getFieldList().iterator();
            while (iterator1.hasNext()) {
                FieldInfo fieldInfo = iterator1.next();
                if (iterator1.hasNext()) {
                    bw.write("\t\t\t" + fieldInfo.getFieldName() + " = " + "VALUES(" + fieldInfo.getFieldName() + ")" + ",");
                } else {
                    bw.write("\t\t\t" + fieldInfo.getFieldName() + " = " + "VALUES(" + fieldInfo.getFieldName() + ")");
                }

                bw.newLine();
            }
            bw.write("\t\t</insert>");
            bw.newLine();
            bw.newLine();

            for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()) {
                List<FieldInfo> keyFieldInfo = entry.getValue();
                StringBuilder methodName = new StringBuilder();
                ArrayList<FieldInfo> paramList = new ArrayList<>();
                Integer index = 0;
                for (FieldInfo fieldInfo : keyFieldInfo) {
                    index++;
                    methodName.append(StringUtils.uperCaseFirstLetter(fieldInfo.getPropertyName()));
                    if (index < keyFieldInfo.size()) {
                        methodName.append("And");
                    }
                    paramList.add(fieldInfo);
                }
                StringBuilder tempSb = new StringBuilder();
                Iterator<FieldInfo> it = paramList.iterator();
                while (it.hasNext()) {
                    FieldInfo fieldInfo = it.next();
                    tempSb.append(fieldInfo.getFieldName()).append(" =#{").append(fieldInfo.getPropertyName()).append("}");
                    if (it.hasNext()) {
                        tempSb.append(" and ");
                    }
                }
                bw.newLine();


                //<!-- 根据 Id 查询 -->
                bw.write("\t<!-- 根据" + methodName + "查询-->\n");
                // <select id="selectById" resultMap="base_result_map">
                bw.write("\t<select id = \"selectBy" + methodName + "\" resultMap=\"base_result_map\">\n");
                bw.write("\t\tselect\n");
                bw.write("\t\t<include refid=\"base_column_list\"/>\n");
                bw.write("\t\tfrom " +  tableInfo.getTableName() + " where " + tempSb.toString() + "\n");
                bw.write("\t</select>\n");
                bw.newLine();
                bw.newLine();

                // <!-- 根据 xxx 更新 -->
                bw.write("\t<!-- 根据" + methodName + "更新-->\n");
                bw.write("\t<update id=\"updateBy" + methodName +"\" parameterType=\""+ poPath +"\">\n");
                bw.write("\tupdate " + tableInfo.getTableName() + "\n");
                bw.write("\t<set>\n");
                for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                    bw.write("\t\t<if test=\"bean." + fieldInfo.getPropertyName() +" != null\">");
                    bw.newLine();
                    bw.write("\t\t\t" + fieldInfo.getFieldName() + "= #{bean." + fieldInfo.getPropertyName() + "},");
                    bw.newLine();
                    bw.write("\t\t</if>");
                    bw.newLine();
                }
                bw.write("\t</set>\n");
                bw.write("\twhere " + tempSb.toString() + "\n");
                bw.write("\t</update>\n");
                bw.newLine();
                bw.newLine();



                // <!-- 根据 xxx 删除 -->
                bw.write("\t<!-- 根据" + methodName + "删除-->\n");
                bw.write("\t<delete id=\"deleteBy"+ methodName +"\">\n");
                bw.write("\tdelete from " +  tableInfo.getTableName() + " where " + tempSb.toString() + "\n");
                bw.write("\t</delete>\n");
                bw.newLine();
                bw.newLine();
            }
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
