import java.util.List;

public class Authenticate {

	public Employee login(String username, String password, List<Employee> employees) {
		for (Employee employee: employees) {
			EmployeeData employeeData = employee.getEmployeeData();
			if (employeeData.getUsername().equals(username) && employeeData.getPassword().equals(password)) {
				System.out.println("User logged in");
				return employee;
			}
		}
		System.out.println("Invalid login");
		return null;
	};

	public void logout() {
		System.out.println("User logged out");
	};

}
