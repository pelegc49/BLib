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
	
	private Session session;
	private static MessageController instance;
	
	public static MessageController getInstance() {
		if (!(instance instanceof MessageController)) {
			instance = new MessageController(); 
		}
		return instance;
	}
	
	private MessageController() {
		initialize();
	}
	
	private void initialize() {
		Properties props = new Properties();
    	props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
    	props.put("mail.smtp.port", "587"); //TLS Port
    	props.put("mail.smtp.auth", "true"); //enable authentication
    	props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
    	Authenticator auth = new Authenticator() {
    		//override the getPasswordAuthentication method
    		protected PasswordAuthentication getPasswordAuthentication() {
    			return new PasswordAuthentication("libraryblib@gmail.com", "jtzm oybh unum qptr");
    		}
    	};
    	session = Session.getInstance(props, auth);
	}


	public static void main(String[] args) {
		String[] mails = {"pelegc49@gmail.com","hitrifox@gmail.com","Gal.moyal111@gmail.com","Edenfur3253@gmail.com","Lidor.ben.david@e.braude.ac.il"};
		getInstance().sendEmail("HALLO\nHALLO\nHALLO\nHALLO\nHALLO\n", "America YA", mails);
	}
	

	public void sendEmail(String text, String subject, String[] emailTo) {
		System.out.println("sending email to " + emailTo);

        try {
            MimeMessage msg = new MimeMessage(session);
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");
            
			msg.setSubject(subject, "UTF-8");
			
			msg.setText(text, "UTF-8");
			
			msg.setSentDate(new Date());
			for(String s : emailTo)
				msg.addRecipient(Message.RecipientType.TO, new InternetAddress(s, false));
			Transport.send(msg);
			System.out.println("email sent");
        } catch (MessagingException mex) {
            System.out.println("send failed, exception: " + mex);
            mex.printStackTrace();
        }
		
		

	}

	public void sendSMS(String msg, String phoneTo) {
		System.out.println("sending SMS to " + phoneTo);

		System.out.println("SMS sent");
	}
}