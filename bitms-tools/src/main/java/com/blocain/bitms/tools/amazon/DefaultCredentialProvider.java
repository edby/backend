package com.blocain.bitms.tools.amazon;


import com.blocain.bitms.tools.exception.BusinessException;

/**
 * DefaultCredentialProvider Introduce
 * <p>File：DefaultCredentialProvider.java</p>
 * <p>Title: DefaultCredentialProvider</p>
 * <p>Description: DefaultCredentialProvider</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class DefaultCredentialProvider implements CredentialsProvider
{
    private volatile Credentials creds;
    
    public DefaultCredentialProvider(Credentials creds)
    {
        setCredentials(creds);
    }
    
    public DefaultCredentialProvider(String accessKeyId, String secretAccessKey)
    {
        this(accessKeyId, secretAccessKey, null);
    }
    
    public DefaultCredentialProvider(String accessKeyId, String secretAccessKey, String securityToken)
    {
        checkCredentials(accessKeyId, secretAccessKey);
        setCredentials(new DefaultCredentials(accessKeyId, secretAccessKey, securityToken));
    }
    
    @Override
    public synchronized void setCredentials(Credentials creds)
    {
        if (creds == null) { throw new BusinessException("creds should not be null."); }
        checkCredentials(creds.getAccessKeyId(), creds.getSecretAccessKey());
        this.creds = creds;
    }
    
    @Override
    public Credentials getCredentials()
    {
        if (this.creds == null) { throw new BusinessException("Invalid credentials"); }
        return this.creds;
    }
    
    private static void checkCredentials(String accessKeyId, String secretAccessKey)
    {
        if (accessKeyId == null || accessKeyId.equals("")) { throw new BusinessException("Access key id should not be null or empty."); }
        if (secretAccessKey == null || secretAccessKey.equals("")) { throw new BusinessException("Secret access key should not be null or empty."); }
    }
}