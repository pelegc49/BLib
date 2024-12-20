package gui.server;

import java.net.URL;
import java.util.Map;
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
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			refresh(null);
			Thread refresher = new Thread(() -> {
				while(true) {
					try {
						Thread.sleep(2000);
						refresh(null);
					} catch (Exception e) {
						System.out.println("failed to rerfesh");
					}
				}
			});
			refresher.setDaemon(true); // ensuring this thread will stop when the main thread is stopped
			refresher.start();
		} catch (Exception e) {}
		
	}
	
	public void refresh(ActionEvent e) throws Exception {
		StringBuilder sb = new StringBuilder("");
		boolean isEmpty = true;
		int i = 1;
		for (Map.Entry<ConnectionToClient, String[]> c : ServerGUI.server.getConnectedClients().entrySet()) {
			sb.append(i+"#IP    : "+c.getValue()[0]+"\n");
			sb.append(i+"#HOST  : "+c.getValue()[1]+"\n");
			sb.append(i+"#STATUS: "+(c.getKey().isAlive()?"Connected":"Disconnected")+"\n\n");
			isEmpty = false;
			i++;
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
