package gui.client;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.Subscriber; 

/**
 * The AuthenticationController class handles user authentication. 
 * It manages the login process, including ID and password validation, 
 * and transitions the user to the main application interface upon successful login.
 */
public class AuthenticationController {
	public static Subscriber subscriber; // Static reference to the currently authenticated subscriber.
	public static String librarianName;

	@FXML
	private Label lblError; // Label for displaying error messages to the user.
	@FXML
	private TextField txtId; // TextField for user to enter their ID.
	@FXML
	private PasswordField txtPassword; // TextField for user to enter their password.
	@FXML
	private Button btnExit = null; // Button for exiting the application.
	@FXML
	private Button btnSend = null; // Button for submitting the login form.
	@FXML
	private Button btnGuest = null; // Button for searching a book as a guest.
	@FXML
	private ImageView img; // Button for searching a book as a guest.
	
	
	public void loadImage() {
		img.setImage(new Image("/images/logoBackground.png"));
	}
	/**
	 * Handles the login process when the user clicks the "Send" button.
	 * Validates the input fields and checks the user's credentials.
	 * If successful, loads the main application interface.
	 * 
	 * @param event The action event triggered by clicking the button.
	 * @throws Exception If an error occurs during the login process.
	 */
	public void sendBtn(Event event) {
		String id; // String to store the entered ID.
		//FXMLLoader loader = new FXMLLoader(); // FXMLLoader for loading the main GUI.
		int digit_id = 0; // Variable to hold the numeric value of the ID.
		id = txtId.getText(); // Retrieve the text entered in the ID field.
		String password = txtPassword.getText(); // Retrieve the text entered in the password field.

		try {
			// Attempt to parse the ID to an integer.
			digit_id = Integer.parseInt(id);
		} catch (Exception e) {
			// Display an error message if the ID is not numeric.
			display("bad username - only digits");
		}

		// Validate that the ID field is not empty.
		if (id.trim().isEmpty()) {
			display("You must enter an id number");
		} 
		// Validate that the password field is not empty.
		else if (txtPassword.getText().isEmpty()) {
			display("You must enter a password");
		} 
		// If both fields are valid, attempt to log in.
		else {
			String name = IPController.client.login(digit_id, password);
			switch(name) {
				case "subscriber":
					subscriber = IPController.client.getSubscriber(digit_id);
					nextPage(event, "SubscriberClientGUIFrame", "Subscriber Main Menu");
					break;
				case "fail":
					display("ID or password are incorrect");
					break;
				// case for the librarian with her name
				default:
					librarianName = name;
					nextPage(event, "LibrarianClientGUIFrame", "Librarian Main Menu");
					break;
			}
		}
	}

	// Enables the enter key to activate the OK button
	public void handleKey(KeyEvent event) {
		if(event.getCode().equals(KeyCode.ENTER)) {
			sendBtn(event);
		}
	}
	
	/**
	 * Displays an error or informational message to the user.
	 * 
	 * @param message The message to display.
	 */
	public void display(String message) {
		lblError.setText(message);
	}
	
	public void nextPage(Event event, String fileName, String title){
		// FXMLLoader for loading the main GUI.
		FXMLLoader loader = new FXMLLoader(); 
		
		// Hide the current window.
		((Node) event.getSource()).getScene().getWindow().hide();

		// Load the main application interface.
		Stage primaryStage = new Stage();
		Pane root = null;
		try {
			root = loader.load(getClass().getResource("/gui/client/"+ fileName +".fxml").openStream());
		} catch (IOException e) {}

		// Set up and display the new scene.
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/client/"+ fileName +".css").toExternalForm());
		primaryStage.setOnCloseRequest((E) -> System.exit(0));
		primaryStage.setTitle(title);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public void guestBtn(ActionEvent event) throws Exception {
		nextPage(event, "SearchFrame", "Guest - Search");
	}
}
