
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
	private static final String URL = "jdbc:mysql://localhost:3306/cafeteriaDB";
	private static final String USER = "root";
	private static final String PASSWORD = "password";
	
	DatabaseConnection() {}
	
	static {

        // Establish a connection to database
        try {
        	Class.forName("org.mysql.Driver");
        	System.out.println("Welcome to the cafeteriaDB!\n");
        	
        } catch (ClassNotFoundException e) {
        	throw new RuntimeException("MySQL JDBC driver not found", e);
        }
    }
	
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}
	
	public static void closeConnecion(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
