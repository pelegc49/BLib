package server;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Scanner;

import javax.swing.JOptionPane;

import logic.Subscriber;

public class BLibDBC {
	private static Connection conn;
	private static Statement stmt;
	public static void main(String[] args) {//// KEEP FOR TESTING
		connect();
	}
	
	public static Subscriber getSubscriberByID(int subscriberid) {
		try {
			ResultSet rs = stmt.executeQuery(
		"SELECT * FROM subscribers WHERE subscriber_id = "+subscriberid);
			if(rs.next()) {
					return new Subscriber(subscriberid, rs.getString(2), rs.getString(4), rs.getString(5), rs.getString(6));
			}
			return null;
		} catch (SQLException e) {
			return null;
		}
	}
	public static Boolean updateSubscriber(Subscriber newSubscriber) {
		try {
			return stmt.execute(
		"UPDATE subscribers SET subscriber_email = '"+newSubscriber.getEmail()+"', subscriber_phone_number = '"+newSubscriber.getPhone()+"' WHERE subscriber_id = "+newSubscriber.getId());
		} catch (SQLException e) {
			return null;
		}
	}
	
	public static String login(int userid,String password ) {
		try {
			ResultSet rs = stmt.executeQuery(
		"SELECT * FROM users WHERE user_id = "+userid);
			if(rs.next()) {
				if(userid == rs.getInt(1) && password.equals(rs.getString(2)))
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
		Scanner scanner = null;
		String password ="";
		try {
			File file = new File("SQL_PASS.txt");
			scanner = new Scanner(file);
			password = scanner.nextLine();
			scanner.close();
		}catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null,"Please create a \"SQL_PASS.txt\" file with a valid password" , "Error", JOptionPane.INFORMATION_MESSAGE);
			System.exit(0);
		}
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/BLibDB?serverTimezone=IST", "root",password);
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
