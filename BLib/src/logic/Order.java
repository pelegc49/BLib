package logic;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * The Order class represents an order with a unique identifier. It implements
 * {@link Serializable} to allow the object to be serialized for network
 * transmission or storage.
 */
public class Order implements Serializable {


	private int id; // The unique identifier for the order
	private Subscriber subscriber;
	private BookTitle title;
	private BookCopy copy;
	private LocalDateTime order_date;
	private LocalDate arive_date;
	
	


	public Order(int id, Subscriber subscriber, BookTitle title, LocalDateTime order_date) {
		this.id = id;
		this.subscriber = subscriber;
		this.title = title;
		this.copy = null;
		this.order_date = order_date;
		this.arive_date = null;
	}

	public boolean setCopy(BookCopy copy) {
		if (copy.getTitle().equals(title)) {
			this.copy = copy;
			return true;
		}
		return false;
			
	}
	
	
	public void setAriveDate(LocalDate arive_date) {
		this.arive_date = arive_date;
			
	}
	/**
	 * Sets the id of the order.
	 * 
	 * @param id the unique identifier to be set for the order
	 */
	public void setId(int id) {
		this.id = id; // Set the id of the order
	}

	/**
	 * Gets the id of the order.
	 * 
	 * @return the unique identifier of the order
	 */
	public int getId() {
		return this.id; // Return the id of the order
	}
	public Subscriber getSubscriber() {
		return subscriber;
	}
	
	public BookTitle getTitle() {
		return title;
	}
	
	public LocalDateTime getOrder_date() {
		return order_date;
	}
	
	public LocalDate getArive_date() {
		return arive_date;
	}
}
