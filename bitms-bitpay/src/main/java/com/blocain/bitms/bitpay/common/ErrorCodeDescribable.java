package com.blocain.bitms.bitpay.common;

/**
 * <p>File：ErrorCodeDescribable.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2017年7月18日 下午1:09:41</p>
 * <p>Company: BloCain</p>
 * @author 施建波
 * @version 1.0
 */
public interface ErrorCodeDescribable 
{
    /**
     * 获取异常代码
     * @return
     */
    Integer getCode();

    /**
     * 获取异常代码描述
     * @return
     */
    String getMessage();
}
