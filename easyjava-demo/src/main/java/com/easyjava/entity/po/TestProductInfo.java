package com.easyjava.entity.po;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 测试 productInfo 的 toString 方法
 */
public class TestProductInfo {
    public static void main(String[] args) {
        // 1. 所有字段都赋值
        productInfo p1 = new productInfo();
        p1.setId(1001);
        p1.setCompanyId("COMP-001");
        p1.setCode("SKU-20260331");
        p1.setProductName("无线蓝牙耳机");
        p1.setPrice(new BigDecimal("299.99"));
        p1.setSkuType(1);
        p1.setColorType(2);
        p1.setCreateTime(new Date());
        p1.setCreateDate(new Date());
        p1.setStock(500L);
        p1.setStatus(1);

        System.out.println("=== 全部字段赋值 ===");
        System.out.println(p1);

//        // 2. 所有字段为 null（默认值）
//        productInfo p2 = new productInfo();
//        System.out.println("\n=== 全部字段为空 ===");
//        System.out.println(p2);
//
//        // 3. 部分字段赋值
//        productInfo p3 = new productInfo();
//        p3.setId(1002);
//        p3.setProductName("机械键盘");
//        p3.setPrice(new BigDecimal("599.00"));
//        p3.setStock(120L);
//
//        System.out.println("\n=== 部分字段赋值 ===");
//        System.out.println(p3);
    }
}
