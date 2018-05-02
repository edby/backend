package com.blocain.bitms.bitpay.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.blocain.bitms.bitpay.common.Pagination;
import com.blocain.bitms.bitpay.entity.WithdrawRecord;
import com.blocain.bitms.bitpay.mapper.WithdrawRecordMapper;
import com.blocain.bitms.bitpay.service.WithdrawRecordService;
import com.blocain.bitms.tools.utils.SerialnoUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * <p>File: WithdrawRecordServiceImpl.java</p>
 * <p>Title: </p>
 * <p>Description:bitpay</p>
 * <p>Copyright: Copyright (c) 2017-07-18 11:05</p>
 * <p>Company: jmwenhua.cn</p>
 * @author 施建波
 * @version 1.0
 */
@Service("withdrawRecordService")
public class WithdrawRecordServiceImpl implements WithdrawRecordService {

	@Resource
	private WithdrawRecordMapper withdrawRecordMapper;
	
	@Override
	public PageInfo<WithdrawRecord> findWithdrawRecordPage(WithdrawRecord withdrawRecord, Pagination pagination) {
		PageHelper.startPage(pagination.getPageNo(), pagination.getPageSize()); 
		String orderByField = " id desc";
		if(null != withdrawRecord && StringUtils.isNotBlank(withdrawRecord.getOrderByField())){
		    withdrawRecord.setOrderByField(orderByField);
		}
		List<WithdrawRecord> list = this.findWithdrawRecordSearch(withdrawRecord);
		return new PageInfo<WithdrawRecord>(list); 
	}
	
	@Override
	public List<WithdrawRecord> findWithdrawRecordSearch(WithdrawRecord withdrawRecord) {
		if(null == withdrawRecord){
			withdrawRecord = new WithdrawRecord();
		}
		EntityWrapper<WithdrawRecord> entityWrapper = new  EntityWrapper<WithdrawRecord> (withdrawRecord);
		entityWrapper.setSqlSelect(withdrawRecord.getEntityColumns());
		if(StringUtils.isNotBlank(withdrawRecord.getOrderByField())){
			entityWrapper.orderBy(withdrawRecord.getOrderByField());
		}
		if(CollectionUtils.isNotEmpty(withdrawRecord.getIdList())) {
		    entityWrapper.in("id", withdrawRecord.getIdList());
		}
		return withdrawRecordMapper.selectList(entityWrapper);
	}
	
	@Override
	public WithdrawRecord findWithdrawRecord(String id) {
		return withdrawRecordMapper.selectById(id);
	}
	
	@Override
	public WithdrawRecord findWithdrawRecord(WithdrawRecord withdrawRecord) {
		return withdrawRecordMapper.selectOne(withdrawRecord);
	}

	@Override
	public Integer saveWithdrawRecord(WithdrawRecord withdrawRecord) {
		if(null != withdrawRecord.getId()){
			return withdrawRecordMapper.updateById(withdrawRecord); 
		}else{
			withdrawRecord.setCreateDate(new Date().getTime());
			withdrawRecord.setId(SerialnoUtils.buildPrimaryKey());
			return this.insertWithdrawRecord(withdrawRecord);
		}
	}
	
	@Override
	public Integer insertWithdrawRecord(WithdrawRecord withdrawRecord) {
	    withdrawRecord.setId(SerialnoUtils.buildPrimaryKey());
		return withdrawRecordMapper.insert(withdrawRecord);
	}
	
	@Override
	public Integer updateWithdrawRecord(WithdrawRecord withdrawRecord) {
		return withdrawRecordMapper.updateById(withdrawRecord);
	}
	
	@Override
	public Integer updateWithdrawRecordBatch(List<WithdrawRecord> withdrawRecordList) {
		if(CollectionUtils.isNotEmpty(withdrawRecordList)){
		    for(WithdrawRecord withdrawRecord:withdrawRecordList) {
		        withdrawRecordMapper.updateById(withdrawRecord);
		    }
	    }
		return 0;
	}
	
	@Override
	public Integer insertWithdrawRecordBatch(List<WithdrawRecord> withdrawRecordList) {
		if(CollectionUtils.isNotEmpty(withdrawRecordList)){
		    for(WithdrawRecord withdrawRecord:withdrawRecordList) {
		        withdrawRecordMapper.insert(withdrawRecord);
		    }
	    }
		return 0;
	}

	@Override
	public Integer deleteWithdrawRecord(String id) {
		return withdrawRecordMapper.deleteById(id);
	}
	
	@Override
	public Integer deleteWithdrawRecord(WithdrawRecord withdrawRecord){
		return withdrawRecordMapper.deleteById(withdrawRecord.getId());//deleteSelective(withdrawRecord);
	}
	
	@Override
	public Integer deleteWithdrawRecordBatch(List<String> idList) {
		if(CollectionUtils.isNotEmpty(idList)){
	        withdrawRecordMapper.deleteBatchIds(idList);
	    }
		return 0;
	}
	
	@Override
	public Integer countWithdrawRecord(WithdrawRecord withdrawRecord) {
	    EntityWrapper<WithdrawRecord> entityWrapper = new EntityWrapper<WithdrawRecord>();
        entityWrapper.setEntity(withdrawRecord);
		return withdrawRecordMapper.selectCount(entityWrapper);
	}
 	
}