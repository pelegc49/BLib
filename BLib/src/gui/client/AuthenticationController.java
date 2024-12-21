package gui.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.Subscriber;

public class AuthenticationController {
	private ClientGUIController sfc;
	public static Subscriber subscriber;
	@FXML
	private Label lblError;
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
		int digit_id = 0;
		id = txtId.getText();
		String password = txtPassword.getText();
		try {
			digit_id = Integer.parseInt(id);
		} catch (Exception e) {
			display("bad username - only digits");
		}
		if (id.trim().isEmpty()) {
			display("You must enter an id number");
		} else if (txtPassword.getText().isEmpty()) {
			display("You must enter a password");
		} else {
			if (!IPController.client.login(digit_id, password)) {
				display("ID or password are incorrect");
			} else {
				System.out.println("Subscriber Found Successfuly");
				((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
				Stage primaryStage = new Stage();
				Pane root = loader.load(getClass().getResource("/gui/client/ClientGUI.fxml").openStream());
				ClientGUIController clientGUIController = loader.getController();
				clientGUIController.loadSubscriber(IPController.client.getSubscriber(digit_id));

				Scene scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("/gui/client/ClientGUI.css").toExternalForm());
				primaryStage.setOnCloseRequest((E) -> System.exit(0));
				primaryStage.setTitle("Subscriber Managment Tool");
				primaryStage.setScene(scene);
				primaryStage.show();
			}
		}
	}

	public void getExitBtn(ActionEvent event) throws Exception {
		System.out.println("Exit Successfully");
		System.exit(0);
	}

	public void loadSubscriber(Subscriber s1) {
		this.sfc.loadSubscriber(s1);
	}

	public void display(String message) {
		lblError.setText(message);
	}

}
