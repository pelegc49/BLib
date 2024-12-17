package logic;

public class Book {
	
	private int id;
	private int shelf;
	private String bookName;
	private String authorName;
	private boolean isBorrowed;
	
	public Book(int id, int shelf, String bookName, String authorName, boolean isBorrowed) {
		this.id = id;
		this.shelf = shelf;
		this.bookName = bookName;
		this.authorName = authorName;
		this.isBorrowed = isBorrowed;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getShelf() {
		return shelf;
	}

	public void setShelf(int shelf) {
		this.shelf = shelf;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public boolean isBorrowed() {
		return isBorrowed;
	}

	public void setBorrowed(boolean isBorrowed) {
		this.isBorrowed = isBorrowed;
	}
}
