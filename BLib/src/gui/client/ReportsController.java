package gui.client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import logic.Message;
import logic.Subscriber;

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
	@FXML
	private Label lblError;


	public void loadChoiceBoxs() {
		choiceBoxMonth.setValue(1);
		choiceBoxYear.setValue(2025);
		choiceBoxGraph.setValue("borrowing report");
		ObservableList<Integer> dataMonth = FXCollections.observableArrayList();
		for(Integer number = 1; number < 13; number++) {
			dataMonth.add(number);
		}
		choiceBoxMonth.setItems(dataMonth);
		choiceBoxYear.getItems().addAll(2024, 2025);
		choiceBoxGraph.getItems().addAll("borrowing report", "subscriber status");

	}

	public void display(String message, Color color) {
		lblError.setTextFill(color); // Sets the color of the error label.
		lblError.setText(message); // Sets the text of the error label.
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

	public void GenerateGraphBtn(ActionEvent event) throws Exception {
		Message msg = IPController.client.getGraph(choiceBoxYear.getValue(), choiceBoxMonth.getValue(), choiceBoxGraph.getValue());
		if (msg.getCommand().equals("failed")) {
			display("No graph in this date", Color.RED);
			return;
		}
		else{
			display("", Color.RED);
			FXMLLoader loader = new FXMLLoader();
			Pane root = loader.load(getClass().getResource("/gui/client/"+ "ShowReportsFrame" +".fxml").openStream());
			ShowReportsController showReportsController = loader.getController();
			showReportsController.loadGraphDetails(choiceBoxGraph.getValue(), choiceBoxYear.getValue(), choiceBoxMonth.getValue());
			showReportsController.loadGraph((byte[]) msg.getArguments().get(0));

			Stage primaryStage = new Stage();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/gui/client/stylesheet.css").toExternalForm());
			primaryStage.setTitle("Show Reports");
			primaryStage.setScene(scene);
			primaryStage.show();
		}
	}
}
