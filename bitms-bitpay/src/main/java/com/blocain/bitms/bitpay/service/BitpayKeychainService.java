package com.blocain.bitms.bitpay.service;

import java.util.List;

import com.blocain.bitms.bitpay.common.Pagination;
import com.blocain.bitms.bitpay.entity.BitpayKeychain;
import com.github.pagehelper.PageInfo;

/**
 * <p>File: BitpayKeychainService.java</p>
 * <p>Title: </p>
 * <p>Description:bitpay</p>
 * <p>Copyright: Copyright (c) 2017-07-18 11:05</p>
 * <p>Company: jmwenhua.cn</p>
 * @author 施建波
 * @version 1.0
 */
public interface BitpayKeychainService{
	
 	/**
	 * 
	 * <pre>
	 * 	2017-07-18 11:05 施建波
	 * 	分页查询
	 * </pre>
	 * 
	 * @param bitpayKeychain	实体
	 * @param page			分页实体
	 * @return
	 */
	public PageInfo<BitpayKeychain> findBitpayKeychainPage(BitpayKeychain bitpayKeychain, Pagination pagination);
	
	/**
	 * 
	 * <pre>
	 * 	2017-07-18 11:05 施建波
	 * 	查询
	 * </pre>
	 * 
	 * @param bitpayKeychain	实体	
	 * @return
	 */
	public List<BitpayKeychain> findBitpayKeychainSearch(BitpayKeychain bitpayKeychain);
	
	/**
	 * 
	 * <pre>
	 * 	2017-07-18 11:05 施建波
	 * 	通过ID查询
	 * </pre>
	 * 
	 * @param id	主键ID
	 * @return
	 */
	public BitpayKeychain findBitpayKeychain(Long id);
	
	/**
	 * 
	 * <pre>
	 * 	2017-07-18 11:05 施建波
	 * 	通过实体查询
	 * </pre>
	 * 
	 * @param bitpayKeychain	实体
	 * @return
	 */
	public BitpayKeychain findBitpayKeychain(BitpayKeychain bitpayKeychain);
	
	/**
	 * 
	 * <pre>
	 * 	2017-07-18 11:05 施建波
	 * 	新增或修改
	 * </pre>
	 * 
	 * @param bitpayKeychain	实体
	 */
	public Integer saveBitpayKeychain(BitpayKeychain bitpayKeychain);
	
	/**
	 * 
	 * <pre>
	 * 	2017-07-18 11:05 施建波
	 * 	新增
	 * </pre>
	 * 
	 * @param bitpayKeychain	实体
	 */
	public Integer insertBitpayKeychain(BitpayKeychain bitpayKeychain);
	
	/**
	 * 
	 * <pre>
	 * 	2017-07-18 11:05 施建波
	 * 	修改
	 * </pre>
	 * 
	 * @param bitpayKeychain	实体
	 */
	public Integer updateBitpayKeychain(BitpayKeychain bitpayKeychain);
	
	/**
	 * 
	 * <pre>
	 * 	2017-07-18 11:05 施建波
	 * 	修改（批量）
	 * </pre>
	 * 
	 * @param bitpayKeychainList	实体集合
	 */
	public Integer updateBitpayKeychainBatch(List<BitpayKeychain> bitpayKeychainList);
	
	/**
	 * 
	 * <pre>
	 * 	2017-07-18 11:05 施建波
	 * 	插入（批量）
	 * </pre>
	 * 
	 * @param bitpayKeychainList	实体集合
	 */
	public Integer insertBitpayKeychainBatch(List<BitpayKeychain> bitpayKeychainList);
	
	/**
	 * 
	 * <pre>
	 * 	2017-07-18 11:05 施建波
	 * 	删除
	 * </pre>
	 * 
	 * @param id	主键ID
	 */
	public Integer deleteBitpayKeychain(Long id);
	
	/**
	 * 
	 * <pre>
	 * 	2017-07-18 11:05 施建波
	 * 	根据 entity 条件，删除
	 * </pre>
	 * 
	 * @param bitpayKeychain	实体
	 */
	public Integer deleteBitpayKeychain(BitpayKeychain bitpayKeychain);
	
	/**
	 * 
	 * <pre>
	 * 	2017-07-18 11:05 施建波
	 * 	删除（根据ID 批量删除）
	 * </pre>
	 * 
	 * @param idList	主键ID集合
	 */
	public Integer deleteBitpayKeychainBatch(List<Long> idList);
	
	/**
	 * 
	 * <pre>
	 * 	2017-07-18 11:05 施建波
	 * 	根据 entity 条件，查询总记录数
	 * </pre>
	 * 
	 * @param bitpayKeychain	实体
	 */
	public Integer countBitpayKeychain(BitpayKeychain bitpayKeychain);
	
	
	
}
