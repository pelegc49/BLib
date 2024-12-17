package client;

import java.awt.print.Book;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Flow.Subscriber;

import common.Activity;
import logic.Borrow;
import logic.Message;
import ocsf.client.AbstractClient;

public class BLibClient extends AbstractClient {

	ClientGUI clientUI;
	public static Message msg;

	public static boolean awaitResponse = false;

	public BLibClient(String host, int port) throws IOException {
		super(host, port);
		//this.clientUI = clientUI;
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		System.out.println("--> handleMessageFromServer");
		awaitResponse = false;
		this.msg = (Message) msg;
	}

	/**
	 * This method handles all data coming from the UI
	 *
	 * @param message The message from the UI.
	 */

	public void handleMessageFromClientUI(Object message) {
		try {
			openConnection();// in order to send more than one message
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
			clientUI.display("Could not send message to server: Terminating client." + e);
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
	
	public static void main(String[] args) {
		BLibClient cl;
		try {
			cl = new BLibClient("localhost", 5555);
			cl.login("user", "pass");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void login(String userName, String password) {
		msg = new Message("login", userName, password);
		handleMessageFromClientUI(msg);
		System.out.println(msg);
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
		msg = new Message("updateSubscriber", updated);
		handleMessageFromClientUI(msg);
		return false;
	}
}
