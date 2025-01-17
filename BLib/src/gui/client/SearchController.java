package gui.client;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import logic.BookTitle; 

/**
 * The AuthenticationController class handles user authentication. 
 * It manages the login process, including ID and password validation, 
 * and transitions the user to the main application interface upon successful login.
 */
public class SearchController{
	
	@FXML
	private TextField txtSearch;
	@FXML
	private Label lblError; // Label for displaying error messages to the user.
	@FXML
	private Button btnBack = null; // Button for exiting the application.
	@FXML
	private Button btnSearch = null; // Button for submitting the login form.
	@FXML
	private TableView<BookTitle> bookTable;
	@FXML
	private TableColumn<BookTitle, String> authorColumn;
	@FXML
	private TableColumn<BookTitle, String> titleColumn;
	
	public void searchBtn(Event event) {
		ObservableList<BookTitle> data;
		String keyword = txtSearch.getText();
		Set<BookTitle> bookTitle = IPController.client.getTitlesByKeyword(keyword);
		data = FXCollections.observableArrayList();
		for(BookTitle bt : bookTitle) {
			data.add(bt);
		}
		authorColumn.setCellValueFactory(new PropertyValueFactory<>("authorName"));
		titleColumn.setCellValueFactory(new PropertyValueFactory<>("titleName"));
		bookTable.setItems(data);
		bookTable.getSortOrder().addAll(authorColumn,titleColumn);
		
		// allows to click on row
		bookTable.setRowFactory(tv -> {
		    TableRow<BookTitle> rowa = new TableRow<>();
		    rowa.setOnMouseClicked(eventa -> {
		        if (eventa.getClickCount() == 2 && !rowa.isEmpty()) {
		        	BookTitle rowData = rowa.getItem();
		    		Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		    	    String currentTitle = currentStage.getTitle();
		    	    String[] title = currentTitle.split(" ");
		    	    
			    	FXMLLoader loader = new FXMLLoader();
			    	Pane root = null;
					try {
						root = loader.load(getClass().getResource("/gui/client/"+ "BookTitleFrame" +".fxml").openStream());
					} catch (IOException e) {e.printStackTrace();}
		    		BookTitleController bookTitleController = loader.getController();
		    		bookTitleController.loadBookTitle(rowData);
		    		bookTitleController.loadOrderButton(title[0]);
			    	nextPage(loader, root, event, title[0] +" - "+ rowData.getTitleName());
		        }
		    });
	    return rowa ;
		});
	}

	/**
	 * Handles the "Exit" button action. Terminates the application.
	 * 
	 * @param event The action event triggered by clicking the button.
	 * @throws Exception If an error occurs during termination.
	 */
	public void backBtn(ActionEvent event) throws Exception {
		Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    String currentTitle = currentStage.getTitle();
	    if(currentTitle.equals("Subscriber - Search")) {
	    	FXMLLoader loader = new FXMLLoader();
	    	Pane root = loader.load(getClass().getResource("/gui/client/"+ "SubscriberClientGUIFrame" +".fxml").openStream());
	    	SubscriberClientGUIController subscriberClientGUIController = loader.getController();
	    	subscriberClientGUIController.loadSubscriber();
	    	nextPage(loader, root, event, "Subscriber Main Menu");
	    }
	    else if(currentTitle.equals("Librarian - Search")) {
	    	FXMLLoader loader = new FXMLLoader();
	    	Pane root = loader.load(getClass().getResource("/gui/client/"+ "LibrarianClientGUIFrame" +".fxml").openStream());
			LibrarianClientGUIController librarianClientGUIController = loader.getController();
			librarianClientGUIController.loadLibrarian();
			librarianClientGUIController.updateMessageCount();
	    	nextPage(loader, root, event, "Librarian Main Menu");
	    }
	    else {
	    	FXMLLoader loader = new FXMLLoader();
	    	Pane root = loader.load(getClass().getResource("/gui/client/"+ "AuthenticationFrame" +".fxml").openStream());
			AuthenticationController authenticationController = loader.getController();
			authenticationController.loadImage();
	    	nextPage(loader, root, event, "Authentication");
	    }
	}

	public void display(String message, Color color) {
		lblError.setText(message);
		lblError.setTextFill(color);
	}
	
	public void nextPage(FXMLLoader loader, Pane root, Event event, String title){
		((Node) event.getSource()).getScene().getWindow().hide();
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/client/stylesheet.css").toExternalForm());
		primaryStage.setOnCloseRequest((E) -> System.exit(0));
		primaryStage.setTitle(title);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
