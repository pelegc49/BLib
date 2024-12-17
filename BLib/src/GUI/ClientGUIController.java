package GUI;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import logic.Subscriber;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ClientGUIController {

	private Subscriber s;
	
	@FXML
	private Label lblName;
	@FXML
	private Label lblPhone;
	@FXML
	private Label lblEmail;

	@FXML
	private TextField txtName;
	@FXML
	private TextField txtPhone;
	
	@FXML
	private TextField txtEmail;

	@FXML
	private Button btnclose=null;

	@FXML
	private Label lblstid;
	@FXML
	private TextField txtid;
	@FXML
	private Button btnSave=null;	

	public void loadSubscriber(Subscriber s1) {
		this.s=s1;
		this.txtid.setText(String.valueOf(s.getId()));		
		this.txtName.setText(s.getName());
		this.txtPhone.setText(s.getPhone());
		this.txtEmail.setText(s.getEmail());
	
}
	public void getbtnClose(ActionEvent event) throws Exception {
		System.out.println("Closing...");
		System.exit(0);
	}

	public void SaveBTN(ActionEvent event) throws Exception{
		Faculty f=new Faculty(this.cmbFaculty.getValue().toString(),"");
		System.out.print(f.toString());
		s.setLName(txtSurname.getText());
		s.setPName(txtName.getText());
		s.setFc(f);
		String str = ("Save "+s.toString());
		ClientUI.chat.accept(str);
		System.out.println("saved sccesfully...");
	}

}


