package com.easyjava.builder;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.CharsetUtil;
import com.easyjava.bean.Constants;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class BuildBase {

    private static final Logger logger = LoggerFactory.getLogger(BuildBase.class);

    public static void execute() {
        // package
        List<String> headerInfoList = new ArrayList<>();
        headerInfoList.add(Constants.PACKAGE_ENUMS);
        BuildBase.build("DateTimePatternEnum", Constants.PATH_Enums, headerInfoList);
        headerInfoList.clear();
        headerInfoList.add(Constants.PACKAGE_UTILS);
        BuildBase.build("DateUtils", Constants.PATH_Utils, headerInfoList);
        headerInfoList.clear();
        headerInfoList.add(Constants.PACKAGE_MAPPER);
        BuildBase.build("BaseMapper", Constants.PATH_Mapper, headerInfoList);

    }


    private static void build(String filename, String outputPath, List<String> headerInfoList) {
        try {
            // 2. 读取 classpath 下的模板文件
            String templateContent = ResourceUtil.readUtf8Str("template/" + filename + ".txt");

            // 3. 拼接 header 信息 导包
            StringBuilder sb = new StringBuilder();
            for (String head : headerInfoList) {
                sb.append("package ").append(head).append(";\n").append("\n");
            }

            // 3. 拼接模板内容
            sb.append(templateContent);

            // 4. 构建目标文件路径
            File javaFile = new File(outputPath, filename + ".java");

            // 5. 写入文件，自动创建父类目录，自动处理流式关闭
            FileUtil.writeString(sb.toString(), javaFile, CharsetUtil.UTF_8);
        } catch (Exception e) {
            logger.error("生成基础类失败: {},失败" + filename, e);
        }
    }

}
