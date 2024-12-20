package server;

import gui.SQLPassController;
import javafx.application.Application;
import javafx.stage.Stage;

public class ServerGUI extends Application{
	
	public static SQLPassController aFrame;
	public static BLibServer server;
	public static final int DEFAULT_PORT = 5555;
	public ServerGUI() {
		super();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
//		server = new BLibServer(DEFAULT_PORT);
		aFrame = new SQLPassController();
		aFrame.start(primaryStage);
	}

	
	
}
