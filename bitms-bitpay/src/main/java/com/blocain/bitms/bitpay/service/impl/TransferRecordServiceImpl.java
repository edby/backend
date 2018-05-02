/*
 * @(#)TransferRecordServiceImpl.java 2017年7月20日 下午1:09:24
 * Copyright 2017 施建波, Inc. All rights reserved. BloCain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.bitpay.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.blocain.bitms.bitpay.common.Pagination;
import com.blocain.bitms.bitpay.entity.TransferRecord;
import com.blocain.bitms.bitpay.mapper.TransferRecordMapper;
import com.blocain.bitms.bitpay.service.TransferRecordService;
import com.blocain.bitms.tools.utils.SerialnoUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * <p>File：TransferRecordServiceImpl.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2017年7月20日 下午1:09:24</p>
 * <p>Company: BloCain</p>
 * @author 施建波
 * @version 1.0
 */
@Service("transferRecordService")
public class TransferRecordServiceImpl implements TransferRecordService
{
    @Resource
    private TransferRecordMapper transferRecordMapper;

    /* (non-Javadoc)
     * @see com.blocain.bitms.bitpay.service.TransferRecordService#findTransferRecordPage(com.blocain.bitms.bitpay.entity.TransferRecord, com.blocain.bitms.bitpay.common.Pagination)
     */
    @Override
    public PageInfo<TransferRecord> findTransferRecordPage(
            TransferRecord transferRecord, Pagination pagination)
    {
        PageHelper.startPage(pagination.getPageNo(), pagination.getPageSize()); 
        String orderByField = " id desc";
        if(null != transferRecord && StringUtils.isNotBlank(transferRecord.getOrderByField())){
            transferRecord.setOrderByField(orderByField);
        }
        List<TransferRecord> list = this.findTransferRecordSearch(transferRecord);
        return new PageInfo<TransferRecord>(list);
    }

    /* (non-Javadoc)
     * @see com.blocain.bitms.bitpay.service.TransferRecordService#findTransferRecordSearch(com.blocain.bitms.bitpay.entity.TransferRecord)
     */
    @Override
    public List<TransferRecord> findTransferRecordSearch(
            TransferRecord transferRecord)
    {
        if(null == transferRecord){
            transferRecord = new TransferRecord();
        }
        EntityWrapper<TransferRecord> entityWrapper = new  EntityWrapper<TransferRecord> (transferRecord);
        entityWrapper.setSqlSelect(transferRecord.getEntityColumns());
        if(StringUtils.isNotBlank(transferRecord.getOrderByField())){
            entityWrapper.orderBy(transferRecord.getOrderByField());
        }
        return transferRecordMapper.selectList(entityWrapper);
    }

    /* (non-Javadoc)
     * @see com.blocain.bitms.bitpay.service.TransferRecordService#findTransferRecord(java.lang.Long)
     */
    @Override
    public TransferRecord findTransferRecord(Long id)
    {
        return transferRecordMapper.selectById(id);
    }

    /* (non-Javadoc)
     * @see com.blocain.bitms.bitpay.service.TransferRecordService#findTransferRecord(com.blocain.bitms.bitpay.entity.TransferRecord)
     */
    @Override
    public TransferRecord findTransferRecord(TransferRecord transferRecord)
    {
        return transferRecordMapper.selectOne(transferRecord);
    }

    /* (non-Javadoc)
     * @see com.blocain.bitms.bitpay.service.TransferRecordService#saveTransferRecord(com.blocain.bitms.bitpay.entity.TransferRecord)
     */
    @Override
    public Integer saveTransferRecord(TransferRecord transferRecord)
    {
        if(null != transferRecord.getId()){
            return transferRecordMapper.updateById(transferRecord); 
        }else{
            transferRecord.setCreateDate(new Date().getTime());
            transferRecord.setId(SerialnoUtils.buildPrimaryKey());
            return this.insertTransferRecord(transferRecord);
        }
    }

    /* (non-Javadoc)
     * @see com.blocain.bitms.bitpay.service.TransferRecordService#insertTransferRecord(com.blocain.bitms.bitpay.entity.TransferRecord)
     */
    @Override
    public Integer insertTransferRecord(TransferRecord transferRecord)
    {
        transferRecord.setId(SerialnoUtils.buildPrimaryKey());
        return transferRecordMapper.insert(transferRecord);
    }

    /* (non-Javadoc)
     * @see com.blocain.bitms.bitpay.service.TransferRecordService#updateTransferRecord(com.blocain.bitms.bitpay.entity.TransferRecord)
     */
    @Override
    public Integer updateTransferRecord(TransferRecord transferRecord)
    {
        return transferRecordMapper.updateById(transferRecord);
    }

    /* (non-Javadoc)
     * @see com.blocain.bitms.bitpay.service.TransferRecordService#updateTransferRecordBatch(java.util.List)
     */
    @Override
    public Integer updateTransferRecordBatch(
            List<TransferRecord> transferRecordList)
    {
        if(CollectionUtils.isNotEmpty(transferRecordList)){
            for(TransferRecord transferRecord:transferRecordList) {
                transferRecordMapper.updateById(transferRecord);
            }
            
        }
        return 0;
    }

    /* (non-Javadoc)
     * @see com.blocain.bitms.bitpay.service.TransferRecordService#insertTransferRecordBatch(java.util.List)
     */
    @Override
    public Integer insertTransferRecordBatch(
            List<TransferRecord> transferRecordList)
    {
        if(CollectionUtils.isNotEmpty(transferRecordList)){
            for(TransferRecord transferRecord:transferRecordList) {
                transferRecord.setId(SerialnoUtils.buildPrimaryKey());
                transferRecordMapper.insert(transferRecord);
            }
        }
        return 0;
    }

    /* (non-Javadoc)
     * @see com.blocain.bitms.bitpay.service.TransferRecordService#deleteTransferRecord(java.lang.Long)
     */
    @Override
    public Integer deleteTransferRecord(Long id)
    {
        return transferRecordMapper.deleteById(id);
    }

    /* (non-Javadoc)
     * @see com.blocain.bitms.bitpay.service.TransferRecordService#deleteTransferRecord(com.blocain.bitms.bitpay.entity.TransferRecord)
     */
    @Override
    public Integer deleteTransferRecord(TransferRecord transferRecord)
    {
        return transferRecordMapper.deleteById(transferRecord.getId());
    }

    /* (non-Javadoc)
     * @see com.blocain.bitms.bitpay.service.TransferRecordService#deleteTransferRecordBatch(java.util.List)
     */
    @Override
    public Integer deleteTransferRecordBatch(List<Long> idList)
    {
        if(CollectionUtils.isNotEmpty(idList)){
            transferRecordMapper.deleteBatchIds(idList);
        }
        return 0;
    }

    /* (non-Javadoc)
     * @see com.blocain.bitms.bitpay.service.TransferRecordService#countTransferRecord(com.blocain.bitms.bitpay.entity.TransferRecord)
     */
    @Override
    public Integer countTransferRecord(TransferRecord transferRecord)
    {
        EntityWrapper<TransferRecord> entityWrapper = new EntityWrapper<TransferRecord>();
        entityWrapper.setEntity(transferRecord);
        return transferRecordMapper.selectCount(entityWrapper);
    }
}
