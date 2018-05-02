package com.blocain.bitms.trade.account.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 策略对象
 * <p>
 *     通常情况下，只需要传入默认验证码即可;
 *     只有在GA和短信同时需要验证时才需要传入ga和sms
 * </p>
 * <p>File：StrategyModel.java</p>
 * <p>Title: StrategyModel</p>
 * <p>Description: StrategyModel</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "策略对象")
public class PolicyModel implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    /**
     * 登陆密码
     */
    @ApiModelProperty(value = "登陆密码")
    private String            pwd;
    
    /**
     * GOOGLE验证码
     */
    @ApiModelProperty(value = "GOOGLE验证码")
    private String            ga;
    
    /**
     * 短信验证码
     */
    @ApiModelProperty(value = "短信验证码")
    private String            sms;
    
    public String getPwd()
    {
        return pwd;
    }
    
    public void setPwd(String pwd)
    {
        this.pwd = pwd;
    }
    
    public String getGa()
    {
        return ga;
    }
    
    public void setGa(String ga)
    {
        this.ga = ga;
    }
    
    public String getSms()
    {
        return sms;
    }
    
    public void setSms(String sms)
    {
        this.sms = sms;
    }
}
