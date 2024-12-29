package gui.client;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class BookTitleController {

	// UI elements defined in the FXML file.
	@FXML
	private Label lblError; // Label to display error messages.
	@FXML
	private TextField txtIp; // Text field to input the server IP address.
	@FXML
	private TextField txtPort; // Text field for port.
	@FXML
	private Button btnExit = null; // Button to exit the application.
	@FXML
	private Button btnSend = null; // Button to initiate the connection to the server.

	/**
	 * Default constructor. Required for JavaFX to initialize the controller.
	 */
	public BookTitleController() {
		super();
	}

	/**
	 * Handles the Send button click event. Attempts to connect to the server using
	 * the provided IP address.
	 * 
	 * @param event The ActionEvent triggered by clicking the Send button.
	 * @throws Exception If an error occurs during the operation.
	 */
	public void sendBtn(Event event) {
		String ip; // Holds the entered IP address.
		String port; // Holds the entered Port.
		int digit_port;
		FXMLLoader loader = new FXMLLoader(); // Used to load the next scene.
		ip = txtIp.getText(); // Retrieves the entered IP address.
		port = txtPort.getText(); // Retrieves the entered Port.

		// Validates the IP address input.
		if (ip.trim().isEmpty()) {
		}
		else if (port.trim().isEmpty()) {
		}
		else {
			try {
				digit_port = Integer.parseInt(txtPort.getText()); // Validates that the ID contains only digits.
			} catch (Exception e) {
				return;
			}
			try {
				// Attempts to create a client instance and connect to the server.
				//client = new BLibClient(ip, 5555);
				System.out.println("IP Entered Successfully");

				// Hides the current window.
				((Node) event.getSource()).getScene().getWindow().hide();

				// Sets up and displays the AuthenticationFrame.
				Stage primaryStage = new Stage();
				Pane root = loader.load(getClass().getResource("/gui/client/AuthenticationFrame.fxml").openStream());
				Scene scene = new Scene(root);
				scene.getStylesheets()
						.add(getClass().getResource("/gui/client/AuthenticationFrame.css").toExternalForm());
				primaryStage.setOnCloseRequest((E) -> System.exit(0)); // Ensures the application exits on close.
				primaryStage.setTitle("Authentication");
				primaryStage.setScene(scene);
				primaryStage.show();
			} catch (Exception e) {
				// Handles connection errors.
				System.out.println("Error: Can't setup connection!" + " Terminating client.");
			}
		}
	}

	/**
	 * Starts the IPController application. Initializes and displays the primary
	 * stage.
	 * 
	 * @param primaryStage The primary stage of the application.
	 * @throws Exception If an error occurs during the operation.
	 */
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setOnCloseRequest((E) -> System.exit(0)); // Ensures the application exits on close.
		Parent root = FXMLLoader.load(getClass().getResource("/gui/client/IPFrame.fxml")); // Loads the FXML file.
		Scene scene = new Scene(root); // Creates the scene with the loaded FXML.
		scene.getStylesheets().add(getClass().getResource("/gui/client/IPFrame.css").toExternalForm()); // Adds the
																										// stylesheet.
		primaryStage.setTitle("IP"); // Sets the window title.
		primaryStage.setScene(scene);
		primaryStage.show(); // Displays the primary stage.
	}

	/**
	 * Handles the Exit button click event. Closes the application.
	 * 
	 * @param event The ActionEvent triggered by clicking the Exit button.
	 * @throws Exception If an error occurs during the operation.
	 */
	public void backBtn(ActionEvent event) throws Exception {
//		Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//	    String currentTitle = currentStage.getTitle();
//	    if(currentTitle.equals("Subscriber - BookTitle")) {
//	    	nextPage(event, "SearchController", "Subscriber - Search");
//	    }
//	    else if(currentTitle.equals("Librarian - BookTitle")) {
//	    	nextPage(event, "SearchController", "Librarian - Search");
//	    }
//	    else {
//	    	nextPage(event, "SearchController", "Guest - Search");
//	    }
	}
}
