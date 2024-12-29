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
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import logic.Subscriber;

public class UpdateDetailsController {
	private Subscriber s; // Holds the Subscriber instance associated with this controller.

	// Labels to display error messages and subscriber details.
	@FXML
	private Label lblError;
	@FXML
	private Label lblId;
	@FXML
	private Label lblName;
	@FXML
	private Label lblPhone;
	@FXML
	private Label lblEmail;

	// Text fields to edit subscriber details.
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtName;
	@FXML
	private TextField txtPhone;
	@FXML
	private TextField txtEmail;

	// Buttons for closing the application and saving subscriber details.
	@FXML
	private Button btnBack = null;
	@FXML
	private Button btnSave = null;

	/**
	 * Loads the given Subscriber's details into the text fields.
	 * 
	 * @param s1 The Subscriber object to be displayed.
	 */
	public void loadSubscriber(Subscriber s1) {
		this.s = s1; // Assigns the subscriber to the controller.
		this.txtId.setText(String.valueOf(s.getId())); // Sets the subscriber's ID.
		this.txtName.setText(s.getName()); // Sets the subscriber's name.
		this.txtPhone.setText(s.getPhone()); // Sets the subscriber's phone.
		this.txtEmail.setText(s.getEmail()); // Sets the subscriber's email.
	}


	/**
	 * Validates and saves the updated subscriber details.
	 * 
	 * @param event The ActionEvent triggered by clicking the Save button.
	 * @throws Exception If an error occurs during the operation.
	 */
	public void saveBtn(ActionEvent event) throws Exception {
		int digit_id;
		try {
			digit_id = Integer.parseInt(txtId.getText()); // Validates that the ID contains only digits.
		} catch (Exception e) {
			display("ID must have only digits", Color.RED); // Displays an error message for invalid ID.
			return;
		}
		// Checks if the ID has been modified.
		if (s.getId() != digit_id) {
			display("Don't change the ID", Color.RED); // Displays an error message if ID is changed.
			return;
		}
		// Checks if the name has been modified.
		else if (!s.getName().equals(txtName.getText())) {
			display("Don't change the name", Color.RED); // Displays an error message if name is changed.
			return;
		}
		try {
			Integer.parseInt(txtPhone.getText()); // Validates that the phone number contains only digits.
		} catch (Exception e) {
			display("Phone must have only digits", Color.RED); // Displays an error message for invalid phone number.
			return;
		}
		// Updates the subscriber's email and phone details.
		this.s.setEmail(txtEmail.getText());
		this.s.setPhone(txtPhone.getText());

		// Attempts to save the updated subscriber details.
		if (IPController.client.updateSubscriber(s)) {
			display("saved Successfully!", Color.GREEN); // Displays a success message if save is successful.
			return;
		}
		// Displays an error message if save fails.
		display("could not save", Color.RED);
	}

	/**
	 * Closes the application when the Close button is clicked.
	 * 
	 * @param event The ActionEvent triggered by clicking the Close button.
	 * @throws Exception If an error occurs during the operation.
	 */
	public void backBtn(ActionEvent event) throws Exception {
		nextPage(event, "SubscriberClientGUIFrame", "Subscriber Main Menu");
	}
	
	/**
	 * Displays a message with a specified color.
	 * 
	 * @param message The message to display.
	 * @param color   The color of the message text.
	 */
	public void display(String message, Color color) {
		lblError.setTextFill(color); // Sets the color of the error label.
		lblError.setText(message); // Sets the text of the error label.
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
