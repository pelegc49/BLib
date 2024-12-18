package gui;



import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import javafx.stage.Stage;
import ocsf.server.ConnectionToClient;
import server.ServerGUI;

public class ServerController implements Initializable {
	@FXML
	private TextArea text;
	@FXML
	private Button exitBtn;
	@FXML
	private Button refreshBtn;

	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("test.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("Server.css").toExternalForm());
		primaryStage.setTitle("BLib Server");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			refresh(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void refresh(ActionEvent e) throws Exception {
		StringBuilder sb = new StringBuilder("");
		boolean isEmpty = true;
		int i = 1;
		for (ConnectionToClient c : ServerGUI.server.getConnectedClients()) {
			sb.append(i+"#IP: "+c.getInetAddress().getHostAddress()+"\n");
			sb.append(i+"#HOST: "+c.getInetAddress().getHostName()+"\n");
			sb.append(i+"#STATUS: "+(c.isAlive()?"Connected":"Disconnected")+"\n");
			isEmpty = false;
		}
		if(isEmpty) {
			sb.append("no clients connected");
		}
		text.setText(sb.toString());
	}
	
	public void exit(ActionEvent e) throws Exception {
		System.out.println("exiting...");
		System.exit(0);
	}

}
