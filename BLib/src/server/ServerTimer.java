package server;

public class ServerTimer implements Runnable{
	private Thread t;
	private BLibServer server;
	private static ServerTimer instance = null;
	private int delayMinutes = 30;
	
	public static void start(BLibServer server) {
		if(!(instance instanceof ServerTimer)) {
			instance = new ServerTimer(server);
		}
	}
	
	private ServerTimer(BLibServer server) { 
		this.server = server;
		t = new Thread(this);
		t.setDaemon(true);
		t.start();
	}
	
	@Override
	public void run() {
		server.getCommands();
		//check relevant commands
		//execute one by one
		
		try {
			Thread.sleep(delayMinutes*60*1000); // sleep 
		} catch (InterruptedException e) {}
	}




}
