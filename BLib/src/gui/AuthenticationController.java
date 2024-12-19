package gui;

import client.ClientGUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.Subscriber;

public class AuthenticationController {
	private ClientGUIController sfc;

	@FXML
	private TextField txtId;
	@FXML
	private TextField txtPassword;
	@FXML
	private Button btnExit = null;
	@FXML
	private Button btnSend = null;

	public void Send(ActionEvent event) throws Exception {
		String id;
		FXMLLoader loader = new FXMLLoader();
		
		id = txtId.getText();
		String password = txtPassword.getText();
		if (id.trim().isEmpty()) {
			System.out.println("You must enter an id number");			
		}
		else {
			if (!ClientGUI.client.login(id, password)) {
				System.out.println("Subscriber ID Not Found");
			} else {
				System.out.println("Subscriber ID Found");
				((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
				Stage primaryStage = new Stage();
				Pane root = loader.load(getClass().getResource("ClientGUI.fxml").openStream());
				ClientGUIController clientGUIController = loader.getController();
				clientGUIController.loadSubscriber(ClientGUI.client.getSubscriber(Integer.valueOf(id)));

				Scene scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("ClientGUI.css").toExternalForm());
				primaryStage.setTitle("Subscriber Managment Tool");

				primaryStage.setScene(scene);
				primaryStage.show();
			}
		}
	}

	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("AuthenticationFrame.fxml"));

		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("AuthenticationFrame.css").toExternalForm());
		primaryStage.setTitle("Authentication Managment Tool");
		primaryStage.setScene(scene);

		primaryStage.show();
	}

	public void getExitBtn(ActionEvent event) throws Exception {
		System.out.println("Exit Successfully");
		System.exit(1);
	}

	public void loadSubscriber(Subscriber s1) {
		this.sfc.loadSubscriber(s1);
	}

	public void display(String message) {
		System.out.println("message");
	}

}
