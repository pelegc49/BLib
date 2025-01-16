package gui.client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.Subscriber;

public class SignUpController {
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
	@FXML
	private Label lblPassword;
	
	// Text fields to edit subscriber details.
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtName;
	@FXML
	private TextField txtPhone;
	@FXML
	private TextField txtEmail;
	@FXML
	private Text txtPassword;

	// Buttons for closing the application and saving subscriber details.
	@FXML
	private Button btnBack = null;
	@FXML
	private Button btnSignUp = null;


	/**
	 * Validates and saves the updated subscriber details.
	 * 
	 * @param event The ActionEvent triggered by clicking the Save button.
	 * @throws Exception If an error occurs during the operation.
	 */
	public void signUpBtn(Event event) {
		Integer id;
		try {
			id = Integer.valueOf(txtId.getText());
		}
		catch(Exception exception) {
			display("ID must have only digits", Color.RED);
			return;
		}
		if(!txtName.getText().isEmpty()) {
			String regex = "^[A-Za-z]{1,99}";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(txtName.getText());
			if(!matcher.matches()) {
				display("Name must have only english letters", Color.RED); // Displays an error message for invalid phone number.
				return;
			}
		}
		else {
			display("Please enter name", Color.RED);
			return;
		}
		if(!txtPhone.getText().isEmpty()) {
			try {
				Long.parseLong(txtPhone.getText()); // Validates that the phone number contains only digits.
			} catch (Exception e) {
				display("Phone must have only digits", Color.RED); // Displays an error message for invalid phone number.
				return;
			}
		}
		else {
			display("Please enter phone number", Color.RED);
			return;
		}
		
		if(!txtEmail.getText().isEmpty()) {
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
		}
		else {
			display("Please enter email address", Color.RED);
			return;
		}
		
			// Updates the subscriber's email and phone details.
		Subscriber subscriber = new Subscriber(id, txtName.getText(), txtPhone.getText(), txtEmail.getText());

		// Attempts to save the updated subscriber details.
		String password = IPController.client.registerSubscriber(subscriber);
		if (password != null) {
			display("Account successfully created!", Color.GREEN); // Displays a success message if save is successful.
			lblPassword.setVisible(true);
			txtPassword.setText(password);
			return;
		}
		// Displays an error message if save fails.
		display("Account with such ID already exists", Color.RED);
		return;
	}

	/**
	 * Closes the application when the Close button is clicked.
	 * 
	 * @param event The ActionEvent triggered by clicking the Close button.
	 * @throws Exception If an error occurs during the operation.
	 */
	public void backBtn(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/gui/client/"+ "LibrarianClientGUIFrame" +".fxml").openStream());
		LibrarianClientGUIController librarianClientGUIController = loader.getController();
		librarianClientGUIController.loadLibrarian();
		nextPage(loader, root, event, "Librarian Main Menu");
	}
	
	// Enables the enter key to activate the OK button
	public void handleKey(KeyEvent event) {
		if(event.getCode().equals(KeyCode.ENTER)) {
			signUpBtn(event);
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
