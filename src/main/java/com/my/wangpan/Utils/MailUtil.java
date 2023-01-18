package com.my.wangpan.Utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Slf4j
public class MailUtil {
    //邮件发送类
    private JavaMailSenderImpl mailSender;

    public MailUtil(JavaMailSenderImpl javaMailSender){
        mailSender=javaMailSender;
    }

    /**
     * 发送简单邮件（不支持html格式）
     * @param title
     * @param msg
     * @param acceptEmail
     */
    public void sendSimpleEmail(String title,String msg,String acceptEmail){
        log.info("开始发送简单邮件...");
        log.info("mailSender对象为："+mailSender);
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject(title);//设置标题
        simpleMailMessage.setText(msg);//设置内容
        simpleMailMessage.setFrom("2445988632@qq.com");//设置发送方邮箱
        simpleMailMessage.setTo(acceptEmail);//设置接收方邮箱
        System.out.println(mailSender);
        log.info("message对象为："+simpleMailMessage);
        mailSender.send(simpleMailMessage);
    }

    /**
     * 发送复杂邮件，支持html格式
     * @param title
     * @param msg
     * @param acceptEmail
     */
    public void sendComplexEmail(String title,String msg,String acceptEmail){
        log.info("开始发送复杂邮件...");
        log.info("mailSender对象为："+mailSender);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setSubject(title);//设置标题
            helper.setText(msg);//设置内容
            helper.setFrom("2445988632@qq.com");//设置发送方邮箱
            helper.setTo(acceptEmail);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        log.info("mimeMessage对象为:"+message);
        mailSender.send(message);
    }

    /**
     * 发送验证邮件，支持html格式
     * @param acceptEmail
     */
    public String sendCodeEmail(String acceptEmail,String username,String password){
//        生成验证码
        StringBuilder code = new StringBuilder();
        for(int i = 0;i < 6;i++){
            code.append(new Random().nextInt(10));
        }
        log.info("开始发送验证邮件...");
        log.info("mailSender对象为："+mailSender);
        log.info("验证码为："+code);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setSubject("邮箱验证");//设置标题
            helper.setText("<h2 >网盘-简洁、优雅、免费</h2>" +
                    "<h3>用户注册-邮箱验证<h3/>" +
                    "您现在正在注册网盘账号<br>" +
                    "验证码: <span style='color : red'>"+code+"</span><br>" +
                    "用户名 :"+username+
                    "<br>密码 :"+password+
                    "<hr>"+
                    "<h5 style='color : red'>如果并非本人操作,请忽略本邮件</h5>",true);//设置内容
            helper.setFrom("2445988632@qq.com");//设置发送方邮箱
            helper.setTo(acceptEmail);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        log.info("mimeMessage对象为:"+message);
        mailSender.send(message);
        return code.toString();
    }
}
