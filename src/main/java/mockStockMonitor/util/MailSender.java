package mockStockMonitor.util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * 发送给邮件的工具类
 */
public class MailSender {
    /**
     * 邮件参数设置
     */
    private Properties props;

    /**
     * 发送人
     */
    private String senderUser;

    /**
     * 发送人密码
     */
    private String senderPass;

    /**
     * 接受者
     */
    private String receivers;

    /**
     * 邮件主题
     */
    private String subject;

    /**
     * 邮件内容
     */
    private String content;

    public void setProps(Properties props) {
        this.props = props;
    }

    public void setSenderUser(String senderUser) {
        this.senderUser = senderUser;
    }

    public void setSenderPass(String senderPass) {
        this.senderPass = senderPass;
    }

    public void setReceivers(String receivers) {
        this.receivers = receivers;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 发送邮件
     */
    public void send() {
        Session session = Session.getInstance(props,
            new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(senderUser, senderPass);
                }
            });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderUser));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receivers));
            message.setSubject(subject);
            message.setText(content);
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}