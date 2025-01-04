package server;

import java.security.AlgorithmParametersSpi;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import logic.Activity;
import logic.BookCopy;
import logic.BookTitle;
import logic.Borrow;
import logic.Message;
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
		System.out.println(db.orderBook(4444, 10));
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
			pstmt = conn.prepareStatement(
					"INSERT INTO borrows(subscriber_id,copy_id,date_of_borrow,due_date) VALUE(?,?,?,?)");
			pstmt.setInt(1, subscriberID);
			pstmt.setInt(2, copyID);
			pstmt.setDate(3, Date.valueOf(today));
			pstmt.setDate(4, Date.valueOf(dueDate));
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
			
			// time the execution of send message
			LocalDateTime now = LocalDateTime.now();
			LocalDateTime reminderTime = now.plusWeeks(2).minusDays(1);
			
			pstmt = conn.prepareStatement(
					"INSERT INTO commands(command, arguments, time_of_execution, identifyer) VALUE(?,?,?,?)");
			pstmt.setString(1, "sendEmail");
			pstmt.setString(2, "%s;dear %s,\nTomorrow (%s), your borrow of %s is due.\nPlease return it soon.\nBraude Library".formatted(sub.getEmail(),sub.getName(),dueDate,copy.getTitle()));
			pstmt.setTimestamp(3, Timestamp.valueOf(reminderTime));
			pstmt.setString(4, "%s;%s".formatted(sub.getId(),copy.getCopyID()));
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
	public Boolean updateSubscriber(Subscriber newSubscriber, String userType) {
		try {
			LocalDate today = LocalDate.now();
			Subscriber oldSubscriber = getSubscriberByID(newSubscriber.getId());
			StringBuilder str = new StringBuilder();
			if(userType.equalsIgnoreCase("subscriber")) {
				str.append(newSubscriber.getName() + " updated their details: ");
			}
			else {
				str.append(userType + " updated " + newSubscriber.getName() + "'s details: ");
			}
			// Execute SQL update to modify subscriber details
			pstmt = conn.prepareStatement(
					"UPDATE subscribers SET subscriber_email = ?, subscriber_phone_number = ?  WHERE subscriber_id = ?");
			pstmt.setString(1, newSubscriber.getEmail());
			pstmt.setString(2, newSubscriber.getPhone());
			pstmt.setInt(3, newSubscriber.getId());
			pstmt.execute();

			if (!newSubscriber.getEmail().equals(oldSubscriber.getEmail())) {
				str.append(
						"changed email from %s to %s ".formatted(oldSubscriber.getEmail(), newSubscriber.getEmail()));
			}
			if (!newSubscriber.getPhone().equals(oldSubscriber.getPhone())) {
				str.append(
						"changed phone from %s to %s ".formatted(oldSubscriber.getPhone(), newSubscriber.getPhone()));
			}
			str.append("on %s".formatted(today));

			// Log the borrow activity in the history table
			pstmt = conn.prepareStatement(
					"INSERT INTO history(subscriber_id,activity_type,activity_description,activity_date) VALUE(?,?,?,?)");
			pstmt.setInt(1, newSubscriber.getId());
			pstmt.setString(2, "update subscriber");
			pstmt.setString(3, str.toString());

			pstmt.setDate(4, Date.valueOf(today));
			pstmt.execute();

			// Commit the transaction
			conn.commit();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
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
	public Boolean registerSubscriber(Subscriber subscriber,String password) {
		try {
			LocalDate today = LocalDate.now();
			// Insert a new subscriber record into the database
			pstmt = conn.prepareStatement("INSERT INTO subscribers VALUE(?,?,?,?,?)");
			pstmt.setInt(1, subscriber.getId());
			pstmt.setString(2, subscriber.getName());
			pstmt.setString(3, subscriber.getPhone());
			pstmt.setString(4, subscriber.getEmail());
			pstmt.setString(5, subscriber.getStatus());
			pstmt.execute();

			// Log the borrow activity in the history table
			pstmt = conn.prepareStatement(
					"INSERT INTO history(subscriber_id,activity_type,activity_description,activity_date) VALUE(?,?,?,?)");
			pstmt.setInt(1, subscriber.getId());
			pstmt.setString(2, "new subscriber");
			pstmt.setString(3, "%s is now a subscriber since %s".formatted(subscriber.getName(), today));

			pstmt.setDate(4, Date.valueOf(today));
			pstmt.execute();

			// create new user to the new subscriber
			pstmt  = conn.prepareStatement("INSERT INTO users VALUE (?,?,?)");
			pstmt.setInt(1, subscriber.getId());
			pstmt.setString(2, password);
			pstmt.setString(3, "subscriber");
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
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost/BLibDB?useSSL=FALSE&serverTimezone=Asia/Jerusalem", "root", password);
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

	/**
	 * Retrieves the active Borrow record for a given BookCopy if it exists.
	 * 
	 * @param copy The BookCopy for which to retrieve the active Borrow.
	 * @return The active Borrow object or null if not found or an error occurs.
	 */
	public Borrow getCopyActiveBorrow(BookCopy copy) {
		try {
			pstmt = conn.prepareStatement("SELECT * FROM borrows WHERE copy_id = ? AND date_of_return IS NULL");
			pstmt.setInt(1, copy.getCopyID());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				Subscriber sub = getSubscriberByID(rs.getInt(2));
				if (sub == null) {
					return null;
				}
				return new Borrow(sub, copy, rs.getDate(4).toLocalDate(), rs.getDate(5).toLocalDate(), null);
			}
			return null;
		} catch (SQLException e) {
			// If an error occurs, return null
			return null;
		}

	}

	
	
	/**
	 * Extends the duration of a Borrow by a specified number of days and logs the
	 * extension activity.
	 * 
	 * @param borrow   The Borrow to extend.
	 * @param days     The number of days to extend the duration by.
	 * @param userType The type of user initiating the extension (e.g., "subscriber"
	 *                 or admin type).
	 * @return True if the extension is successful, false otherwise.
	 */
	public Boolean extendDuration(Borrow borrow, int days, String userType) {
		try {
			LocalDate newDueDate = borrow.getDueDate().plusDays(days);
			// Log the extension activity in the history table
			pstmt = conn.prepareStatement("UPDATE borrows SET due_date = ? WHERE "
					+ "subscriber_id = ? AND copy_id = ? AND date_of_borrow = ?");
			pstmt.setDate(1, Date.valueOf(newDueDate));
			pstmt.setInt(2, borrow.getSubscriber().getId());
			pstmt.setInt(3, borrow.getBook().getCopyID());
			pstmt.setDate(4, Date.valueOf(borrow.getDateOfBorrow()));
			pstmt.execute();

			LocalDate today = LocalDate.now();
			// Log the extension activity in the history table
			pstmt = conn.prepareStatement(
					"INSERT INTO history(subscriber_id,activity_type,activity_description,activity_date) VALUE(?,?,?,?)");
			pstmt.setInt(1, borrow.getSubscriber().getId());
			if (userType.equals("subscriber")) {
				pstmt.setString(2, "extension");
				pstmt.setString(3, "\"%s\" extended borrow by %s on %s, the new due date is %s"
						.formatted(borrow.getBook().getTitle(), borrow.getSubscriber().getName(), today, newDueDate));
			
			} else {
				pstmt.setString(2, "manual extension");
				pstmt.setString(3,
						"\"%s\" manually extended borrow of %s by %s on %s, the new due date is %s".formatted(
								borrow.getBook().getTitle(), borrow.getSubscriber().getName(), userType, today,
								newDueDate));
			}
			pstmt.setDate(4, Date.valueOf(today));
			pstmt.execute();
			
			if(userType.equals("subscriber")) {
				LocalDateTime now = LocalDateTime.now();
				pstmt = conn.prepareStatement("INSERT INTO librarian_messages(message, time) VALUE(?,?);");
				pstmt.setString(1, "the subscriber %s extended their borrow duration of %s by %d days, the new due date is %s"
						.formatted(borrow.getSubscriber().getName(),borrow.getBook().getTitle(),days,newDueDate));
				pstmt.setTimestamp(2, Timestamp.valueOf(now));
				pstmt.execute();
			}
			// Commit the transaction
			conn.commit();
			return true; // Return true if the extension is successful
		} catch (SQLException e) {
			rollback(); // Rollback transaction if any error occurs
			return false; // Return false if an error occurs
		}
	}

	/**
	 * Retrieves the number of allowed extensions for a given BookTitle.
	 * 
	 * @param title The BookTitle for which to calculate the number of allowed
	 *              extensions.
	 * @return The number of allowed extensions, or null if an error occurs.
	 */
	public Integer getTitleMagicNumber(BookTitle title) {
		// 0 < num  <= num of copies  : { there are between 0 to num of copies active borrows for the title}
		// num = 0 : {all the copies are borrowed}
		// -num of copies <= num < 0 : {there are |num| active orders for the title} 
		// num = num of copies - num of borrows - num of orders 
		int sum = 0;
		ResultSet rs;
		try {
			pstmt = conn.prepareStatement("SELECT num_of_copies FROM titles WHERE title_id = ?;");
			pstmt.setInt(1, title.getTitleID());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				sum += rs.getInt(1); // + num of copies
			} else {
				return null;
			}

			pstmt = conn.prepareStatement("SELECT sum(is_borrowed) FROM copies WHERE title_id = ? GROUP BY title_id;");
			pstmt.setInt(1, title.getTitleID());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				sum -= rs.getInt(1); // - num of borrows
			} else {
				return null;
			}

			pstmt = conn.prepareStatement("SELECT num_of_orders FROM titles WHERE title_id = ?;");
			pstmt.setInt(1, title.getTitleID());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				sum -= rs.getInt(1); // - num of orders
			} else {
				return null;
			}
			return sum ;
		} catch (SQLException e) {
			return null;
		}
	}

	/**
	 * Processes the return of a borrowed book copy and logs the return activity.
	 * 
	 * @param book         The BookCopy being returned.
	 * @param isLateReturn True if the return is late, false otherwise.
	 * @return True if the return is successful, false otherwise.
	 */
	public Boolean returnBook(BookCopy book, boolean isLateReturn) {
		try {
			Borrow borrow = getCopyActiveBorrow(book);
			if (borrow == null)
				return false;

			LocalDate today = LocalDate.now();
			pstmt = conn.prepareStatement(
					"UPDATE borrows SET date_of_return = ? WHERE subscriber_id = ? AND copy_id =? AND date_of_borrow = ?;");
			pstmt.setDate(1, Date.valueOf(today));
			pstmt.setInt(2, borrow.getSubscriber().getId());
			pstmt.setInt(3, book.getCopyID());
			pstmt.setDate(4, Date.valueOf(borrow.getDateOfBorrow()));
			pstmt.execute();

			pstmt = conn.prepareStatement("UPDATE copies SET is_borrowed = ? WHERE copy_id = ?;");
			pstmt.setBoolean(1, false);
			pstmt.setInt(2, book.getCopyID());
			pstmt.execute();

			//TODO: update orders
			
			// Log the return activity in the history table
			pstmt = conn.prepareStatement(
					"INSERT INTO history(subscriber_id,activity_type,activity_description,activity_date) VALUE(?,?,?,?)");
			pstmt.setInt(1, borrow.getSubscriber().getId());
			if (!isLateReturn) {
				pstmt.setString(2, "return");
				pstmt.setString(3, "\"%s\" return by %s on %s".formatted(book.getTitle(),
						borrow.getSubscriber().getName(), today));
			} else {
				int late = today.compareTo(borrow.getDueDate());
				pstmt.setString(2, "late return");
				pstmt.setString(3, "\"%s\" late return by %s on %s late by %d days".formatted(book.getTitle(),
						borrow.getSubscriber().getName(), today, Math.abs(late)));
			}

			pstmt.setDate(4, Date.valueOf(today));
			pstmt.execute();

			// Commit the transaction
			conn.commit();
			return true;

		} catch (SQLException e) {
			rollback(); // Rollback transaction if any error occurs
			return false; // Return false if an error occurs
		}
	}

	/**
	 * Freezes a subscriber's account and logs the action.
	 * 
	 * @param subID The ID of the subscriber to freeze.
	 * @return True if the operation is successful, false otherwise. Null if the
	 *         subscriber does not exist.
	 */
	public Boolean freezeSubscriber(int subID) {
		try {
			LocalDate today = LocalDate.now();
			Subscriber sub = getSubscriberByID(subID);
			if (sub == null)
				return null;
			pstmt = conn.prepareStatement("UPDATE subscribers SET subscriber_status = ? WHERE subscriber_id = ? ;");
			pstmt.setString(1, "frozen");
			pstmt.setInt(2, subID);
			pstmt.execute();

			// Log the freeze Subscriber activity in the history table
			pstmt = conn.prepareStatement(
					"INSERT INTO history(subscriber_id,activity_type,activity_description,activity_date) VALUE(?,?,?,?)");
			pstmt.setInt(1, subID);
			pstmt.setString(2, "freeze");
			pstmt.setString(3, "%s got frozen on %s until %s".formatted(sub.getName(), today, today.plusMonths(1)));
			pstmt.setDate(4, Date.valueOf(today));
			pstmt.execute();
			
			
			LocalDateTime unfreezeTime = LocalDateTime.now().plusMonths(1);
			pstmt = conn.prepareStatement(
					"INSERT INTO commands(command, arguments, time_of_execution, identifyer) VALUE(?,?,?,?)");
			pstmt.setString(1, "unfreeze");
			pstmt.setString(2, "%d".formatted(sub.getId()));
			pstmt.setTimestamp(3, Timestamp.valueOf(unfreezeTime));
			pstmt.setString(4, "%d".formatted(sub.getId()));
			pstmt.execute();
			
			// Commit the transaction
			conn.commit();
			return true;

		} catch (SQLException e) {
			rollback(); // Rollback transaction if any error occurs
			return false; // Return false if an error occurs
		}
	}

	/**
	 * Unfreezes a subscriber's account and logs the action.
	 * 
	 * @param subID The ID of the subscriber to unfreeze.
	 * @return True if the operation is successful, false otherwise. Null if the
	 *         subscriber does not exist.
	 */
	public Boolean unfreezeSubscriber(int subID) {
		try {
			LocalDate today = LocalDate.now();
			Subscriber sub = getSubscriberByID(subID);
			if (sub == null)
				return null;
			pstmt = conn.prepareStatement("UPDATE subscribers SET subscriber_status = ? WHERE subscriber_id = ? ;");
			pstmt.setString(1, "active");
			pstmt.setInt(2, subID);
			pstmt.execute();

			// Log the unfreeze Subscriber activity in the history table
			pstmt = conn.prepareStatement(
					"INSERT INTO history(subscriber_id,activity_type,activity_description,activity_date) VALUE(?,?,?,?)");
			pstmt.setInt(1, subID);
			pstmt.setString(2, "unfreeze");
			pstmt.setString(3, "%s got unfrozen on %s ".formatted(sub.getName(), today));

			pstmt.setDate(4, Date.valueOf(today));
			pstmt.execute();
			// Commit the transaction
			conn.commit();
			return true;

		} catch (SQLException e) {
			rollback(); // Rollback transaction if any error occurs
			return false; // Return false if an error occurs
		}
	}
	
	
	public List<Activity> getSubscriberHistory(int subID){
		try {
		Subscriber sub = getSubscriberByID(subID);
		if (sub == null)
			return null;
		pstmt = conn.prepareStatement("SELECT * FROM history WHERE subscriber_id = ? ORDER BY activity_date;");
		pstmt.setInt(1, subID);
		ResultSet rs = pstmt.executeQuery();
		List<Activity> ret = new ArrayList<>();
		while (rs.next()) {
			Activity activity = new Activity(rs.getInt(1), rs.getString(3), rs.getString(4), rs.getDate(5).toLocalDate());
			ret.add(activity);
		}
		return ret;
		} catch (SQLException e) {
			return null;
		}
	}
	
	public Boolean orderTitle(BookTitle title, Subscriber sub) {
		try {
			LocalDate today = LocalDate.now();
			pstmt = conn.prepareStatement("UPDATE titles SET num_of_orders = num_of_orders + 1 WHERE title_id = ? ;");
			pstmt.setInt(1, title.getTitleID());
			pstmt.execute();

			
			pstmt = conn.prepareStatement(
					"INSERT INTO history(subscriber_id,activity_type,activity_description,activity_date) VALUE(?,?,?,?)");
			pstmt.setInt(1, sub.getId());
			pstmt.setString(2, "order");
			pstmt.setString(3, "%s ordered %s on %s".formatted(sub.getName(),title, today));

			pstmt.setDate(4, Date.valueOf(today));
			pstmt.execute();
			// Commit the transaction
			conn.commit();
			return true;

		} catch (SQLException e) {
			rollback(); // Rollback transaction if any error occurs
			return false; // Return false if an error occurs
		}
	}
	
	//TODO:test
	public List<Message> getCommands(){
		LocalDateTime now = LocalDateTime.now();
		try {
			pstmt = conn.prepareStatement("SELECT * FROM commands WHERE time_of_execution < ?");
			pstmt.setTimestamp(1, Timestamp.valueOf(now));
			ResultSet rs = pstmt.executeQuery();
			List<Integer> commandIDs = new ArrayList<>();
			List<Message> ret = new ArrayList<>();
			while(rs.next()) {
				commandIDs.add(rs.getInt(1));
				Message msg = new Message(rs.getString(2));
				for(String arg: rs.getString(3).split(";")) {
					msg.addArgument(arg);
				}
				ret.add(msg);
			}

			for(int i:commandIDs) {
				pstmt = conn.prepareStatement("DELETE FROM commands WHERE id = ?");
				pstmt.setInt(1, i);
				pstmt.execute();
			}
			// Commit the transaction
			conn.commit();
			return ret;

		} catch (SQLException e) {
			rollback(); // Rollback transaction if any error occurs
			return null; // Return false if an error occurs
		}
	}
	//TODO:test
	public List<Borrow> getSubscriberActiveBorrows(Subscriber sub) {
		try {
			pstmt = conn.prepareStatement("SELECT * FROM borrows WHERE subscriber_id = ? AND date_of_return IS NULL");
			pstmt.setInt(1, sub.getId());
			ResultSet rs = pstmt.executeQuery();
			List<Borrow> ret = new ArrayList<>();
			while (rs.next()) {
				BookCopy copy = getCopyByID(rs.getInt(3));
				if (copy == null) {
					System.out.println("copy id " + rs.getInt(3) + " not found");
					return null;
				}
				ret.add(new Borrow(sub, copy, rs.getDate(4).toLocalDate(), rs.getDate(5).toLocalDate(), null));
			}
			return ret;
		} catch (SQLException e) {
			// If an error occurs, return null
			return null;
		}

	}

	public List<String> getLibrarianMessages(){
		try {
			pstmt = conn.prepareStatement("SELECT message FROM librarian_messages ORDER BY time");
			ResultSet rs = pstmt.executeQuery();
			List<String> ret = new ArrayList<>();
			while (rs.next()) {
				ret.add(rs.getString(1));
			}
			return ret;
		} catch (SQLException e) {
			// If an error occurs, return null
			return null;
		}
	}
	public Boolean clearLibrarianMessages(){
		try {
			pstmt = conn.prepareStatement("DELETE FROM librarian_messages;");
			pstmt.execute();
			conn.commit();
			return true;
		} catch (SQLException e) {
			rollback();
			return false;
		}
	}
	
	public Boolean orderBook(int subID, int titleID){
		try {
			Subscriber sub = getSubscriberByID(subID);
			if(sub==null) {
				return false;
			}
			BookTitle title = getTitleByID(titleID);
			if(title==null) {
				return false;
			}
			LocalDate today = LocalDate.now();
			
			
			//orders
			pstmt = conn.prepareStatement("INSERT INTO orders(subscriber_id, title_id, order_date) VALUE (?,?,?);");
			pstmt.setInt(1,subID);
			pstmt.setInt(2,titleID);
			pstmt.setDate(3,Date.valueOf(today));
			pstmt.execute();
			
			//titles
			pstmt = conn.prepareStatement("UPDATE titles SET num_of_orders = num_of_orders + 1 WHERE title_id = ?;");
			pstmt.setInt(1,titleID);
			pstmt.execute();
			
			//history
			pstmt = conn.prepareStatement(
					"INSERT INTO history(subscriber_id,activity_type,activity_description,activity_date) VALUE(?,?,?,?)");
			pstmt.setInt(1, subID);
			pstmt.setString(2, "order");
			pstmt.setString(3, "%s ordered the book \"%s\" on %s".formatted(sub.getName(), title, today));
			pstmt.setDate(4, Date.valueOf(today));
			pstmt.execute();
			conn.commit();
			return true;
		} catch (SQLException e) {
			rollback();
			return false;
		}
	}

	public Integer getNumOfCopies(BookTitle title) {	
		try {
			pstmt = conn.prepareStatement("SELECT sum(is_borrowed) FROM copies WHERE title_id = ? GROUP BY title_id;");
			pstmt.setInt(1, title.getTitleID());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1); 
			} else {
				return null;
			}
		} catch (SQLException e) {
			return null;					
		}
	}

	
	
	
	
	
}









