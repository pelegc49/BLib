package gui;

import java.net.URL;
import java.util.ResourceBundle;

import client.ClientGUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import logic.Subscriber;

public class ClientGUIController {

	private Subscriber s;

	@FXML
	private Label lblId;
	@FXML
	private Label lblName;
	@FXML
	private Label lblPhone;
	@FXML
	private Label lblEmail;

	@FXML
	private TextField txtId;
	@FXML
	private TextField txtName;
	@FXML
	private TextField txtPhone;
	@FXML
	private TextField txtEmail;

	@FXML
	private Button btnClose = null;
	@FXML
	private Button btnSave = null;

	public void loadSubscriber(Subscriber s1) {
		this.s = s1;
		this.txtId.setText(String.valueOf(s.getId()));
		this.txtName.setText(s.getName());
		this.txtPhone.setText(s.getPhone());
		this.txtEmail.setText(s.getEmail());
	}

	public void getbtnClose(ActionEvent event) throws Exception {
		System.out.println("Closing");
		System.exit(0);
	}

	public void getbtnSave(ActionEvent event) throws Exception {
		int digit_id;
		try {
			digit_id = Integer.parseInt(txtId.getText());
		}
		catch (Exception e) {
			System.out.println("ID must have only digits.");
			return;
		}
		if (s.getId() != digit_id) {
			System.out.println("Don't change the ID.");
			return;
		}
		if (!s.getName().equals(txtName.getText())) {
			System.out.println("Don't change the name.");
			return;
		}
		this.s.setEmail(txtEmail.getText());
		this.s.setPhone(txtPhone.getText());
		
		if (ClientGUI.client.updateSubscriber(s)) {
			System.out.println("saved Succesfully");
			return;
		}
		System.out.println("could not save");		
	}

	

}
