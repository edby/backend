package com.blocain.bitms.tools.amazon;

import com.blocain.bitms.tools.exception.BusinessException;

/**
 * DefaultCredentials Introduce
 * <p>File：DefaultCredentials.java</p>
 * <p>Title: DefaultCredentials</p>
 * <p>Description: DefaultCredentials</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class DefaultCredentials implements Credentials
{
    private final String accessKeyId;
    
    private final String secretAccessKey;
    
    private final String securityToken;
    
    public DefaultCredentials(String accessKeyId, String secretAccessKey)
    {
        this(accessKeyId, secretAccessKey, null);
    }
    
    public DefaultCredentials(String accessKeyId, String secretAccessKey, String securityToken)
    {
        if (accessKeyId == null || accessKeyId.equals("")) { throw new BusinessException("Access key id should not be null or empty."); }
        if (secretAccessKey == null || accessKeyId.equals("")) { throw new BusinessException("Secret access key should not be null or empty."); }
        this.accessKeyId = accessKeyId;
        this.secretAccessKey = secretAccessKey;
        this.securityToken = securityToken;
    }
    
    @Override
    public String getAccessKeyId()
    {
        return accessKeyId;
    }
    
    @Override
    public String getSecretAccessKey()
    {
        return secretAccessKey;
    }
    
    @Override
    public String getSecurityToken()
    {
        return securityToken;
    }
    
    @Override
    public boolean useSecurityToken()
    {
        return this.securityToken != null;
    }
}
