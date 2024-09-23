package com.push.service.platform;

/**
 * Description: 邮件service接口
 * Create DateTime: 2020-04-16 09:59
 *
 * 

 */
public interface MailService {

    /**
     * 发送普通文本邮件
     *
     * @param to      收件方
     * @param subject 邮件主题
     * @param content 邮件内容
     */
    void sendSimpleMail(String to, String subject, String content);

    /**
     * 发送html页面邮件
     *
     * @param to      收件方
     * @param subject 邮件主题
     * @param content 邮件内容
     */
    void sendHtmlMail(String to, String subject, String content);

    /**
     * 发送带有附件的邮件
     *
     * @param to       收件方
     * @param subject  邮件主题
     * @param content  邮件内容
     * @param filePath 附件绝对路径
     */
    void sendAttachmentsMail(String to, String subject, String content, String filePath);

    /**
     * 发送正文中有静态资源（图片）的邮件
     *
     * @param to      收件方
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param rscPath 静态资源的绝对路径
     * @param rscId   rscId
     */
    void sendInlineResourceMail(String to, String subject, String content, String rscPath, String rscId);

}
