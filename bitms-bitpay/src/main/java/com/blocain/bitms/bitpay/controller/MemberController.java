/*
 * @(#)MemberController.java 2017年7月21日 上午10:02:28
 * Copyright 2017 施建波, Inc. All rights reserved. BloCain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.bitpay.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import com.blocain.bitms.bitpay.common.ApplicationConst;
import com.blocain.bitms.bitpay.common.JsonMessage;
import com.blocain.bitms.bitpay.entity.Member;
import com.blocain.bitms.bitpay.service.MemberService;
import com.blocain.bitms.tools.exception.BusinessException;

/**
 * <p>File：MemberController.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2017年7月21日 上午10:02:28</p>
 * <p>Company: BloCain</p>
 * @author 施建波
 * @version 1.0
 */
@Controller
@RequestMapping("/admin/member")
public class MemberController extends BaseController
{
    @Autowired
    private MemberService memberService;
    
    /**
     * 修改密码
     * @param oldPass   旧密码
     * @param newPass   新密码
     * @return
     * @author 施建波  2017年7月19日 下午4:49:19
     * @throws BusinessException 
     */
    @RequestMapping(value = "/pass/update.ajax")
    @ResponseBody
    public JsonMessage passUpdate(String oldPass, String newPass, HttpServletRequest request) throws BusinessException{
        if(StringUtils.isBlank(oldPass)) {
            throw new BusinessException("旧密码不能为空");
        }
        if(StringUtils.isBlank(newPass)) {
            throw new BusinessException("新密码不能为空");
        }
        Member sessionMember = super.getSession(request, ApplicationConst.SESSION_MEMBER_KEY);
        Member member = memberService.findMember(sessionMember.getId());
        oldPass = DigestUtils.md5Hex(oldPass);
        if(!oldPass.equals(member.getPassWord())) {
            throw new BusinessException("旧密码错误"); 
        }
        newPass = DigestUtils.md5Hex(newPass);
        member.setPassWord(newPass);
        memberService.saveMember(member);
        return super.getJsonMessage(ApplicationConst.ERROR_CODE_SUCCESS);
    }
}
