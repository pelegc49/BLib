package logic;

import java.io.Serializable;
import java.time.LocalDate;

import javafx.scene.control.CheckBox;

public class BorrowPlus implements Serializable {

	// Private member variables for storing borrowing details
	private CheckBox checkBox;
	private String errorMessage;

	// Constructor to initialize the borrowing details
	public BorrowPlus() {
		checkBox = new CheckBox();
		checkBox.setSelected(false);
		errorMessage = "";
	}

	public CheckBox getCheckBox() {
		return checkBox;
	}
	
	public void setCheckBox(boolean state) {
		checkBox.setSelected(state);;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
