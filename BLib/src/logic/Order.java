package logic;

import java.io.Serializable;

public class Order implements Serializable{

	private int id;
	
	public Order(int id) {
		this.id=id;
	}
	
	public void setId(int id) {
		this.id=id;
	}
	
	public int getId() {
		return this.id;
	}
}
