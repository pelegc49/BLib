package gui.client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.Subscriber;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * The AuthenticationController class handles user authentication. 
 * It manages the login process, including ID and password validation, 
 * and transitions the user to the main application interface upon successful login.
 */
public class ShowReportsController {
	//private Subscriber subscriber; // Static reference to the currently authenticated subscriber.
	@FXML
	private Button btnBack = null; // Button to exit the application.
	@FXML
	private Text txtName;
	@FXML
	private Text txtYear;
	@FXML
	private Text txtMonth;
	@FXML
	private ImageView imageView;


	public void loadGraphDetails(String name, int year, int month) {
		txtName.setText(name);
		txtYear.setText(Integer.toString(year));
		txtMonth.setText(Integer.toString(month));
	}

	public void loadGraph(DataInputStream dataInputStream) {
		try {
			int imageSize = dataInputStream.readInt();

			byte[] imageData = new byte[imageSize];

			dataInputStream.readFully(imageData);

			ByteArrayInputStream byteStream = new ByteArrayInputStream(imageData);
			Image image = new Image(byteStream);

			imageView.setImage(image);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		((Node) event.getSource()).getScene().getWindow().hide();
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
