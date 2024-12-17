package client;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import logic.Subscriber;

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
		//launch(args);
		// for the prototype
		ClientGUI cl = new ClientGUI("localhost", 5555);
		Subscriber sb = new Subscriber(123, "Jhon", "054555", "Jhon@mail.com");
		String newMail = "JhonSmith@mail.com";
		String newPhoneNumber = "052333";
		cl.client.getSubscriberData();
		cl.client.updateSubscriber(new Subscriber(sb.getId(), sb.getName(), newPhoneNumber,	newMail));
		if(sb.getPhone().equals(newPhoneNumber) && sb.getEmail().equals(newMail))
			System.out.println("ITS WORKING!");
		else
			System.out.println("Not Working..");
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		client = new BLibClient("localhost", 5555, this);
	}
	
	public void display(String message) {
		System.out.println("> " + message);
	}
}