package com.easyjava.utils;

public class StringUtils {
    public static String uperCaseFirstLetter(String field) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(field)) {
            return field;
        }

        // 截取字符串索引从0到1，不包含1
        return field.substring(0, 1).toUpperCase() + field.substring(1); // 取第二个字符到末尾
    }

    public static String lowerCaseFirstLetter(String field) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(field)) {
            return field;
        }

        // 截取字符串索引从0到1，不包含1
        return field.substring(0, 1).toLowerCase() + field.substring(1); // 取第二个字符到末尾
    }

    public static void main(String[] args) {
        System.out.println(StringUtils.lowerCaseFirstLetter("Company"));
    }
}
