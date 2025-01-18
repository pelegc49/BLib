package gui.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import logic.BookCopy;
import logic.Message; 

/**
 * The AuthenticationController class handles user authentication. 
 * It manages the login process, including ID and password validation, 
 * and transitions the user to the main application interface upon successful login.
 */
public class BookActionsController {

	@FXML
	private Label lblError; // Label for displaying error messages to the user.
	@FXML
	private TextField txtSubscriberId; // TextField for user to enter their ID.
	@FXML
	private TextField txtBookId;
	@FXML
	private Button btnBack = null;
	@FXML
	private Button btnBorrow = null; 
	@FXML
	private Button btnReturn = null;
	
	
	public void borrowBtn(ActionEvent event) {
	    Integer bookID;
	    try {
	        // Attempt to parse the ID as an integer
	    	bookID = Integer.valueOf(txtBookId.getText());
	    } catch (NumberFormatException e) {
	        // Handle invalid input
	        IPController.client.display(lblError,"Invalid Book ID", Color.RED);
	        return;
	    }
	    
	    Integer subID;
	    try {
	        // Attempt to parse the ID as an integer
	        subID = Integer.valueOf(txtSubscriberId.getText());
	    } catch (NumberFormatException e) {
	        // Handle invalid input
	        IPController.client.display(lblError,"Invalid subscriber ID", Color.RED);
	        return;
	    }

	    Message msg = IPController.client.createBorrow(subID, bookID);
	    if(msg.getCommand().equals("failed")) {
	    	IPController.client.display(lblError,(String)msg.getArguments().get(0), Color.RED);
	    	return;
	    }
	    IPController.client.display(lblError,"Borrow succeeded", Color.GREEN);
	}

	
	public void returnBtn(ActionEvent event) throws Exception {
		Integer bookID;
	    try {
	        // Attempt to parse the ID as an integer
	    	bookID = Integer.valueOf(txtBookId.getText());
	    } catch (NumberFormatException e) {
	        // Handle invalid input
	        IPController.client.display(lblError,"Invalid Book ID", Color.RED);
	        return;
	    }

	    // Search for the subscriber using the provided ID
	    BookCopy searchedBook = IPController.client.getCopyByID(bookID);
	    
	    Message msg = IPController.client.returnBook(searchedBook);
	    
	    if(msg.getCommand().equals("failed")) {
	    	IPController.client.display(lblError,(String)msg.getArguments().get(0), Color.RED);
	    	return;
	    }
	    IPController.client.display(lblError,(String)msg.getArguments().get(0), Color.GREEN);
	}
	
		
	public void backBtn(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/client/"+ "LibrarianClientGUIFrame" +".fxml"));
		Parent root = loader.load();
		LibrarianClientGUIController librarianClientGUIController = loader.getController();
		librarianClientGUIController.loadLibrarian();
		librarianClientGUIController.updateMessageCount();
		IPController.client.nextPage(loader, root, event, "Librarian Main Menu");
	}
	

}
