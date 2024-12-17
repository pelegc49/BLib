package client;

import java.awt.print.Book;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Flow.Subscriber;

import common.BLibIF;
import logic.Message;
import ocsf.client.AbstractClient;

public class BLibClient extends AbstractClient {

	BLibIF clientUI;
	public static Message msg;

	public static boolean awaitResponse = false;

	public BLibClient(String host, int port, BLibIF clientUI)  throws IOException{
		super(host, port);
		this.clientUI = clientUI;
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
	
	public void searchBook(String keyWord) {
		Message msg = new Message("searchBook", keyWord);
		handleMessageFromClientUI(msg);
	}
	
	public void borrowBook(Book book, Subscriber sub){
		Message msg = new Message("borrowBook", book, sub);
		handleMessageFromClientUI(msg);
	}
	
	public void extendDuration(Borrow borrow, int days){
		Message msg = new Message("extendDuration", borrow, days);
		handleMessageFromClientUI(msg);
	}
	
	public void orderBook(Book book, Subscriber sub){
		Message msg = new Message("orderBook", book, sub);
		handleMessageFromClientUI(msg);
	}
	
	public void returnBook(Book book){
		Message msg = new Message("returnBook", book);
		handleMessageFromClientUI(msg);
	}
	
	public List<Activity> getSubscriberHistory(Subscriber sub){
		Message msg = new Message("getSubscriberHistory", sub);
		handleMessageFromClientUI(msg);
	}
	
	public List<Subscriber> getSubscriberReaderCards(){
		Message msg = new Message("getSubscriberReaderCards");
		handleMessageFromClientUI(msg);
	}
	
	public void updateBorrowDueDate(Borrow borrow, Date newDate){
		Message msg = new Message("updateBorrowDueDate", borrow, newDate);
		handleMessageFromClientUI(msg);
	}

	public boolean updateSubscriber(Subscriber updated) {
		Message msg = new Message("updateSubscriber", updated);
		handleMessageFromClientUI(msg);
	}
}
