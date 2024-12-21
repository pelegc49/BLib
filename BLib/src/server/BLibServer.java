package server;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import logic.Message;
import logic.Subscriber;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

public class BLibServer extends AbstractServer {
	
	public static BLibServer instance = null;
	private static Map<ConnectionToClient, String[]> connectedClients = new HashMap<>();

	private BLibServer(int port) throws IOException {
		super(port);
		listen();
	}
	
	public static BLibServer getInstance(int port) throws IOException{
		if (instance instanceof BLibServer) {
			return instance;
		}
		return new BLibServer(port);
	}
	
	@Override
	protected void clientConnected(ConnectionToClient client) {
		connectedClients.put(client,
				new String[] { client.getInetAddress().getHostAddress(), client.getInetAddress().getHostName() });
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

	public Map<ConnectionToClient, String[]> getConnectedClients() {
		return connectedClients;
	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		System.out.println("receive message:"+msg);
		if (msg instanceof Message) {
			List<Object> args = ((Message) msg).getArguments();
			try {
				Object ret;
				switch (((Message) msg).getCommand()) {
				case "login":
					ret = BLibDBC.login((Integer) args.get(0), (String) args.get(1));
					System.out.println(ret);
					if (ret != null) {
						client.sendToClient(new Message("loginSuccess", (String) ret));
					} else {
						client.sendToClient(new Message("loginFail"));
					}
					break;
				case "getSubscriber":
					ret = BLibDBC.getSubscriberByID((Integer) args.get(0));
					if (ret != null) {
						client.sendToClient(new Message("subscriberFound", (Subscriber) ret));
					} else {
						client.sendToClient(new Message("subscriberNotFound"));
					}
					break;

				case "updateSubscriber":
					ret = BLibDBC.updateSubscriber((Subscriber) args.get(0));
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

}
