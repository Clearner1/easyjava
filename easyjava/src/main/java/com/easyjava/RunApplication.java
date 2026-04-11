package com.easyjava;

import com.easyjava.bean.TableInfo;
import com.easyjava.builder.*;

import java.io.IOException;
import java.util.List;

public class RunApplication {
    public static void main(String[] args) throws IOException {
        List<TableInfo> tableInfoList = BuildTable.getTables();
        for (TableInfo tableInfo : tableInfoList) {
            // 通过BuildPo创建Po对象
            BuildPo.execute(tableInfo);
            BuildQuery.execute(tableInfo);
            BuildMapper.execute(tableInfo);
            BuildMapperXML.execute(tableInfo);
        }
        BuildBase.execute();
    }
}
