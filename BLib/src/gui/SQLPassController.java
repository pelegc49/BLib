package gui;

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
import server.BLibDBC;
import server.BLibServer;
import server.ServerGUI;

public class SQLPassController {

	@FXML
	private Label lblMSG;
	@FXML
	private Label lblEnterPass;
	@FXML
	private TextField txtPass;
	@FXML
	private Button btnClose;
	@FXML
	private Button btnOK;

	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("SQLPass.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("Server.css").toExternalForm());
		primaryStage.setTitle("Enter Password");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void actionOnClose(ActionEvent e) {
		System.out.println("exiting...");
		System.exit(0);
	}

	public void actionOnOK(ActionEvent e) {
		String password = txtPass.getText();
		if (password.isBlank()) {
			txtPass.setText("");
			display("An SQL password must be entered");
			return;
		}
		if(!BLibDBC.connect(password)) {
			display("Can't establish connection with DB");
			return;
		}
		// Connected successfully. setup next window
		try {
			FXMLLoader loader = new FXMLLoader();
			ServerGUI.server = new BLibServer(ServerGUI.DEFAULT_PORT);
			Stage primaryStage = new Stage();
			Pane root = loader.load(getClass().getResource("Server.fxml").openStream());
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("Server.css").toExternalForm());
			primaryStage.setTitle("BLib Server");
			primaryStage.setScene(scene);
			((Node) e.getSource()).getScene().getWindow().hide();
			primaryStage.show();
		}catch (Exception ex) {
			display("Can't setup server. make sure port "+ServerGUI.DEFAULT_PORT+" isn't used");
			return;
		}
	}

	public void display(String str) {
		lblMSG.setText(str);
	}

}
