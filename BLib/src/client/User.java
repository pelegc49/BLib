package client;

import java.io.Serializable; // Importing the Serializable interface to allow object serialization.

/**
 * The User class represents a user in the client application. It implements the
 * Serializable interface to allow instances of this class to be serialized and
 * transmitted over a network.
 */
public class User implements Serializable {

	private String id; // The unique identifier for the user.
	private String password; // The password for the user.
	private String type; // The type of user (e.g., admin, subscriber, etc.).

	/**
	 * Constructor to create a new User object with the specified ID, password, and
	 * type.
	 * 
	 * @param id       The unique identifier for the user.
	 * @param password The password for the user.
	 * @param type     The type of user.
	 */
	public User(String id, String password, String type) {
		this.id = id;
		this.password = password;
		this.type = type;
	}

	/**
	 * Getter method for the user's ID.
	 * 
	 * @return The ID of the user.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Getter method for the user's password.
	 * 
	 * @return The password of the user.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Getter method for the user's type.
	 * 
	 * @return The type of the user.
	 */
	public String getType() {
		return type;
	}

	/**
	 * Setter method to update the user's ID.
	 * 
	 * @param id The new ID for the user.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Setter method to update the user's password.
	 * 
	 * @param password The new password for the user.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Setter method to update the user's type.
	 * 
	 * @param type The new type for the user.
	 */
	public void setType(String type) {
		this.type = type;
	}
}
