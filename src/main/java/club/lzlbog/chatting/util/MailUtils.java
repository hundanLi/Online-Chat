package club.lzlbog.chatting.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

/**
 * @author li
 * @version 1.0
 * @date 2019-03-31 13:15
 **/
@Component
public class MailUtils {

    private JavaMailSender javaMailSender;

    @Autowired
    public void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    private static final String SITE_MAIL_ADDRESS = "zulongli@aliyun.com";
    private static final String SUBJECT = "“无聊”实时聊天系统";
    private static final int MAX_EXPIRE_TIME = 60 * 5;
    public static final String LAST_SEND = "last-send";
    public boolean sendEmail(String emailTo, String emailKey, String sendFor, HttpSession session) {
        //生成验证码
        String code = CodeGenerator.generateCode();
        //生成邮件
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject(SUBJECT);
        mailMessage.setFrom(SITE_MAIL_ADDRESS);
        mailMessage.setTo(emailTo);
        mailMessage.setText("你的邮箱验证码为：\n" + code + "\n" + "5分钟内有效！");

        //发送邮件
        try {
            javaMailSender.send(mailMessage);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        //将验证码保存到回话中
        session.setAttribute(sendFor, code);
        session.setAttribute(emailKey, emailTo);
        //设置有效期为五分钟
        session.setMaxInactiveInterval(MAX_EXPIRE_TIME);
        session.setAttribute(LAST_SEND,System.currentTimeMillis());
        return true;
    }
}
