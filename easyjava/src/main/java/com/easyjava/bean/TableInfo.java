package com.easyjava.bean;

import java.util.List;

import lombok.Data;

@Data
public class TableInfo {
    // 表名
    private String tableName;

    // bean名称
    private String beanName;

    // 参数名称
    private String beanPararName;

    // 表注释
    private String comment;

    // 字段信息
    private List<FieldInfo> fieldList;

}
