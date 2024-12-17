package logic;

public class Subscriber {

	private int id;
	private String name;
	private String phone;
	private String email;
	private String status;

	public Subscriber(int id, String name, String phone, String email) {
		this.id = id;
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.status = "active";
	}
	
	public Subscriber(int id, String name, String phone, String email, String status) {
		this.id = id;
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
