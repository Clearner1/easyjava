-- =====================================================
-- EasyJava 代码生成器测试表
-- 数据库: test
-- 表名: tb_product_info
-- =====================================================

CREATE DATABASE IF NOT EXISTS `test` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `test`;

CREATE TABLE IF NOT EXISTS `tb_product_info` (
    `id`            INT(11)         NOT NULL AUTO_INCREMENT  COMMENT '商品ID',
    `company_id`    VARCHAR(30)     DEFAULT NULL             COMMENT '公司ID',
    `code`          VARCHAR(11)     DEFAULT NULL             COMMENT '商品编码',
    `product_name`  VARCHAR(200)    DEFAULT NULL             COMMENT '商品名称',
    `price`         DECIMAL(15,2)   DEFAULT NULL             COMMENT '价格',
    `sku_type`      TINYINT(4)      DEFAULT NULL             COMMENT 'SKU类型',
    `color_type`    TINYINT(4)      DEFAULT NULL             COMMENT '颜色类型',
    `create_time`   DATETIME        DEFAULT NULL             COMMENT '创建时间',
    `create_date`   DATE            DEFAULT NULL             COMMENT '创建日期',
    `stock`         BIGINT(20)      DEFAULT NULL             COMMENT '库存',
    `status`        TINYINT(4)      DEFAULT NULL             COMMENT '状态',
    PRIMARY KEY (`id`),
    -- 按公司查商品编码, 高频精确查询
    UNIQUE KEY `idx_company_code` (`company_id`, `code`),
    -- 按公司筛选某状态的商品
    KEY `idx_company_status` (`company_id`, `status`),
    -- 按SKU类型和颜色类型组合筛选
    KEY `idx_sku_color` (`sku_type`, `color_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品信息表';
