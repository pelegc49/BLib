package logic;

import java.io.Serializable;

/**
 * The Order class represents an order with a unique identifier. It implements
 * {@link Serializable} to allow the object to be serialized for network
 * transmission or storage.
 */
public class Order implements Serializable {

	private int id; // The unique identifier for the order

	/**
	 * Constructs an Order object with a given id.
	 * 
	 * @param id the unique identifier for the order
	 */
	public Order(int id) {
		this.id = id; // Initialize the id of the order
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
}
