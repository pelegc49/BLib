package gui.server;

import javafx.event.ActionEvent;
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
import server.BLibDBC;
import server.BLibServer;
import server.ServerGUI;

public class SQLPassController {

	// FXML elements to be injected from the FXML file
	@FXML
	private Label lblMSG; // Label to display messages to the user (e.g., error or success)
	@FXML
	private Label lblEnterPass; // Label prompting the user to enter the password
	@FXML
	private TextField txtPass; // TextField for the user to input the password
	@FXML
	private Button btnClose; // Button to close the window
	@FXML
	private Button btnOK; // Button to confirm the password entry

	// Method to start the stage (window) and show the password prompt
	public void start(Stage primaryStage) throws Exception {
		// Handling close request to ensure the application exits when the window is
		// closed
		primaryStage.setOnCloseRequest((E) -> System.exit(0));

		// Load the FXML layout for this window and set the scene
		Parent root = FXMLLoader.load(getClass().getResource("/gui/server/SQLPass.fxml"));
		Scene scene = new Scene(root);

		// Adding the stylesheet to the scene to apply styles
		scene.getStylesheets().add(getClass().getResource("/gui/server/Server.css").toExternalForm());

		// Set the title for the window
		primaryStage.setTitle("Enter Password");
		primaryStage.setScene(scene); // Set the scene for the primary stage
		primaryStage.show(); // Show the window
	}

	// Method to handle the "Close" button click (exits the application)
	public void actionOnClose(ActionEvent e) {
		System.out.println("exiting..."); // Log message to indicate exit
		System.exit(0); // Exit the application
	}

	// Method to handle the "OK" button click (validates the entered password and
	// proceeds)
	public void actionOnOK(ActionEvent e) {
		// Retrieve the entered password
		String password = txtPass.getText();

		// Check if the password field is empty, if so, prompt the user for a password
		if (password.isBlank()) {
			txtPass.setText(""); // Clear the text field
			display("An SQL password must be entered"); // Show the message in lblMSG
			return; // Exit the method without proceeding further
		}

		// Try to establish a connection to the database using the entered password
		if (!BLibDBC.connect(password)) {
			// If the connection fails, display an error message
			display("Can't establish connection with DB");
			return; // Exit the method
		}

		// If the connection is successful, proceed to set up the next window
		try {
			// Load the server interface window
			FXMLLoader loader = new FXMLLoader();
			ServerGUI.server = BLibServer.getInstance(ServerGUI.DEFAULT_PORT); // Get the server instance

			// Create a new stage (window) for the server interface
			Stage primaryStage = new Stage();
			Pane root = loader.load(getClass().getResource("/gui/server/Server.fxml").openStream());
			Scene scene = new Scene(root);

			// Apply the stylesheet to the new scene
			scene.getStylesheets().add(getClass().getResource("/gui/server/Server.css").toExternalForm());

			// Handle close request for the server window
			primaryStage.setOnCloseRequest((E) -> System.exit(0));

			// Set the title for the new window
			primaryStage.setTitle("BLib Server");

			// Set the scene for the new window and show it
			primaryStage.setScene(scene);
			((Node) e.getSource()).getScene().getWindow().hide(); // Hide the current window (password prompt)
			primaryStage.show(); // Show the server interface window

		} catch (Exception ex) {
			// If there is an exception (e.g., port already in use), display an error
			// message
			display("make sure port " + ServerGUI.DEFAULT_PORT + " isn't used");
			return; // Exit the method
		}
	}

	// Method to update the message label with a specific string
	public void display(String str) {
		lblMSG.setText(str); // Set the message text on the label
	}

}