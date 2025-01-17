package gui.client;

import java.io.ByteArrayInputStream;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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
	private Label lblTitle;
	@FXML
	private Text txtYear;
	@FXML
	private Text txtMonth;
	@FXML
	private ImageView imgGraph;
	@FXML
	private HBox hboxCenter;
	@FXML
	private VBox vboxCenter;
	
	public void loadGraphDetails(String name, int year, int month) {
		lblTitle.setText(name+" "+month+"/"+year);
	}

	public void loadGraph(byte[] image) {
		ByteArrayInputStream byteStream = new ByteArrayInputStream(image);
		Image fxImage = new Image(byteStream);
		imgGraph.setImage(fxImage);
		imgGraph.fitWidthProperty().bind(hboxCenter.widthProperty());
		imgGraph.fitHeightProperty().bind(hboxCenter.heightProperty());
		imgGraph.setPreserveRatio(true);
		imgGraph.setSmooth(true);
	    hboxCenter.prefWidthProperty().bind(vboxCenter.widthProperty());
	    hboxCenter.prefHeightProperty().bind(vboxCenter.heightProperty().subtract(100));
	}

	/**
	 * Handles the "Exit" button action. Terminates the application.
	 * 
	 * @param event The action event triggered by clicking the button.
	 * @throws Exception If an error occurs during termination.
	 */
	public void backBtn(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide();
	}
}
