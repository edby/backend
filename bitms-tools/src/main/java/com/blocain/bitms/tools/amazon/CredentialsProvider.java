package com.blocain.bitms.tools.amazon;

/**
 * CredentialsProvider Introduce
 * <p>File：CredentialsProvider.java</p>
 * <p>Title: CredentialsProvider</p>
 * <p>Description: CredentialsProvider</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public interface CredentialsProvider
{
    void setCredentials(Credentials creds);
    
    Credentials getCredentials();
}
