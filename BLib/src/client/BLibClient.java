package client;

import java.io.IOException;

import common.BlibIF;
import logic.Message;
import ocsf.client.AbstractClient;

public class BLibClient extends AbstractClient {
	
	BlibIF clientUI;
	public static Message msg;
	
	public static boolean awaitResponse = false;
	
	public BLibClient(String host, int port, BlibIF clientUI) {
		super(host, port);
		this.clientUI = clientUI;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		// TODO Auto-generated method stub
		System.out.println("--> handleMessageFromServer");
		awaitResponse = false;
		this.msg = (Message)msg;
	}
	
	/**
	 * This method handles all data coming from the UI
	 *
	 * @param message The message from the UI.
	 */

	public void handleMessageFromClientUI(Object message) {
		try {
			openConnection();// in order to send more than one message
			awaitResponse = true;
			sendToServer(message);
			// wait for response
			while (awaitResponse) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			clientUI.display("Could not send message to server: Terminating client." + e);
			quit();
		}
	}
}
