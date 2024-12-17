package server;

import java.io.IOException;
import java.util.List;

import logic.Message;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

public class BLibServer extends AbstractServer {

	public BLibServer(int port) {
		super(port);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		System.out.println("recive message");
		if (msg instanceof Message) {
			List<Object> args = ((Message) msg).getArguments();
			String ret;
			try {
				switch (((Message) msg).getCommand().toLowerCase()) {
				case "login":
					ret = BLibDBC.login((Integer) args.get(0), (String) args.get(1));
					if (ret != null) {
						client.sendToClient(new Message("loginSuccess", ret));
					}else {
						client.sendToClient(new Message("loginFail"));
					}
				case "getsubscriber":
					
					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

	}
	public static void main(String[] args) {
		BLibServer s = new BLibServer(5555);
		BLibDBC.connect();
		try {
			s.listen();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
