package gui.client;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
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
import javafx.scene.paint.Color;
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
	 * @throws IOException 
	 * @throws Exception If an error occurs during the login process.
	 */
	public void sendBtn(Event event) throws IOException {
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
			IPController.client.display(lblError,"bad username - only digits", Color.RED);
		}

		// Validate that the ID field is not empty.
		if (id.trim().isEmpty()) {
			IPController.client.display(lblError,"You must enter an id number", Color.RED);
		} 
		// Validate that the password field is not empty.
		else if (txtPassword.getText().isEmpty()) {
			IPController.client.display(lblError,"You must enter a password", Color.RED);
		} 
		// If both fields are valid, attempt to log in.
		else {
			String name = IPController.client.login(digit_id, password);
			
			switch(name) {
				case "subscriber":
					subscriber = IPController.client.getSubscriber(digit_id);
					FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/gui/client/"+ "SubscriberClientGUIFrame" +".fxml"));
					Parent root1 = loader1.load();
					SubscriberClientGUIController subscriberClientGUIController = loader1.getController();
					subscriberClientGUIController.loadSubscriber();
					IPController.client.nextPage(loader1, root1, event, "Subscriber Main Menu");
					break;
				case "fail":
					IPController.client.display(lblError,"ID or password are incorrect", Color.RED);
					break;
				// case for the librarian with her name
				default:
					librarianName = name;
					FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/gui/client/"+ "LibrarianClientGUIFrame" +".fxml"));
					Parent root2 = loader2.load();
					LibrarianClientGUIController librarianClientGUIController = loader2.getController();
					librarianClientGUIController.loadLibrarian();
					librarianClientGUIController.updateMessageCount();
					IPController.client.nextPage(loader2, root2, event, "Librarian Main Menu");
					break;
			}
		}
	}

	// Enables the enter key to activate the OK button
	public void handleKey(KeyEvent event) throws IOException {
		if(event.getCode().equals(KeyCode.ENTER)) {
			sendBtn(event);
		}
	}

	
	public void guestBtn(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/gui/client/"+ "SearchFrame" +".fxml").openStream());
		IPController.client.nextPage(loader, root, event, "Guest - Search");
	}
	

}
