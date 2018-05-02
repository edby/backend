package com.blocain.bitms.boss.common.model;

import java.io.Serializable;

/**
 * 短信发送对象 Introduce
 * <p>File：SMSModel.java</p>
 * <p>Title: SMSModel</p>
 * <p>Description: SMSModel</p>
 * <p>Copyright: Copyright (c) 2017/7/5</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class SMSModel implements Serializable
{
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -7722032931791156854L;
    
    /**
     * 用户账号,50位以内
     */
    private String  account;
    
    /**
     * 用户密码，联系客服获取
     */
    private String  password;
    
    /**
     * 短信内容。长度不能超过536个字符
     */
    private String  msg;
    
    /**
     * 手机号码，格式(区号+手机号码) 例如：8615800000000，
     * 其中86为中国的区号，区号前不使用00开头,
     * 15800000000为接收短信的真实手机号码。
     */
    private String  mobile;
    
    /**
     *手机号码。多个手机号码使用英文逗号分隔，必填
     */
    private String  phone;
    
    /**
     *是否需要状态报告（默认false）
     */
    private Boolean report;
    
    public String getAccount()
    {
        return account;
    }
    
    public void setAccount(String account)
    {
        this.account = account;
    }
    
    public String getPassword()
    {
        return password;
    }
    
    public void setPassword(String password)
    {
        this.password = password;
    }
    
    public String getMsg()
    {
        return msg;
    }
    
    public void setMsg(String msg)
    {
        this.msg = msg;
    }
    
    public String getMobile()
    {
        return mobile;
    }
    
    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }
    
    public String getPhone()
    {
        return phone;
    }
    
    public void setPhone(String phone)
    {
        this.phone = phone;
    }
    
    public Boolean getReport()
    {
        return report;
    }
    
    public void setReport(Boolean report)
    {
        this.report = report;
    }
}
