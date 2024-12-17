package client;

import java.io.IOException;

import common.BlibIF;

public class BlibClientController implements BlibIF{
	/**
	 * The default port to connect on.
	 */
	public static int DEFAULT_PORT;
	BLibClient client;
	
	public BlibClientController(String host, int port) {
		try {
			client = new BLibClient(host, port, this);
		} catch (IOException exception) {
			System.out.println("Error: Can't setup connection!" + " Terminating client.");
			System.exit(1);
		}
	}
	
	public void accept(Object str) {
		client.handleMessageFromClientUI(str);
	}
	
	public void display(String message) {
		System.out.println("> " + message);
	}
}
