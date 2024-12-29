package gui.client;

import javafx.event.ActionEvent; 
import javafx.fxml.FXML; 
import javafx.fxml.FXMLLoader; 
import javafx.scene.Node; 
import javafx.scene.Scene; 
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField; 
import javafx.scene.layout.Pane; 
import javafx.stage.Stage; 
import logic.Subscriber; 

/**
 * The AuthenticationController class handles user authentication. 
 * It manages the login process, including ID and password validation, 
 * and transitions the user to the main application interface upon successful login.
 */
public class SubscriberFindController {
	public static Subscriber subscriber; // Static reference to the currently authenticated subscriber.

	@FXML
	private Label lblError; // Label for displaying error messages to the user.
	@FXML
	private TextField txtId; // TextField for user to enter their ID.
	@FXML
	private Button btnExit = null; // Button for exiting the application.
	@FXML
	private Button btnSend = null; // Button for submitting the login form.
	/**
	 * Handles the login process when the user clicks the "Send" button.
	 * Validates the input fields and checks the user's credentials.
	 * If successful, loads the main application interface.
	 * 
	 * @param event The action event triggered by clicking the button.
	 * @throws Exception If an error occurs during the login process.
	 */
	public void Send(ActionEvent event) throws Exception {
		String id; // String to store the entered ID.
		FXMLLoader loader = new FXMLLoader(); // FXMLLoader for loading the main GUI.
		int digit_id = 0; // Variable to hold the numeric value of the ID.
		id = txtId.getText(); // Retrieve the text entered in the ID field.

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
		else {
			Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		    String currentTitle = currentStage.getTitle();
			subscriber = IPController.client.getSubscriber(digit_id);
			if(currentTitle.equals("Extend Time")) {
		    	//nextPage(event, "ExtendTimeFrame", "Librarian Extend Time");
		    }
		    else if(currentTitle.equals("Update Details")) {
		    	nextPage(event, "UpdateDetailsFrame", "Librarian Main Menu");
		    }
		    else {
		    	//nextPage(event, "ViewHistoryFrame", "Librarian View History");
		    }
			}
		}

	/**
	 * Handles the "Exit" button action. Terminates the application.
	 * 
	 * @param event The action event triggered by clicking the button.
	 * @throws Exception If an error occurs during termination.
	 */
	public void getExitBtn(ActionEvent event) throws Exception {
		nextPage(event, "LibrarianClientGUIFrame", "Librarian Main Menu");
	}

	/**
	 * Displays an error or informational message to the user.
	 * 
	 * @param message The message to display.
	 */
	public void display(String message) {
		lblError.setText(message);
	}
	
	public void nextPage(ActionEvent event, String fileName, String title) throws Exception{
		// FXMLLoader for loading the main GUI.
		FXMLLoader loader = new FXMLLoader(); 
		
		// Hide the current window.
		((Node) event.getSource()).getScene().getWindow().hide();

		// Load the main application interface.
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/gui/client/"+ fileName +".fxml").openStream());

		// Set up and display the new scene.
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/client/"+ fileName +".css").toExternalForm());
		primaryStage.setOnCloseRequest((E) -> System.exit(0));
		primaryStage.setTitle(title);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
}
