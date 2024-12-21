package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import logic.Subscriber;

/**
 * This class provides database connection and operations for interacting with
 * the BLibDB database. It allows retrieving and updating subscriber
 * information, user login, and handling SQL connection/disconnection.
 */
public class BLibDBC {
	private static Connection conn; // Connection object to interact with the database
	private static Statement stmt; // Statement object for executing SQL queries

	/**
	 * Retrieves a subscriber's details from the database using their subscriber ID.
	 * 
	 * @param subscriberid the ID of the subscriber to be fetched from the database
	 * @return a Subscriber object if found, otherwise null
	 */
	public static Subscriber getSubscriberByID(int subscriberid) {
		try {
			// Execute SQL query to fetch the subscriber by their ID
			ResultSet rs = stmt.executeQuery("SELECT * FROM subscribers WHERE subscriber_id = " + subscriberid);
			// If a result is found, create and return a Subscriber object
			if (rs.next()) {
				return new Subscriber(subscriberid, rs.getString(2), rs.getString(4), rs.getString(5), rs.getString(6));
			}
			// If no result is found, return null
			return null;
		} catch (SQLException e) {
			// If an error occurs, return null
			return null;
		}
	}

	/**
	 * Updates a subscriber's email and phone number in the database.
	 * 
	 * @param newSubscriber the Subscriber object containing updated information
	 * @return true if the update was successful, false if it failed
	 */
	public static Boolean updateSubscriber(Subscriber newSubscriber) {
		try {
			// Execute SQL update to modify subscriber details
			return stmt.execute("UPDATE subscribers SET subscriber_email = '" + newSubscriber.getEmail()
					+ "', subscriber_phone_number = '" + newSubscriber.getPhone() + "' WHERE subscriber_id = "
					+ newSubscriber.getId());
		} catch (SQLException e) {
			// If an error occurs, return false
			return null;
		}
	}

	/**
	 * Attempts to log in a user by matching the user ID and password.
	 * 
	 * @param userid   the ID of the user attempting to log in
	 * @param password the password of the user
	 * @return the user's role if the login is successful, otherwise null
	 */
	public static String login(int userid, String password) {
		try {
			// Execute SQL query to check if the user ID and password match
			ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE user_id = " + userid);
			// If a result is found and the credentials match, return the user's role
			if (rs.next()) {
				if (userid == rs.getInt(1) && password.equals(rs.getString(2)))
					return rs.getString(3);
			}
			// If login fails, return null
			return null;
		} catch (SQLException e) {
			// If an error occurs, return null
			return null;
		}
	}

	/**
	 * Establishes a connection to the database using the provided password.
	 * 
	 * @param password the password for the database connection
	 * @return true if the connection is successful, false if it fails
	 */
	public static boolean connect(String password) {
		try {
			// Try loading the MySQL JDBC driver
			Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();
			System.out.println("Driver definition succeed");
		} catch (Exception ex) {
			// If the driver fails to load, print error message and return false
			System.out.println("Driver definition failed");
			return false;
		}
		try {
			// Try connecting to the database
			conn = DriverManager.getConnection("jdbc:mysql://localhost/BLibDB?serverTimezone=IST", "root", password);
			stmt = conn.createStatement(); // Create a Statement object for executing queries
			System.out.println("SQL connection succeed");
			return true; // Return true if connection is successful
		} catch (Exception e) {
			// If the connection fails, print error message and return false
			System.out.println("SQL connection failed");
			return false;
		}
	}

	/**
	 * Closes the connection to the database.
	 * 
	 * @return true if the disconnection is successful, false if it fails
	 */
	public static boolean disconnect() {
		try {
			// Try closing the connection
			conn.close();
			return true; // Return true if disconnection is successful
		} catch (SQLException e) {
			// If disconnection fails, print error message and return false
			System.out.println("SQL disconnection failed");
			return false;
		}
	}
}
