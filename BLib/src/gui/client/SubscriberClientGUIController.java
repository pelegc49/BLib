package gui.client;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage; 

/**
 * The AuthenticationController class handles user authentication. 
 * It manages the login process, including ID and password validation, 
 * and transitions the user to the main application interface upon successful login.
 */
public class SubscriberClientGUIController {

	@FXML
	private Label lblTitle; // Button for exiting the application.
	@FXML
	private Label lblName; // Button for exiting the application.
	@FXML
	private Label lblId; // Button for exiting the application.
	@FXML
	private Button btnBack = null; // Button for exiting the application.
	@FXML
	private Button btnExtendTime = null; // Button for submitting the login form.
	@FXML
	private Button btnUpdateDetails = null; // Button for submitting the login form.
	@FXML
	private Button btnViewHistory = null; // Button for submitting the login form.
	@FXML
	private Button btnSearch = null; // Button for submitting the login form.

	public void loadSubscriber() {
		this.lblTitle.setText("Welcome, "+AuthenticationController.subscriber.getName().split(" ")[0]+"!");
		this.lblName.setText("Name: "+AuthenticationController.subscriber.getName());
		this.lblId.setText("Subscriber ID: "+AuthenticationController.subscriber.getId());
	}
	
	public void backBtn(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader(); 
		Pane root = loader.load(getClass().getResource("/gui/client/"+ "AuthenticationFrame" +".fxml").openStream());
		AuthenticationController authenticationController = loader.getController();
		authenticationController.loadImage();
		nextPage(loader, root, event, "Authentication");
	}
	
	public void extendTimeBtn(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader(); 
		Pane root = loader.load(getClass().getResource("/gui/client/"+ "ExtendTimeFrame" +".fxml").openStream());
		ExtendTimeController extendTimeController = loader.getController();
		extendTimeController.loadBorrows(AuthenticationController.subscriber);
		nextPage(loader, root, event, "Subscriber - Extend Time");
	}
	
	public void updateDetailsBtn(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader(); 
		Pane root = loader.load(getClass().getResource("/gui/client/"+ "UpdateDetailsFrame" +".fxml").openStream());
		UpdateDetailsController updateDetailsController = loader.getController();
		updateDetailsController.loadSubscriber(AuthenticationController.subscriber);
		nextPage(loader, root, event, "Update Details");
	}
	
	public void viewHistoryBtn(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/gui/client/"+ "ViewHistoryFrame" +".fxml").openStream());
		ViewHistoryController viewHistoryController = loader.getController();
		viewHistoryController.loadHistory(AuthenticationController.subscriber);
		nextPage(loader, root, event, "Subscriber - View History");
	}
	
	public void searchBtn(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/gui/client/"+ "SearchFrame" +".fxml").openStream());
		nextPage(loader, root, event, "Subscriber - Search");
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
