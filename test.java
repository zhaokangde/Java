package test;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class test {
 public static void main(String []arg) throws MessagingException{
	 Properties properties = new Properties();
	 properties.setProperty("mail.host", "smtp.qq.com");
	 properties.setProperty("mail.transport.protocol", "smtp");
	 properties.setProperty("mail.smtp.auth", "true");
	 properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	 properties.setProperty("mail.smtp.port", "465");
	 Session session = Session.getDefaultInstance(properties);
	 session.setDebug(true);
	 
	 MimeMessage mimeMessage = new MimeMessage(session);
	 mimeMessage.addRecipients(Message.RecipientType.TO, "917420671@qq.com");//设置收信人
	 mimeMessage.addRecipients(Message.RecipientType.CC, "917420671@qq.com");//抄送
	 mimeMessage.setFrom(new InternetAddress("932247030@qq.com"));//邮件发送人
	 mimeMessage.setSubject("测试邮件主题");//邮件主题
	 mimeMessage.setContent("Hello,这是一封测试邮件", "text/html;charset=utf-8");//正文
	   
	 Transport transport = session.getTransport();
	 transport.connect("smtp.qq.com", "932247030@qq.com", "buljwubvithybdjb");
	 transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());//发送邮件，第二个参数为收件人
	 transport.close();
 }
}