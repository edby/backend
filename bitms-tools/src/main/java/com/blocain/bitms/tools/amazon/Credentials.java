package com.blocain.bitms.tools.amazon;

/**
 * Credentials Introduce
 * <p>File：Credentials.java</p>
 * <p>Title: Credentials</p>
 * <p>Description: Credentials</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public interface Credentials
{
    /**
     * Returns the access key ID for this credentials.
     */
    String getAccessKeyId();
    
    /**
     * Returns the secret access key for this credentials.
     */
    String getSecretAccessKey();
    
    /**
     * Returns the security token for this credentials.
     */
    String getSecurityToken();
    
    /**
     * Determines whether to use security token for http requests.
     */
    boolean useSecurityToken();
}
