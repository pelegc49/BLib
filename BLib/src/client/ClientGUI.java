package client;

import gui.client.IPController;
import javafx.application.Application;
import javafx.stage.Stage;

public class ClientGUI extends Application {

//	public ClientGUI() {
//		// must have empty constructor so JavaFX would run
//		super();
//	}
	
	public static void main(String args[]) throws Exception {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		IPController aFrame = new IPController();
		aFrame.start(primaryStage);
	}
	
}