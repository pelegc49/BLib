package gui.client;

import java.time.LocalDate;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.BookCopy;
import logic.Borrow;
import logic.Subscriber; 

/**
 * The AuthenticationController class handles user authentication. 
 * It manages the login process, including ID and password validation, 
 * and transitions the user to the main application interface upon successful login.
 */
public class SubscriberReaderCardController {
	public static Subscriber subscriber; // Static reference to the currently authenticated subscriber.

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
	private TextField txtSearch = null; // Button to initiate the connection to the server.
	@FXML
	private TableView<Borrow> bookTable; // Button to exit the application.
	@FXML
	private TableColumn<Borrow, String> columnBookId; // Label to display error messages.
	@FXML
	private TableColumn<Borrow, String> columnTitle; // Label to display error messages.
	@FXML
	private TableColumn<Borrow, String> columnAuthorName; // Label to display error messages.
	@FXML
	private TableColumn<Borrow, String> lblDueDate; // Label to display error messages.

	
	public void loadSubscriber(Subscriber subscriber) {
		this.txtStatus.setText(subscriber.getStatus());
		this.txtId.setText(String.valueOf(subscriber.getId()));
		this.txtName.setText(subscriber.getName()); // Sets the subscriber's ID.
		this.txtPhone.setText(subscriber.getPhone()); // Sets the subscriber's name.
		this.txtEmail.setText(subscriber.getEmail()); // Sets the subscriber's phone.
	}
	
	public void loadBorrows(Subscriber subscriber) {
		ObservableList<Borrow> data;
		List<Borrow> borrows = IPController.client.getSubscriberBorrows(subscriber);
		data = FXCollections.observableArrayList();
		for(Borrow borrow : borrows) {
			data.add(borrow);
		}
		
		if(data.isEmpty()) {
			lblDueDate.setText("No borrows");
		}
		else {
			columnBookId.setCellValueFactory(new PropertyValueFactory<>("copyId"));
			columnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
			columnAuthorName.setCellValueFactory(new PropertyValueFactory<>("author"));
			lblDueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
			bookTable.setItems(data);
			bookTable.getSortOrder().add(columnBookId);
		}
	}
	
	public void historyBtn(ActionEvent event) throws Exception {
		//nextPage();
	}

	/**
	 * Handles the "Exit" button action. Terminates the application.
	 * 
	 * @param event The action event triggered by clicking the button.
	 * @throws Exception If an error occurs during termination.
	 */
	public void bakcBtn(ActionEvent event) throws Exception {
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
