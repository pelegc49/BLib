package gui.client;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.Subscriber;

import java.util.List;

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
	private Button btnSubReprt = null;
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

	public void initialize() {
		updateMessageCount();
	}

	public void messagesBtn(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/gui/client/"+ "ViewMessagesFrame" +".fxml").openStream());
		ViewMessagesController viewMessagesController = loader.getController();
		viewMessagesController.loadMessages();
		nextPage(loader, root, event, "View Messages");
	}

	public void backBtn(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/gui/client/"+ "AuthenticationFrame" +".fxml").openStream());
		AuthenticationController authenticationController = loader.getController();
		authenticationController.loadImage();
		nextPage(loader, root, event, "Authentication");
	}
	
	public void signUpBtn(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/gui/client/"+ "SignUpFrame" +".fxml").openStream());
		nextPage(loader, root, event, "Sign Up Subscriber");
	}
	
	public void subscribersBtn(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/gui/client/"+ "SubscriberListFrame" +".fxml").openStream());
		nextPage(loader, root, event, "List of Subscribers");
	}
	
	public void viewHistoryBtn(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/gui/client/"+ "SubscriberListFrame" +".fxml").openStream());
		nextPage(loader, root, event, "View History");
	}
	
	public void searchBtn(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/gui/client/"+ "SearchFrame" +".fxml").openStream());
		nextPage(loader, root, event, "Librarian - Search");
	}
	
	public void bookActionsBtn(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/gui/client/"+ "BookActionsFrame" +".fxml").openStream());
		nextPage(loader, root, event, "Book Actions");
	}
	
	public void subReport(ActionEvent event) throws Exception {
		//nextPage(event, "SubscriberReportFrame", "Subscribers Report");
	}
	
	public void borrowReport(ActionEvent event) throws Exception {
		//nextPage(event, "BorrowReportFrame", "Borrow Report");
	}
	
	public void nextPage(FXMLLoader loader, Pane root, Event event, String title){
		((Node) event.getSource()).getScene().getWindow().hide();
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/client/stylesheet.css").toExternalForm());
		primaryStage.setOnCloseRequest((E) -> System.exit(0));
		primaryStage.setTitle(title);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
