public class UserData {
	private String id;
	private String name;
	private String email;
//	private String phone;
	
	public UserData(String id, String name, String email) {
		this.id = id;
		this.name = name;
		this.email = email;
//		this.phone = phone;
	}
	
	// Getters
	public String getId() {
		return this.id;
	}
	public String getName() {
		return this.name;
	}
//	public String getLastName() {
//		return this.lastName;
//	}
	public String getEmail() {
		return this.email;
	}
//	public String getPhone() {
//		return this.phone;
//	}
	// Setters
	public void setId(String id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
//	public void setLastName(String lastName) {
//		this.lastName = lastName;
//	}
	public void setEmail(String email) {
		this.email = email;
	}
//	public void setPhone(String phone) {
//		this.phone = phone;
//	}
}
