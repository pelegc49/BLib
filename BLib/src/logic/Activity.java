package logic;

import java.time.LocalDate;

public class Activity {

	// Private member variables to store the activity details
	private int id; // ID of the subscriber
	private String type; // Type of the activity (e.g., "Meeting", "Task", etc.)
	private String description; // A description of the activity
	private LocalDate date; // The date and time the activity took place

	// Constructor to initialize the activity with all fields
	public Activity(int id, String type, String description, LocalDate date) {
		this.id = id; // Set the activity ID
		this.type = type; // Set the activity type
		this.description = description; // Set the activity description
		this.date = date; // Set the activity date
	}

	// Getter method for activity ID
	public int getId() {
		return id; // Return the activity ID
	}

	// Getter method for activity type
	public String getType() {
		return type; // Return the activity type
	}

	// Getter method for activity description
	public String getDescription() {
		return description; // Return the activity description
	}

	// Getter method for activity date
	public LocalDate getDate() {
		return date; // Return the activity date
	}

	// Setter method for activity ID
	public void setId(int id) {
		this.id = id; // Set the activity ID
	}

	// Setter method for activity type
	public void setType(String type) {
		this.type = type; // Set the activity type
	}

	// Setter method for activity description
	public void setDescription(String description) {
		this.description = description; // Set the activity description
	}

	// Setter method for activity date
	public void setDate(LocalDate date) {
		this.date = date; // Set the activity date
	}
}