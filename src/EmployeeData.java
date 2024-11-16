public class EmployeeData extends UserData{
	private int empID;
	private String role;
	private String username;
	private String password;
	
	public EmployeeData(UserData userData, int empID, String role, String username, String password) {
		super(userData.getUserID(), userData.getName(), userData.getEmail());
		this.empID = empID;
		this.role = role;
		this.username = username;
		this.password = password;
	}
	
	// Getters
	public int getEmpID() {
		return this.empID;
	}
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
	// shouldn't be able to change the id
//	public void setId(int empID) {
//		this.empID = empID;
//	}
}
