package GUI;



import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class ServerController {
	@FXML
	private TextFlow text;
	@FXML
	private Button exitBtn;
	@FXML
	private Button refreshBtn;

	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("test.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/GUI/Server.css").toExternalForm());
		primaryStage.setTitle("BLib Server");
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}

	
	public void refresh(ActionEvent e) throws Exception {
		System.out.println("refreshing...");
		
	}
	
	public void exit(ActionEvent e) throws Exception {
		System.out.println("exiting...");
		System.exit(0);
	}

}
