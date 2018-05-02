/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.account.notice.controller;

import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.annotation.CSRFToken;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.consts.DateConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.CalendarUtils;
import com.blocain.bitms.trade.account.entity.Notice;
import com.blocain.bitms.trade.account.service.NoticeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 消息 公告管理
 * <p>File：NoticeController.java</p>
 * <p>Title: NoticeController</p>
 * <p>Description:NoticeController</p>
 * <p>Copyright: Copyright (c) 2017年7月4日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.ACCOUNT)
public class NoticeController extends GenericController
{
    @Autowired(required = false)
    private NoticeService noticeService;
    
    /**
     * 列表页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/notice")
    @RequiresPermissions("trade:setting:notice:index")
    public String list() throws BusinessException
    {
        return "trade/account/notice/list";
    }
    
    /**
     * 通知 公告 保存
     * @param notice
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws com.blocain.bitms.tools.exception.BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/notice/save", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:notice:operator")
    public JsonMessage save(Notice notice, String publicDate) throws BusinessException
    {
        Long pdate = CalendarUtils.getLongFromTime(publicDate, DateConst.DATE_FORMAT_YMDHMS);
        notice.setPublicDate(pdate);
        JsonMessage json = getJsonMessage(CommonEnums.SUCCESS);
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == notice.getId())
        {
            notice.setCreateBy(principal.getId());
            notice.setCreateDate(CalendarUtils.getCurrentLong());
        }
        if (notice.getStatus()==null)
        {
            notice.setStatus(false);
        }
        notice.setUpdateBy(principal.getId());
        notice.setUpdateDate(CalendarUtils.getCurrentLong());
        if (beanValidator(json, notice))
        {
            noticeService.save(notice);
        }
        return json;
    }
    
    /**
     * 添加或修改 通知公告
     * @param id
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/notice/modify")
    @RequiresPermissions("trade:setting:notice:operator")
    public ModelAndView modify(Long id) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("trade/account/notice/modify");
        Notice notice = new Notice();
        if (null != id)
        {
            notice = noticeService.selectByPrimaryKey(id);
        }
        mav.addObject("notice", notice);
        return mav;
    }
    
    /**
     * 查询  通知公告
     * @param entity
     * @param pagin
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws com.blocain.bitms.tools.exception.BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/notice/data", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:notice:data")
    public JsonMessage data(Notice entity, Pagination pagin) throws BusinessException
    {
        PaginateResult<Notice> result = noticeService.search(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
    
    /**
     * 根据指定ID删除
     * @param ids
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws com.blocain.bitms.tools.exception.BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/notice/del", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:notice:operator")
    public JsonMessage del(String ids) throws BusinessException
    {
        noticeService.deleteBatch(ids.split(","));
        return getJsonMessage(CommonEnums.SUCCESS);
    }
    
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/notice/updatestatus", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:notice:operator")
    public JsonMessage updateStatusById(Long id, Integer status) throws BusinessException
    {
        Notice notice = new Notice();
        notice.setId(id);
        if(status.equals(1)){
            notice.setStatus(true);
        }else{
            notice.setStatus(false);
        }
        noticeService.updateByPrimaryKeySelective(notice);
        return getJsonMessage(CommonEnums.SUCCESS);
    }
}
