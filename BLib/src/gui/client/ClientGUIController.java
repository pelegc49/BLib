package gui.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import logic.Subscriber;

public class ClientGUIController {

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
	private Button btnClose = null;
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
	 * Closes the application when the Close button is clicked.
	 * 
	 * @param event The ActionEvent triggered by clicking the Close button.
	 * @throws Exception If an error occurs during the operation.
	 */
	public void getbtnClose(ActionEvent event) throws Exception {
		System.out.println("Closing"); // Logs a message indicating the application is closing.
		System.exit(0); // Exits the application.
	}

	/**
	 * Validates and saves the updated subscriber details.
	 * 
	 * @param event The ActionEvent triggered by clicking the Save button.
	 * @throws Exception If an error occurs during the operation.
	 */
	public void getbtnSave(ActionEvent event) throws Exception {
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
	 * Displays a message with a specified color.
	 * 
	 * @param message The message to display.
	 * @param color   The color of the message text.
	 */
	public void display(String message, Color color) {
		lblError.setTextFill(color); // Sets the color of the error label.
		lblError.setText(message); // Sets the text of the error label.
	}

}
