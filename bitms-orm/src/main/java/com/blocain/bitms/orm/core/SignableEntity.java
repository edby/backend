package com.blocain.bitms.orm.core;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import com.alibaba.fastjson.annotation.JSONField;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import com.blocain.bitms.orm.utils.Cryptos;
import com.blocain.bitms.orm.utils.ExceptionUtils;

/**
 * <p>File：SignableEntity.java</p>
 * <p>Title: SignableEntity</p>
 * <p>Description:需要进行签名验证防止关键信息篡改的抽象实体.</p>
 * <p>Copyright: Copyright (c) 2015/04/21 11:17</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public abstract class SignableEntity extends GenericEntity implements SignableService
{
    private static final long serialVersionUID = 1L;
    
    @JSONField(serialize = false)
    protected String          sign;
    
    @JSONField(serialize = false)
    protected String          randomKey;
    
    public String getRandomKey()
    {
        return randomKey;
    }
    
    public void setRandomKey(String randomKey)
    {
        this.randomKey = randomKey;
    }
    
    public String getSign()
    {
        return sign;
    }
    
    public void setSign(String sign)
    {
        this.sign = sign;
    }
    
    @Override
    public byte[] doSign() throws UnsupportedEncodingException
    {
        byte[] bytes = Cryptos.hmacSha1(acquiresSignValue(), acquiresSignKey());
        this.sign = new String(Hex.encodeHex(bytes, true));
        return bytes;
    }
    
    @Override
    public boolean verifySignature()
    {
        try
        {
            byte[] expected = Hex.decodeHex(this.sign.toCharArray());
            return Cryptos.isMacValid(expected, acquiresSignValue(), acquiresSignKey());
        }
        catch (Exception e)
        {
            throw ExceptionUtils.unchecked(e);
        }
    }
    
    /**
     * 获取需要进行签名认证的值
     * 
     * @return
     */
    protected abstract byte[] acquiresSignValue() throws UnsupportedEncodingException;
    
    /**
     * 获取进行签名时所用到的key
     * 
     * @return
     */
    protected byte[] acquiresSignKey()
    {
        if (StringUtils.isEmpty(this.randomKey))
        {
            this.randomKey = RandomStringUtils.randomAlphabetic(6);
        }
        return randomKey.getBytes(StandardCharsets.UTF_8);
    }
}
