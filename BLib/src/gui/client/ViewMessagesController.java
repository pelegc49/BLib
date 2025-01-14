package gui.client;

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.Activity;
import logic.Subscriber;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

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
		FXMLLoader loader = new FXMLLoader();

		// Hide the current window.
		((Node) event.getSource()).getScene().getWindow().hide();

		// Load the main application interface.
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/gui/client/"+ "LibrarianClientGUIFrame" +".fxml").openStream());

		// Set up and display the new scene.
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/client/"+ "LibrarianClientGUIFrame" +".css").toExternalForm());
		primaryStage.setOnCloseRequest((E) -> System.exit(0));
		primaryStage.setTitle("Librarian Main Menu");
		primaryStage.setScene(scene);
		primaryStage.show();



	}

}
