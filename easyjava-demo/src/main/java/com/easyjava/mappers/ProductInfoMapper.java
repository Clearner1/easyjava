package com.easyjava.mappers;

import org.apache.ibatis.annotations.Param;

/**
 * @author Zane
 * @Description 商品信息表Mapper查询对象
 * @date 2026/4/14
 */
public interface ProductInfoMapper<T, P> extends BaseMapper {

	/**
	 * 根据Id查询
	 */
	public T selectById(@Param("id") Integer id);

	/**
	 * 根据Id更新
	 */
	public Integer updateById(@Param("bean") T t, @Param("id") Integer id);

	/**
	 * 根据Id删除
	 */
	public Integer deleteById(@Param("id") Integer id);


	/**
	 * 根据SkuTypeAndColorType查询
	 */
	public T selectBySkuTypeAndColorType(@Param("skuType") Integer skuType, @Param("colorType") Integer colorType);

	/**
	 * 根据SkuTypeAndColorType更新
	 */
	public Integer updateBySkuTypeAndColorType(@Param("bean") T t, @Param("skuType") Integer skuType, @Param("colorType") Integer colorType);

	/**
	 * 根据SkuTypeAndColorType删除
	 */
	public Integer deleteBySkuTypeAndColorType(@Param("skuType") Integer skuType, @Param("colorType") Integer colorType);


	/**
	 * 根据Code查询
	 */
	public T selectByCode(@Param("code") String code);

	/**
	 * 根据Code更新
	 */
	public Integer updateByCode(@Param("bean") T t, @Param("code") String code);

	/**
	 * 根据Code删除
	 */
	public Integer deleteByCode(@Param("code") String code);


}