import java.util.List;

public class Authenticate {

	public boolean login(String username, String password, List<Employee> employees) {
		// TODO:
		for (Employee employee: employees) {
			EmployeeData employeeData = employee.getEmployeeData();
			if (employeeData.getUsername().equals(username) && employeeData.getPassword().equals(password)) {
				System.out.println("User logged in");
				return true;
			}
		}
		System.out.println("Invalid login");
		return false;
	};

	public void logout() {
		System.out.println("User logged out");
	};

}
