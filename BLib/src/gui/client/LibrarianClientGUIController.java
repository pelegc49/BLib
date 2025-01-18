package gui.client;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * The AuthenticationController class handles user authentication. 
 * It manages the login process, including ID and password validation, 
 * and transitions the user to the main application interface upon successful login.
 */
public class LibrarianClientGUIController {

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
		lblNumMessages.setText(String.valueOf(totalCount));
	}


	public void messagesBtn(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/client/"+ "ViewMessagesFrame" +".fxml"));
		Parent root = loader.load();
		ViewMessagesController viewMessagesController = loader.getController();
		viewMessagesController.loadMessages();
		IPController.client.nextPage(loader, root, event, "View Messages");
	}

	public void backBtn(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/client/"+ "AuthenticationFrame" +".fxml"));
		Parent root = loader.load();
		AuthenticationController authenticationController = loader.getController();
		authenticationController.loadImage();
		IPController.client.nextPage(loader, root, event, "Authentication");
	}
	
	public void signUpBtn(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/client/"+ "SignUpFrame" +".fxml"));
		Parent root = loader.load();
		IPController.client.nextPage(loader, root, event, "Sign Up Subscriber");
	}
	
	public void subscribersBtn(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/client/"+ "SubscriberListFrame" +".fxml"));
		Parent root = loader.load();
		IPController.client.nextPage(loader, root, event, "List of Subscribers");
	}
	
	public void viewHistoryBtn(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/client/"+ "SubscriberListFrame" +".fxml"));
		Parent root = loader.load();
		IPController.client.nextPage(loader, root, event, "View History");
	}
	
	public void searchBtn(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/client/"+ "SearchFrame" +".fxml"));
		Parent root = loader.load();
		IPController.client.nextPage(loader, root, event, "Librarian - Search");
	}
	
	public void bookActionsBtn(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/client/"+ "BookActionsFrame" +".fxml"));
		Parent root = loader.load();
		IPController.client.nextPage(loader, root, event, "Book Actions");
	}
	
	public void ReportsBtn(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/client/"+ "ReportsFrame" +".fxml"));
		Parent root = loader.load();
		ReportsController reportsController = loader.getController();
		reportsController.loadChoiceBoxs();
		IPController.client.nextPage(loader, root, event, "Choose Report");
	}

	

}
