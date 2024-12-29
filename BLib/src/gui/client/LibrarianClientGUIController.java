package gui.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.Subscriber; 

/**
 * The AuthenticationController class handles user authentication. 
 * It manages the login process, including ID and password validation, 
 * and transitions the user to the main application interface upon successful login.
 */
public class LibrarianClientGUIController {
	private UpdateDetailsController udc; // Reference to the main GUI controller.

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
		//nextPage(event, loader, "OrderBookFrame", "Order Book");
	}
	
	public void extendTimeBtn(ActionEvent event) throws Exception {
		//nextPage(event, loader, "ExtendTimeFrame", "Extend Time");
	}
	
	public void updateDetailsBtn(ActionEvent event) throws Exception {
		// FXMLLoader for loading the main GUI.
		FXMLLoader loader = new FXMLLoader(); 
		// Hide the current window.
		((Node) event.getSource()).getScene().getWindow().hide();

		// Load the main application interface.
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/gui/client/"+ "UpdateDetailsFrame" +".fxml").openStream());
		UpdateDetailsController updateDetailsController = loader.getController();
		updateDetailsController.loadSubscriber(AuthenticationController.subscriber);
		// Set up and display the new scene.
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/client/"+ "UpdateDetailsFrame" +".css").toExternalForm());
		primaryStage.setOnCloseRequest((E) -> System.exit(0));
		primaryStage.setTitle("Update Details");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public void viewHistoryBtn(ActionEvent event) throws Exception {
		//nextPage(event, loader, "ViewHistoryFrame", "View History");
	}
	
	public void searchBtn(ActionEvent event) throws Exception {
		nextPage(event, "SearchFrame", "Subscriber - Search");
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
	
	/**
	 * Loads the subscriber's data into the main GUI controller.
	 * 
	 * @param s1 The authenticated subscriber.
	 */
	public void loadSubscriber(Subscriber s1) {
		this.udc.loadSubscriber(s1);
	}
}
