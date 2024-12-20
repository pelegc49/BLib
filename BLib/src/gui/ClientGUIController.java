package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import logic.Subscriber;

public class ClientGUIController {

	private Subscriber s;

	@FXML
	private Label lblError;
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
		} catch (Exception e) {
			display("ID must have only digits", Color.RED);
			return;
		}
		if (s.getId() != digit_id) {
			display("Don't change the ID", Color.RED);
			return;
		} else if (!s.getName().equals(txtName.getText())) {
			display("Don't change the name", Color.RED);
			return;
		}
		try {
			Integer.parseInt(txtPhone.getText());
		} catch (Exception e) {
			display("Phone must have only digits", Color.RED);
			return;
		}
		this.s.setEmail(txtEmail.getText());
		this.s.setPhone(txtPhone.getText());

		if (IPController.client.updateSubscriber(s)) {
			display("saved Succesfully!", Color.GREEN);
			return;
		}
		display("could not save", Color.RED);
	}

	public void display(String message, Color color) {
		lblError.setTextFill(color);
		lblError.setText(message);
	}

}
