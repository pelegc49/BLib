package gui.client;

import java.util.List;
import java.util.stream.Collectors;

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
import logic.BorrowWithCheckBox;
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
	private TableView<BorrowWithCheckBox> bookTable; // Button to exit the application.
	@FXML
	private TableColumn<BorrowWithCheckBox, CheckBox> columnCheckBox;
	@FXML
	private TableColumn<BorrowWithCheckBox, String> columnBookId; // Label to display error messages.
	@FXML
	private TableColumn<BorrowWithCheckBox, String> columnTitleName; // Label to display error messages.
	@FXML
	private TableColumn<BorrowWithCheckBox, String> columnAuthorName; // Label to display error messages.
	@FXML
	private TableColumn<BorrowWithCheckBox, String> columnDueDate; // Label to display error messages.
	@FXML
	private TableColumn<BorrowWithCheckBox, String> columnErrorMessage;
	@FXML
	private CheckBox selectAllCheckBox;

	
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
		ObservableList<BorrowWithCheckBox> data;
		List<Borrow> borrows = IPController.client.getSubscriberBorrows(subscriber);
		
		
		List<BorrowWithCheckBox> borrowWithCheckBoxes = borrows.stream()
				.map(borrowWCB -> new BorrowWithCheckBox(
						borrowWCB.getSubscriber(),
						borrowWCB.getBook(),
						borrowWCB.getDateOfBorrow(),
						borrowWCB.getDueDate(),
						borrowWCB.getDateOfReturn()))
				.collect(Collectors.toList());
		
		data = FXCollections.observableArrayList();
		for(BorrowWithCheckBox borrowWCB : borrowWithCheckBoxes) {
			data.add(borrowWCB);
		}
		
		if(data.isEmpty()) {
			lblTable.setText("No borrows");
		}
		else {
			columnCheckBox.setCellValueFactory(new PropertyValueFactory<>("selected"));
			columnBookId.setCellValueFactory(new PropertyValueFactory<>("copyId"));
			columnTitleName.setCellValueFactory(new PropertyValueFactory<>("title"));
			columnAuthorName.setCellValueFactory(new PropertyValueFactory<>("author"));
			columnDueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
			columnErrorMessage.setCellValueFactory(new PropertyValueFactory<>("errorMessage"));
			
			bookTable.setItems(data);
			bookTable.getSortOrder().add(columnBookId);
		}
	}
	
	public void searchBtn(ActionEvent event) {}
	
	public void extendBtn(ActionEvent event) {
	    boolean flag = false;
	    for (BorrowWithCheckBox borrowWCB : bookTable.getItems()) {
	        if (borrowWCB.isSelected().isSelected()) {
	    		Borrow borrow = new Borrow(
	    				borrowWCB.getSubscriber(),
	    				borrowWCB.getBook(),
	    				borrowWCB.getDateOfBorrow(),
	    				borrowWCB.getDueDate(),
	    				borrowWCB.getDateOfReturn());
	            Message msg = IPController.client.extendDuration(borrow, 7, "subscriber");
	            if (msg.getCommand().equals("failed")) {
                    if(((String) msg.getArguments().get(0)).equals("the subscriber is frozen")) {
                        display("Your account is suspended", Color.RED);
                        flag = true;
                        break;
                    }
                    else {
                    	borrowWCB.setErrorMessage((String) msg.getArguments().get(0));
                    }
                }
	            else {
	            	borrowWCB.setErrorMessage("Extended succeed");
	            }
            }
            if (flag) {
                break;
            }
        }
	    selectAllCheckBox.setSelected(false);
	    bookTable.refresh();
	    loadBorrowsAlready(subscriber, List.copyOf(bookTable.getItems()));
	}
	
	public void loadBorrowsAlready(Subscriber subscriber, List<BorrowWithCheckBox> bWCB) {
		this.subscriber = subscriber;
		ObservableList<BorrowWithCheckBox> data;
		List<Borrow> borrow = IPController.client.getSubscriberBorrows(subscriber);
		
		List<BorrowWithCheckBox> borrowWithCheckBoxes = borrow.stream()
				.map(borrowWCB -> new BorrowWithCheckBox(
						borrowWCB.getSubscriber(),
						borrowWCB.getBook(),
						borrowWCB.getDateOfBorrow(),
						borrowWCB.getDueDate(),
						borrowWCB.getDateOfReturn()))
				.collect(Collectors.toList());
		
		for(BorrowWithCheckBox b : borrowWithCheckBoxes) {
			for(BorrowWithCheckBox c : bWCB) {
				if(b.getBook().getTitle().equals(c.getBook().getTitle())) {
					b.setErrorMessage(c.getErrorMessage());
				}
			}
		}
		
		data = FXCollections.observableArrayList();
		for(BorrowWithCheckBox borrowWCB : borrowWithCheckBoxes) {
			data.add(borrowWCB);
		}
		
		columnCheckBox.setCellValueFactory(new PropertyValueFactory<>("selected"));
		columnAuthorName.setCellValueFactory(new PropertyValueFactory<>("author"));
		columnTitleName.setCellValueFactory(new PropertyValueFactory<>("title"));
		columnDueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
		columnErrorMessage.setCellValueFactory(new PropertyValueFactory<>("errorMessage"));
		
		bookTable.setItems(data);
		bookTable.getSortOrder().add(columnDueDate);
	}
	
	public void selectAllBtn(Event event) {
		for (BorrowWithCheckBox borrowWCB : bookTable.getItems()) {
			borrowWCB.setSelected(selectAllCheckBox.isSelected());
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
