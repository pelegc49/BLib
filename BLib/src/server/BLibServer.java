package server;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import logic.Message;
import logic.Subscriber;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

public class BLibServer extends AbstractServer {
	private static Set<ConnectionToClient> connectedClients = new HashSet<>();
	public BLibServer(int port) {
		super(port);
		BLibDBC.connect();
		try {
			listen();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	protected void clientConnected(ConnectionToClient client) {
		connectedClients.add(client);
	}
	@Override
	protected synchronized void clientDisconnected(ConnectionToClient client) {
		connectedClients.remove(client);
	}
	@Override
	protected synchronized void clientException(ConnectionToClient client, Throwable exception) {
		try {
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		System.out.println("recive message");
		if (msg instanceof Message) {
			List<Object> args = ((Message) msg).getArguments();
			try {
				Object ret;
				switch (((Message) msg).getCommand().toLowerCase()) {
				case "login":
					ret = BLibDBC.login((Integer) args.get(0), (String) args.get(1));
					if (ret != null) {
						client.sendToClient(new Message("loginSuccess", (String) ret));
					} else {
						client.sendToClient(new Message("loginFail"));
					}
					break;
				case "getsubscriber":
					ret = BLibDBC.getSubscriberByID((Integer) args.get(0));
					if (ret != null) {
						client.sendToClient(new Message("subscriberFound", (Subscriber) ret));
					} else {
						client.sendToClient(new Message("subscriberNotFound"));
					}
					break;

				case "updatesubscriber":
					ret = BLibDBC.updateSubscriber((Subscriber)args.get(0));
					if (ret != null) {
						client.sendToClient(new Message("subscriberUpdated"));
					} else {
						client.sendToClient(new Message("subscriberFailedUpdated"));
					}
					break;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
	public static void main(String[] args) {
		new BLibServer(5555);
	}

}
