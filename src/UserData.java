public class UserData {
	private int userID;
	private String name;
	private String email;
//	private String phone;
	
	public UserData(int userID, String name, String email) {
		this.userID = userID;
		this.name = name;
		this.email = email;
//		this.phone = phone;
	}
	
	// Getters
	public int getUserID() {
		return this.userID;
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
	public void setUserID(int userID) {
		this.userID = userID;
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
