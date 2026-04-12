package com.easyjava.mappers;

import org.apache.ibatis.annotations.Param;

/**
 * @author Zane
 * @Description 商品信息表Mapper查询对象
 * @date 2026/4/13
 */
public interface ProductInfoMapper<T, P> extends BaseMapper {

	/**
	 * 根据Id查询
	 */
	public T selectById(@Param("(id") Integer id);

	/**
	 * 根据Id更新
	 */
	public T updateById(@Param("bean") T t, @Param("(id") Integer id);

	/**
	 * 根据Id删除
	 */
	public T deleteById(@Param("(id") Integer id);


	/**
	 * 根据CompanyIdAndCode查询
	 */
	public T selectByCompanyIdAndCode(@Param("(companyId") String companyId, @Param("(code") String code);

	/**
	 * 根据CompanyIdAndCode更新
	 */
	public T updateByCompanyIdAndCode(@Param("bean") T t, @Param("(companyId") String companyId, @Param("(code") String code);

	/**
	 * 根据CompanyIdAndCode删除
	 */
	public T deleteByCompanyIdAndCode(@Param("(companyId") String companyId, @Param("(code") String code);


}