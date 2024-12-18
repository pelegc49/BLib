package server;

import GUI.ServerController;
import javafx.application.Application;
import javafx.stage.Stage;

public class ServerGUI extends Application{
	
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		ServerController aFrame = new ServerController();
		aFrame.start(primaryStage);
	}

	
	
}
