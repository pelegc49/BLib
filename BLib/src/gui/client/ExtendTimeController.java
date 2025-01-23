package gui.client;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map.Entry;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;
import logic.Borrow;
import logic.BorrowPlus;
import logic.Message;
import logic.Subscriber;

/**
 * The AuthenticationController class handles user authentication. 
 * It manages the login process, including ID and password validation, 
 * and transitions the user to the main application interface upon successful login.
 */
public class ExtendTimeController{
	@FXML
	private Label lblError; // Label for displaying error messages to the user.
	@FXML
	private Button btnBack = null; // Button for exiting the application.
	@FXML
	private Button btnSearch = null; // Button for submitting the login form.
	@FXML
	private Button btnExtend = null; // Button for submitting the login form.
	@FXML
	private TableView<Entry<BorrowPlus, Borrow>> tableBook;
	@FXML
	private TableColumn<Entry<BorrowPlus, Borrow>, CheckBox> columnCheckBox;
	@FXML
	private TableColumn<Entry<BorrowPlus, Borrow>, String> columnAuthor;
	@FXML
	private TableColumn<Entry<BorrowPlus, Borrow>, String> columnTitle;
	@FXML
	private TableColumn<Entry<BorrowPlus, Borrow>, String> columnDueDate;
	@FXML
	private TableColumn<Entry<BorrowPlus, Borrow>, String> columnErrorMessage;
	@FXML
	private CheckBox checkBoxSelectAll;
	
	/**
	 * Handles the "Exit" button action. Terminates the application.
	 * 
	 * @param event The action event triggered by clicking the button.
	 * @throws Exception If an error occurs during termination.
	 */
	public void backBtn(ActionEvent event) throws Exception {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/client/"+ "SubscriberClientGUIFrame" +".fxml"));
    	Parent root = loader.load();
    	SubscriberClientGUIController subscriberClientGUIController = loader.getController();
    	subscriberClientGUIController.loadSubscriber();
    	IPController.client.nextPage(loader, root, event, "Subscriber Main Menu");
	}

	/**
	 * Handles the "extend" button action. Extends the selected borrows by 7 days.
	 *
	 * @param event The action event triggered by clicking the button.
	 * @throws Exception If an error occurs during the operation.
	 */
	public void extendBtn(ActionEvent event) {
		// Extend the selected borrows by 7 days
	    for(Entry<BorrowPlus, Borrow> entry : tableBook.getItems()) {
	    	Borrow borrow = entry.getValue();
	    	BorrowPlus borrowPlus = entry.getKey();
	        if (borrowPlus.getCheckBox().isSelected()) {
	            Message msg = IPController.client.extendDuration(borrow, 7, "subscriber");
	            if (msg.getCommand().equals("failed")) {
                    if(((String) msg.getArguments().get(0)).equals("the subscriber is frozen")) {
                        IPController.client.display(lblError,"Your account is suspended", Color.RED);
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
		// Refresh the table
	    checkBoxSelectAll.setSelected(false);
	    selectAllBtn(event);
	    tableBook.refresh();
	}

	/**
	 * loads the borrows of the subscriber to the table.
	 *
	 * @param subscriber The subscriber whose borrows are to be loaded.
	 */
	public void loadBorrows(Subscriber subscriber) {
		// Load the borrows of the subscriber to the table

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

	/**
	 * Handles the "Select All" button action. Selects all the borrows in the table.
	 *
	 * @param event The action event triggered by clicking the button.
	 */
	public void selectAllBtn(Event event) {
		for (Entry<BorrowPlus, Borrow> entry : tableBook.getItems()) {
			entry.getKey().setCheckBox(checkBoxSelectAll.isSelected());
		}
	}

}
