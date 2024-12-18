package server;

import gui.ServerController;
import javafx.application.Application;
import javafx.stage.Stage;

public class ServerGUI extends Application{
	
	public static BLibServer server;
	public static final int DEFAULT_PORT = 5555;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		server = new BLibServer(DEFAULT_PORT);
		ServerController aFrame = new ServerController();
		aFrame.start(primaryStage);
	}

	
	
}
