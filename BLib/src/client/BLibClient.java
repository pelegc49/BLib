package client;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import logic.Activity;
import logic.BookCopy;
import logic.BookTitle;
import logic.Borrow;
import logic.Message;
import logic.Subscriber;
import ocsf.client.AbstractClient;

public class BLibClient extends AbstractClient {

	// Static variables to store the message and await response status
	public static Message msg;
	public static boolean awaitResponse = false;

	// Constructor to initialize the client and open a connection
	public BLibClient(String host, int port) throws Exception {
		super(host, port);
		try {
			openConnection(); // Attempt to open a connection
		} catch (IOException exception) {
			throw new Exception(); // Handle connection errors
		}
	}

	// Override method to handle messages received from the server
	@Override
	protected void handleMessageFromServer(Object msg) {
		BLibClient.msg = (Message) msg; // Update static message variable
		awaitResponse = false; // Set response status to false
	}

	/**
	 * Handles messages sent from the client UI to the server
	 * 
	 * @param message The message from the UI.
	 */
	public void handleMessageFromClientUI(Object message) {
		try {
			awaitResponse = true; // Indicate that a response is awaited
			sendToServer(message); // Send the message to the server
			// Wait for a response in a loop
			while (awaitResponse) {
				try {
					Thread.sleep(100); // Avoid busy-waiting
				} catch (InterruptedException e) {
					e.printStackTrace(); // Handle interruptions
				}
			}
		} catch (IOException e) {
			e.printStackTrace(); // Handle errors during communication
			System.out.println("Could not send message to server: Terminating client." + e);
			quit(); // Terminate the client
		}
	}

	/**
	 * Terminates the client by closing the connection and exiting the program.
	 */
	public void quit() {
		try {
			closeConnection(); // Close the connection
		} catch (IOException e) {
		}
		System.exit(0); // Exit the program
	}

	/**
	 * Retrieves subscriber data by ID for the prototype.
	 * 
	 * @param id The subscriber's ID.
	 */
	public void getSubscriberData(int id) {
		msg = new Message("getSubscriberData", id); // Create a message
		handleMessageFromClientUI(msg); // Send the message to the server
		System.out.println(msg); // Print the message
	}

	/**
	 * Handles the login process for a user.
	 * 
	 * @param userName The user's username.
	 * @param password The user's password.
	 * @return True if login is successful, false otherwise.
	 */
	public String login(int userName, String password) {
		msg = new Message("login", userName, password); // Create login message
		handleMessageFromClientUI(msg); // Send to server
		if (msg.getCommand().equals("loginSuccess")) { // Check for success
			return (String)msg.getArguments().get(0);
		}
		return "fail"; // Login failed
	}

	// Searches for a book using a keyword
	public Set<BookTitle> getTitlesByKeyword(String keyword) {
		msg = new Message("getTitlesByKeyword", keyword); // Create search message
		handleMessageFromClientUI(msg); // Send to server
		if(msg.getCommand().equals("searchResult")){
			return (Set<BookTitle>) msg.getArguments().get(0);
		}
		return null;
	}
	
	public Set<BookCopy> getCopiesByTitle(BookTitle bt) {
		msg = new Message("getCopiesByTitle", bt); // Create search message
		handleMessageFromClientUI(msg); // Send to server
		if(msg.getCommand().equals("searchResult")){
			return (Set<BookCopy>) msg.getArguments().get(0);
		}
		return null;
	}
	
	public Borrow getCopyActiveBorrow(BookCopy bc) {
		msg = new Message("getCopyActiveBorrow", bc); // Create search message
		handleMessageFromClientUI(msg); // Send to server
		if(msg.getCommand().equals("borrowFound")){
			return (Borrow) msg.getArguments().get(0);
		}
		return null;
	}
	
	// Borrow a book for a subscriber
	public void borrowBook(BookCopy book, Subscriber sub) {
		msg = new Message("createBorrow", book, sub); // Create borrow message
		handleMessageFromClientUI(msg); // Send to server
	}

	// Extend the borrowing duration for a book
	public Message extendDuration(Borrow borrow, int days, String type) {
		msg = new Message("extend", borrow, days, type); // Create message
		handleMessageFromClientUI(msg); // Send to server
		return msg; // Return subscriber
	}

	// Order a book for a subscriber
	public void orderBook(BookCopy book, Subscriber sub) {
		msg = new Message("orderBook", book, sub); // Create order message
		handleMessageFromClientUI(msg); // Send to server
	}

	// Return a borrowed book
	public Message returnBook(BookCopy book) {
		msg = new Message("return", book); // Create return message
		handleMessageFromClientUI(msg); // Send to server
		return msg;
	}

	// Retrieve the subscriber's activity history
	public List<Activity> getSubscriberHistory(int subID) {
		msg = new Message("history", subID); // Create message
		handleMessageFromClientUI(msg); // Send to server
		if (msg.getCommand().equals("historyRetrieved")) // Check if found
			return (List<Activity>) msg.getArguments().get(0); // Return subscriber
		return null; // Placeholder return value
	}

	// Retrieve all subscriber reader cards
	public List<Subscriber> getSubscriberReaderCards() {
		msg = new Message("getSubscriberReaderCards"); // Create message
		handleMessageFromClientUI(msg); // Send to server
		return null; // Placeholder return value
	}

	// Update the due date for a borrow record
	public void updateBorrowDueDate(Borrow borrow, Date newDate) {
		msg = new Message("updateBorrowDueDate", borrow, newDate); // Create message
		handleMessageFromClientUI(msg); // Send to server
	}

	/**
	 * Updates subscriber information and checks if the update is successful.
	 * 
	 * @param updated The updated subscriber object.
	 * @return True if the subscriber is successfully updated, false otherwise.
	 */
	public boolean updateSubscriber(Subscriber updated) {
		msg = new Message("updateSubscriber", updated, "subscriber"); // Create update message
		handleMessageFromClientUI(msg); // Send to server
		if (msg.getCommand().equals("subscriberUpdated")) { // Check for success
			return true;
		}
		return false; // Update failed
	}

	/**
	 * Retrieves a subscriber by their ID.
	 * 
	 * @param id The subscriber's ID.
	 * @return The subscriber object if found, null otherwise.
	 */
	public Subscriber getSubscriber(int id) {
		msg = new Message("getSubscriber", id); // Create getSubscriber message
		handleMessageFromClientUI(msg); // Send to server
		if (msg.getCommand().equals("subscriberFound")) // Check if found
			return (Subscriber) msg.getArguments().get(0); // Return subscriber
		return null; // Subscriber not found
	}
	
	public Integer getTitleNumOfAllowedExtend(BookTitle bt) {
		msg = new Message("getTitleNumOfAllowedExtend", bt); 
		handleMessageFromClientUI(msg); 
		if (msg.getCommand().equals("allowedOrder")) 
			return (Integer) msg.getArguments().get(0); 
		return null; 
	}
	
	public Message orderTitle(Subscriber sub, BookTitle bt) {
		msg = new Message("order", sub, bt); 
		handleMessageFromClientUI(msg); 
		return msg;
	}
	
	public List<Borrow> getSubscriberBorrows(Subscriber subscriber) {
		msg = new Message("getSubscriberBorrows", subscriber); 
		handleMessageFromClientUI(msg); 
		if (msg.getCommand().equals("success")) 
			return (List<Borrow>) msg.getArguments().get(0); 
		return null; 
	}
	
	public List<Subscriber> getAllSubscribers() {
		msg = new Message("getAllSubscribers"); 
		handleMessageFromClientUI(msg); 
		if (msg.getCommand().equals("success")) 
			return (List<Subscriber>) msg.getArguments().get(0); 
		return null; 
	}
	
	public BookCopy getCopyByID(int id) {
		msg = new Message("getCopyByID", id); 
		handleMessageFromClientUI(msg); 
		if (msg.getCommand().equals("success")) 
			return (BookCopy) msg.getArguments().get(0); 
		return null; 
	}
}
