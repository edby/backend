/*
 * @(#)MemberServiceImpl.java 2017年7月21日 上午9:39:38
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
import com.blocain.bitms.bitpay.entity.Member;
import com.blocain.bitms.bitpay.mapper.MemberMapper;
import com.blocain.bitms.bitpay.service.MemberService;
import com.blocain.bitms.tools.utils.SerialnoUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * <p>File：MemberServiceImpl.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2017年7月21日 上午9:39:38</p>
 * <p>Company: BloCain</p>
 * @author 施建波
 * @version 1.0
 */
@Service("memberService")
public class MemberServiceImpl implements MemberService
{
    @Resource
    private MemberMapper memberMapper;

    /* (non-Javadoc)
     * @see com.blocain.bitms.bitpay.service.MemberService#findMemberPage(com.blocain.bitms.bitpay.entity.Member, com.blocain.bitms.bitpay.common.Pagination)
     */
    @Override
    public PageInfo<Member> findMemberPage(Member member, Pagination pagination)
    {
        PageHelper.startPage(pagination.getPageNo(), pagination.getPageSize()); 
        String orderByField = " id desc";
        if(null != member && StringUtils.isNotBlank(member.getOrderByField())){
            member.setOrderByField(orderByField);
        }
        List<Member> list = this.findMemberSearch(member);
        return new PageInfo<Member>(list);
    }

    /* (non-Javadoc)
     * @see com.blocain.bitms.bitpay.service.MemberService#findMemberSearch(com.blocain.bitms.bitpay.entity.Member)
     */
    @Override
    public List<Member> findMemberSearch(Member member)
    {
        if(null == member){
            member = new Member();
        }
        EntityWrapper<Member> entityWrapper = new  EntityWrapper<Member> (member);
        entityWrapper.setSqlSelect(member.getEntityColumns());
        if(StringUtils.isNotBlank(member.getOrderByField())){
            entityWrapper.orderBy(member.getOrderByField());
        }
        return memberMapper.selectList(entityWrapper);
    }

    /* (non-Javadoc)
     * @see com.blocain.bitms.bitpay.service.MemberService#findMember(java.lang.Long)
     */
    @Override
    public Member findMember(Long id)
    {
        return memberMapper.selectById(id);
    }

    /* (non-Javadoc)
     * @see com.blocain.bitms.bitpay.service.MemberService#findMember(com.blocain.bitms.bitpay.entity.Member)
     */
    @Override
    public Member findMember(Member member)
    {
        return memberMapper.selectOne(member);
    }

    /* (non-Javadoc)
     * @see com.blocain.bitms.bitpay.service.MemberService#saveMember(com.blocain.bitms.bitpay.entity.Member)
     */
    @Override
    public Integer saveMember(Member member)
    {
        if(null != member.getId()){
            System.out.println(member.toString());
            return memberMapper.updateById(member); 
        }else{
            member.setCreateDate(new Date().getTime());
            member.setId(SerialnoUtils.buildPrimaryKey());
            return this.insertMember(member);
        }
    }

    /* (non-Javadoc)
     * @see com.blocain.bitms.bitpay.service.MemberService#insertMember(com.blocain.bitms.bitpay.entity.Member)
     */
    @Override
    public Integer insertMember(Member member)
    {
        member.setId(SerialnoUtils.buildPrimaryKey());
        return memberMapper.insert(member);
    }

    /* (non-Javadoc)
     * @see com.blocain.bitms.bitpay.service.MemberService#updateMember(com.blocain.bitms.bitpay.entity.Member)
     */
    @Override
    public Integer updateMember(Member member)
    {
        return memberMapper.updateById(member);
    }

    /* (non-Javadoc)
     * @see com.blocain.bitms.bitpay.service.MemberService#updateMemberBatch(java.util.List)
     */
    @Override
    public Integer updateMemberBatch(List<Member> memberList)
    {
        if(CollectionUtils.isNotEmpty(memberList)){
            for(Member member:memberList) {
                memberMapper.updateById(member);
            }
            
        }
        return 0;
    }

    /* (non-Javadoc)
     * @see com.blocain.bitms.bitpay.service.MemberService#insertMemberBatch(java.util.List)
     */
    @Override
    public Integer insertMemberBatch(List<Member> memberList)
    {
        if(CollectionUtils.isNotEmpty(memberList)){
            for(Member member:memberList) {
                member.setId(SerialnoUtils.buildPrimaryKey());
                memberMapper.insert(member);
            }
        }
        return 0;
    }

    /* (non-Javadoc)
     * @see com.blocain.bitms.bitpay.service.MemberService#deleteMember(java.lang.Long)
     */
    @Override
    public Integer deleteMember(Long id)
    {
        return memberMapper.deleteById(id);
    }

    /* (non-Javadoc)
     * @see com.blocain.bitms.bitpay.service.MemberService#deleteMember(com.blocain.bitms.bitpay.entity.Member)
     */
    @Override
    public Integer deleteMember(Member member)
    {
        return memberMapper.deleteById(member.getId());
    }

    /* (non-Javadoc)
     * @see com.blocain.bitms.bitpay.service.MemberService#deleteMemberBatch(java.util.List)
     */
    @Override
    public Integer deleteMemberBatch(List<Long> idList)
    {
        if(CollectionUtils.isNotEmpty(idList)){
            memberMapper.deleteBatchIds(idList);
        }
        return 0;
    }

    /* (non-Javadoc)
     * @see com.blocain.bitms.bitpay.service.MemberService#countMember(com.blocain.bitms.bitpay.entity.Member)
     */
    @Override
    public Integer countMember(Member member)
    {
        EntityWrapper<Member> entityWrapper = new EntityWrapper<Member>();
        entityWrapper.setEntity(member);
        return memberMapper.selectCount(entityWrapper);
    }
}
