package com.blocain.bitms.bitpay.service;

import java.util.List;

import com.blocain.bitms.bitpay.common.Pagination;
import com.blocain.bitms.bitpay.entity.WithdrawRecord;
import com.github.pagehelper.PageInfo;

/**
 * <p>File: WithdrawRecordService.java</p>
 * <p>Title: </p>
 * <p>Description:bitpay</p>
 * <p>Copyright: Copyright (c) 2017-07-18 11:05</p>
 * <p>Company: jmwenhua.cn</p>
 * @author 施建波
 * @version 1.0
 */
public interface WithdrawRecordService{
	
 	/**
	 * 
	 * <pre>
	 * 	2017-07-18 11:05 施建波
	 * 	分页查询
	 * </pre>
	 * 
	 * @param withdrawRecord	实体
	 * @param page			分页实体
	 * @return
	 */
	public PageInfo<WithdrawRecord> findWithdrawRecordPage(WithdrawRecord withdrawRecord, Pagination pagination);
	
	/**
	 * 
	 * <pre>
	 * 	2017-07-18 11:05 施建波
	 * 	查询
	 * </pre>
	 * 
	 * @param withdrawRecord	实体	
	 * @return
	 */
	public List<WithdrawRecord> findWithdrawRecordSearch(WithdrawRecord withdrawRecord);
	
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
	public WithdrawRecord findWithdrawRecord(String id);
	
	/**
	 * 
	 * <pre>
	 * 	2017-07-18 11:05 施建波
	 * 	通过实体查询
	 * </pre>
	 * 
	 * @param withdrawRecord	实体
	 * @return
	 */
	public WithdrawRecord findWithdrawRecord(WithdrawRecord withdrawRecord);
	
	/**
	 * 
	 * <pre>
	 * 	2017-07-18 11:05 施建波
	 * 	新增或修改
	 * </pre>
	 * 
	 * @param withdrawRecord	实体
	 */
	public Integer saveWithdrawRecord(WithdrawRecord withdrawRecord);
	
	/**
	 * 
	 * <pre>
	 * 	2017-07-18 11:05 施建波
	 * 	新增
	 * </pre>
	 * 
	 * @param withdrawRecord	实体
	 */
	public Integer insertWithdrawRecord(WithdrawRecord withdrawRecord);
	
	/**
	 * 
	 * <pre>
	 * 	2017-07-18 11:05 施建波
	 * 	修改
	 * </pre>
	 * 
	 * @param withdrawRecord	实体
	 */
	public Integer updateWithdrawRecord(WithdrawRecord withdrawRecord);
	
	/**
	 * 
	 * <pre>
	 * 	2017-07-18 11:05 施建波
	 * 	修改（批量）
	 * </pre>
	 * 
	 * @param withdrawRecordList	实体集合
	 */
	public Integer updateWithdrawRecordBatch(List<WithdrawRecord> withdrawRecordList);
	
	/**
	 * 
	 * <pre>
	 * 	2017-07-18 11:05 施建波
	 * 	插入（批量）
	 * </pre>
	 * 
	 * @param withdrawRecordList	实体集合
	 */
	public Integer insertWithdrawRecordBatch(List<WithdrawRecord> withdrawRecordList);
	
	/**
	 * 
	 * <pre>
	 * 	2017-07-18 11:05 施建波
	 * 	删除
	 * </pre>
	 * 
	 * @param id	主键ID
	 */
	public Integer deleteWithdrawRecord(String id);
	
	/**
	 * 
	 * <pre>
	 * 	2017-07-18 11:05 施建波
	 * 	根据 entity 条件，删除
	 * </pre>
	 * 
	 * @param withdrawRecord	实体
	 */
	public Integer deleteWithdrawRecord(WithdrawRecord withdrawRecord);
	
	/**
	 * 
	 * <pre>
	 * 	2017-07-18 11:05 施建波
	 * 	删除（根据ID 批量删除）
	 * </pre>
	 * 
	 * @param idList	主键ID集合
	 */
	public Integer deleteWithdrawRecordBatch(List<String> idList);
	
	/**
	 * 
	 * <pre>
	 * 	2017-07-18 11:05 施建波
	 * 	根据 entity 条件，查询总记录数
	 * </pre>
	 * 
	 * @param withdrawRecord	实体
	 */
	public Integer countWithdrawRecord(WithdrawRecord withdrawRecord);
	
	
	
}
