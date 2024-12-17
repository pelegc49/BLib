package GUI;

import client.ClientGUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import logic.Subscriber;

public class ClientGUIController {

	private Subscriber s;

	@FXML
	private Label lblstid;
	@FXML
	private Label lblName;
	@FXML
	private Label lblPhone;
	@FXML
	private Label lblEmail;

	@FXML
	private TextField txtid;
	@FXML
	private TextField txtName;
	@FXML
	private TextField txtPhone;
	@FXML
	private TextField txtEmail;

	@FXML
	private Button btnclose = null;
	@FXML
	private Button btnSave = null;

	public void loadSubscriber(Subscriber s1) {
		this.s = s1;
		this.txtid.setText(String.valueOf(s.getId()));
		this.txtName.setText(s.getName());
		this.txtPhone.setText(s.getPhone());
		this.txtEmail.setText(s.getEmail());
	}

	public void getbtnClose(ActionEvent event) throws Exception {
		System.out.println("Closing");
		System.exit(0);
	}

	public void getBtnSave(ActionEvent event) throws Exception {
		s.setName(txtName.getText());
		s.setEmail(txtEmail.getText());
		s.setPhone(txtPhone.getText());
		ClientGUI.client.updateSubscriber(s);
		System.out.println("saved Succesfully");
	}

}
