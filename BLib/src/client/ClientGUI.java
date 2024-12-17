package client;

import java.io.IOException;

import common.BLibIF;
import javafx.application.Application;
import javafx.stage.Stage;

public class ClientGUI extends Application {
	/**
	 * The default port to connect on.
	 */
	BLibClient client;
	public static int DEFAULT_PORT;

	public ClientGUI(String host, int port) {
		try {
			client = new BLibClient(host, port, this);
		} catch (IOException exception) {
			System.out.println("Error: Can't setup connection!" + " Terminating client.");
			System.exit(1);
		}
	}
	
	public static void main(String args[]) throws Exception {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		client = new BLibClient("localhost", 5555, this);
	}
	
	public void display(String message) {
		System.out.println("> " + message);
	}
}