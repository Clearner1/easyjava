package com.easyjava.builder;

import com.easyjava.utils.PropertiesUtils;
import org.slf4j.LoggerFactory;

import java.sql.*;

import org.slf4j.Logger;

public class BuilderTable {
    private static final Logger logger = LoggerFactory.getLogger(BuilderTable.class);
    private static Connection conn = null;

    private static String SQL_SHOW_TABLE_STATUS = "show table status";

    static {
        String driverName = PropertiesUtils.getString("db.driver.name");
        String urlName = PropertiesUtils.getString("db.url");
        String username = PropertiesUtils.getString("db.username");
        String pwd = PropertiesUtils.getString("db.password");

        try {
            Class.forName(driverName);
            conn = DriverManager.getConnection(urlName, username, pwd);
        } catch (Exception e) {
            logger.error("数据库连接失败", e);
        }

    }

    public static void getTables() {
        // 需要执行的SQL
        PreparedStatement ps = null;
        // 执行SQL后的结果
        ResultSet tableResult = null;
        try {
            // 输入SQL语句
            ps = conn.prepareStatement(SQL_SHOW_TABLE_STATUS);
            // 执行SQL语句
            tableResult = ps.executeQuery();
            // 一行一行地遍历（每一行代表一张表）
            while (tableResult.next()) {
                String tableName = tableResult.getString("name");
                String comment = tableResult.getString("comment");
                logger.info("tableName:{},comment:{}", tableName, comment);
            }
        } catch (Exception e) {
            logger.error("读取表信息失败", e);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            if (tableResult != null) {
                try {
                    tableResult.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }
}
