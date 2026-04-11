package com.easyjava.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description: 生成时间类
 * @Author: Zane
 * @Date: 8/3/2024 下午11:40
 */
public class DateUtils {

    public static final String YYYY_MM_DD_HH_MM_SS= "yyyy-MM-dd HH:mm:ss";

    public static final String YYYY_MM_DD= "yyyy-MM-dd";

    public static final String _YYYY_MM_DD = "yyyy/M/dd";

    public static String dateFormat(Date date, String patten){
        SimpleDateFormat sdf = new SimpleDateFormat(patten);
        return sdf.format(date);
    }
}