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
import logic.Subscriber; 

/**
 * The AuthenticationController class handles user authentication. 
 * It manages the login process, including ID and password validation, 
 * and transitions the user to the main application interface upon successful login.
 */
public class SubscriberClientGUIController {

	@FXML
	private Button btnBack = null; // Button for exiting the application.
	@FXML
	private Button btnOrder = null; // Button for submitting the login form.
	@FXML
	private Button btnExtendTime = null; // Button for submitting the login form.
	@FXML
	private Button btnUpdateDetails = null; // Button for submitting the login form.
	@FXML
	private Button btnViewHistory = null; // Button for submitting the login form.
	@FXML
	private Button btnSearch = null; // Button for submitting the login form.

	public void backBtn(ActionEvent event) throws Exception {
		nextPage(event, "AuthenticationFrame", "Authentication");
	}
	
	public void orderBtn(ActionEvent event) throws Exception {
		nextPage(event, "OrderBookFrame", "Order Book");
	}
	
	public void extendTimeBtn(ActionEvent event) throws Exception {
		nextPage(event, "ExtendTimeFrame", "Extend Time");
	}
	
	public void updateDetailsBtn(ActionEvent event) throws Exception {
		nextPage(event, "UpdateDetailsFrame", "Update Details");
	}
	
	public void viewHistoryBtn(ActionEvent event) throws Exception {
		nextPage(event, "ViewHistoryFrame", "View History");
	}
	
	public void searchBtn(ActionEvent event) throws Exception {
		nextPage(event, "SearchFrame", "Search");
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
