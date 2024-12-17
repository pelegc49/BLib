package client;


import javafx.application.Application;
import javafx.stage.Stage;

public class BlibClientUI extends Application{
	public static BlibClientController clientController; //only one instance
	
	public static void main( String args[] ) throws Exception{ 
		launch(args);  
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		clientController = new BlibClientController("localhost", 5555);
			
		// change me! TODO
		//AcademicFrameController aFrame = new AcademicFrameController(); // create StudentFrame
		 
		//aFrame.start(primaryStage);
	}
}
