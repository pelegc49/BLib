package server;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.InstanceNotFoundException;

import logic.Activity;
import logic.BookCopy;
import logic.BookTitle;
import logic.Borrow;
import logic.Message;
import logic.Subscriber;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;
import java.util.Random;

/**
 * This class represents the server of the BLib application. It extends the
 * AbstractServer from the ocsf library and manages client connections and
 * messages from clients.
 */
public class BLibServer extends AbstractServer {

	// Singleton instance of BLibServer
	public static BLibServer instance = null;

	// A map storing connected clients and their respective information (IP address
	// and host name)
	private static Map<ConnectionToClient, String[]> connectedClients = new HashMap<>();

	/**
	 * Private constructor that initializes the server and begins listening on the
	 * provided port.
	 * 
	 * @param port the port the server listens on for incoming client connections
	 * @throws IOException if there is an error when starting the server
	 */
	private BLibServer(int port) throws IOException {
		super(port); // Call the superclass constructor to initialize the server
		listen(); // Start listening for client connections
		ServerTimer.start(this);
	}

	/**
	 * Retrieves the singleton instance of the BLibServer. If the instance does not
	 * exist, it is created.
	 * 
	 * @param port the port the server listens on
	 * @return the instance of the BLibServer
	 * @throws IOException if there is an error when starting the server
	 */
	public static BLibServer getInstance(int port) throws IOException {
		if (instance instanceof BLibServer) {
			return instance; // Return existing instance if it exists
		}
		// Create and return a new instance if one does not exist
		instance = new BLibServer(port);
		return instance;
	}

	public static BLibServer getInstance() throws InstanceNotFoundException {
		if (instance instanceof BLibServer) {
			return instance; // Return existing instance if it exists
		}
		// Create and return a new instance if one does not exist
		throw new InstanceNotFoundException();
	}

	/**
	 * Connects to the database using the provided password.
	 * 
	 * @param password the password for the database connection
	 * @return true if the connection is successful, false if it fails
	 */
	public static boolean connect(String password) {
		return BLibDBC.getInstance().connect(password); // Call the connect method from the BLibDBC class
	}

	/**
	 * Called when a new client connects to the server. Adds the client's connection
	 * details (IP and host name) to the `connectedClients` map.
	 * 
	 * @param client the connection object for the newly connected client
	 */
	@Override
	protected void clientConnected(ConnectionToClient client) {
		// Store the client connection details in the connectedClients map
		connectedClients.put(client,
				new String[] { client.getInetAddress().getHostAddress(), client.getInetAddress().getHostName() });
	}

	/**
	 * Handles any exceptions that occur during communication with a client. It
	 * closes the client connection if an exception occurs.
	 * 
	 * @param client    the client whose connection caused the exception
	 * @param exception the exception that was thrown
	 */
	@Override
	protected synchronized void clientException(ConnectionToClient client, Throwable exception) {
		try {
			client.close(); // Close the client connection in case of an exception
		} catch (IOException e) {
			e.printStackTrace(); // Log the exception stack trace
		}
	}

	/**
	 * Returns a map of all connected clients with their respective details (IP and
	 * host name).
	 * 
	 * @return a map of connected clients
	 */
	public Map<ConnectionToClient, String[]> getConnectedClients() {
		return connectedClients;
	}

	/**
	 * Handles messages sent by clients. It processes different types of messages
	 * 
	 * @param msg    the message sent by the client
	 * @param client the connection object for the client who sent the message
	 */
	@SuppressWarnings("unchecked") // the return type is guaranteed for each method
	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		System.out.println("receive message:" + msg); // Log the received message
		LocalDate today = LocalDate.now();
		if (msg instanceof Message) { // Check if the received message is an instance of the Message class
			List<Object> args = ((Message) msg).getArguments(); // Retrieve arguments from the message
			try {
				Object ret; // Variable to store the result of database operations
				// Switch-case to handle different types of commands
				switch (((Message) msg).getCommand()) {

				// Handle login request
				case "login":
					ret = BLibDBC.getInstance().login((Integer) args.get(0), (String) args.get(1)); // Perform login
																									// check in database
					System.out.println(ret); // Log the result
					if (ret != null) { // If login is successful
						client.sendToClient(new Message("loginSuccess", (String) ret)); // Send success message with
																						// user role
					} else {
						client.sendToClient(new Message("loginFail")); // Send failure message if login fails
					}
					break;

				// Handle getSubscriber request
				case "getSubscriber":
					ret = BLibDBC.getInstance().getSubscriberByID((Integer) args.get(0)); // Fetch subscriber details
																							// from the
					// database
					if (ret != null) { // If subscriber found
						client.sendToClient(new Message("subscriberFound", (Subscriber) ret)); // Send subscriber
																								// details to client
					} else {
						client.sendToClient(new Message("subscriberNotFound")); // Send failure message if subscriber
																				// not found
					}
					break;

				// Handle updateSubscriber request
				case "updateSubscriber":
					ret = BLibDBC.getInstance().updateSubscriber((Subscriber) args.get(0), (String) args.get(1)); // Update
																													// subscriber
																													// details
					// in the database
					// database
					if (((Boolean) ret) == true) { // If update is successful
						client.sendToClient(new Message("subscriberUpdated")); // Send success message
					} else {
						client.sendToClient(new Message("subscriberFailedUpdated")); // Send failure message if update
																						// fails
					}
					break;
				// Handle search for book titles by keyword
				case "getTitlesByKeyword":
					ret = BLibDBC.getInstance().getTitlesByKeyword((String) args.get(0));// Search for book titles by
																							// keyword
					if (ret != null) { // If search is successful
						client.sendToClient(new Message("searchResult", (Set<BookTitle>) ret)); // Send search results
					} else {
						client.sendToClient(new Message("searchFailed")); // Send failure message if no titles found
					}
					break;

				// Handle search for book copies by title
				case "getCopiesByTitle":
					ret = BLibDBC.getInstance().getCopiesByTitle((BookTitle) args.get(0)); // Search for book copies by
																							// title
					if (ret != null) { // If search is successful
						client.sendToClient(new Message("searchResult", (Set<BookCopy>) ret)); // Send search results
					} else {
						client.sendToClient(new Message("searchFailed")); // Send failure message if no copies found
					}
					break;

				// Handle registerSubscriber request
				case "registerSubscriber":
					String pass = generatePassword(4);
					ret = BLibDBC.getInstance().registerSubscriber((Subscriber) args.get(0), pass); // Register a new
					// subscriber
					if ((Boolean) ret == true) {
						client.sendToClient(new Message("success", pass)); // Send success message if registration is
																			// successful
					} else {
						client.sendToClient(new Message("failed")); // Send failure message if registration fails
					}
					break;

				// Handle creating a new borrow request
				case "createBorrow":
					ret = BLibDBC.getInstance().createBorrow((Integer) args.get(0), (Integer) args.get(1)); // Create a
																											// borrow
					if ((Boolean) ret == true) {
						client.sendToClient(new Message("success")); // Send success message
					} else {
						client.sendToClient(new Message("failed")); // Send failure message if borrow creation fails
					}
					break;

				// Handle fetching active borrow for a book copy
				case "getCopyActiveBorrow":
					ret = BLibDBC.getInstance().getCopyActiveBorrow((BookCopy) args.get(0)); // Retrieve active borrow
																								// for a book copy
					if (ret != null) {
						client.sendToClient(new Message("borrowFound", (Borrow) ret)); // Send success message
					} else {
						client.sendToClient(new Message("borrowNotFound")); // Send failure message if borrow creation
																			// fails
					}
					break;

				// Handle extend borrow duration request
				case "extend":
					if (BLibDBC.getInstance()
							.getTitleNumOfAllowedExtend(((Borrow) args.get(0)).getBook().getTitle()) < 0) {
						client.sendToClient(new Message("failed")); // If no extension allowed, send failure message
						break;
					}

					if (args.get(2).equals("subscriber")) {
						if (!canExtend((Borrow) args.get(0))) { // Check if the borrow can be extended
							client.sendToClient(new Message("failed"));
							break;
						}
					}
					ret = BLibDBC.getInstance().extendDuration((Borrow) args.get(0), (Integer) args.get(1),
							(String) args.get(2)); // Extend the borrow duration
					if ((Boolean) ret == true) {
						client.sendToClient(new Message("success")); // Send success message
					} else {
						client.sendToClient(new Message("failed")); // Send failure message
					}
					// TODO: add message logic

					break;

				// Handle return book request
				case "return":
					Borrow borrow = BLibDBC.getInstance().getCopyActiveBorrow((BookCopy) args.get(0)); // Retrieve
																										// active borrow
																										// for book copy
					if (borrow == null) {
						client.sendToClient(new Message("failed")); // Send failure message if borrow creation fails
						break;
					}
					// If the due date is passed, process as late return and potentially freeze the
					// subscriber
					if (borrow.getDueDate().compareTo(today) < 0) {
						BLibDBC.getInstance().returnBook((BookCopy) args.get(0), true);
						if (borrow.getDueDate().plusWeeks(1).compareTo(today) <= 0) {
							BLibDBC.getInstance().freezeSubscriber(borrow.getSubscriber().getId());
							client.sendToClient(new Message("successFreeze")); // Send freeze success message
						} else {
							client.sendToClient(new Message("successLate")); // Send late return success message
						}
					} else {
						BLibDBC.getInstance().returnBook((BookCopy) args.get(0), false);
						client.sendToClient(new Message("success")); // Send success message for regular return
					}
					break;

				case "history":
					ret = BLibDBC.getInstance().getSubscriberHistory((Integer) args.get(0));
					if (ret != null) {
						client.sendToClient(new Message("historyRetrieved", (List<Activity>) ret));
					} else {
						client.sendToClient(new Message("Failed")); // Send failure message
					}
					break;
				// case "getTitleByID":
//					ret = BLibDBC.getInstance().getTitleByID((String) args.get(0));
//					if (ret != null) { 
//						client.sendToClient(new Message("searchResult",(Set<BookTitle>)ret)); // Send success message
//					} else {
//						client.sendToClient(new Message("searchFailed")); 
//					}
//					break;
				}
			} catch (IOException e) {
				e.printStackTrace(); // Log any exceptions that occur during message handling
			}

		}

	}

	public void execute(Message msg) {
		List<Object> args = ((Message) msg).getArguments();
		switch (msg.getCommand()) {
		case "unfreeze":

			break;
		case "sendEmail":
			//TODO: create and call sendEmail()
			break;
		}
	}

	/**
	 * Checks if a borrow can be extended based on the borrower's status and the
	 * borrow date.
	 * 
	 * @param borrow the borrow object to check
	 * @return true if the borrow can be extended, false otherwise
	 */
	public static boolean canExtend(Borrow borrow) {
		// Check if the subscriber's status is "frozen". If yes, they can't extend the
		// borrow period.
		if (borrow.getSubscriber().getStatus().equals("frozen")) {
			return false;
		}

		// Check if the borrow period is less than or equal to one week. If the borrow
		// was made within the last week,
		// it cannot be extended.
		if (borrow.getDateOfBorrow().plusWeeks(1).compareTo(LocalDate.now()) >= 0) {
			return false;
		}
		// If none of the conditions above are met, the borrow can be extended.
		return true;
	}

	private String generatePassword(int length) {
		StringBuilder str = new StringBuilder();
		Random rand = new Random();
		for (int i = 0; i < length; i++) {
			str.append(rand.nextInt(10));
		}
		return str.toString();
	}

	public List<Message> getCommands() {
		return BLibDBC.getInstance().getCommands();
	}
}