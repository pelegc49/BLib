package gui.client;

import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.BookTitle; 

/**
 * The AuthenticationController class handles user authentication. 
 * It manages the login process, including ID and password validation, 
 * and transitions the user to the main application interface upon successful login.
 */
public class SearchController {
	
	@FXML
	private TextField txtSearch;
	@FXML
	private Label lblError; // Label for displaying error messages to the user.
	@FXML
	private Button btnBack = null; // Button for exiting the application.
	@FXML
	private Button btnSearch = null; // Button for submitting the login form.
	@FXML
	private TableView<BookTitle> bookTable;
	@FXML
	private TableColumn<BookTitle, String> authorColumn;
	@FXML
	private TableColumn<BookTitle, String> titleColumn;
	
	
	public void searchBtn(Event event) {
		ObservableList<BookTitle> data;
		String keyword = txtSearch.getText();
		System.out.println("keyword = "+keyword);
		Set<BookTitle> bookTitle = IPController.client.getTitlesByKeyword(keyword);
		System.out.println(bookTitle);
		if(bookTitle == null) {
			display("No result found");
		}
		else {
			data = FXCollections.observableArrayList();
			for(BookTitle bt : bookTitle) {
				data.add(bt);
			}
			authorColumn.setCellValueFactory(new PropertyValueFactory<>("Author"));
			titleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
			bookTable.setItems(data);
		}
		
		
//		String id; // String to store the entered ID.
//		FXMLLoader loader = new FXMLLoader(); // FXMLLoader for loading the main GUI.
//		int digit_id = 0; // Variable to hold the numeric value of the ID.
//		id = txtId.getText(); // Retrieve the text entered in the ID field.
//		String password = txtPassword.getText(); // Retrieve the text entered in the password field.
//
//		try {
//			// Attempt to parse the ID to an integer.
//			digit_id = Integer.parseInt(id);
//		} catch (Exception e) {
//			// Display an error message if the ID is not numeric.
//			display("bad username - only digits");
//		}
//
//		// Validate that the ID field is not empty.
//		if (id.trim().isEmpty()) {
//			display("You must enter an id number");
//		} 
//		// Validate that the password field is not empty.
//		else if (txtPassword.getText().isEmpty()) {
//			display("You must enter a password");
//		} 
//		// If both fields are valid, attempt to log in.
//		else {
//			switch(IPController.client.login(digit_id, password)) {
//				case "subscriber":
//					subscriber = IPController.client.getSubscriber(digit_id);
//					nextPage(event, "SubscriberClientGUIFrame", "Subscriber Main Menu");
//					break;
//				case "librarian":
//					nextPage(event, "LibrarianClientGUIFrame", "Librarian Main Menu");
//					break;
//				case "fail":
//					display("ID or password are incorrect");
//					break;
//				// For debugging purposes. delete me later \/
//				default:
//					System.out.println("Developers! Something went wrong at login...");
//			}
//		}
	}

	/**
	 * Handles the "Exit" button action. Terminates the application.
	 * 
	 * @param event The action event triggered by clicking the button.
	 * @throws Exception If an error occurs during termination.
	 */
	public void backBtn(ActionEvent event) throws Exception {
		Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    String currentTitle = currentStage.getTitle();
	    if(currentTitle.equals("Subscriber - Search")) {
	    	nextPage(event, "SubscriberClientGUIFrame", "Subscriber Main Menu");
	    }
	    else if(currentTitle.equals("Librarian - Search")) {
	    	nextPage(event, "LibrarianClientGUIFrame", "Librarian Main Menu");
	    }
	    else {
	    	nextPage(event, "AuthenticationFrame", "Authentication");
	    }
	}

	/**
	 * Displays an error or informational message to the user.
	 * 
	 * @param message The message to display.
	 */
	public void display(String message) {
		lblError.setText(message);
	}
	
	// Enables the enter key to activate the OK button
	public void handleKey(KeyEvent event) {
		if(event.getCode().equals(KeyCode.ENTER)) {
			searchBtn(event);
		}
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
