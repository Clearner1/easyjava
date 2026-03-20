package com.easyjava;

import com.easyjava.builder.BuilderTable;
import com.easyjava.utils.PropertiesUtils;

public class RunApplication {
    public static void main(String[] args) {
        System.out.println(PropertiesUtils.getString("hello"));
    }
}
