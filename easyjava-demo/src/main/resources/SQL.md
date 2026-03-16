-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS `test`
    DEFAULT CHARSET=utf8
    COLLATE=utf8_general_ci;

-- 切换到该数据库
USE `test`;

-- 再创建表
CREATE TABLE `tb_product_info` (
                                   `id`           INT(11)        NOT NULL AUTO_INCREMENT COMMENT '自增ID',
                                   `company_id`   VARCHAR(30)    DEFAULT NULL             COMMENT '公司ID',
                                   `code`         VARCHAR(11)    DEFAULT NULL             COMMENT '商品编号',
                                   `product_name` VARCHAR(200)   DEFAULT NULL             COMMENT '商品名称',
                                   `price`        DECIMAL(15,2)  DEFAULT NULL             COMMENT '价格',
                                   `sku_type`     TINYINT(4)     DEFAULT NULL             COMMENT 'sku类型',
                                   `color_type`   TINYINT(4)     DEFAULT NULL             COMMENT '颜色类型',
                                   `create_time`  DATETIME       DEFAULT NULL             COMMENT '创建时间',
                                   `create_date`  DATE           DEFAULT NULL             COMMENT '创建日期',
                                   `stock`        BIGINT(20)     DEFAULT NULL             COMMENT '库存',
                                   `status`       TINYINT(4)     DEFAULT NULL             COMMENT '状态',
                                   PRIMARY KEY (`id`),
                                   UNIQUE KEY `idx_code` (`code`),
                                   UNIQUE KEY `idx_sku_color` (`sku_type`, `color_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='商品信息表';
