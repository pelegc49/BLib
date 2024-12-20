package gui;

import client.BLibClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class IPController {

	public static BLibClient client;

	@FXML
	private Label lblError;
	@FXML
	private TextField txtIp;
	@FXML
	private TextField txtPassword;
	@FXML
	private Button btnExit = null;
	@FXML
	private Button btnSend = null;

	public IPController() {
		// must have empty constructor so JavaFX would run
		super();
	}

	public void Send(ActionEvent event) throws Exception {
		String ip;
		FXMLLoader loader = new FXMLLoader();
		ip = txtIp.getText();
		if (ip.trim().isEmpty()) {
			display("You must enter an IP Address");
		} else {
			try {
				client = new BLibClient(ip, 5555);
				System.out.println("IP Entered Successfuly");
				((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
				Stage primaryStage = new Stage();
				Pane root = loader.load(getClass().getResource("AuthenticationFrame.fxml").openStream());
				Scene scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("AuthenticationFrame.css").toExternalForm());
				primaryStage.setTitle("Authentication");
				primaryStage.setScene(scene);
				primaryStage.show();
			} 
			catch (Exception e) {
				System.out.println("Error: Can't setup connection!" + " Terminating client.");
				display("Can't setup connection");
			}
		}
	}

	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("IPFrame.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("IPFrame.css").toExternalForm());
		primaryStage.setTitle("IP");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void getExitBtn(ActionEvent event) throws Exception {
		System.out.println("Exit Successfully");
		System.exit(0);
	}

	public void display(String message) {
		lblError.setText(message);
	}

}
