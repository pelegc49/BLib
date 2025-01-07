package server;

public class MessageController {

	public void sendEmail(String msg, String subject, String emailTo) {
		System.out.println("sending email to " + emailTo);

		// TODO: implement

		System.out.println("email sent");
	}

	public void sendSMS(String msg, String phoneTo) {
		System.out.println("sending SMS to " + phoneTo);

		System.out.println("SMS sent");
	}
}