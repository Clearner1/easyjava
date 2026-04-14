package com.easyjava.service;
import com.easyjava.entity.po.ProductInfo;
import com.easyjava.entity.query.ProductInfoQuery;
import com.easyjava.entity.vo.PaginationResultVO;
import java.util.List;

/**
 * @author Zane
 * @Description 商品信息表Service
 * @date 2026/4/14
 */
public interface ProductInfoService {

	/**
	 * 根据条件查询列表
	 */
	List<ProductInfo> findListByParam(ProductInfoQuery query);

	/**
	 * 根据条件查询数量
	 */
	Integer findCountByParam(ProductInfoQuery query);

	/**
	 * 分页查询
	 */
	PaginationResultVO<ProductInfo> findListByPage(ProductInfoQuery query);

	/**
	 * 新增
	 */
	Integer add(ProductInfo bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<ProductInfo> listBean);

	/**
	 * 批量新增或修改
	 */
	Integer insertOrUpdateBatch(List<ProductInfo> listBean);

	/**
	 * 根据 Id 查询
	 */
	ProductInfo getProductInfoById(Integer id);

	/**
	 * 根据 Id 更新
	 */
	Integer updateProductInfoById(ProductInfo bean, Integer id);

	/**
	 * 根据 Id 删除
	 */
	Integer deleteProductInfoById(Integer id);

	/**
	 * 根据 SkuTypeAndColorType 查询
	 */
	ProductInfo getProductInfoBySkuTypeAndColorType(Integer skuType, Integer colorType);

	/**
	 * 根据 SkuTypeAndColorType 更新
	 */
	Integer updateProductInfoBySkuTypeAndColorType(ProductInfo bean, Integer skuType, Integer colorType);

	/**
	 * 根据 SkuTypeAndColorType 删除
	 */
	Integer deleteProductInfoBySkuTypeAndColorType(Integer skuType, Integer colorType);

	/**
	 * 根据 Code 查询
	 */
	ProductInfo getProductInfoByCode(String code);

	/**
	 * 根据 Code 更新
	 */
	Integer updateProductInfoByCode(ProductInfo bean, String code);

	/**
	 * 根据 Code 删除
	 */
	Integer deleteProductInfoByCode(String code);

}
