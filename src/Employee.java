public class Employee extends User {
	private EmployeeData employeeData;
	
	public Employee(UserData userData, EmployeeData employeeData) {
		super(userData);
		this.employeeData = employeeData;
	}

	public EmployeeData getEmployeeData() {
		return this.employeeData;
	}
	
	public void setEmployeeData(EmployeeData employeeData) {
		this.employeeData = employeeData;
	}
}
