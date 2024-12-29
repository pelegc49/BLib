package gui.client;

import client.BLibClient;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class IPController {

	// Static client instance for handling server communication.
	public static BLibClient client;

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
	public IPController() {
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
			display("You must enter an IP Address"); // Displays an error message if the input is empty.
		}
		else if (port.trim().isEmpty()) {
			display("You must enter Port"); // Displays an error message if the input is empty.
		}
		else {
			try {
				digit_port = Integer.parseInt(txtPort.getText()); // Validates that the ID contains only digits.
			} catch (Exception e) {
				display("Port must have only digits"); // Displays an error message for invalid ID.
				return;
			}
			try {
				// Attempts to create a client instance and connect to the server.
				client = new BLibClient(ip, 5555);
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
				display("Can't setup connection"); // Displays an error message.
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
	public void exitBtn(ActionEvent event) throws Exception {
		System.out.println("Exit Successfully"); // Logs the exit event.
		System.exit(0); // Exits the application.
	}
	
	// Enables the enter key to activate the OK button
	public void handleKey(KeyEvent event) {
		if(event.getCode().equals(KeyCode.ENTER)) {
			sendBtn(event);
		}
	}

	/**
	 * Displays a message in the lblError label.
	 * 
	 * @param message The message to display.
	 */
	public void display(String message) {
		lblError.setText(message); // Sets the text of the error label.
	}
}
