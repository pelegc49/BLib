package logic;

import java.time.LocalDate;

import javafx.scene.control.CheckBox;

public class BorrowWithCheckBox extends Borrow {

	// Private member variables for storing borrowing details
	private CheckBox selected;
	private String errorMessage;

	// Constructor to initialize the borrowing details
	public BorrowWithCheckBox(Subscriber subscriber,BookCopy book,LocalDate dateOfBorrow, LocalDate dueDate, LocalDate dateOfReturn) {
		super(subscriber, book, dateOfBorrow, dueDate, dateOfReturn);
		selected.setSelected(false);
		errorMessage = "";
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public String getAuthor() {
		return this.book.getTitle().getAuthorName();
	}
	
	public String getTitle() {
		return this.book.getTitle().getTitleName();
	}
	
	public boolean isSelected() {
		return selected.isSelected();
	}
	
	public void setSelected(boolean bool) {
		selected.setSelected(bool);
	}
	
	// Getter method for the date of borrow
	public LocalDate getDateOfBorrow() {
		return dateOfBorrow; // Return the date of borrow
	}

	// Getter method for the due date
	public LocalDate getDueDate() {
		return dueDate; // Return the due date
	}

	// Getter method for the date of return
	public LocalDate getDateOfReturn() {
		return dateOfReturn; // Return the date of return
	}

	// Setter method for the date of borrow
	public void setDateOfBorrow(LocalDate dateOfBorrow) {
		this.dateOfBorrow = dateOfBorrow; // Set the date of borrow
	}

	// Setter method for the due date
	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate; // Set the due date
	}

	// Setter method for the date of return
	public void setDateOfReturn(LocalDate dateOfReturn) {
		this.dateOfReturn = dateOfReturn; // Set the date of return
	}
	public Subscriber getSubscriber() {
		return subscriber;
	}
	
	public BookCopy getBook() {
		return book;
	}
}
