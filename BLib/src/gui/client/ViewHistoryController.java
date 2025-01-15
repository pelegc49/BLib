package gui.client;

import java.io.IOException;
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
import logic.Subscriber; 

/**
 * The AuthenticationController class handles user authentication. 
 * It manages the login process, including ID and password validation, 
 * and transitions the user to the main application interface upon successful login.
 */
public class ViewHistoryController{
	private Subscriber subscriber;
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
	
	public void loadHistory(Subscriber subscriber) {
		this.subscriber = subscriber;
		List<Activity> activities = IPController.client.getSubscriberHistory(subscriber.getId());
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
		FXMLLoader loader = new FXMLLoader();
		Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    String currentTitle = currentStage.getTitle();
	    if(currentTitle.split(" ")[0].equals("Subscriber")) {
	    	Pane root = loader.load(getClass().getResource("/gui/client/"+ "SubscriberClientGUIFrame" +".fxml").openStream());
	    	SubscriberClientGUIController subscriberClientGUIController = loader.getController();
	    	subscriberClientGUIController.loadSubscriber();
	    	nextPage(loader, root, event, "Subscriber Main Menu");
	    }
	    else{
	    	Pane root = loader.load(getClass().getResource("/gui/client/"+ "SubscriberReaderCardFrame" +".fxml").openStream());
			SubscriberReaderCardController subscriberReaderCardController = loader.getController();
			subscriberReaderCardController.loadSubscriber(subscriber);
			subscriberReaderCardController.loadBorrows(subscriber);
	    	nextPage(loader, root, event, "Subscriber's Reader Card");
	    }
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
