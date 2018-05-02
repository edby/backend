package com.blocain.bitms.apps.basic.utils;

import com.alibaba.fastjson.JSON;
import com.blocain.bitms.apps.account.beans.SessionInfo;
import com.blocain.bitms.orm.utils.EncryptUtils;
import com.blocain.bitms.tools.utils.StringUtils;

/**
 * AuthTokenUtils Introduce
 * <p>Title: AuthTokenUtils</p>
 * <p>File：AuthTokenUtils.java</p>
 * <p>Description: AuthTokenUtils</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class AuthTokenUtils
{
    /**
     * 将auth_token转为SESSION对象
     * @param authToken
     * @return
     */
    public static SessionInfo getSession(String authToken)
    {
        if (StringUtils.isBlank(authToken)) return null;
        String session = EncryptUtils.desDecrypt(authToken);
        return JSON.parseObject(session, SessionInfo.class);
    }
}
