package server;

import logic.Message;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

public class BLibServer extends AbstractServer{

	
	public BLibServer(int port) {
		super(port);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		if(msg instanceof Message) {
			switch(((Message) msg).getCommand().toLowerCase()) {
			case "login":
				
			
			
			
			}
			
			
		}
		
	}
	
	

}
