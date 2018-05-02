/*
 * @(#)TransferRecordService.java 2017年7月20日 下午1:05:07
 * Copyright 2017 施建波, Inc. All rights reserved. BloCain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.bitpay.service;

import java.util.List;

import com.blocain.bitms.bitpay.common.Pagination;
import com.blocain.bitms.bitpay.entity.TransferRecord;
import com.github.pagehelper.PageInfo;

/**
 * <p>File：TransferRecordService.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2017年7月20日 下午1:05:07</p>
 * <p>Company: BloCain</p>
 * @author 施建波
 * @version 1.0
 */
public interface TransferRecordService
{
    /**
     * 
     * <pre>
     *  2017-07-18 11:05 施建波
     *  分页查询
     * </pre>
     * 
     * @param transferRecord    实体
     * @param page          分页实体
     * @return
     */
    public PageInfo<TransferRecord> findTransferRecordPage(TransferRecord transferRecord, Pagination pagination);
    
    /**
     * 
     * <pre>
     *  2017-07-18 11:05 施建波
     *  查询
     * </pre>
     * 
     * @param transferRecord    实体  
     * @return
     */
    public List<TransferRecord> findTransferRecordSearch(TransferRecord transferRecord);
    
    /**
     * 
     * <pre>
     *  2017-07-18 11:05 施建波
     *  通过ID查询
     * </pre>
     * 
     * @param id    主键ID
     * @return
     */
    public TransferRecord findTransferRecord(Long id);
    
    /**
     * 
     * <pre>
     *  2017-07-18 11:05 施建波
     *  通过实体查询
     * </pre>
     * 
     * @param transferRecord    实体
     * @return
     */
    public TransferRecord findTransferRecord(TransferRecord transferRecord);
    
    /**
     * 
     * <pre>
     *  2017-07-18 11:05 施建波
     *  新增或修改
     * </pre>
     * 
     * @param transferRecord    实体
     */
    public Integer saveTransferRecord(TransferRecord transferRecord);
    
    /**
     * 
     * <pre>
     *  2017-07-18 11:05 施建波
     *  新增
     * </pre>
     * 
     * @param transferRecord    实体
     */
    public Integer insertTransferRecord(TransferRecord transferRecord);
    
    /**
     * 
     * <pre>
     *  2017-07-18 11:05 施建波
     *  修改
     * </pre>
     * 
     * @param transferRecord    实体
     */
    public Integer updateTransferRecord(TransferRecord transferRecord);
    
    /**
     * 
     * <pre>
     *  2017-07-18 11:05 施建波
     *  修改（批量）
     * </pre>
     * 
     * @param transferRecordList    实体集合
     */
    public Integer updateTransferRecordBatch(List<TransferRecord> transferRecordList);
    
    /**
     * 
     * <pre>
     *  2017-07-18 11:05 施建波
     *  插入（批量）
     * </pre>
     * 
     * @param transferRecordList    实体集合
     */
    public Integer insertTransferRecordBatch(List<TransferRecord> transferRecordList);
    
    /**
     * 
     * <pre>
     *  2017-07-18 11:05 施建波
     *  删除
     * </pre>
     * 
     * @param id    主键ID
     */
    public Integer deleteTransferRecord(Long id);
    
    /**
     * 
     * <pre>
     *  2017-07-18 11:05 施建波
     *  根据 entity 条件，删除
     * </pre>
     * 
     * @param transferRecord    实体
     */
    public Integer deleteTransferRecord(TransferRecord transferRecord);
    
    /**
     * 
     * <pre>
     *  2017-07-18 11:05 施建波
     *  删除（根据ID 批量删除）
     * </pre>
     * 
     * @param idList    主键ID集合
     */
    public Integer deleteTransferRecordBatch(List<Long> idList);
    
    /**
     * 
     * <pre>
     *  2017-07-18 11:05 施建波
     *  根据 entity 条件，查询总记录数
     * </pre>
     * 
     * @param transferRecord    实体
     */
    public Integer countTransferRecord(TransferRecord transferRecord);
}
