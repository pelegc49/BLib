package server;

import java.sql.*;

public class BLibDBC {
	private static Connection conn;
	private static Statement stmt;
	public static void main(String[] args) {//// KEEP FOR TESTING
		connect();
	}
	
	public static String login(String username,String password ) {
		try {
			ResultSet rs = stmt.executeQuery(
		"SELECT * FROM users WHERE user_name = '"+username+"'"
					);
			if(rs.next()) {
				if(username.equalsIgnoreCase(rs.getString(1)) && password.equals(rs.getString(2)))
					return rs.getString(3);
			}
			return null;
		} catch (SQLException e) {
			return null;
		}

	}
	
	public static boolean connect() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();
			System.out.println("Driver definition succeed");
		} catch (Exception ex) {
			System.out.println("Driver definition failed");
			return false;
		}
		
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/BLibDB?serverTimezone=IST", "root","12341234");
			stmt = conn.createStatement();
			System.out.println("SQL connection succeed");
			return true;
		}catch (Exception e){
			System.out.println("SQL connection failed");
			return false;
		}
	}
	public static boolean disconnect() {
		try {
			conn.close();
			return true;
		} catch (SQLException e) {
			System.out.println("SQL disconnection failed");
			return false;
		}
	}
}
