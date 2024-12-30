package server;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import logic.BookCopy;
import logic.BookTitle;
import logic.Borrow;
import logic.Message;
import logic.Subscriber;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

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

		if (msg instanceof Message) { // Check if the received message is an instance of the Message class
			List<Object> args = ((Message) msg).getArguments(); // Retrieve arguments from the message
			try {
				Object ret; // Variable to store the result of database operations
				// Switch-case to handle different types of commands
				switch (((Message) msg).getCommand()) {

				// Handle login request
				case "login":
					ret = BLibDBC.getInstance().login((Integer) args.get(0), (String) args.get(1)); // Perform login check in database
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
					ret = BLibDBC.getInstance().getSubscriberByID((Integer) args.get(0)); // Fetch subscriber details from the
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
					ret = BLibDBC.getInstance().updateSubscriber((Subscriber) args.get(0)); // Update subscriber details in the
																				// database
					if ((Boolean)ret == true) { // If update is successful
						client.sendToClient(new Message("subscriberUpdated")); // Send success message
					} else {
						client.sendToClient(new Message("subscriberFailedUpdated")); // Send failure message if update
																						// fails
					}
					break;
				// Handle search for book titles by keyword
				case "getTitlesByKeyword":
					ret = BLibDBC.getInstance().getTitlesByKeyword((String) args.get(0));
					if (ret != null) { // If search is successful
						client.sendToClient(new Message("searchResult",(Set<BookTitle>)ret)); // Send search results
					} else {
						client.sendToClient(new Message("searchFailed"));  // Send failure message if no titles found
					}
					break;
					
				// Handle search for book copies by title
				case "getCopiesByTitle":
					ret = BLibDBC.getInstance().getCopiesByTitle((BookTitle) args.get(0));
					if (ret != null) { // If search is successful
						client.sendToClient(new Message("searchResult",(Set<BookCopy>)ret)); // Send search results
					} else {
						client.sendToClient(new Message("searchFailed")); // Send failure message if no copies found
					}
					break;
					
				// Handle registerSubscriber request
				case "registerSubscriber":
					ret = BLibDBC.getInstance().registerSubscriber((Subscriber) args.get(0));
					if ((Boolean)ret == true) {
						client.sendToClient(new Message("success")); // Send success message if registration is successful
					} else {
						client.sendToClient(new Message("failed")); // Send failure message if registration fails
					}
					break;
					
				// Handle creating a new borrow request	
				case "createBorrow":
					ret = BLibDBC.getInstance().createBorrow((Integer) args.get(0),(Integer) args.get(1));
					if ((Boolean)ret == true) {
						client.sendToClient(new Message("success")); // Send success message
					} else {
						client.sendToClient(new Message("failed")); // Send failure message if borrow creation fails
					}
					break;
					
				case "getCopyActiveBorrow":
					ret = BLibDBC.getInstance().getCopyActiveBorrow((BookCopy) args.get(0));
					if (ret != null) {
						client.sendToClient(new Message("borrowFound",(Borrow)ret)); // Send success message
					} else {
						client.sendToClient(new Message("borrowNotFound")); // Send failure message if borrow creation fails
					}
					break;
					
				// TODO: complete
				case "extend":
					ret = BLibDBC.getInstance().getCopyActiveBorrow((BookCopy) args.get(0));
					if (ret != null) {
						client.sendToClient(new Message("borrowFound",(Borrow)ret)); // Send success message
					} else {
						client.sendToClient(new Message("borrowNotFound")); // Send failure message if borrow creation fails
					}
					break;
//				case "getTitleByID":
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
	
	private boolean canExtend(Borrow borrow) {
		if(borrow.getSubscriber().getStatus().equals("frozen")) {
			return false;
		}
		if(borrow.getDateOfBorrow().plusWeeks(1).compareTo(LocalDate.now())>=0) {
			return false;
		}
		if(BLibDBC.getInstance().getTitleNumOfAllowedExtend(borrow.getBook().getTitle())<=0) {
			return false;
		}
		// TODO: add message logic
		return true;
	}
	
	
	
}