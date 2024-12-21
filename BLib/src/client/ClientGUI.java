package client;

import gui.client.IPController; // Importing the IPController class from the GUI client package.
import javafx.application.Application; // Importing the JavaFX Application class.
import javafx.stage.Stage; // Importing the Stage class from JavaFX.

/**
 * The ClientGUI class serves as the entry point for launching the graphical
 * user interface (GUI) of the client application. It uses JavaFX for creating
 * the GUI.
 */
public class ClientGUI extends Application {

	/**
	 * Default constructor for the ClientGUI class. JavaFX requires a no-argument
	 * constructor to launch the application.
	 */
	public ClientGUI() {
		super();
	}

	/**
	 * The main method, which serves as the entry point of the Java application. It
	 * calls the JavaFX `launch` method to start the GUI application.
	 * 
	 * @param args Command-line arguments.
	 * @throws Exception If an error occurs during application launch.
	 */
	public static void main(String args[]) throws Exception {
		launch(args);
	}

	/**
	 * The `start` method is overridden from the Application class. It sets up the
	 * primary stage of the JavaFX application and initializes the GUI.
	 * 
	 * @param primaryStage The primary stage (window) for this application.
	 * @throws Exception If an error occurs during GUI initialization.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		IPController aFrame = new IPController(); // Creating an instance of the IPController class.
		aFrame.start(primaryStage); // Starting the GUI by passing the primary stage to the IPController.
	}

}
