package gui.client;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * The AuthenticationController class handles user authentication. 
 * It manages the login process, including ID and password validation, 
 * and transitions the user to the main application interface upon successful login.
 */
public class LibrarianClientGUIController implements Initializable{
	boolean flag = true;
	
	@FXML
	private Label lblTitle; // Button for exiting the application.
	@FXML
	private Button btnBack = null; // Button for exiting the application.
	@FXML
	private Button btnOrder = null; // Button for submitting the login form.
	@FXML
	private Button btnExtendBook = null; // Button for submitting the login form.
	@FXML
	private Button btnUpdateDetails = null; // Button for submitting the login form.
	@FXML
	private Button btnViewHistory = null; // Button for submitting the login form.
	@FXML
	private Button btnSearch = null; // Button for submitting the login form.
	@FXML
	private Button btnSignUp = null;
	@FXML
	private Button btnBookActions = null;
	@FXML
	private Button btnReports = null;
	@FXML
	private Button btnBorrowRep = null;
	@FXML
	private Button messagesBtn = null;
	@FXML
	private Label lblNumMessages; // Label for displaying error messages to the user.

	public void loadLibrarian() {
		this.lblTitle.setText("Welcome, "+AuthenticationController.librarianName+"!");
	}
	
	public void updateMessageCount() {
		List<String> messages = IPController.client.getLibrarianMessages();
		int totalCount = messages.size();
		System.out.println("num of messages = " + totalCount);
		Platform.runLater(() -> lblNumMessages.setText(String.valueOf(totalCount)));
	}


	public void messagesBtn(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/client/"+ "ViewMessagesFrame" +".fxml"));
		Parent root = loader.load();
		ViewMessagesController viewMessagesController = loader.getController();
		viewMessagesController.loadMessages();
		cleanUp();
		IPController.client.nextPage(loader, root, event, "View Messages");
	}

	public void backBtn(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/client/"+ "AuthenticationFrame" +".fxml"));
		Parent root = loader.load();
		AuthenticationController authenticationController = loader.getController();
		authenticationController.loadImage();
		cleanUp();
		IPController.client.nextPage(loader, root, event, "Authentication");
	}
	
	public void signUpBtn(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/client/"+ "SignUpFrame" +".fxml"));
		Parent root = loader.load();
		cleanUp();
		IPController.client.nextPage(loader, root, event, "Sign Up Subscriber");
	}
	
	public void subscribersBtn(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/client/"+ "SubscriberListFrame" +".fxml"));
		Parent root = loader.load();
		cleanUp();
		IPController.client.nextPage(loader, root, event, "List of Subscribers");
	}
	
	public void searchBtn(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/client/"+ "SearchFrame" +".fxml"));
		Parent root = loader.load();
		cleanUp();
		IPController.client.nextPage(loader, root, event, "Librarian - Search");
	}
	
	public void bookActionsBtn(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/client/"+ "BookActionsFrame" +".fxml"));
		Parent root = loader.load();
		cleanUp();
		IPController.client.nextPage(loader, root, event, "Book Actions");
	}
	
	public void ReportsBtn(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/client/"+ "ReportsFrame" +".fxml"));
		Parent root = loader.load();
		ReportsController reportsController = loader.getController();
		reportsController.loadChoiceBoxs();
		cleanUp();
		IPController.client.nextPage(loader, root, event, "Choose Report");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		flag = true;
		try {
			// Initially populate the client information by calling refresh method
			updateMessageCount();

			// Create a new thread that refreshes the client list every 2 seconds
			Thread refresher = new Thread(() -> {
				while (flag) {
					try {
						Thread.sleep(60*1000); // Pause for 2 seconds before refreshing
						updateMessageCount();
					} catch (Exception e) {
						// If there's an error in refreshing, log a simple message
						System.out.println("failed to refresh");
					}
				}
			});
			// Mark the refresher thread as a daemon so it will stop when the main thread is
			// stopped
			refresher.setDaemon(true);
			refresher.start(); // Start the thread

		} catch (Exception e) {
			// Log exception or handle as needed
			System.out.println("Initialization failed: " + e.getMessage());
		}
	}
	
	public void cleanUp() {
		this.flag = false;
	}
}
