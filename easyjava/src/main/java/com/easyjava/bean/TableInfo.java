package com.easyjava.bean;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * MySQL 表的元信息封装成 Java 对象，供后续代码生成使用
 */
@Data
public class TableInfo {
    // 原始表名，用于生成 SQL、Mapper 等
    private String tableName;

    // 作为生成的 Java 类名
    private String beanName;

    // 作为生成的查询参数类名
    private String beanParaName;

    // 写到生成类的 类注释 上
    private String comment;

    // 生成类的 所有属性（字段名、类型、注释等）
    private List<FieldInfo> fieldList = new ArrayList<>();

    // 用来存储表的索引信息的
    private Map<String, List<FieldInfo>> keyIndexMap = new LinkedHashMap<>();

    /**
     * 是否拥有日期 date 类型，决定是否加 import java.util.Date
     */
    private Boolean haveDate;
    /**
     * 是否拥有日期时间 datetime 类型
     */
    private Boolean haveDateTime;
    /**
     * 是否拥有 BigDecimal 类型，决定是否加 import java.math.BigDecimal
     */
    private Boolean haveBigDecimal;

    public static void main(String[] args) {
        TableInfo tableInfo = new TableInfo();
        System.out.println(tableInfo.toString());
    }
}
