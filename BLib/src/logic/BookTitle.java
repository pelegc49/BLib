package logic;

import java.io.Serializable;

public class BookTitle implements Serializable{
	private int id;
	private String titleName;
	private String authorName;
	private String description;
	private int numOfOrders;
	private int numOfCopies;
	
	
	public BookTitle(int id, String titleName, String authorName, String description, int numOfOrders, int numOfCopies) {
		this.id = id;
		this.titleName = titleName;
		this.authorName = authorName;
		this.description = description;
		this.numOfOrders = numOfOrders;
		this.numOfCopies = numOfCopies;
	}


	public int getId() {
		return id;
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
		
}
