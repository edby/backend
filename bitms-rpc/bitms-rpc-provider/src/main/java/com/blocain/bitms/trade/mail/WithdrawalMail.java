package com.blocain.bitms.trade.mail;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.activation.DataSource;
import javax.activation.FileTypeMap;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.Resource;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import com.blocain.bitms.tools.utils.PropertiesUtils;

/**
 * 提现邮件发送服务
 * <p>File：WithdrawalMail.java </p>
 * <p>Title: WithdrawalMail </p>
 * <p>Description:WithdrawalMail </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public class WithdrawalMail
{
    private static final boolean        IS_CN_FILENAME = false;

    private static final Log            loger          = LogFactory.getLog(WithdrawalMail.class);

    public static final PropertiesUtils loader         = new PropertiesUtils("email.properties");

    private JavaMailSender              sender;

    private MimeMessage                 message;

    // 附件是否为中文名称
    private boolean                     cnFileName     = IS_CN_FILENAME;

    /**
     * 使用示例一（简单邮件）：
     *      sendMail.initialize();
     String[] to={"lzj@mail.com","xxx@mail.com"};
     sendMail.setTo(to);
     //sendMail.setCc(cc)
     sendMail.setSubject("邮件发送测试！");
     sendMail.setText("一二三四五六七八九十！");
     sendMail.send();
     * 使用示例二（Html邮件）：
     sendMail.initialize();
     String[] to={"lzj@mail.com","xxx@mail.com"};
     sendMail.setTo(to);
     sendMail.setSubject("邮件发送测试！");
     sendMail.setText("text/html","<font color='red'>一二三四五六七八九十！</font>");
     sendMail.send();
     * 使用示例三（非中文附件邮件，不改变文件名）
     sendMail.initialize();
     String[] to={"lzj@mail.com"};
     sendMail.setTo(to);
     sendMail.setSubject("邮件发送测试！");
     sendMail.setText("text/html","<font color='red'>一二三四五六七八九十！</font>");
     File file=new File("d:\\系统功能改善.txt");
     sendMail.addAttachment(file);
     sendMail.send();
     * 使用示例四（中文附件邮件，不改变文件名）
     sendMail.initialize();
     String[] to={"lzj@mail.com"};
     sendMail.setTo(to);
     sendMail.setSubject("邮件发送测试！");
     sendMail.setCnFileName(true);
     sendMail.setText("text/html","<font color='red'>一二三四五六七八九十！</font>");
     File file=new File("d:\\系统功能改善.txt");
     sendMail.addAttachment(file);
     sendMail.send();
     * 使用示例五（中文附件邮件，改变文件名）
     sendMail.initialize();
     String[] to={"lzj@mail.com"};
     sendMail.setTo(to);
     sendMail.setSubject("邮件发送测试！");
     sendMail.setCnFileName(true);
     sendMail.setText("text/html","<font color='red'>一二三四五六七八九十！</font>");
     File file=new File("d:\\系统功能改善.txt");
     sendMail.addAttachment("文件名.txt", file)
     sendMail.send();
     */
    public WithdrawalMail()
    {
        super();
    }
    
    /**
     * 邮件发送前的初始化设置，主要是创建MimeMessage，设置编码及发件人 以下参数需要修改成放在cache中，并可以在系统后台进行修改
     */
    public void initialize()
    {
        if (null == sender) sender = new JavaMailSenderImpl();
        message = sender.createMimeMessage();
        InternetAddress from;
        try
        {
            helper = new MimeMessageHelper(message, true, "UTF-8");
            from = new InternetAddress(loader.getProperty("mailserver.username.prop"), loader.getProperty("mailserver.personal.prop"), "UTF-8");
            helper.setFrom(from);
        }
        catch (MessagingException e)
        {
            loger.error(e);
        }
        catch (UnsupportedEncodingException e)
        {
            loger.error(e);
        }
    }
    
    public JavaMailSender getSender()
    {
        return sender;
    }
    
    public void setSender(JavaMailSender sender)
    {
        this.sender = sender;
    }
    
    private MimeMessageHelper helper;
    
    public MimeMessageHelper getHelper()
    {
        return helper;
    }
    
    public void setHelper(MimeMessageHelper helper)
    {
        this.helper = helper;
    }
    
    public MimeMessage getMessage()
    {
        return message;
    }
    
    public void setMessage(MimeMessage message)
    {
        this.message = message;
    }
    
    public boolean getCnFileName()
    {
        return cnFileName;
    }
    
    public void setCnFileName(boolean cnFileName)
    {
        this.cnFileName = cnFileName;
    }
    
    public boolean send()
    {
        try
        {
            sender.send(message);
            return true;
        }
        catch (MailSendException ex)
        {
            loger.error(ex);
            return false;
        }
        catch (MailException ex)
        {
            loger.error(ex);
            return false;
        }
    }
    
    public void execute()
    {
        try
        {
            sender.send(message);
        }
        catch (MailSendException ex)
        {
            loger.error(ex);
        }
        catch (MailException ex)
        {
            loger.error(ex);
        }
    }
    
    /**
     * 根据当前类对附件名是否为中文的设置取得处理后的文件名（主要是对中文的支持）
     * @param fileName 处理后的文件名
     * @return Stirng 处理后的文件名（主要是对中文的支持）
     */
    private String getFileName(String fileName)
    {
        if (this.cnFileName) try
        {
            fileName = MimeUtility.encodeWord(fileName);
        }
        catch (UnsupportedEncodingException e)
        {
            loger.error(e);
        }
        return fileName;
    }
    
    /**
     * 不改变附件名称发送邮件附件
     * @param file 文件对象
     * @throws MessagingException 抛出的消息异常
     *             调用之前如果附件为中文名，则需要在调用前setCnFileName(true);
     */
    public void addAttachment(File file) throws MessagingException
    {
        String attachmentFilename = file.getName();
        attachmentFilename = this.getFileName(attachmentFilename);// 增加对中文附件的处理
        this.addAttachment(attachmentFilename, file);
    }
    
    public void addAttachment(String attachmentFilename, DataSource dataSource) throws MessagingException
    {
        attachmentFilename = this.getFileName(attachmentFilename);// 增加对中文附件的处理
        helper.addAttachment(attachmentFilename, dataSource);
    }
    
    /**
     * 改变附件名称发送邮件附件
     * @param attachmentFilename 自定义的文件名称
     * @param file 文件对象
     * @throws MessagingException 抛出的消息异常
     */
    public void addAttachment(String attachmentFilename, File file) throws MessagingException
    {
        attachmentFilename = this.getFileName(attachmentFilename);// 增加对中文附件的处理
        helper.addAttachment(attachmentFilename, file);
    }
    
    public void addAttachment(String attachmentFilename, InputStreamSource inputStreamSource, String contentType) throws MessagingException
    {
        attachmentFilename = this.getFileName(attachmentFilename);// 增加对中文附件的处理
        helper.addAttachment(attachmentFilename, inputStreamSource, contentType);
    }
    
    public void addAttachment(String attachmentFilename, InputStreamSource inputStreamSource) throws MessagingException
    {
        attachmentFilename = this.getFileName(attachmentFilename);// 增加对中文附件的处理
        helper.addAttachment(attachmentFilename, inputStreamSource);
    }
    
    public void addBcc(InternetAddress bcc) throws MessagingException
    {
        helper.addBcc(bcc);
    }
    
    public void addBcc(String bcc, String personal) throws MessagingException, UnsupportedEncodingException
    {
        helper.addBcc(bcc, personal);
    }
    
    public void addBcc(String bcc) throws MessagingException
    {
        helper.addBcc(bcc);
    }
    
    public void addCc(InternetAddress cc) throws MessagingException
    {
        helper.addCc(cc);
    }
    
    public void addCc(String cc, String personal) throws MessagingException, UnsupportedEncodingException
    {
        helper.addCc(cc, personal);
    }
    
    public void addCc(String cc) throws MessagingException
    {
        helper.addCc(cc);
    }
    
    public void addInline(String contentId, DataSource dataSource) throws MessagingException
    {
        helper.addInline(contentId, dataSource);
    }
    
    public void addInline(String contentId, File file) throws MessagingException
    {
        helper.addInline(contentId, file);
    }
    
    public void addInline(String contentId, InputStreamSource inputStreamSource, String contentType) throws MessagingException
    {
        helper.addInline(contentId, inputStreamSource, contentType);
    }
    
    public void addInline(String contentId, Resource resource) throws MessagingException
    {
        helper.addInline(contentId, resource);
    }
    
    public void addTo(InternetAddress to) throws MessagingException
    {
        helper.addTo(to);
    }
    
    public void addTo(String to, String personal) throws MessagingException, UnsupportedEncodingException
    {
        helper.addTo(to, personal);
    }
    
    public void addTo(String to) throws MessagingException
    {
        helper.addTo(to);
    }
    
    @Override
    public boolean equals(Object obj)
    {
        return helper.equals(obj);
    }
    
    public String getEncoding()
    {
        return helper.getEncoding();
    }
    
    public FileTypeMap getFileTypeMap()
    {
        return helper.getFileTypeMap();
    }
    
    public final MimeMessage getMimeMessage()
    {
        return helper.getMimeMessage();
    }
    
    public final MimeMultipart getMimeMultipart() throws IllegalStateException
    {
        return helper.getMimeMultipart();
    }
    
    public final MimeMultipart getRootMimeMultipart() throws IllegalStateException
    {
        return helper.getRootMimeMultipart();
    }
    
    @Override
    public int hashCode()
    {
        // 避免空指针异常
        return helper == null ? 0 : helper.hashCode();
    }
    
    public final boolean isMultipart()
    {
        return helper.isMultipart();
    }
    
    public boolean isValidateAddresses()
    {
        return helper.isValidateAddresses();
    }
    
    public void setBcc(InternetAddress bcc) throws MessagingException
    {
        helper.setBcc(bcc);
    }
    
    public void setBcc(InternetAddress[] bcc) throws MessagingException
    {
        helper.setBcc(bcc);
    }
    
    public void setBcc(String bcc) throws MessagingException
    {
        helper.setBcc(bcc);
    }
    
    public void setBcc(String[] arg0) throws MessagingException
    {
        helper.setBcc(arg0);
    }
    
    public void setCc(InternetAddress cc) throws MessagingException
    {
        helper.setCc(cc);
    }
    
    public void setCc(InternetAddress[] cc) throws MessagingException
    {
        helper.setCc(cc);
    }
    
    public void setCc(String cc) throws MessagingException
    {
        helper.setCc(cc);
    }
    
    public void setCc(String[] arg0) throws MessagingException
    {
        helper.setCc(arg0);
    }
    
    public void setFileTypeMap(FileTypeMap fileTypeMap)
    {
        helper.setFileTypeMap(fileTypeMap);
    }
    
    public void setFrom(InternetAddress from) throws MessagingException
    {
        helper.setFrom(from);
    }
    
    public void setFrom(String from, String personal) throws MessagingException, UnsupportedEncodingException
    {
        helper.setFrom(from, personal);
    }
    
    public void setFrom(String from) throws MessagingException
    {
        helper.setFrom(from);
    }
    
    public void setPriority(int priority) throws MessagingException
    {
        helper.setPriority(priority);
    }
    
    public void setReplyTo(InternetAddress replyTo) throws MessagingException
    {
        helper.setReplyTo(replyTo);
    }
    
    public void setReplyTo(String replyTo, String personal) throws MessagingException, UnsupportedEncodingException
    {
        helper.setReplyTo(replyTo, personal);
    }
    
    public void setReplyTo(String replyTo) throws MessagingException
    {
        helper.setReplyTo(replyTo);
    }
    
    public void setSentDate(Date sentDate) throws MessagingException
    {
        helper.setSentDate(sentDate);
    }
    
    public void setSubject(String subject) throws MessagingException
    {
        helper.setSubject(subject);
    }
    
    public void setText(String text, boolean html) throws MessagingException
    {
        helper.setText(text, html);
    }
    
    public void setText(String plainText, String htmlText) throws MessagingException
    {
        helper.setText(plainText, htmlText);
    }
    
    public void setText(String text) throws MessagingException
    {
        helper.setText(text);
    }
    
    public void setTo(InternetAddress to) throws MessagingException
    {
        helper.setTo(to);
    }
    
    public void setTo(InternetAddress[] to) throws MessagingException
    {
        helper.setTo(to);
    }
    
    public void setTo(String to) throws MessagingException
    {
        helper.setTo(to);
    }
    
    public void setTo(String[] arg0) throws MessagingException
    {
        helper.setTo(arg0);
    }
    
    public void setValidateAddresses(boolean validateAddresses)
    {
        helper.setValidateAddresses(validateAddresses);
    }
    
    @Override
    public String toString()
    {
        return helper.toString();
    }
    
    public MimeMessage createMimeMessage()
    {
        return sender.createMimeMessage();
    }
    
    public MimeMessage createMimeMessage(InputStream arg0) throws MailException
    {
        return sender.createMimeMessage(arg0);
    }
    
    public void send(MimeMessage arg0) throws MailException
    {
        sender.send(arg0);
    }
    
    public void send(MimeMessage[] arg0) throws MailException
    {
        sender.send(arg0);
    }
    
    public void send(MimeMessagePreparator arg0) throws MailException
    {
        sender.send(arg0);
    }
    
    public void send(MimeMessagePreparator[] arg0) throws MailException
    {
        sender.send(arg0);
    }
    
    public void send(SimpleMailMessage arg0) throws MailException
    {
        sender.send(arg0);
    }
    
    public void send(SimpleMailMessage[] arg0) throws MailException
    {
        sender.send(arg0);
    }
}
