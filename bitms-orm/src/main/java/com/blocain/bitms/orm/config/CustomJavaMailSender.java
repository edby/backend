package com.blocain.bitms.orm.config;

import com.blocain.bitms.orm.utils.EncryptUtils;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 *  自定义邮件发送实例
 * <p>File： CustomJavaMailSender.java </p>
 * <p>Title:  CustomJavaMailSender </p>
 * <p>Description: CustomJavaMailSender </p>
 * <p>Copyright: Copyright (c) 2017/8/2 </p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class CustomJavaMailSender extends JavaMailSenderImpl
{
    @Override
    public void setPassword(String password)
    {
        super.setPassword(EncryptUtils.desDecrypt(password));
    }
}
