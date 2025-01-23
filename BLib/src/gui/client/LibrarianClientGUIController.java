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

public class LibrarianClientGUIController implements Initializable{
	boolean flag = true;
	
	@FXML
	private Label lblTitle;
	@FXML
	private Button btnBack = null; 
	@FXML
	private Button btnOrder = null;
	@FXML
	private Button btnExtendBook = null; 
	@FXML
	private Button btnUpdateDetails = null; 
	@FXML
	private Button btnViewHistory = null; 
	@FXML
	private Button btnSearch = null; 
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
	private Label lblNumMessages;

	public void loadLibrarian() {
		this.lblTitle.setText("Welcome, "+AuthenticationController.librarianName+"!");
	}

	/**
	 * updateMessageCount method updates the number of messages in the librarian's inbox.
	 */
	public void updateMessageCount() {
		List<String> messages = IPController.client.getLibrarianMessages();
		int totalCount = messages.size();
		Platform.runLater(() -> lblNumMessages.setText(String.valueOf(totalCount)));
	}


	/**
	 * Handles the "Messages" button action. Opens the messages frame.
	 *
	 * @param event The action event triggered by clicking the button.
	 * @throws Exception If an error occurs during termination.
	 */
	public void messagesBtn(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/client/"+ "ViewMessagesFrame" +".fxml"));
		Parent root = loader.load();
		ViewMessagesController viewMessagesController = loader.getController();
		viewMessagesController.loadMessages();
		cleanUp();
		IPController.client.nextPage(loader, root, event, "View Messages");
	}

	/**
	 * Handles the "Back" button action. Returns the user to the previous page.
	 *
	 * @param event The action event triggered by clicking the button.
	 * @throws Exception If an error occurs during termination.
	 */
	public void backBtn(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/client/"+ "AuthenticationFrame" +".fxml"));
		Parent root = loader.load();
		AuthenticationController authenticationController = loader.getController();
		authenticationController.loadImage();
		cleanUp();
		IPController.client.nextPage(loader, root, event, "Authentication");
	}

	/**
	 * Handles the "sign up" button action. Transitions the user to the sign up page.
	 *
	 * @param event The action event triggered by clicking the button.
	 * @throws Exception If an error occurs during termination.
	 */
	public void signUpBtn(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/client/"+ "SignUpFrame" +".fxml"));
		Parent root = loader.load();
		cleanUp();
		IPController.client.nextPage(loader, root, event, "Sign Up Subscriber");
	}

	/**
	 * Handles the "subscribers" button action. Transitions the user to the subscribe page.
	 *
	 * @param event The action event triggered by clicking the button.
	 * @throws Exception If an error occurs during termination.
	 */
	public void subscribersBtn(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/client/"+ "SubscriberListFrame" +".fxml"));
		Parent root = loader.load();
		cleanUp();
		IPController.client.nextPage(loader, root, event, "List of Subscribers");
	}

	/**
	 * Handles the "search" button action. Transitions the user to the search page.
	 *
	 * @param event The action event triggered by clicking the button.
	 * @throws Exception If an error occurs during termination.
	 */
	public void searchBtn(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/client/"+ "SearchFrame" +".fxml"));
		Parent root = loader.load();
		cleanUp();
		IPController.client.nextPage(loader, root, event, "Librarian - Search");
	}

	/**
	 * Handles the "book actions" button action. Transitions the user to the book actions page.
	 *
	 * @param event The action event triggered by clicking the button.
	 * @throws Exception If an error occurs during termination.
	 */
	public void bookActionsBtn(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/client/"+ "BookActionsFrame" +".fxml"));
		Parent root = loader.load();
		cleanUp();
		IPController.client.nextPage(loader, root, event, "Book Actions");
	}

	/**
	 * Handles the "reports" button action. Transitions the user to the reports page.
	 *
	 * @param event The action event triggered by clicking the button.
	 * @throws Exception If an error occurs during termination.
	 */
	public void ReportsBtn(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/client/"+ "ReportsFrame" +".fxml"));
		Parent root = loader.load();
		ReportsController reportsController = loader.getController();
		reportsController.loadChoiceBoxs();
		cleanUp();
		IPController.client.nextPage(loader, root, event, "Choose Report");
	}

	/**
	 * initialize method initializes the librarian client GUI.
	 */
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

	/**
	 * cleanUp method cleans up the GUI.
	 */
	public void cleanUp() {
		this.flag = false;
	}
}
