package gui.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label; 

/**
 * The SubscriberClientGUIController class manages the user interface for the subscriber. 
 * It provides various functions such as updating subscriber details, extending borrowing time, 
 * viewing the borrow history, and searching for resources.
 */
public class SubscriberClientGUIController {

	@FXML
	private Label lblTitle; // Displays a welcome message to the subscriber.
	@FXML
	private Label lblName; // Displays the subscriber's name.
	@FXML
	private Label lblId; // Displays the subscriber's ID.
	@FXML
	private Button btnBack = null; // Button to navigate back to the authentication screen.
	@FXML
	private Button btnExtendTime = null; // Button to extend the borrowing time for resources.
	@FXML
	private Button btnUpdateDetails = null; // Button to update the subscriber's personal details.
	@FXML
	private Button btnViewHistory = null; // Button to view the subscriber's borrowing history.
	@FXML
	private Button btnSearch = null; // Button to search for resources.

	/**
	 * Loads the subscriber's information into the UI elements.
	 * Displays the subscriber's name and ID.
	 */
	public void loadSubscriber() {
		this.lblTitle.setText("Welcome, "+AuthenticationController.subscriber.getName().split(" ")[0]+"!");
		this.lblName.setText("Name: "+AuthenticationController.subscriber.getName());
		this.lblId.setText("Subscriber ID: "+AuthenticationController.subscriber.getId());
	}
	
	/**
	 * Handles the "Back" button action. Navigates back to the authentication screen.
	 * 
	 * @param event The action event triggered by clicking the "Back" button.
	 * @throws Exception If an error occurs during navigation.
	 */
	public void backBtn(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/client/"+ "AuthenticationFrame" +".fxml")); 
		Parent root = loader.load();
		AuthenticationController authenticationController = loader.getController();
		authenticationController.loadImage();
		IPController.client.nextPage(loader, root, event, "Authentication");
	}
	
	/**
	 * Handles the "Extend Time" button action. Navigates to the page where the subscriber can extend their borrow time.
	 * 
	 * @param event The action event triggered by clicking the "Extend Time" button.
	 * @throws Exception If an error occurs during navigation.
	 */
	public void extendTimeBtn(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/client/"+ "ExtendTimeFrame" +".fxml")); 
		Parent root = loader.load();
		ExtendTimeController extendTimeController = loader.getController();
		extendTimeController.loadBorrows(AuthenticationController.subscriber);
		IPController.client.nextPage(loader, root, event, "Subscriber - Extend Time");
	}
	
	/**
	 * Handles the "Update Details" button action. Navigates to the page where the subscriber can update their details.
	 * 
	 * @param event The action event triggered by clicking the "Update Details" button.
	 * @throws Exception If an error occurs during navigation.
	 */
	public void updateDetailsBtn(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/client/"+ "UpdateDetailsFrame" +".fxml")); 
		Parent root = loader.load();
		UpdateDetailsController updateDetailsController = loader.getController();
		updateDetailsController.loadSubscriber(AuthenticationController.subscriber);
		IPController.client.nextPage(loader, root, event, "Update Details");
	}
	
	/**
	 * Handles the "View History" button action. Navigates to the page where the subscriber can view their borrowing history.
	 * 
	 * @param event The action event triggered by clicking the "View History" button.
	 * @throws Exception If an error occurs during navigation.
	 */
	public void viewHistoryBtn(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/client/"+ "ViewHistoryFrame" +".fxml"));
		Parent root = loader.load();
		ViewHistoryController viewHistoryController = loader.getController();
		viewHistoryController.loadHistory(AuthenticationController.subscriber);
		IPController.client.nextPage(loader, root, event, "Subscriber - View History");
	}
	
	/**
	 * Handles the "Search" button action. Navigates to the page where the subscriber can search for resources.
	 * 
	 * @param event The action event triggered by clicking the "Search" button.
	 * @throws Exception If an error occurs during navigation.
	 */
	public void searchBtn(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/client/"+ "SearchFrame" +".fxml"));
		Parent root = loader.load();
		IPController.client.nextPage(loader, root, event, "Subscriber - Search");
	}
}
