package com.blocain.bitms.bitpay.controller;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.blocain.bitms.bitpay.common.ApplicationConst;
import com.blocain.bitms.bitpay.common.JsonMessage;
import com.blocain.bitms.bitpay.entity.Member;
import com.blocain.bitms.bitpay.service.MemberService;
import com.blocain.bitms.tools.exception.BusinessException;

/**
 * <p>File：CommonController.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2017年7月21日 上午11:16:09</p>
 * <p>Company: BloCain</p>
 * @author 施建波
 * @version 1.0
 */
@Controller
@RequestMapping("/common")
public class CommonController extends BaseController
{
    public static final Logger logger = LoggerFactory.getLogger(CommonController.class);

    @Resource
    private MemberService memberService;
    
    /**
     * 登陆页面
     * @return
     * @author 施建波  2017年7月21日 上午11:05:05
     */
    @RequestMapping(value = "/login")
    public String index(){
        return "common/login";
    }
    
    /**
     * 登陆
     * @param username  用户名
     * @param password  密码
     * @param request
     * @return
     * @author 施建波  2017年7月21日 上午11:12:04
     * @throws BusinessException 
     */
    @RequestMapping(value = "/login/submit.ajax")
    @ResponseBody
    public JsonMessage submit(String username, String password, HttpServletRequest request) throws BusinessException{
        if(StringUtils.isBlank(username)) {
            throw new BusinessException("用户名不能为空");
        }
        if(StringUtils.isBlank(password)) {
            throw new BusinessException("密码不能为空");
        }
        Member member = new Member();
        member.setUserName(username);
        member = memberService.findMember(member);
        if(null == member) {
            throw new BusinessException("用户不存在");
        }
        if(!DigestUtils.md5Hex(password).equals(member.getPassWord())) {
            throw new BusinessException("密码错误");
        }
        super.setSession(request, ApplicationConst.SESSION_MEMBER_KEY, member);
        return this.getJsonMessage(ApplicationConst.ERROR_CODE_SUCCESS); 
    }
    
    /**
     * 退出
     * @param request
     * @return
     * @author 施建波  2017年7月21日 上午11:15:52
     */
    @RequestMapping(value = "/logout")
    public String logout(HttpServletRequest request){
        super.clearSession(request);
        return "redirect:/admin";
    }
}
