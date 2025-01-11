package gui.client;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import logic.BookTitle;
import logic.Subscriber; 

/**
 * The AuthenticationController class handles user authentication. 
 * It manages the login process, including ID and password validation, 
 * and transitions the user to the main application interface upon successful login.
 */
public class SubscriberListController{
	
	private List<Subscriber> allSubscribers = IPController.client.getAllSubscribers();
	
	@FXML
	private TextField txtSearch;
	@FXML
	private Label lblError; // Label for displaying error messages to the user.
	@FXML
	private Button btnBack = null; // Button for exiting the application.
	@FXML
	private Button btnSearch = null; // Button for submitting the login form.
	@FXML
	private TableView<Subscriber> subTable;
	@FXML
	private TableColumn<Subscriber, Integer> idColumn;
	@FXML
	private TableColumn<Subscriber, String> nameColumn;
	@FXML
	private TableColumn<Subscriber, String> phoneColumn;
	@FXML
	private TableColumn<Subscriber, String> emailColumn;
	@FXML
	private TableColumn<Subscriber, String> statusColumn;
	
	
	public void searchBtn(Event event) {
		String id, phone, name, email;
	    ObservableList<Subscriber> data = FXCollections.observableArrayList();
        //List<Subscriber> allSubscribers = IPController.client.getAllSubscribers();
        
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        String text = txtSearch.getText().toLowerCase();
        
        if (text.isEmpty()){
        	data.addAll(allSubscribers);
        }
        else {
        	for(Subscriber subscriber : allSubscribers) {
	    		id = String.valueOf(subscriber.getId()).toLowerCase();
	    		phone = subscriber.getPhone().toLowerCase();
	    		name = subscriber.getName().toLowerCase();
	    		email = subscriber.getEmail().toLowerCase();
	    		if(id.contains(String.valueOf(text)) ||
	    		   phone.contains(String.valueOf(text)) ||
	    		   name.contains(String.valueOf(text)) ||
	    		   email.contains(String.valueOf(text)))
	    		{
	    			data.add(subscriber);
	    		}
	    	}
        }
        subTable.setItems(data);
        
	 // allows to click on row
	 		subTable.setRowFactory(tv -> {
	 		    TableRow<Subscriber> rowa = new TableRow<>();
	 		    rowa.setOnMouseClicked(eventa -> {
	 		        if (eventa.getClickCount() == 2 && !rowa.isEmpty()) {
	 		        	Subscriber rowData = rowa.getItem();
	 		    	    
	 		    		// FXMLLoader for loading the main GUI.
	 		    		FXMLLoader loader = new FXMLLoader(); 
	 		    		// Hide the current window.
	 		    		((Node) event.getSource()).getScene().getWindow().hide();

	 		    		// Load the main application interface.
	 		    		Stage primaryStage = new Stage();
	 		    		Pane root = null;
	 					try {
	 						root = loader.load(getClass().getResource("/gui/client/"+ "SubscriberReaderCardFrame" +".fxml").openStream());
	 					} catch (IOException e) {
	 						e.printStackTrace();
	 					}
	 					SubscriberReaderCardController subscriberReaderCardController = loader.getController();
	 					subscriberReaderCardController.loadSubscriber(rowData);
	 					subscriberReaderCardController.loadBorrows(rowData);
	 		    		// Set up and display the new scene.
	 		    		Scene scene = new Scene(root);
	 		    		scene.getStylesheets().add(getClass().getResource("/gui/client/"+ "SubscriberReaderCardFrame" +".css").toExternalForm());
	 		    		primaryStage.setOnCloseRequest((E) -> System.exit(0));
	 		    		primaryStage.setTitle("Subscriber's Reader Card");
	 		    		primaryStage.setScene(scene);
	 		    		primaryStage.show();
	 		        }
	 		    });
	 	    return rowa ;
	 		});
	}
	

	/**
	 * Handles the "Exit" button action. Terminates the application.
	 * 
	 * @param event The action event triggered by clicking the button.
	 * @throws Exception If an error occurs during termination.
	 */
	public void backBtn(ActionEvent event) throws Exception {
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
