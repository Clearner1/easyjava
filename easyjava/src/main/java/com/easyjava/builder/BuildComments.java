package com.easyjava.builder;

import com.easyjava.bean.Constants;
import com.easyjava.utils.DateUtils;
import com.easyjava.utils.PropertiesUtils;

import java.io.BufferedWriter;
import java.util.Date;
import java.util.Properties;

public class BuildComments {
    public static void createClassComment(BufferedWriter bw, String comment) {
        try {
            bw.write("/**");
            bw.newLine();
            bw.write(" * @author " + Constants.COMMENT_AUTHOR);
            bw.newLine();
            bw.write(" * @Description " + comment);
            bw.newLine();
            bw.write(" * @date " + DateUtils.dateFormat(new Date(), DateUtils._YYYY_MM_DD));
            bw.newLine();
            bw.write(" */");
            bw.newLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createFieldComment(BufferedWriter bw, String comment) {
        try {
            bw.write("\t/**");
            bw.newLine();
            bw.write( "\t * " + (comment == null ? "" : comment));
            bw.newLine();
            bw.write("\t */");
            bw.newLine();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void createMethodComment() {

    }
}
