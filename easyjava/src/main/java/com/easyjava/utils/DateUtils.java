package com.easyjava.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static String dataFormat(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/M/d");
        return sdf.format(date);
    }
}
