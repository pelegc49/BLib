package server;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MessageController {
//	public static void sendEmail(Session session, String toEmail, String subject, String body) {
//		try {
//			Message msg = new MimeMessage(session);
//			// set message headers
//			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
//			msg.addHeader("format", "flowed");
//			msg.addHeader("Content-Transfer-Encoding", "8bit");
//
//			msg.setFrom("pelegc49@gmail.com");
////	      msg.setFrom(new InternetAddress("no_reply@example.com", "NoReply-JD"));
//
////	      msg.setReplyTo(InternetAddress.parse("no_reply@example.com", false));
//
//			msg.setSubject(subject, "UTF-8");
//
//			msg.setText(body, "UTF-8");
//
//			msg.setSentDate(new Date());
//
//			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
//			System.out.println("Message is ready");
//			Transport.send(msg)
//
//			System.out.println("EMail Sent Successfully!!");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public static void main(String[] args) {
////	Properties props = System.getProperties();
////	props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
////	props.put("mail.smtp.port", "587"); //TLS Port
////	props.put("mail.smtp.auth", "true"); //enable authentication
////	props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
////    Authenticator auth = new Authenticator() {
////		//override the getPasswordAuthentication method
////		protected PasswordAuthentication getPasswordAuthentication() {
////			return new PasswordAuthentication("pelegc49@gmail.com", "uylk piuq abih gtsg");
////		}
////	};
////    Session session = Session.getInstance(props, auth);
////
////    
////    sendEmail(session, "gal.moyal111@gmail.com", "America Ya", "halo halo");
//		System.out.println("SimpleEmail Start");
//
//		String smtpHostServer = "smtp.gmail.com";
//		String emailID = "gal.moyal111@gmail.com";
//
//		Properties props = System.getProperties();
//
//		props.put("mail.smtp.host", smtpHostServer);
//
//		Session session = Session.getInstance(props, null);
//
//		sendEmail(session, emailID, "SimpleEmail Testing Subject", "SimpleEmail Testing Body");
//	}
	
	public void sendEmail(String msg,String subject,String emailTo) {
		System.out.println("sending email to "+emailTo);
		
		//TODO: implement
		
		System.out.println("email sent");
	}
	public void sendSMS(String msg,String phoneTo) {
		System.out.println("sending SMS to "+phoneTo);
		
		System.out.println("SMS sent");
	}

}
