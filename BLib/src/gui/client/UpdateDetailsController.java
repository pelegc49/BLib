package gui.client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	@FXML
	private Label lblStatus;

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
		if(s.getStatus().equals("active")) {
			this.lblStatus.setTextFill(Color.GREEN);
		}
		else {
			this.lblStatus.setTextFill(Color.RED);
		}
		String capitalizedStatus = s.getStatus().substring(0, 1).toUpperCase() + s.getStatus().substring(1);
		this.lblStatus.setText(capitalizedStatus);
		
	}


	/**
	 * Validates and saves the updated subscriber details.
	 * 
	 * @param event The ActionEvent triggered by clicking the Save button.
	 * @throws Exception If an error occurs during the operation.
	 */
	public void saveBtn(Event event) {
		boolean changed = false;
		if(!txtPhone.getText().equals(s.getPhone())) {
			try {
				Long.parseLong(txtPhone.getText()); // Validates that the phone number contains only digits.
			} catch (Exception e) {
				display("Phone must have only digits", Color.RED); // Displays an error message for invalid phone number.
				return;
			}
			changed = true;
		}
		
		if(!txtEmail.getText().equals(s.getEmail())) {
			String regex = "^[A-Za-z0-9.]{1,99}@"
						 + "[A-Za-z0-9]{1,99}"
						 + "(?:\\.[A-Za-z0-9]{1,99}){0,99}"
						 + "\\.[A-Za-z]{1,}$";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(txtEmail.getText());
			
			if(!matcher.matches()) {
				display("Email not valid", Color.RED); // Displays an error message for invalid phone number.
				return;
			}
			changed = true;
		}
		
		if(changed) {
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
			return;
		}
		display("You didn't change anything", Color.RED);
	}

	/**
	 * Closes the application when the Close button is clicked.
	 * 
	 * @param event The ActionEvent triggered by clicking the Close button.
	 * @throws Exception If an error occurs during the operation.
	 */
	public void backBtn(ActionEvent event) throws Exception {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/client/"+ "SubscriberClientGUIFrame" +".fxml"));
    	Parent root = loader.load();
    	SubscriberClientGUIController subscriberClientGUIController = loader.getController();
    	subscriberClientGUIController.loadSubscriber();
    	IPController.client.nextPage(loader, root, event, "Subscriber Main Menu");
	}
	
	// Enables the enter key to activate the OK button
	public void handleKey(KeyEvent event) {
		if(event.getCode().equals(KeyCode.ENTER)) {
			saveBtn(event);
		}
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
