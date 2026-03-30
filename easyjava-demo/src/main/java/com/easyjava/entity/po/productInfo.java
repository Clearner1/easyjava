package com.easyjava.entity.po;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

/**
 * @Description 商品信息表
 * @date 2026/3/30
 */
public class productInfo implements Serializable {
	private Integer id;

	private String companyId;

	private String code;

	private String productName;

	private BigDecimal price;

	private Integer skuType;

	private Integer colorType;

	private Date createTime;

	private Date createDate;

	private Long stock;

	private Integer status;

}