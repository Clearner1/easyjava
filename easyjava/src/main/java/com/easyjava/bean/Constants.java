package com.easyjava.bean;

import com.easyjava.utils.PropertiesUtils;
import com.easyjava.utils.StringUtils;

public class Constants {
    // ==================== 配置文件读取 ====================
    public static Boolean IGNORE_TABLE_PREFIX;
    public static String SUFFIX_BEAN_QUERY;
    public static String SUFFIX_BEAN_QUERY_FUZZY;
    public static String SUFFIX_BEAN_QUERY_TIME_START;
    public static String SUFFIX_BEAN_QUERY_TIME_END;
    public static String SUFFIX_BEAN_MAPPER;

    // ==================== 路径相关 ====================
    public static String PATH_BASE;
    public static String PATH_BASE_RESOURCE;
    public static String PATH_PO;
    public static String PATH_Query;
    public static String PATH_VO;
    public static String PATH_Utils;
    public static String PATH_Enums;
    public static String PATH_Mapper;
    public static String PATH_Mapper_XML;

    // ==================== 包名相关 ====================
    public static String PACKAGE_BASE;
    public static String PACKAGE_PO;
    public static String PACKAGE_UTILS;
    public static String PACKAGE_MAPPER;
    public static String PACKAGE_ENUMS;
    public static String PACKAGE_Query;
    public static String PACKAGE_PARAM;


    public static String PATH_JAVA = "java";
    public static String RESOURCES = "resources";

    // ==================== 需要忽略的属性 ====================
    public static String[] IGNORE_BEAN_TOJSON_FIELD;
    public static String IGNORE_BEAN_TOJSON_EXPRESSION;
    public static String IGNORE_BEAN_TOJSON_CLASS;

    // ==================== 日期序列化和反序列化 ====================
    public static String BEAN_DATE_FORMAT_EXPRESSION;
    public static String BEAN_DATE_FORMAT_CLASS;
    public static String BEAN_DATE_UNFORMAT_EXPRESSION;
    public static String BEAN_DATE_UNFORMAT_CLASS;

    public static String COMMENT_AUTHOR;

    static {
        IGNORE_TABLE_PREFIX = Boolean.valueOf(PropertiesUtils.getString("ignore.table.prefix"));
        SUFFIX_BEAN_QUERY = StringUtils.uperCaseFirstLetter(PropertiesUtils.getString("suffix.bean.query"));
        SUFFIX_BEAN_QUERY_FUZZY = StringUtils.uperCaseFirstLetter(PropertiesUtils.getString("suffix.bean.query.fuzzy"));
        SUFFIX_BEAN_QUERY_TIME_START = StringUtils.uperCaseFirstLetter(PropertiesUtils.getString("suffix.bean.query.time.start"));
        SUFFIX_BEAN_QUERY_TIME_END = StringUtils.uperCaseFirstLetter(PropertiesUtils.getString("suffix.bean.query.time.end"));
        SUFFIX_BEAN_MAPPER = StringUtils.uperCaseFirstLetter(PropertiesUtils.getString("suffix.bean.mapper"));

        // ==================== 包名相关 ====================
        PACKAGE_BASE = PropertiesUtils.getString("package.base");
        PACKAGE_PO = PACKAGE_BASE + "." + PropertiesUtils.getString("package.po");
        PACKAGE_UTILS = PACKAGE_BASE + "." + PropertiesUtils.getString("package.utils");
        PACKAGE_MAPPER = PACKAGE_BASE + "." + PropertiesUtils.getString("package.mapper");
        PACKAGE_ENUMS = PACKAGE_BASE + "." + PropertiesUtils.getString("package.enums");
        PACKAGE_Query = PACKAGE_BASE + "." + PropertiesUtils.getString("package.query");
        PACKAGE_PARAM = PropertiesUtils.getString("bean.package.param");


        // ==================== 路径相关 ====================
        PATH_BASE = PropertiesUtils.getString("path.base");
        PATH_BASE_RESOURCE = PropertiesUtils.getString("path.base");
        PATH_BASE = PATH_BASE + PATH_JAVA;

        PATH_PO = PATH_BASE + "/" + PACKAGE_PO.replace(".", "/");
        PATH_Utils = PATH_BASE + "/" + PACKAGE_UTILS.replace(".", "/");
        PATH_Mapper = PATH_BASE + "/" + PACKAGE_MAPPER.replace(".", "/");
        PATH_Mapper_XML = PATH_BASE_RESOURCE  + RESOURCES + "/" + PACKAGE_MAPPER.replace(".", "/");
        PATH_Enums = PATH_BASE + "/" + PACKAGE_ENUMS.replace(".", "/");
        PATH_Query = PATH_BASE + "/" + PACKAGE_Query.replace(".", "/");
        COMMENT_AUTHOR = PropertiesUtils.getString("comment.author");
        // ==================== 暂时未用到 ====================
        // PATH_PARAM = PATH_BASE + PATH_JAVA + "/" + PACKAGE_BASE +
        // PropertiesUtils.getString("package.param").replace(".", "/");
        // PATH_VO = PATH_BASE + PATH_JAVA + "/" + PACKAGE_BASE +
        // PropertiesUtils.getString("package.vo").replace(".", "/");
        // ==================== 需要忽略的属性 ====================
        String ignoreFields = PropertiesUtils.getString("ignore.bean.tojson.field");

        if (ignoreFields != null && !ignoreFields.isEmpty()) {
            IGNORE_BEAN_TOJSON_FIELD = ignoreFields.split(",");
        } else {
            IGNORE_BEAN_TOJSON_FIELD = new String[0];
        }
        IGNORE_BEAN_TOJSON_EXPRESSION = PropertiesUtils.getString("ignore.bean.tojson.expression");
        IGNORE_BEAN_TOJSON_CLASS = PropertiesUtils.getString("ignore.bean.tojson.class");
        // ==================== 日期序列化和反序列化 ====================
        BEAN_DATE_FORMAT_EXPRESSION = PropertiesUtils.getString("bean.date.format.expression");
        BEAN_DATE_FORMAT_CLASS = PropertiesUtils.getString("bean.date.format.class");
        BEAN_DATE_UNFORMAT_EXPRESSION = PropertiesUtils.getString("bean.date.unformat.expression");
        BEAN_DATE_UNFORMAT_CLASS = PropertiesUtils.getString("bean.date.unformat.class");

    }
    public static final String[] SQL_DATE_TIME_TYPES = new String[] { "datetime", "timestamp" };
    public static final String[] SQL_DATE_TYPES = new String[] { "date" };
    public static final String[] SQL_DECIMAL_TYPES = new String[] { "decimal", "double", "float" };
    public static final String[] SQL_STRING_TYPE = new String[] { "char", "varchar", "text", "mediumtext", "longtext" };
    // Integer
    public static final String[] SQL_INTEGER_TYPE = new String[] { "int", "tinyint" };
    // Long
    public static final String[] SQL_LONG_TYPE = new String[] { "bigint" };

    public static void main(String[] args) {
        System.out.println(PATH_Mapper_XML);
    }
}
