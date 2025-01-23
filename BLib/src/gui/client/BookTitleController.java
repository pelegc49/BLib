package gui.client;

import java.time.LocalDate;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.BookCopy;
import logic.BookTitle;
import logic.Message;

public class BookTitleController {
	private BookTitle bt;
	// UI elements defined in the FXML file.
	
	@FXML
	private Label lblTitle;
	@FXML
	private Label lblAuthorName;
	@FXML
	private Label lblDescription;
	@FXML
	private Label lblError;
	@FXML
	private Label lblDueDate;
	@FXML
	private Text txtAuthorName;
	@FXML
	private Text txtGenre;
	@FXML
	private Text txtDescription;
	@FXML
	private Button btnBack = null;
	@FXML
	private Button btnOrder = null;
	@FXML
	private TableView<BookCopy> bookTable;
	@FXML
	private TableColumn<BookCopy, Integer> columnBookId;
	@FXML
	private TableColumn<BookCopy, String>  columnShelf;
	

	/**
	 * Handles the Exit button click event. Closes the application.
	 * 
	 * @param event The ActionEvent triggered by clicking the Exit button.
	 * @throws Exception If an error occurs during the operation.
	 */
	public void backBtn(ActionEvent event) throws Exception {
		// Get the current stage and title
		Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    String currentTitle = currentStage.getTitle();
	    String title;
	    if(currentTitle.split(" ")[0].equals("Subscriber")) {
	    	title = "Subscriber - Search";
	    }
	    else if(currentTitle.split(" ")[0].equals("Librarian")) {
	    	title = "Librarian - Search";
	    }
	    else {
	    	title = "Guest - Search";
	    }
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/client/"+ "SearchFrame" +".fxml"));
		Parent root = loader.load();
		IPController.client.nextPage(loader, root, event, title);
	}

	/**
	 * Handles the Order button click event.
	 *
	 * @param event The ActionEvent triggered by clicking the Order button.
	 * @throws Exception If an error occurs during the operation.
	 */
	public void orderBtn(ActionEvent event) throws Exception {
		// try to order the book
		Message msg = IPController.client.orderTitle(AuthenticationController.subscriber, bt);
		if(msg.getCommand().equals("success")) {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/client/"+ "SearchFrame" +".fxml"));
			Parent root = loader.load();
    		SearchController searchController = loader.getController();
    		searchController.display("Order succeed.", Color.GREEN);
    		IPController.client.nextPage(loader, root, event, "Subscriber - Search");
		}
		else {
			IPController.client.display(lblError,(String)msg.getArguments().get(0), Color.RED);
		}
		
	}

	/**
	 * Loads the book's information into the GUI.
	 *
	 * @param bt1 The book to load.
	 */
	public void loadBookTitle(BookTitle bt1) {
		// Create an observable list to store the book copies.
		ObservableList<BookCopy> data;
		Set<BookCopy> bookCopy = IPController.client.getCopiesByTitle(bt1);
		data = FXCollections.observableArrayList();
		for(BookCopy bc : bookCopy) {
			if(!bc.isBorrowed())
				data.add(bc);
		}
		// If the book has no copies, display the closest return date.
		if(data.isEmpty()) {
			LocalDate dueDate = IPController.client.getTitleClosestReturnDate(bt1);
			lblDueDate.setText("Closest date of return is: " + dueDate.toString());
		}
		// Otherwise, display the copies.
		else {
			columnBookId.setCellValueFactory(new PropertyValueFactory<>("copyID"));
			columnShelf.setCellValueFactory(new PropertyValueFactory<>("shelf"));
			bookTable.setItems(data);
			bookTable.getSortOrder().add(columnBookId);
		}
		// Set the book title, author, description, and genre.
		this.bt = bt1; // Assigns the subscriber to the controller.
		this.lblTitle.setText(String.valueOf(bt.getTitleName()));
		this.txtAuthorName.setText(bt.getAuthorName()); // Sets the subscriber's name.
		this.txtDescription.setText(bt.getDescription()); // Sets the subscriber's phone.
		this.txtGenre.setText(bt.getGenre());
	}

	/**
	 * Handles the order button.
	 *
	 * @param title The title of the user.
	 */
	public void loadOrderButton(String title) {
		// If the user is a guest or librarian, hide the order button.
		if(title.equals("Guest") || title.equals("Librarian")) {
			btnOrder.setVisible(false);
		}
		else {
			btnOrder.setVisible(true);
		}
	}
	

}
