import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import config.DBConfig;

public class Authenticate {
	private Employee currentUser;
	
	private Connection connectToDatabase() throws SQLException {
        return DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
    }

	public Employee login(String username, String password) {
	    try (Connection connection = connectToDatabase()) {
	        String employeeQuery = "SELECT * FROM EmployeeData WHERE username = ? AND password = ?";
	        try (PreparedStatement stmt = connection.prepareStatement(employeeQuery)) {
	            stmt.setString(1, username);
	            stmt.setString(2, password);

	            try (ResultSet rs = stmt.executeQuery()) {
	                if (rs.next()) {
	                    int userID = rs.getInt("userID");
	                    String name = rs.getString("name");
	                    String email = rs.getString("email");
	                    int employeeID = rs.getInt("empID");
	                    String role = rs.getString("role");

	                    UserData userData = new UserData(userID, name, email);
	                    EmployeeData employeeData = new EmployeeData(userData, employeeID, role, username, password);

	                    System.out.println("Login successful for user: " + username);
	                    currentUser = new Employee(userData, employeeData);
	                    return currentUser;
	                } else {
	                    System.out.println("Invalid username or password.");
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Database error occurred during login.");
	    }
	    return null;
	}
	
	public void logout() {
	    if (currentUser != null) {
	        System.out.println("Logging out user: " + currentUser.getEmployeeData().getUsername());
	        currentUser = null;
	    } else {
	        System.out.println("No user is currently logged in.");
	    }
	}

}
