package com.blocain.bitms.bitpay.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.blocain.bitms.bitpay.common.Pagination;
import com.blocain.bitms.bitpay.entity.BitpayKeychain;
import com.blocain.bitms.bitpay.mapper.BitpayKeychainMapper;
import com.blocain.bitms.bitpay.service.BitpayKeychainService;
import com.blocain.bitms.tools.utils.SerialnoUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * <p>File: BitpayKeychainServiceImpl.java</p>
 * <p>Title: </p>
 * <p>Description:bitpay</p>
 * <p>Copyright: Copyright (c) 2017-07-18 11:05</p>
 * <p>Company: jmwenhua.cn</p>
 * @author 施建波
 * @version 1.0
 */
@Service("bitpayKeychainService")
public class BitpayKeychainServiceImpl implements BitpayKeychainService {

	@Resource
	private BitpayKeychainMapper bitpayKeychainMapper;
	
	@Override
	public PageInfo<BitpayKeychain> findBitpayKeychainPage(BitpayKeychain bitpayKeychain, Pagination pagination) {
		PageHelper.startPage(pagination.getPageNo(), pagination.getPageSize()); 
		String orderByField = " id desc";
		if(null != bitpayKeychain && StringUtils.isNotBlank(bitpayKeychain.getOrderByField())){
		    bitpayKeychain.setOrderByField(orderByField);
		}
		List<BitpayKeychain> list = this.findBitpayKeychainSearch(bitpayKeychain);
		return new PageInfo<BitpayKeychain>(list); 
	}
	
	@Override
	public List<BitpayKeychain> findBitpayKeychainSearch(BitpayKeychain bitpayKeychain) {
		if(null == bitpayKeychain){
			bitpayKeychain = new BitpayKeychain();
		}
		EntityWrapper<BitpayKeychain> entityWrapper = new  EntityWrapper<BitpayKeychain> (bitpayKeychain);
		entityWrapper.setSqlSelect(bitpayKeychain.getEntityColumns());
		if(StringUtils.isNotBlank(bitpayKeychain.getOrderByField())){
			entityWrapper.orderBy(bitpayKeychain.getOrderByField());
		}
		return bitpayKeychainMapper.selectList(entityWrapper);
	}
	
	@Override
	public BitpayKeychain findBitpayKeychain(Long id) {
		return bitpayKeychainMapper.selectById(id);
	}
	
	@Override
	public BitpayKeychain findBitpayKeychain(BitpayKeychain bitpayKeychain) {
		return bitpayKeychainMapper.selectOne(bitpayKeychain);
	}

	@Override
	public Integer saveBitpayKeychain(BitpayKeychain bitpayKeychain) {
		if(bitpayKeychain.getId() != null){
			return bitpayKeychainMapper.updateById(bitpayKeychain); 
		}else{
			bitpayKeychain.setCreateDate(new Date().getTime());
			bitpayKeychain.setId(SerialnoUtils.buildPrimaryKey());
			System.out.println(bitpayKeychain.getId());
			return this.insertBitpayKeychain(bitpayKeychain);
		}
	}
	
	@Override
	public Integer insertBitpayKeychain(BitpayKeychain bitpayKeychain) {
	    bitpayKeychain.setId(SerialnoUtils.buildPrimaryKey());
	    System.out.println(bitpayKeychain.getId());
		return bitpayKeychainMapper.insert(bitpayKeychain);
	}
	
	@Override
	public Integer updateBitpayKeychain(BitpayKeychain bitpayKeychain) {
		return bitpayKeychainMapper.updateById(bitpayKeychain);
	}
	
	@Override
	public Integer updateBitpayKeychainBatch(List<BitpayKeychain> bitpayKeychainList) {
		if(CollectionUtils.isNotEmpty(bitpayKeychainList)){
		    for(BitpayKeychain bitpayKeychain:bitpayKeychainList) {
		        bitpayKeychainMapper.updateById(bitpayKeychain);
		    }
	    }
		return 0;
	}
	
	@Override
	public Integer insertBitpayKeychainBatch(List<BitpayKeychain> bitpayKeychainList) {
		if(CollectionUtils.isNotEmpty(bitpayKeychainList)){
		    for(BitpayKeychain bitpayKeychain:bitpayKeychainList) {
		        bitpayKeychain.setId(SerialnoUtils.buildPrimaryKey());
		        System.out.println(bitpayKeychain.getId());
		        bitpayKeychainMapper.insert(bitpayKeychain);
		    }
	    }
		return 0;
	}

	@Override
	public Integer deleteBitpayKeychain(Long id) {
		return bitpayKeychainMapper.deleteById(id);
	}
	
	@Override
	public Integer deleteBitpayKeychain(BitpayKeychain bitpayKeychain){
		return bitpayKeychainMapper.deleteById(bitpayKeychain.getId());
	}
	
	@Override
	public Integer deleteBitpayKeychainBatch(List<Long> idList) {
		if(CollectionUtils.isNotEmpty(idList)){
	        bitpayKeychainMapper.deleteBatchIds(idList);
	    }
		return 0;
	}
	
	@Override
	public Integer countBitpayKeychain(BitpayKeychain bitpayKeychain) {
	    EntityWrapper<BitpayKeychain> entityWrapper = new EntityWrapper<BitpayKeychain>();
	    entityWrapper.setEntity(bitpayKeychain);
		return bitpayKeychainMapper.selectCount(entityWrapper);
	}
 	
}