package gui.client;

import java.util.List;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.Borrow;
import logic.BorrowPlus;
import logic.Message;
import logic.Subscriber; 

/**
 * The AuthenticationController class handles user authentication. 
 * It manages the login process, including ID and password validation, 
 * and transitions the user to the main application interface upon successful login.
 */
public class SubscriberReaderCardController {
	private Subscriber subscriber; // Static reference to the currently authenticated subscriber.

	@FXML
	private Label lblTable; // Text field to input the server IP address.
	@FXML
	private Label lblError; // Text field to input the server IP address.
	@FXML
	private Text txtStatus; // Text field to input the server IP address.
	@FXML
	private Text txtId; // Text field to input the server IP address.
	@FXML
	private Text txtName; // Text field to input the server IP address.
	@FXML
	private Text txtPhone; // Text field to input the server IP address.
	@FXML
	private Text txtEmail; // Text field to input the server IP address.
	@FXML
	private Button btnBack = null; // Button to exit the application.
	@FXML
	private Button btnHistory = null; // Button to initiate the connection to the server.
	@FXML
	private Button btnExtend = null; // Button to initiate the connection to the server.
	@FXML
	private TableView<Entry<BorrowPlus, Borrow>> tableBook; // Button to exit the application.
	@FXML
	private TableColumn<Entry<BorrowPlus, Borrow>, CheckBox> columnCheckBox;
	@FXML
	private TableColumn<Entry<BorrowPlus, Borrow>, String> columnBookId; // Label to display error messages.
	@FXML
	private TableColumn<Entry<BorrowPlus, Borrow>, String> columnTitle; // Label to display error messages.
	@FXML
	private TableColumn<Entry<BorrowPlus, Borrow>, String> columnAuthor; // Label to display error messages.
	@FXML
	private TableColumn<Entry<BorrowPlus, Borrow>, String> columnDueDate; // Label to display error messages.
	@FXML
	private TableColumn<Entry<BorrowPlus, Borrow>, String> columnErrorMessage;
	@FXML
	private CheckBox checkBoxSelectAll;

	public void loadSubscriber(Subscriber subscriber) {
		this.subscriber = subscriber;
		this.txtId.setText(String.valueOf(subscriber.getId()));
		this.txtName.setText(subscriber.getName()); // Sets the subscriber's ID.
		this.txtPhone.setText(subscriber.getPhone()); // Sets the subscriber's name.
		this.txtEmail.setText(subscriber.getEmail()); // Sets the subscriber's phone.
		if(subscriber.getStatus().equals("active")) {
			this.txtStatus.setFill(Color.GREEN);
		}
		else {
			this.txtStatus.setFill(Color.RED);
		}
		String capitalizedStatus = subscriber.getStatus().substring(0, 1).toUpperCase() + subscriber.getStatus().substring(1);
		this.txtStatus.setText(capitalizedStatus);
	}
	
	public void loadBorrows(Subscriber subscriber) {
		ObservableList<Entry<BorrowPlus, Borrow>> data = FXCollections.observableArrayList();
		List<Borrow> borrows = IPController.client.getSubscriberBorrows(subscriber);
		
		for (Borrow borrow : borrows) {
			data.add(new SimpleEntry<>(new BorrowPlus(), borrow));
		}
		
        columnCheckBox.setCellValueFactory(entry -> new SimpleObjectProperty<>(entry.getValue().getKey().getCheckBox()));
        columnAuthor.setCellValueFactory(entry -> new SimpleStringProperty(entry.getValue().getValue().getAuthor()));
        columnTitle.setCellValueFactory(entry -> new SimpleStringProperty(entry.getValue().getValue().getTitleName()));
        columnDueDate.setCellValueFactory(entry -> new SimpleStringProperty(entry.getValue().getValue().getDueDate().toString()));
        columnErrorMessage.setCellValueFactory(entry -> new SimpleStringProperty(entry.getValue().getKey().getErrorMessage()));
        
		tableBook.setItems(data);
		tableBook.getSortOrder().add(columnDueDate);
	}
	
	public void extendBtn(ActionEvent event) {
	    for(Entry<BorrowPlus, Borrow> entry : tableBook.getItems()) {
	    	Borrow borrow = entry.getValue();
	    	BorrowPlus borrowPlus = entry.getKey();
	        if (borrowPlus.getCheckBox().isSelected()) {
	            Message msg = IPController.client.extendDuration(borrow, 7, AuthenticationController.librarianName);
	            if (msg.getCommand().equals("failed")) {
                    if(((String) msg.getArguments().get(0)).equals("the subscriber is frozen")) {
                        display("Your account is suspended", Color.RED);
                        break;
                    }
                    borrowPlus.setErrorMessage((String) msg.getArguments().get(0));
                }
	            else {
	            	borrowPlus.setErrorMessage("Extend succeed");
	            	borrow.setDueDate(borrow.getDueDate().plusDays(7));
	            }
            }
        }
	    checkBoxSelectAll.setSelected(false);
	    selectAllBtn(event);
	    tableBook.refresh();
	}
	
	public void selectAllBtn(Event event) {
		for (Entry<BorrowPlus, Borrow> entry : tableBook.getItems()) {
			entry.getKey().setCheckBox(checkBoxSelectAll.isSelected());
		}
	}
	
	public void display(String message, Color color) {
		lblError.setTextFill(color); // Sets the color of the error label.
		lblError.setText(message); // Sets the text of the error label.
	}
	
	public void historyBtn(ActionEvent event) throws Exception {
		// FXMLLoader for loading the main GUI.
		FXMLLoader loader = new FXMLLoader();
		// Hide the current window.
		((Node) event.getSource()).getScene().getWindow().hide();

		// Load the main application interface.
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/gui/client/"+ "ViewHistoryFrame" +".fxml").openStream());
		ViewHistoryController viewHistoryController = loader.getController();
		viewHistoryController.loadHistory(subscriber);
		// Set up and display the new scene.
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/client/"+ "ViewHistoryFrame" +".css").toExternalForm());
		primaryStage.setOnCloseRequest((E) -> System.exit(0));
		primaryStage.setTitle("Librarian - View History");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Handles the "Exit" button action. Terminates the application.
	 * 
	 * @param event The action event triggered by clicking the button.
	 * @throws Exception If an error occurs during termination.
	 */
	public void backBtn(ActionEvent event) throws Exception {
		nextPage(event, "SubscriberListFrame", "Librarian Main Menu");
	}

	/**
	 * Displays an error or informational message to the user.
	 * 
	 * @param message The message to display.
	 */
//	public void display(String message) {
//		lblError.setText(message);
//	}
	
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
