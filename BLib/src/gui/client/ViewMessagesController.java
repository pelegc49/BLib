package gui.client;

import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * The AuthenticationController class handles user authentication. 
 * It manages the login process, including ID and password validation, 
 * and transitions the user to the main application interface upon successful login.
 */
public class ViewMessagesController {
	@FXML
	private Button btnBack = null; // Button for exiting the application.
	@FXML
	private Button btnClearAllMessages = null;
	@FXML
	private TableView<String> messageTable;
	@FXML
	private TableColumn<String, String> messageColumn;


	public void loadMessages() {
		List<String> messages = IPController.client.getLibrarianMessages();
		ObservableList<String> data = FXCollections.observableArrayList(messages);

		messageColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
		messageTable.setItems(data);
	}

	public void clearAllMessages() {
		IPController.client.clearLibrarianMessages();
		loadMessages();
	}


	/**
	 * Handles the "Exit" button action. Terminates the application.
	 * 
	 * @param event The action event triggered by clicking the button.
	 * @throws Exception If an error occurs during termination.
	 */
	public void backBtn(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/client/"+ "LibrarianClientGUIFrame" +".fxml"));
		Parent root = loader.load();
		LibrarianClientGUIController librarianClientGUIController = loader.getController();
		librarianClientGUIController.updateMessageCount();
		librarianClientGUIController.loadLibrarian();
		IPController.client.nextPage(loader, root, event, "Librarian Main Menu");
	}


}
