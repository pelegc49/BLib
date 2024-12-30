package logic;

import java.io.Serializable;

public class BookCopy implements Serializable {

	// Private member variables for storing book details
	private BookTitle title;
	private int copyID; // ID of the title
	private String shelf; // Shelf number where the book is located
	private boolean isBorrowed; // Whether the book is currently borrowed or not
	

//	// override equals to ensure Set works as expected
//	@Override
//	public boolean equals(Object obj) {
//		if(obj instanceof BookCopy) {
//			return titleID == ((BookCopy)obj).titleID;
//		}
//		return false;
//	}
//	
	
	// Constructor to initialize a new Book object with all the necessary details
	public BookCopy(BookTitle title,int copyID, String shelf, boolean isBorrowed) {
		this.title = title; 
		this.copyID = copyID; // Set the book ID
		this.shelf = shelf; // Set the shelf number
		this.isBorrowed = isBorrowed; // Set whether the book is borrowed
	}

	
	
	public int getCopyID() {
		return copyID;
	}
	
	
	public String getShelf() {
		if (isBorrowed)
			return "-";
		return shelf;
	}
	
	
	public BookTitle getTitle() {
		return title;
	}
	
	
	// Getter method to check if the book is borrowed
	public boolean isBorrowed() {
		return isBorrowed; // Return whether the book is borrowed
	}
	
	// Setter method for the borrowed status
	public void setBorrowed(boolean isBorrowed) {
		this.isBorrowed = isBorrowed; // Set the borrowed status of the book
	}
//	// Getter method for the book name
//	public String getBookName() {
//		return bookName; // Return the book name
//	}
//	// Getter method for the book description
//	public String getBookDescription() {
//		return bookDescription; // Return the book description
//	}
	
//	// Setter method for the book description
//	public void setBookDescription(String bookDescription) {
//		this.bookDescription = bookDescription; // Set the book description
//	}
//	// Setter method for the book name
//	public void setBookName(String bookName) {
//		this.bookName = bookName; // Set the book name
//	}
//
//	// Getter method for the author's name
//	public String getAuthorName() {
//		return authorName; // Return the author's name
//	}
//
//	// Setter method for the author's name
//	public void setAuthorName(String authorName) {
//		this.authorName = authorName; // Set the author's name
//	}

}
