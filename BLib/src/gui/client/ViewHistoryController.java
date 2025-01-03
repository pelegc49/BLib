package gui.client;

import java.time.LocalDate;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.Activity; 

/**
 * The AuthenticationController class handles user authentication. 
 * It manages the login process, including ID and password validation, 
 * and transitions the user to the main application interface upon successful login.
 */
public class ViewHistoryController{
	
	@FXML
	private Button btnBack = null; // Button for exiting the application.
	@FXML
	private TableView<Activity> historyTable;
	@FXML
	private TableColumn<Activity, String> typeColumn;
	@FXML
	private TableColumn<Activity, String> descriptionColumn;
	@FXML
	private TableColumn<Activity, LocalDate> dateColumn;
	
	public void loadHistory(int subID) {
		List<Activity> activities = IPController.client.getSubscriberHistory(subID);
		ObservableList<Activity> data;
		data = FXCollections.observableArrayList();
		for(Activity ac : activities) {
			data.add(ac);
		}
		typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
		descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
		dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
		historyTable.setItems(data);
		historyTable.getSortOrder().add(dateColumn);
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
	    String title;
	    if(currentTitle.split(" ")[0].equals("Subscriber")) {
	    	title = "Subscriber Main Menu";
	    	nextPage(event, "SubscriberClientGUIFrame", title);
	    }
	    else{
	    	title = "Librarian Main Menu";
	    	nextPage(event, "LibrarianClientGUIFrame", title);
	    }
	}

	public void nextPage(Event event, String fileName, String title) throws Exception{
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
