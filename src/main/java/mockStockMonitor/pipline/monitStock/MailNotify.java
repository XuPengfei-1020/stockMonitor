package mockStockMonitor.pipline.monitStock;

import mockStockMonitor.util.MailSender;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 邮件通知助手
 */
public class MailNotify {
    /**
     * outlook 账户名称
     */
    private String outlookName;

    /**
     * outlook 账户密码
     */
    private String outlookPass;

    public MailNotify(String outlookName, String outlookPass) {
        this.outlookName = outlookName;
        this.outlookPass = outlookPass;
    }

    /**
     * 日志内容
     */
    private Logger logger = Logger.getLogger(MailNotify.class.getName());

    /**
     * 发送邮件，使用 outlook
     * @return
     */
    public void sendMail(final String receiver, final String subject, final String content) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // 最多尝试发送三次
                for (int i = 0; i < 3; i++) {
                    try {
                        if(sendMail0(receiver, subject, content)) {
                            break;
                        }
                    } catch (Exception ex) {
                        logger.log(Level.INFO, ex.getMessage(), ex);
                    }
                }
            }
        });
        thread.setName("Mail-Notify-Thread");
        thread.start();
    }

    /**
     * 发送邮件
     * @param receiver
     * @param subject
     * @param content
     * @return
     */
    private boolean sendMail0(String receiver, String subject, String content) {
        MailSender mailSender = new MailSender();
        mailSender.setSenderUser(outlookName);
        mailSender.setSenderPass(outlookPass);
        mailSender.setSubject(subject);
        mailSender.setContent(content);
        mailSender.setReceivers(receiver);
        Properties mailProps = new Properties();
        mailProps.setProperty("mail.smtp.auth", "true");
        mailProps.setProperty("mail.transport.protocol", "smtp");
        mailProps.setProperty("mail.smtp.host", "smtp.office365.com");
        mailProps.put("mail.smtp.starttls.enable", "true");
        mailSender.setProps(mailProps);

        try {
            mailSender.send();
        } catch (Exception e) {
            logger.log(Level.INFO, e.getMessage(), e);
            return false;
        }

        return true;
    }
}