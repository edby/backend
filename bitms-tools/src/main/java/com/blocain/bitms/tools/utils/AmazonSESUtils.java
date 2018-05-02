package com.blocain.bitms.tools.utils;

import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.blocain.bitms.tools.exception.BusinessException;

/**
 * AmazonSESUtils Introduce
 * <p>Title: AmazonSESUtils</p>
 * <p>File：AmazonSESUtils.java</p>
 * <p>Description: AmazonSESUtils</p>
 * <p>Copyright: Copyright (c) 2018/4/26</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class AmazonSESUtils
{
    public static final PropertiesUtils properties    = new PropertiesUtils("AmazonSES.properties");
    
    static final String                 FROM          = properties.getProperty("ses.fromuser");
    
    static final String                 FROMNAME      = properties.getProperty("ses.personal");
    
    static final String                 SMTP_USERNAME = properties.getProperty("ses.username");
    
    static final String                 SMTP_PASSWORD = properties.getProperty("ses.password");
    
    static final String                 HOST          = properties.getProperty("ses.host");
    
    static final int                    PORT          = properties.getInteger("ses.port");
    
    /**
     * 发送亚马逊邮件
     * @param subject 主题
     * @param body 内容
     * @param to 接收帐户
     * @param cc 抄送帐户
     * @throws BusinessException
     */
    public static void sendMail(String subject, String body, String to, String ... cc) throws BusinessException
    {
        Properties props = System.getProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.port", PORT);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        Session session = Session.getDefaultInstance(props);
        MimeMessage msg = new MimeMessage(session);
        Transport transport = null;
        try
        {
            msg.setFrom(new InternetAddress(FROM, FROMNAME));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            if (null != cc)
            {// 是否添加抄送
                Address[] addresses = new InternetAddress[cc.length];
                for (int i = 0; i < cc.length; i++)
                {
                    addresses[i] = new InternetAddress(cc[i]);
                }
                msg.setRecipients(Message.RecipientType.CC, addresses);
            }
            msg.setSubject(subject);
            msg.setContent(body, "text/html");
            msg.setHeader("X-SES-CONFIGURATION-SET", "ConfigSet");
            transport = session.getTransport();
            transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();
        }
        catch (Exception ex)
        {
            throw new BusinessException("The email was not sent." + ex.getLocalizedMessage());
        }
        finally
        {
            try
            {
                transport.close();
            }
            catch (MessagingException e)
            {
            }
        }
    }
    
    /**
     * 发送亚马逊邮件
     * @param subject 主题
     * @param body 内容
     * @param to 接收帐户
     * @param cc 抄送帐户
     * @throws BusinessException
     */
    public static void sendMail(String subject, String body, String to, InternetAddress[] cc) throws BusinessException
    {
        Properties props = System.getProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.port", PORT);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        Session session = Session.getDefaultInstance(props);
        MimeMessage msg = new MimeMessage(session);
        Transport transport = null;
        try
        {
            msg.setFrom(new InternetAddress(FROM, FROMNAME));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            if (null != cc)
            {// 是否添加抄送
                msg.setRecipients(Message.RecipientType.CC, cc);
            }
            msg.setSubject(subject);
            msg.setContent(body, "text/html");
            msg.setHeader("X-SES-CONFIGURATION-SET", "ConfigSet");
            transport = session.getTransport();
            transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();
        }
        catch (Exception ex)
        {
            throw new BusinessException("The email was not sent." + ex.getLocalizedMessage());
        }
        finally
        {
            try
            {
                transport.close();
            }
            catch (MessagingException e)
            {
            }
        }
    }
}
