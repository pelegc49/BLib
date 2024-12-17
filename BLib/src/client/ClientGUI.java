package client;

import java.io.IOException;

import common.BLibIF;

public class ClientGUI implements BLibIF {
	/**
	 * The default port to connect on.
	 */
	public static int DEFAULT_PORT;
	BLibClient client;

	public ClientGUI(String host, int port) {
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