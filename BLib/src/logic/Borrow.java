package logic;

import java.io.Serializable;
import java.time.LocalDate;

import javafx.scene.control.CheckBox;

public class Borrow implements Serializable {

	// Private member variables for storing borrowing details
	private Subscriber subscriber;
	private BookCopy book;
	private LocalDate dateOfBorrow; // The date when the book was borrowed
	private LocalDate dueDate; // The due date for returning the book
	private LocalDate dateOfReturn; // The actual date when the book was returned
	private CheckBox checkBox;

	// Constructor to initialize the borrowing details
	public Borrow(Subscriber subscriber,BookCopy book,LocalDate dateOfBorrow, LocalDate dueDate, LocalDate dateOfReturn) {
		this.subscriber = subscriber;
		this.book = book;
		this.dateOfBorrow = dateOfBorrow; // Set the date of borrow
		this.dueDate = dueDate; // Set the due date
		this.dateOfReturn = dateOfReturn; // Set the return date
		checkBox.setSelected(false);
	}

	public boolean isSelected() {
		return checkBox.isSelected();
	}
	
	public void setSelected(boolean bool) {
		checkBox.setSelected(bool);
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
