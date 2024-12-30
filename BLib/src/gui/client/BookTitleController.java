package gui.client;

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
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.BookTitle;

public class BookTitleController {
	private BookTitle tb;
	// UI elements defined in the FXML file.
	@FXML
	private Label lblTitle; // Label to display error messages.
	@FXML
	private Label lblAuthorName; // Label to display error messages.
	@FXML
	private Label lblDescription; // Label to display error messages.
	@FXML
	private Label lblError; // Label to display error messages.
	@FXML
	private Text txtTitle; // Text field to input the server IP address.
	@FXML
	private Text txtAuthorName; // Text field to input the server IP address.
	@FXML
	private Text txtDescription; // Text field to input the server IP address.
	@FXML
	private Button btnExit = null; // Button to exit the application.
	@FXML
	private Button btnSend = null; // Button to initiate the connection to the server.
	@FXML
	private TableView bookTable; // Button to exit the application.
	@FXML
	private TableColumn columnBookId; // Button to exit the application.
	@FXML
	private TableColumn columnStatus; // Button to exit the application.
	@FXML
	private TableColumn columnShelf; // Button to exit the application.
	@FXML
	private TableColumn columnDateOfReturn; // Button to exit the application.
	

	/**
	 * Handles the Exit button click event. Closes the application.
	 * 
	 * @param event The ActionEvent triggered by clicking the Exit button.
	 * @throws Exception If an error occurs during the operation.
	 */
	public void backBtn(ActionEvent event) throws Exception {
		Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    String currentTitle = currentStage.getTitle();
	    String title;
	    if(currentTitle.equals("Subscriber - BookTitle")) {
	    	title = "Subscriber - Search";
	    }
	    else if(currentTitle.equals("Librarian - BookTitle")) {
	    	title = "Librarian - Search";
	    }
	    else {
	    	title = "Guest - Search";
	    }
	    nextPage(event, "SearchFrame", title);
	}

	public void orderBtn(ActionEvent event) throws Exception {
	}
	
	public void loadBookTitle(BookTitle tb1) {
		this.tb = tb1; // Assigns the subscriber to the controller.
		this.txtTitle.setText(String.valueOf(tb.getTitleName())); // Sets the subscriber's ID.
		this.txtAuthorName.setText(tb.getAuthorName()); // Sets the subscriber's name.
		this.txtDescription.setText(tb.getDescription()); // Sets the subscriber's phone.
		
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
