package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.management.InstanceNotFoundException;

import logic.Activity;
import logic.BookCopy;
import logic.BookTitle;
import logic.Borrow;
import logic.Message;
import logic.Order;
import logic.Subscriber;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

/**
 * This class represents the server of the BLib application. It extends the
 * AbstractServer from the ocsf library and manages client connections and
 * messages from clients.
 */
public class BLibServer extends AbstractServer {


	// A map storing connected clients and their respective information (IP address
	// and host name)
	private static Map<ConnectionToClient, String[]> connectedClients = new HashMap<>();
	
	private ReportGenerator reportGenerator;
	
	/**
	 * Private constructor that initializes the server and begins listening on the
	 * provided port.
	 * 
	 * @param port the port the server listens on for incoming client connections
	 * @throws IOException if there is an error when starting the server
	 */
	public BLibServer(int port) throws IOException {
		super(port); // Call the superclass constructor to initialize the server
		listen(); // Start listening for client connections
		reportGenerator = new ReportGenerator();
		ServerTimer.start(this);
	}

//	/**
//	 * Retrieves the singleton instance of the BLibServer. If the instance does not
//	 * exist, it is created.
//	 * 
//	 * @param port the port the server listens on
//	 * @return the instance of the BLibServer
//	 * @throws IOException if there is an error when starting the server
//	 */
//	public static BLibServer getInstance(int port) throws IOException {
//		if (instance instanceof BLibServer) {
//			return instance; // Return existing instance if it exists
//		}
//		// Create and return a new instance if one does not exist
//		instance = new BLibServer(port);
//		return instance;
//	}

//	public static BLibServer getInstance() throws InstanceNotFoundException {
//		if (instance instanceof BLibServer) {
//			return instance; // Return existing instance if it exists
//		}
//		// Create and return a new instance if one does not exist
//		throw new InstanceNotFoundException();
//	}

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
		LocalDateTime now = LocalDateTime.now();
		String err;
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
					Order o = BLibDBC.getInstance().getOrderByCopy((Integer) args.get(1));
					Subscriber sub = BLibDBC.getInstance().getSubscriberByID((Integer) args.get(0));
					BookCopy copy = BLibDBC.getInstance().getCopyByID((Integer) args.get(1));
					
					err = canBorrow(sub,copy);
					if(err != null) {
						client.sendToClient(new Message("failed",err)); 
						break;
					}
						
					if(o != null) {
						if(o.getSubscriber().getId() != (Integer) args.get(0)) {
							client.sendToClient(new Message("failed","this copy is ordered")); // Send success message
							break;
						}else {
							BLibDBC.getInstance().cancelCommand("cancelOrder","%d".formatted((Integer) args.get(1)));
							execute(new Message("cancelOrder",(Integer) args.get(1)));
						}
					}
					
					ret = BLibDBC.getInstance().createBorrow((Integer) args.get(0), (Integer) args.get(1)); // Create a
																											// borrow
					if ((Boolean) ret == true) {
						client.sendToClient(new Message("success")); // Send success message
					} else {
						client.sendToClient(new Message("failed","DB error")); // Send failure message if borrow creation fails
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
					
					if (args.get(2).equals("subscriber")) {
						err = canExtend((Borrow) args.get(0));
						if (err != null) { // Check if the borrow can be extended
							client.sendToClient(new Message("failed",err));
							break;
						}
					}
					
					if (BLibDBC.getInstance()
							.getTitleMagicNumber(((Borrow) args.get(0)).getBook().getTitle()) < 0) {
						client.sendToClient(new Message("failed","this title is ordered")); // If no extension allowed, send failure message
						break;
					}

					ret = BLibDBC.getInstance().extendDuration((Borrow) args.get(0), (Integer) args.get(1),
							(String) args.get(2)); // Extend the borrow duration
					if ((Boolean) ret == true) {
						client.sendToClient(new Message("success")); // Send success message
					} else {
						client.sendToClient(new Message("failed","DB error")); // Send failure message
					}

					break;

				// Handle return book request
				case "return":
					Borrow borrow = BLibDBC.getInstance().getCopyActiveBorrow((BookCopy) args.get(0)); // Retrieve																				// active borrow
					
					if(BLibDBC.getInstance().isTitleOrdered(((BookCopy) args.get(0)).getTitle().getTitleID())) {
						if(BLibDBC.getInstance().updateOrder((BookCopy) args.get(0))){
							Order order = BLibDBC.getInstance().getOrderByCopy(((BookCopy) args.get(0)).getCopyID());
							MessageController.getInstance().sendEmail(order.getSubscriber(),
									"Your order has arrived!",
									"Dear %s,\n\n".formatted(order.getSubscriber().getName())+
									"Your book order of \"%s\" has arrived and is ready for pickup.\n".formatted(((BookCopy) args.get(0)).getTitle())+
									"Please collect it within the next two days, or the order will be canceled.\n\n"+
									"Best regards, BLib library");
							BLibDBC.getInstance().createCommand("cancelOrder", "%d".formatted(((BookCopy) args.get(0)).getCopyID()), now.plusDays(2), "%d".formatted(((BookCopy) args.get(0)).getCopyID()));
						}
					}
					
																										// for book copy
					if (borrow == null) {
						client.sendToClient(new Message("failed","No such borrowed book")); // Send failure message if borrow creation fails
						break;
					}
					// If the due date is passed, process as late return and potentially freeze the
					// subscriber
					if (borrow.getDueDate().compareTo(today) < 0) {
						BLibDBC.getInstance().returnBook((BookCopy) args.get(0), true);
						if (borrow.getDueDate().plusWeeks(1).compareTo(today) <= 0) {
							if(!borrow.getSubscriber().getStatus().equalsIgnoreCase("frozen")) {
								BLibDBC.getInstance().freezeSubscriber(borrow.getSubscriber().getId());
								client.sendToClient(new Message("success","Freezing account")); // Send freeze success message
							}else {
								BLibDBC.getInstance().cancelCommand("unfreeze", "%s".formatted(borrow.getSubscriber().getId()));
								BLibDBC.getInstance().createCommand("unfreeze", "%s".formatted(borrow.getSubscriber().getId()), now.plusMonths(1), "%s".formatted(borrow.getSubscriber().getId()));
								client.sendToClient(new Message("success","Freeze updated"));
							}
						} else {
							client.sendToClient(new Message("success","The return was late")); // Send late return success message
						}
					} else {
						BLibDBC.getInstance().cancelCommand("sendMessage","%s;%s".formatted(borrow.getSubscriber().getId(), borrow.getBook().getCopyID()));
						BLibDBC.getInstance().returnBook((BookCopy) args.get(0), false);
						client.sendToClient(new Message("success", "The return was successful")); // Send success message for regular return
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
					
				case "order":
					err = canOrder((Subscriber)args.get(0), (BookTitle)args.get(1));
					if(err != null) {
						client.sendToClient(new Message("Failed", err));
						break;
					}
	
					ret = BLibDBC.getInstance().orderBook(((Subscriber) args.get(0)).getId(),((BookTitle)args.get(1)).getTitleID());
					if (ret != null) {
						client.sendToClient(new Message("success"));
					} else {
						client.sendToClient(new Message("Failed","DB error")); // Send failure message
					}
					break;
					
				case "getSubscriberBorrows":
					ret = BLibDBC.getInstance().getSubscriberActiveBorrows((Subscriber)args.get(0));
					if (ret != null) {
						client.sendToClient(new Message("success",(List<Borrow>)ret));
					}else {
						client.sendToClient(new Message("failed"));
					}
					break;
					
				
				case "getAllSubscribers":
					ret = BLibDBC.getInstance().getAllSubscribers();
					if (ret != null) {
						client.sendToClient(new Message("success",(List<Subscriber>)ret));
					}else {
						client.sendToClient(new Message("failed"));
					}
					break;
					
					
				case "getSubscriberOrders":
					ret = BLibDBC.getInstance().getSubscriberActiveOrders((Subscriber)args.get(0));
					if (ret != null) {
						client.sendToClient(new Message("success",(List<Order>)ret));
					}else {
						client.sendToClient(new Message("failed"));
					}
					break;
					
				
					
				case "getLibrarianMessages":
					ret = BLibDBC.getInstance().getLibrarianMessages();
					if (ret != null) {
						client.sendToClient(new Message("success",(List<String>)ret));
					}else {
						client.sendToClient(new Message("failed"));
					}
					break;
					
				case "clearLibrarianMessages":
					ret = BLibDBC.getInstance().clearLibrarianMessages();
					if ((Boolean) ret == true) {
						client.sendToClient(new Message("success"));
					} else {
						client.sendToClient(new Message("failed"));
					}
					break;
					
					
				case "getCopyByID":
					ret = BLibDBC.getInstance().getCopyByID((Integer) args.get(0));
					
					if (ret != null) {
						client.sendToClient(new Message("success",(BookCopy)ret));
					} else {
						client.sendToClient(new Message("failed"));
					}
					break;
					
				case "getTitleClosestReturnDate":
					ret = BLibDBC.getInstance().getTitleClosestReturnDate((BookTitle) args.get(0));
					
					if (ret != null) {
						client.sendToClient(new Message("success",(LocalDate)ret));
					} else {
						client.sendToClient(new Message("failed"));
					}
					break;
					
					
				case "getGraph":
					ret = BLibDBC.getInstance().getGraph((Integer) args.get(0) ,(Integer) args.get(1) ,(String) args.get(2));
					
					if (ret != null) {
						client.sendToClient(new Message("success",((DataInputStream)ret).readAllBytes()));
					} else {
						client.sendToClient(new Message("failed"));
					}
					break;
					
					
					
				default:
					client.sendToClient(new Message("unknownCommand: "+((Message) msg).getCommand()));
					
					
					
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
		Subscriber sub;
		switch (msg.getCommand()) {
		case "unfreeze":
			BLibDBC.getInstance().unfreezeSubscriber(Integer.parseInt((String)args.get(0)));
			break;
			
		case "sendEmail":
			sub = BLibDBC.getInstance().getSubscriberByID(Integer.parseInt((String)args.get(0)));
			MessageController.getInstance().sendEmail(sub, (String) args.get(1), (String) args.get(2));
			break;
		case "sendMessage":
			sub = BLibDBC.getInstance().getSubscriberByID(Integer.parseInt((String)args.get(0)));
			MessageController.getInstance().sendMessage(sub, (String) args.get(1), (String) args.get(2));
			break;
		case "cancelOrder":
			BLibDBC.getInstance().cancelOrder(Integer.parseInt((String)args.get(0)));
			break;
		
		case "generateGraphs":
//			Thread t = new Thread(()->{
			LocalDate date = LocalDate.of(Integer.parseInt((String)args.get(0)), Integer.parseInt((String)args.get(1)),1); // TODO: no plusMonths 
			System.out.println(date);
			byte[] data =  reportGenerator.generateSubscriberStatusReport(date);
			BLibDBC.getInstance().saveGraph(date, "subscriber status", data);
			data =  reportGenerator.generateBorrowTimeReport(date);
			BLibDBC.getInstance().saveGraph(date, "borrowing report", data);
			System.out.println("41213");
			LocalDate today = LocalDate.now();
			LocalDate nextMonth = LocalDate.of(today.getYear(), today.getMonthValue(), 1).plusMonths(1);
			LocalDate timeOfNextExecution = nextMonth.plusMonths(1).minusDays(1);
			BLibDBC.getInstance().createCommand("generateGraphs", "%04d;%02d".formatted(nextMonth.getYear(),nextMonth.getMonthValue()), LocalDateTime.of(timeOfNextExecution,LocalTime.of(0, 1)) , "");
		
//			});
//			t.start();
		}
	}

	
	
	
	/**
	 * Checks if a borrow can be extended based on the borrower's status and the
	 * borrow date.
	 * 
	 * @param borrow the borrow object to check
	 * @return true if the borrow can be extended, false otherwise
	 */
	private String canExtend(Borrow borrow) { 
		// Check if the subscriber's status is "frozen". If yes, they can't extend the
		// borrow period.
		if (borrow.getSubscriber().getStatus().equals("frozen")) {
			return "the subscriber is frozen";
		}

		// Check if the borrow period is less than or equal to one week. If the borrow
		// was made within the last week,
		// it cannot be extended.
		
		if (borrow.getDueDate().minusWeeks(1).compareTo(LocalDate.now()) >= 0) {
			return "extention not available until %s".formatted(borrow.getDueDate().minusWeeks(1));
		}
		if (borrow.getDueDate().compareTo(LocalDate.now()) < 0) {
			return "extention not available after due date";
		}
		// If none of the conditions above are met, the borrow can be extended.
		return null;
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
	
	private String canOrder(Subscriber sub, BookTitle title) {
		if (sub==null) {
			return "The subscriber is not found";
		}
		if (title==null) {
			return "The book is not found";
		}
		if(sub.getStatus().equals("frozen"))
			return "The subscriber is frozen";
		
		for(Borrow b : BLibDBC.getInstance().getSubscriberActiveBorrows(sub)) {
			if(b.getBook().getTitle().equals(title)) 
				return "This Book is already Borrowed";
		}

		for(Order o : BLibDBC.getInstance().getSubscriberActiveOrders(sub)) {
			if(o.getTitle().equals(title)) 
				return "This Book is already ordered";
		}
		
		if(BLibDBC.getInstance().getTitleMagicNumber(title)>0) {
			return "Not all of the title copies are borrowed"; 
		}
		
		if(BLibDBC.getInstance().getTitleMagicNumber(title)+BLibDBC.getInstance().getNumOfCopies(title)<=0) {
			return "There are too many active orders";
		}
		
		return null;
			
	}
	
	
	private String canBorrow(Subscriber sub, BookCopy copy) {
		if (copy==null) {
			return "The book is not found";
		}
		if (sub==null) {
			return "The subscriber is not found";
		}
		if(sub.getStatus().equals("frozen"))
			return "The subscriber is frozen";
		
		for(Borrow b : BLibDBC.getInstance().getSubscriberActiveBorrows(sub)) {
			if(b.getBook().getTitle().equals(copy.getTitle())) 
				return "This Book is already Borrowed by the subscriber";
		}
		if (BLibDBC.getInstance().getCopyActiveBorrow(copy) != null) {
			return "This copy is already Borrowed, return it first";
		}
		return null;
		
	}

	public Map<LocalDate, Integer[]> getSubscribersStatusOnMonth(LocalDate date) {
		// TODO Auto-generated method stub
		return BLibDBC.getInstance().getSubscribersStatusOnMonth(date);
	}

	public int SumNewSubscriber(LocalDate now) {
		Integer ret =  BLibDBC.getInstance().SumNewSubscriber(now);
		if(ret != null)
			return ret;
		return 0;
	}
	
	public Map<String, Double[]> getBorrowTimeOnMonth(LocalDate date) {
		// TODO Auto-generated method stub
		return BLibDBC.getInstance().getBorrowTimeOnMonth(date);
	}
	
	public Double getAvgBorrowTimeOnMonth(LocalDate date) {
		return BLibDBC.getInstance().getAvgBorrowTimeOnMonth(date);
	}
	
	
}
















