package server;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import logic.BookCopy;
import logic.BookTitle;
import logic.Subscriber;

/**
 * This class provides database connection and operations for interacting with
 * the BLibDB database. It allows retrieving and updating subscriber
 * information, user login, and handling SQL connection/disconnection.
 */
public class BLibDBC {
	private String pass;
	private static BLibDBC instance;

	private static Connection conn; // Connection object to interact with the database
	private static PreparedStatement pstmt; // Statement object for executing SQL queries

	// TODO: main for testing ONLY!!! delete before production!!!
	public static void main(String[] args) {
		BLibDBC db = getInstance();
		if (!db.connect("12341234"))
			return;

		System.out.println(db.createBorrow(1234, 3));
		System.out.println(db.createBorrow(1234, 5));
		System.out.println(db.createBorrow(321, 9));

		db.disconnect();
	}

	// Singleton pattern to ensure only one instance of BLibDBC exists
	public static BLibDBC getInstance() {
		if (!(instance instanceof BLibDBC)) {
			instance = new BLibDBC();
		}
		return instance;
	}

	/**
	 * Retrieves a book title from the database based on the title ID.
	 * 
	 * @param titleID the ID of the book title to be fetched
	 * @return the BookTitle object if found, otherwise null
	 */
	public BookTitle getTitleByID(int titleID) {
		try {
			pstmt = conn.prepareStatement("SELECT * FROM titles WHERE title_id = ?");
			pstmt.setInt(1, titleID); // Set the title ID parameter
			ResultSet rs = pstmt.executeQuery();
			// If a result is found, return a BookTitle object created from the result
			if (rs.next()) {
				return new BookTitle(titleID, rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5),
						rs.getInt(6));
			}
			return null; // Return null if no result is found
		} catch (SQLException e) {
			return null;// Return null if an error occurs
		}

	}

	// in order to make singleton work
	private BLibDBC() {
	}

	/**
	 * Retrieves all book copies associated with a particular book title.
	 * 
	 * @param title the BookTitle object whose copies are to be fetched
	 * @return a set of BookCopy objects associated with the given title
	 */
	public Set<BookCopy> getCopiesByTitle(BookTitle title) {
		try {
			// Execute SQL query
			pstmt = conn.prepareStatement("SELECT * FROM copies WHERE title_id = ?;");
			if (pstmt.isClosed())
				return null;
			pstmt.setInt(1, title.getTitleID()); // Set the title ID parameter
			ResultSet rs = pstmt.executeQuery();
			Set<BookCopy> bookSet = new HashSet<>();
			// Loop through result set and create BookCopy objects
			while (rs.next()) {
				BookCopy copy = new BookCopy(title, rs.getInt(2), rs.getString(3), rs.getBoolean(4));
				bookSet.add(copy);
			}
			return bookSet; // Return the set of book copies
		} catch (SQLException e) {
			return null; // Return null if an error occurs
		}
	}

	/**
	 * Retrieves a set of book titles that match a given keyword in the title,
	 * author, or description.
	 * 
	 * @param keyword the search keyword to be used
	 * @return a set of BookTitle objects that match the keyword
	 */
	public Set<BookTitle> getTitlesByKeyword(String keyword) {
		try {
			keyword = "%" + keyword + "%"; // Use wildcards for partial matching
			// Execute SQL query
			pstmt = conn.prepareStatement(
					"SELECT * FROM titles WHERE title_name LIKE ? OR author_name LIKE ? OR title_description LIKE ?;");
			if (pstmt.isClosed())
				return null;
			pstmt.setString(1, keyword);
			pstmt.setString(2, keyword);
			pstmt.setString(3, keyword);
			ResultSet rs = pstmt.executeQuery();
			Set<BookTitle> bookSet = new HashSet<>();
			// Loop through result set and create BookTitle objects
			while (rs.next()) {
				BookTitle title = new BookTitle(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getInt(5), rs.getInt(6));
				bookSet.add(title);
			}
			return bookSet; // Return the set of book titles
		} catch (SQLException e) {
			return null; // Return null if an error occurs
		}
	}

	/**
	 * Retrieves a subscriber's details from the database using their subscriber ID.
	 * 
	 * @param subscriberid the ID of the subscriber to be fetched from the database
	 * @return a Subscriber object if found, otherwise null
	 */
	public Subscriber getSubscriberByID(int subscriberid) {
		try {
			// Execute SQL query to fetch the subscriber by their ID
			pstmt = conn.prepareStatement("SELECT * FROM subscribers WHERE subscriber_id = ?");
			pstmt.setInt(1, subscriberid); // Set the subscriber ID parameter
			ResultSet rs = pstmt.executeQuery();
			// If a result is found, create and return a Subscriber object
			if (rs.next()) {
				return new Subscriber(subscriberid, rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
			}
			return null; // Return null if no result is found
		} catch (SQLException e) {
			return null; // Return null if an error occurs
		}
	}

	/**
	 * Creates a new borrow record and updates the book copy's status to 'borrowed'.
	 * 
	 * @param subscriberID the ID of the subscriber borrowing the book
	 * @param copyID       the ID of the book copy being borrowed
	 * @return true if the borrow operation is successful, otherwise false
	 */
	public Boolean createBorrow(int subscriberID, int copyID) {
		try {
			// Fetch subscriber and book copy objects to ensure they exist
			Subscriber sub = getSubscriberByID(subscriberID);
			if (sub == null) {
				return false; // Return false if subscriber not found
			}
			BookCopy copy = getCopyByID(copyID);
			if (copy == null) {
				return false; // Return false if book copy not found
			}

			// Calculate today's date and the due date (2 weeks from today)
			LocalDate today = LocalDate.now();
			LocalDate dueDate = today.plusWeeks(2);

			// Insert new borrow record into the database
			pstmt = conn.prepareStatement("INSERT INTO borrows VALUE(?,?,?,?,?)");
			pstmt.setInt(1, subscriberID);
			pstmt.setInt(2, copyID);
			pstmt.setDate(3, Date.valueOf(today));
			pstmt.setDate(4, Date.valueOf(dueDate));
			pstmt.setDate(5, null); // Set return date as null for now
			pstmt.execute();

			// Update the book copy's status to borrowed
			pstmt = conn.prepareStatement("UPDATE copies SET is_borrowed = ? WHERE copy_id = ?");
			pstmt.setBoolean(1, true);
			pstmt.setInt(2, copyID);
			pstmt.execute();

			// Log the borrow activity in the history table
			pstmt = conn.prepareStatement(
					"INSERT INTO history(subscriber_id,activity_type,activity_description,activity_date) VALUE(?,?,?,?)");
			pstmt.setInt(1, subscriberID);
			pstmt.setString(2, "borrow");
			pstmt.setString(3,
					"\"%s\" borrowed by %s on %s".formatted(copy.getTitle(), sub.getName(), today.toString()));
			pstmt.setDate(4, Date.valueOf(today));
			pstmt.execute();

			// Commit the transaction
			conn.commit();
			return true; // Return true if all operations succeed
		} catch (SQLException e) {
			rollback(); // Rollback transaction if any error occurs
			return false; // Return false if an error occurs
		}
	}

	/**
	 * Retrieves a book copy from the database based on the copy ID.
	 * 
	 * @param copyID the ID of the book copy to be fetched
	 * @return the BookCopy object if found, otherwise null
	 */
	public BookCopy getCopyByID(int copyID) {
		try {
			pstmt = conn.prepareStatement("SELECT * FROM copies WHERE copy_id = ?");
			pstmt.setInt(1, copyID); // Set the copy ID parameter
			ResultSet rs = pstmt.executeQuery();
			// If a result is found, create a BookCopy object
			if (rs.next()) {
				BookTitle title = getTitleByID(rs.getInt(1)); // Get the title associated with this copy
				if (title == null) {
					return null; // Return null if the title is not found
				}
				return new BookCopy(title, copyID, rs.getString(3), rs.getBoolean(4));
			}
			return null; // Return null if no copy is found
		} catch (SQLException e) {
			return null; // Return null if an error occurs
		}
	}

	/**
	 * Updates a subscriber's email and phone number in the database.
	 * 
	 * @param newSubscriber the Subscriber object containing updated information
	 * @return true if the update was successful, false if it failed
	 */
	public Boolean updateSubscriber(Subscriber newSubscriber) {
		try {
			// Execute SQL update to modify subscriber details
			pstmt = conn.prepareStatement(
					"UPDATE subscribers SET subscriber_email = ?, subscriber_phone_number = ?  WHERE subscriber_id = ?");
			pstmt.setString(1, newSubscriber.getEmail());
			pstmt.setString(2, newSubscriber.getPhone());
			pstmt.setInt(3, newSubscriber.getId());
			pstmt.execute();

			// Commit the transaction to make the changes persistent
			conn.commit();
			return true; // Return true if the update is successful
		} catch (SQLException e) {
			rollback(); // Rollback transaction if any error occurs
			return false; // Return false if an error occurs
		}
	}

	/**
	 * Rolls back the current transaction in case of an error.
	 */
	private void rollback() {
		try {
			conn.rollback(); // Rollback the current transaction
		} catch (SQLException e) {
			// If rollback fails, attempt to disconnect and reconnect to the database
			disconnect();
			connect(pass);
		}
	}

	/**
	 * Registers a new subscriber by inserting their information into the database.
	 * 
	 * @param subscriber the Subscriber object containing the information to be
	 *                   inserted
	 * @return true if the registration was successful, false if it failed
	 */
	public Boolean registerSubscriber(Subscriber subscriber) {
		try {
			// Insert a new subscriber record into the database
			pstmt = conn.prepareStatement("INSERT INTO subscribers VALUE(?,?,?,?,?)");
			pstmt.setInt(1, subscriber.getId());
			pstmt.setString(2, subscriber.getName());
			pstmt.setString(3, subscriber.getPhone());
			pstmt.setString(4, subscriber.getEmail());
			pstmt.setString(5, subscriber.getStatus());
			pstmt.execute();

			// Commit the transaction
			conn.commit();
			return true; // Return true if the registration is successful
		} catch (SQLException e) {
			rollback(); // Rollback transaction if any error occurs
			return false; // Return false if an error occurs
		}
	}

	/**
	 * Attempts to log in a user by matching the user ID and password.
	 * 
	 * @param userid   the ID of the user attempting to log in
	 * @param password the password of the user
	 * @return the user's role if the login is successful, otherwise null
	 */
	public String login(int userid, String password) {
		try {
			// Execute SQL query to check if the user ID and password match
			pstmt = conn.prepareStatement("SELECT * FROM users WHERE user_id = ?");
			pstmt.setInt(1, userid);
			ResultSet rs = pstmt.executeQuery();
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
	public boolean connect(String password) {
		pass = password; // Store the password for later use in reconnection
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
			conn = DriverManager.getConnection("jdbc:mysql://localhost/BLibDB?useSSL=FALSE&serverTimezone=IST", "root",
					password);
			conn.setAutoCommit(false); // Disable auto-commit to handle transactions manually
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
	public boolean disconnect() {
		try {
			if (conn == null) {
				System.out.println("SQL disconnection failed (connection is null)");
				return false; // Return false if the connection is null
			}
			if (conn.isClosed()) {
				System.out.println("SQL disconnection failed (connection is closed)");
				return false; // Return false if the connection is already closed
			}
			// Try closing the connection
			conn.close();
			return true; // Return true if disconnection is successful
		} catch (SQLException e) {
			// If disconnection fails, print error message and return false
			System.out.println("SQL disconnection failed (exception thrown)");
			return false;
		}
	}

}
