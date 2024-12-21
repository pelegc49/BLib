package logic;

import java.io.Serializable;

public class Book implements Serializable {

	// Private member variables for storing book details
	private int id; // ID of the book
	private int shelf; // Shelf number where the book is located
	private String bookName; // Name of the book
	private String authorName; // Author of the book
	private boolean isBorrowed; // Whether the book is currently borrowed or not

	// Constructor to initialize a new Book object with all the necessary details
	public Book(int id, int shelf, String bookName, String authorName, boolean isBorrowed) {
		this.id = id; // Set the book ID
		this.shelf = shelf; // Set the shelf number
		this.bookName = bookName; // Set the name of the book
		this.authorName = authorName; // Set the author's name
		this.isBorrowed = isBorrowed; // Set whether the book is borrowed
	}

	// Getter method for the book ID
	public int getId() {
		return id; // Return the book ID
	}

	// Setter method for the book ID
	public void setId(int id) {
		this.id = id; // Set the book ID
	}

	// Getter method for the shelf number
	public int getShelf() {
		return shelf; // Return the shelf number
	}

	// Setter method for the shelf number
	public void setShelf(int shelf) {
		this.shelf = shelf; // Set the shelf number
	}

	// Getter method for the book name
	public String getBookName() {
		return bookName; // Return the book name
	}

	// Setter method for the book name
	public void setBookName(String bookName) {
		this.bookName = bookName; // Set the book name
	}

	// Getter method for the author's name
	public String getAuthorName() {
		return authorName; // Return the author's name
	}

	// Setter method for the author's name
	public void setAuthorName(String authorName) {
		this.authorName = authorName; // Set the author's name
	}

	// Getter method to check if the book is borrowed
	public boolean isBorrowed() {
		return isBorrowed; // Return whether the book is borrowed
	}

	// Setter method for the borrowed status
	public void setBorrowed(boolean isBorrowed) {
		this.isBorrowed = isBorrowed; // Set the borrowed status of the book
	}
}
