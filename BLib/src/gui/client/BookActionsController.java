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
import logic.BookCopy;
import logic.Message;
import logic.Subscriber; 

/**
 * The AuthenticationController class handles user authentication. 
 * It manages the login process, including ID and password validation, 
 * and transitions the user to the main application interface upon successful login.
 */
public class BookActionsController {

	@FXML
	private Label lblError; // Label for displaying error messages to the user.
	@FXML
	private TextField txtId; // TextField for user to enter their ID.
	@FXML
	private TextField bookIDTXT;
	@FXML
	private Button btnBorrow = null; 
	@FXML
	private Button btnExit = null;
	@FXML
	private Button btnReturn = null;
	
	
	public void Borrow(ActionEvent event) {
	    // Check if the ID text field is empty
	    if (txtId.getText().isEmpty()) {
	        display("No subscribers found in the database");
	        return;
	    }

	    Integer subID;
	    try {
	        // Attempt to parse the ID as an integer
	        subID = Integer.valueOf(txtId.getText());
	    } catch (NumberFormatException e) {
	        // Handle invalid input gracefully
	        display("Invalid subscriber ID. Please enter a valid number.");
	        return;
	    }

	    // Search for the subscriber using the provided ID
	    Subscriber searchedSub = IPController.client.getSubscriber(subID);

	    Integer bookID;
	    try {
	        // Attempt to parse the ID as an integer
	    	bookID = Integer.valueOf(bookIDTXT.getText());
	    } catch (NumberFormatException e) {
	        // Handle invalid input gracefully
	        display("Invalid Book ID. Please enter a valid number.");
	        return;
	    }

	    // Search for the subscriber using the provided ID
	    BookCopy searchedBook = IPController.client.getCopyByID(bookID);
	    
	    IPController.client.borrowBook(searchedBook.getTitle(), searchedSub);
	}

	
	public void Return(ActionEvent event) throws Exception {
		Integer bookID;
	    try {
	        // Attempt to parse the ID as an integer
	    	bookID = Integer.valueOf(bookIDTXT.getText());
	    } catch (NumberFormatException e) {
	        // Handle invalid input gracefully
	        display("Invalid Book ID. Please enter a valid number.");
	        return;
	    }

	    // Search for the subscriber using the provided ID
	    BookCopy searchedBook = IPController.client.getCopyByID(bookID);
	    
	    Message msg = IPController.client.returnBook(searchedBook);
	    System.out.println(msg.getCommand());
	}
	
		
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
