package logic;

import java.io.Serializable;

public class BookTitle implements Serializable {
	private int titleID;
	private String titleName;
	private String authorName;
	private String description;
	private int numOfOrders;
	private int numOfCopies;
	//private String genre;

	public BookTitle(int titleID, String titleName, String authorName, String description, int numOfOrders,
			int numOfCopies/*, String genre*/) {
		this.titleID = titleID;
		this.titleName = titleName;
		this.authorName = authorName;
		this.description = description;
		this.numOfOrders = numOfOrders;
		this.numOfCopies = numOfCopies;
		//this.genre = genre;
	}

//	public String getGenre() {
//		return genre;
//	}
//
//	public void setGenre(String genre) {
//		this.genre = genre;
//	}
	
	public int getTitleID() {
		return titleID;
	}

	public String getTitleName() {
		return titleName;
	}

	public String getAuthorName() {
		return authorName;
	}

	public String getDescription() {
		return description;
	}

	public int getNumOfOrders() {
		return numOfOrders;
	}

	public int getNumOfCopies() {
		return numOfCopies;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BookTitle)
			return ((BookTitle)obj).titleID == this.titleID;
		return false;
	}

	@Override
	public String toString() {
		return authorName + " : " + titleName;
	}

}
