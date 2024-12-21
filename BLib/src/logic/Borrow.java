package logic;

import java.io.Serializable;
import java.util.Date;

public class Borrow implements Serializable {

	// Private member variables for storing borrowing details
	private Date dateOfBorrow; // The date when the book was borrowed
	private Date dueDate; // The due date for returning the book
	private Date dateOfReturn; // The actual date when the book was returned

	// Constructor to initialize the borrowing details (date of borrow, due date,
	// and return date)
	public Borrow(Date dateOfBorrow, Date dueDate, Date dateOfReturn) {
		this.dateOfBorrow = dateOfBorrow; // Set the date of borrow
		this.dueDate = dueDate; // Set the due date
		this.dateOfReturn = dateOfReturn; // Set the return date
	}

	// Getter method for the date of borrow
	public Date getDateOfBorrow() {
		return dateOfBorrow; // Return the date of borrow
	}

	// Getter method for the due date
	public Date getDueDate() {
		return dueDate; // Return the due date
	}

	// Getter method for the date of return
	public Date getDateOfReturn() {
		return dateOfReturn; // Return the date of return
	}

	// Setter method for the date of borrow
	public void setDateOfBorrow(Date dateOfBorrow) {
		this.dateOfBorrow = dateOfBorrow; // Set the date of borrow
	}

	// Setter method for the due date
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate; // Set the due date
	}

	// Setter method for the date of return
	public void setDateOfReturn(Date dateOfReturn) {
		this.dateOfReturn = dateOfReturn; // Set the date of return
	}
}
