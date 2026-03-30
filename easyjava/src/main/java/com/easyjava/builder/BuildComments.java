package com.easyjava.builder;

import com.easyjava.utils.DateUtils;

import java.io.BufferedWriter;
import java.util.Date;

public class BuildComments {
    public static void createClassComment(BufferedWriter bw, String comment) {
        try {
            bw.write("/**");
            bw.newLine();
            bw.write(" * @Description " + comment);
            bw.newLine();
            bw.write(" * @date " + DateUtils.dataFormat(new Date()));
            bw.newLine();
            bw.write(" */");
            bw.newLine();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void createFieldComment(BufferedWriter bw, String comment) {

    }

    public static void createMethodComment() {

    }
}
