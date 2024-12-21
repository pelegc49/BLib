package client;

import logic.Activity;
import logic.Book;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import logic.Borrow;
import logic.Message;
import logic.Subscriber;
import ocsf.client.AbstractClient;

public class BLibClient extends AbstractClient {

	public static Message msg;
	public static boolean awaitResponse = false;

	public BLibClient(String host, int port) throws Exception {
		super(host, port);
		try {
			openConnection();
		} catch (IOException exception) {
			throw new Exception();
		}
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		BLibClient.msg = (Message) msg; // change to BLibClient.msg
		awaitResponse = false;
	}

	/**
	 * This method handles all data coming from the UI
	 *
	 * @param message The message from the UI.
	 */

	public void handleMessageFromClientUI(Object message) {
		try {
			//openConnection();// in order to send more than one message
			awaitResponse = true;
			sendToServer(message);
			// wait for response
			while (awaitResponse) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Could not send message to server: Terminating client." + e);
			quit();
		}
	}

	/**
	 * This method terminates the client.
	 */
	public void quit() {
		try {
			closeConnection();
		} catch (IOException e) {
		}
		System.exit(0);
	}
	
	public void getSubscriberData(int id) {
		// for the prototype
		/* expected: by id print the subscriber_id, subscriber_name, detailed_subscription_history,
		 * subscriber_phone_number, subscriber_email
		 */
		msg = new Message("getSubscriberData", id);
		handleMessageFromClientUI(msg);
		// after analyzing the message I want to print it.
		System.out.println(msg);
	}
	
	public boolean login(int userName, String password) {
		msg = new Message("login", userName, password);
		handleMessageFromClientUI(msg);
		System.out.println(msg);
		if(msg.getCommand().equals("loginSuccess")) {
			System.out.println(msg.getArguments());
			return true;
		}
		return false;
	}
	
	public void searchBook(String keyWord) {
		msg = new Message("searchBook", keyWord);
		handleMessageFromClientUI(msg);
	}

	public void borrowBook(Book book, Subscriber sub) {
		msg = new Message("borrowBook", book, sub);
		handleMessageFromClientUI(msg);
	}

	public void extendDuration(Borrow borrow, int days) {
		msg = new Message("extendDuration", borrow, days);
		handleMessageFromClientUI(msg);
	}

	public void orderBook(Book book, Subscriber sub) {
		msg = new Message("orderBook", book, sub);
		handleMessageFromClientUI(msg);
	}

	public void returnBook(Book book) {
		msg = new Message("returnBook", book);
		handleMessageFromClientUI(msg);
	}

	public List<Activity> getSubscriberHistory(Subscriber sub) {
		msg = new Message("getSubscriberHistory", sub);
		handleMessageFromClientUI(msg);
		return null;
	}

	public List<Subscriber> getSubscriberReaderCards() {
		msg = new Message("getSubscriberReaderCards");
		handleMessageFromClientUI(msg);
		return null;
	}

	public void updateBorrowDueDate(Borrow borrow, Date newDate) {
		msg = new Message("updateBorrowDueDate", borrow, newDate);
		handleMessageFromClientUI(msg);
	}

	public boolean updateSubscriber(Subscriber updated) {
		// for the prototype
		/* expect to send a subscriber and get a "Success" message.
		 * if so - return true, otherwise - return false.
		 */
		msg = new Message("updateSubscriber", updated);
		handleMessageFromClientUI(msg);
		if (msg.getCommand().equals("subscriberUpdated")) {
			return true;
		}
		System.out.println("notUpdated");
		return false;
	}
	
	public Subscriber getSubscriber(int id) {
		// I want to get the subscriber by his id.
		msg = new Message("getSubscriber", id);
		handleMessageFromClientUI(msg);
		if(msg.getCommand().equals("subscriberFound"))
			return (Subscriber)msg.getArguments().get(0);
		System.out.println("subscriberNotFound");
		return null;
	}
}
