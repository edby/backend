package com.blocain.bitms.bitpay.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * <p>File：BitpayKeychain.java</p>
 * <p>Title: </p>
 * <p>Description:bitpay</p>
 * <p>Copyright: Copyright (c) 2017-07-18 11:05</p>
 * <p>Company: jmwenhua.cn</p>
 * @author 施建波
 * @version 1.0
 */
public class BitpayKeychain extends BaseEntity
{
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.INPUT)
    private Long              id;
    
    /**钱包ID*/
    private String            walletId;
    
    /**钱包名称*/
    private String            walletName;
    
    /**token*/
    private String            token;
    
    /**加密后的私钥*/
    private String            xprv;
    
    /**公钥*/
    private String            xpub;
    
    /**系统密码*/
    private String            systemPass;
    
    /**钱包密码加密后的密文*/
    private String            ciphertext;
    
    /**证券信息ID*/
    private String            stockinfoId;
    
    /**按每千字节计费，目标为设定数量区块的交易确认。默认：2， 最小：2，最大：20.*/
    private Integer           feeTxConfirmTarget;
    
    /**钱包类型：1、收款 2、付款*/
    private Integer           type;
    
    /**创建时间*/
    private Long              createDate;
    
    /**
     *方法: 取得Long
     *@return: Long  id
     */
    public Long getId()
    {
        return this.id;
    }
    
    /**
     *方法: 设置Long
     *@param: Long  id
     */
    public void setId(Long id)
    {
        this.id = id;
    }
    
    public String getWalletId()
    {
        return walletId;
    }
    
    public void setWalletId(String walletId)
    {
        this.walletId = walletId;
    }
    
    /**
     *方法: 取得String
     *@return: String  token
     */
    public String getToken()
    {
        return this.token;
    }
    
    /**
     *方法: 设置String
     *@param: String  token
     */
    public void setToken(String token)
    {
        this.token = token;
    }
    
    /**
     *方法: 取得String
     *@return: String  xprv
     */
    public String getXprv()
    {
        return this.xprv;
    }
    
    /**
     *方法: 设置String
     *@param: String  xprv
     */
    public void setXprv(String xprv)
    {
        this.xprv = xprv;
    }
    
    /**
     *方法: 取得String
     *@return: String  xpub
     */
    public String getXpub()
    {
        return this.xpub;
    }
    
    /**
     *方法: 设置String
     *@param: String  xpub
     */
    public void setXpub(String xpub)
    {
        this.xpub = xpub;
    }
    
    /**
     *方法: 取得String
     *@return: String  systemPass
     */
    public String getSystemPass()
    {
        return this.systemPass;
    }
    
    /**
     *方法: 设置String
     *@param: String  systemPass
     */
    public void setSystemPass(String systemPass)
    {
        this.systemPass = systemPass;
    }
    
    public String getCiphertext()
    {
        return ciphertext;
    }
    
    public void setCiphertext(String ciphertext)
    {
        this.ciphertext = ciphertext;
    }
    
    /**
     *方法: 取得String
     *@return: String  stockinfoId
     */
    public String getStockinfoId()
    {
        return this.stockinfoId;
    }
    
    /**
     *方法: 设置String
     *@param: String  stockinfoId
     */
    public void setStockinfoId(String stockinfoId)
    {
        this.stockinfoId = stockinfoId;
    }
    
    /**
     *方法: 取得Date
     *@return: Date  createDate
     */
    public Long getCreateDate()
    {
        return this.createDate;
    }
    
    /**
     *方法: 设置Date
     *@param: Date  createDate
     */
    public void setCreateDate(Long createDate)
    {
        this.createDate = createDate;
    }
    
    public String getWalletName()
    {
        return walletName;
    }
    
    public void setWalletName(String walletName)
    {
        this.walletName = walletName;
    }
    
    public Integer getFeeTxConfirmTarget()
    {
        return feeTxConfirmTarget;
    }
    
    public void setFeeTxConfirmTarget(Integer feeTxConfirmTarget)
    {
        this.feeTxConfirmTarget = feeTxConfirmTarget;
    }
    
    public Integer getType()
    {
        return type;
    }
    
    public void setType(Integer type)
    {
        this.type = type;
    }
}
