package com.easyjava.bean;

import lombok.Data;

@Data
public class FieldInfo {

    // 字段名称
    private String FieldName;

    // 属性名称
    private String propertyName;

    // SQL类型
    private String sqlType;

    // java类型
    private String javaType;

    // 备注
    private String comment;

    // 是否自增
    private Boolean isAutoIncrement;
}
