public class EmployeeData extends UserData{
	private String role;
	private String username;
	private String password;
	
	public EmployeeData(UserData userData, String role, String username, String password) {
		super(userData.getId(), userData.getName(), userData.getEmail());
		this.role = role;
		this.username = username;
		this.password = password;
	}
	
	// Getters
	public String getRole() {
		return this.role;
	}
	public String getUsername() {
		return this.username;
	}
	public String getPassword() {
		return this.password;
	}
	// Setters
	public void setRole(String role) {
		this.role = role;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
