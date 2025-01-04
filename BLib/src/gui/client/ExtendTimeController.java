package gui.client;

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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
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
public class ExtendTimeController{
	private int subID;
	@FXML
	private TextField txtSearch;
	@FXML
	private Label lblError; // Label for displaying error messages to the user.
	@FXML
	private Button btnBack = null; // Button for exiting the application.
	@FXML
	private Button btnSearch = null; // Button for submitting the login form.
	@FXML
	private Button btnExtend = null; // Button for submitting the login form.
	@FXML
	private TableView<Borrow> bookTable;
	@FXML
	private TableColumn<BorrowWithCheckBox, Boolean> checkBoxColumn;
	@FXML
	private TableColumn<Borrow, String> authorColumn;
	@FXML
	private TableColumn<Borrow, String> titleColumn;
	@FXML
	private TableColumn<Borrow, Boolean> dueDateColumn;
	@FXML
	private TableColumn<BorrowWithCheckBox, String> errorMessageColumn;
	@FXML
	private CheckBox checkBox;

	/**
	 * Handles the "Exit" button action. Terminates the application.
	 * 
	 * @param event The action event triggered by clicking the button.
	 * @throws Exception If an error occurs during termination.
	 */
	public void backBtn(ActionEvent event) throws Exception {
		Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    String currentTitle = currentStage.getTitle();
	    if(currentTitle.equals("Subscriber - Extend Time")) {
	    	nextPage(event, "SubscriberClientGUIFrame", "Subscriber Main Menu");
	    }
	    else {
	    	nextPage(event, "LibrarianClientGUIFrame", "Librarian Main Menu");
	    }
	}

	public void extendBtn(ActionEvent event) {
	    boolean flag = false;
	    for (Borrow borrow : bookTable.getItems()) {
	    	BorrowWithCheckBox borrowCheckBox = ((BorrowWithCheckBox)borrow);
	        if (borrowCheckBox.isSelected()) {
	            Message msg = IPController.client.extendDuration(borrow, 7);
	            if (msg.getCommand().equals("failed")) {
	                switch ((String) msg.getArguments().get(0)) {
	                    case "frozen":
	                        display("Your account is suspended", Color.RED);
	                        flag = true;
	                        break;
	                    case "moreThanWeek":
	                    	borrowCheckBox.setErrorMessage("moreThanWeek");
	                        break;
	                    case "thereAreOrder":
	                    	borrowCheckBox.setErrorMessage("thereAreOrder");
	                        break;
	                }
	            } else {
	            	borrowCheckBox.setErrorMessage("Extended succeed");
	            }
	            if (flag) {
	                break;
	            }
	        }
	    }
	    loadBorrows(subID);
	}
	
	public void loadBorrows(int subID) {
		this.subID = subID;
		ObservableList<Borrow> data;
		List<Borrow> borrows = IPController.client.getBorrowBySubscriberID(subID);
		data = FXCollections.observableArrayList();
		for(Borrow b : borrows) {
			data.add(b);
		}
		
		checkBoxColumn.setCellValueFactory(new PropertyValueFactory<>("selected"));
		authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
		titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
		dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
		errorMessageColumn.setCellValueFactory(new PropertyValueFactory<>("errorMessage"));
		
		bookTable.setItems(data);
		bookTable.getSortOrder().add(dueDateColumn);
	}
	
	/**
	 * Displays an error or informational message to the user.
	 * 
	 * @param message The message to display.
	 */
	public void display(String message, Color color) {
		lblError.setTextFill(color); // Sets the color of the error label.
		lblError.setText(message); // Sets the text of the error label.
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
