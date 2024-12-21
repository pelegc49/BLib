package logic;

import java.util.Date;

public class Activity {

	private int id;
	private String type;
	private String description;
	private Date date;
	
	public Activity(int id, String type, String description, Date date) {
		this.id=id;
		this.type=type;
		this.description=description;
		this.date=date;
	}
	
    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
