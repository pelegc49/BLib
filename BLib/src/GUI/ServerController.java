package GUI;


import java.awt.Button;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ServerController {
	@FXML
	private TextArea text;
	
	@FXML
	private Button exitBtn;
	@FXML
	private Button refreshBtn;

	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/GUI/Server.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/GUI/Server.css").toExternalForm());
		primaryStage.setTitle("BLib Server");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		
	}
	
	
}
