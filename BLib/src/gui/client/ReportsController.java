package gui.client;

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
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.Borrow;
import logic.BorrowPlus;
import logic.Message;
import logic.Subscriber;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map.Entry;

/**
 * The AuthenticationController class handles user authentication. 
 * It manages the login process, including ID and password validation, 
 * and transitions the user to the main application interface upon successful login.
 */
public class ReportsController {
	private Subscriber subscriber; // Static reference to the currently authenticated subscriber.
	@FXML
	private Button btnBack = null; // Button to exit the application.
	@FXML
	private Button btnGenerateGraph = null; // Button to exit the application.
	@FXML
	private ChoiceBox<Integer> choiceBoxMonth;
	@FXML
	private ChoiceBox<Integer> choiceBoxYear;
	@FXML
	private ChoiceBox<String> choiceBoxGraph;



	public void loadChoiceBoxs() {
		choiceBoxMonth.setValue(1);
		choiceBoxYear.setValue(2024);
		choiceBoxGraph.setValue("Borrowing Report");
		ObservableList<Integer> dataMonth = FXCollections.observableArrayList();
		for(Integer number = 1; number < 13; number++) {
			dataMonth.add(number);
		}
		choiceBoxMonth.setItems(dataMonth);
		choiceBoxYear.getItems().addAll(2024, 2025);
		choiceBoxGraph.getItems().addAll("Borrowing Report", "Subscriber Status Report");

	}

//	public void display(String message, Color color) {
//		lblError.setTextFill(color); // Sets the color of the error label.
//		lblError.setText(message); // Sets the text of the error label.
//	}


	/**
	 * Handles the "Exit" button action. Terminates the application.
	 * 
	 * @param event The action event triggered by clicking the button.
	 * @throws Exception If an error occurs during termination.
	 */
	public void backBtn(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/gui/client/"+ "LibrarianClientGUIFrame" +".fxml").openStream());
		LibrarianClientGUIController librarianClientGUIController = loader.getController();
		librarianClientGUIController.updateMessageCount();
		librarianClientGUIController.loadLibrarian();
		nextPage(loader, root, event, "Librarian Main Menu");
	}

	public void GenerateGraphBtn(ActionEvent event) throws Exception {
//		FXMLLoader loader = new FXMLLoader();
//		Pane root = loader.load(getClass().getResource("/gui/client/"+ "LibrarianClientGUIFrame" +".fxml").openStream());
//		LibrarianClientGUIController librarianClientGUIController = loader.getController();
//		librarianClientGUIController.updateMessageCount();
//		librarianClientGUIController.loadLibrarian();
//		nextPage(loader, root, event, "Librarian Main Menu");
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
