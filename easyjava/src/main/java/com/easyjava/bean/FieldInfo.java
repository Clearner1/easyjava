package com.easyjava.bean;

import lombok.Data;

@Data
public class FieldInfo {

    // 字段名称->id company_id
    private String fieldName;

    // 属性名称-> id companyId
    private String propertyName;

    // SQL类型 int varchar(30)
    private String sqlType;

    // java类型 int String
    private String javaType;

    // 备注
    private String comment;

    // 是否自增
    private Boolean isAutoIncrement;
}
