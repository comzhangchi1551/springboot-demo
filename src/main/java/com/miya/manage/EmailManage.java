package com.miya.manage;

import com.alibaba.excel.EasyExcel;
import com.google.common.collect.Lists;
import com.miya.entity.model.mysql.TempUser;
import com.sun.mail.util.MailSSLSocketFactory;
import lombok.SneakyThrows;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Auth: 张翅
 * Date: 2021/12/20
 * Desc:
 */
public class EmailManage {

    public static void main(String[] args) {
        sendEmail();
    }

    @SneakyThrows
    public static void sendEmail(){
        // 收件人电子邮箱
        String to = "351163675@qq.com";

        // 发件人电子邮箱
        String from = "smtp.163.com";

        // 指定发送邮件的主机为 smtp.qq.com
        String host = "smtp.163.com";  //QQ 邮件服务器

        // 获取系统属性
        Properties properties = System.getProperties();

        // 设置邮件服务器
        properties.setProperty("mail.smtp.host", host);

        properties.put("mail.smtp.auth", "true");
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.socketFactory", sf);
        // 获取默认session对象
        Session session = Session.getDefaultInstance(properties,new Authenticator(){
            public PasswordAuthentication getPasswordAuthentication()
            {     //qq邮箱服务器账户、第三方登录授权码
                return new PasswordAuthentication("351163675@qq.com", "iqazgycnpyycecbg"); //发件人邮件用户名、密码
            }
        });


        // 创建默认的 MimeMessage 对象
        MimeMessage message = new MimeMessage(session);

        // Set From: 头部头字段
        message.setFrom(new InternetAddress(from));

        // Set To: 头部头字段
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

        // Set Subject: 主题文字
        message.setSubject("家医康心电诊断结果");

        // 创建消息部分
        BodyPart messageBodyPart = new MimeBodyPart();

        // 消息
        messageBodyPart.setText("233333333333333");

        // 创建多重消息
        Multipart multipart = new MimeMultipart();

        // 设置文本消息部分
        multipart.addBodyPart(messageBodyPart);

        // 附件部分
        messageBodyPart = new MimeBodyPart();

        //设置要发送附件的文件路径
        DataSource source = new FileDataSource("测试文件.xlsx");

        EasyExcel.write(source.getOutputStream()).head(timeHead())
                .autoCloseStream(Boolean.FALSE).sheet()
                .doWrite(Lists.newArrayList(Lists.newArrayList("商品集嘿嘿嘿", "发布人嘿嘿嘿"), Lists.newArrayList("商品集嘿嘿嘿22", "发布人嘿嘿嘿22")));

        messageBodyPart.setDataHandler(new DataHandler(source));

        //messageBodyPart.setFileName(filename);
        //处理附件名称中文（附带文件路径）乱码问题
        messageBodyPart.setFileName(MimeUtility.encodeText("测试文件.xlsx"));
        multipart.addBodyPart(messageBodyPart);

        // 发送完整消息
        message.setContent(multipart );

        //   发送消息
        Transport.send(message);
        System.out.println("Sent message successfully....");

    }



    private static List<List<String>> timeHead() {
        List<List<String>> list = new ArrayList<List<String>>();

        list.add(Arrays.asList("商品集"));
        list.add(Arrays.asList("发布人"));

        return list;
    }
}
