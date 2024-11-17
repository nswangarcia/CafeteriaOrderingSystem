import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import config.DBConfig;

public class Main {
	private static List<User> users;
	private static List<Employee> employees;
	private static List<Customer> customers;
	private static Authenticate authenticate;
	private static MenuManagementSystem MMS;
	private static OrderManagementSystem OMS;
	
	public static void main(String[] args) {
		users = new ArrayList<>();
		employees = new ArrayList<>();
		customers = new ArrayList<>();
		authenticate = new Authenticate();
		
		loadUsersFromDB();
		initEmployees();
		initCustomers();
		
		MMS = new MenuManagementSystem();
		OMS = new OrderManagementSystem();
		
		run();
		System.exit(0);
	}
	
	// db connection
	private static Connection connectToDatabase() throws SQLException {
        return DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
    }
	// User
	public static void run() {
		String name;
		String email;
		
		Scanner in = new Scanner(System.in);
		
		while (true) {
		System.out.println("\n********************************\nWelcome to the Cafeteria System!\n********************************\n"
				+ "Please select an option:\n1: Customer\n2: Employee\n3: Exit");
	    String option = in.nextLine();
		    if (option.equals("1")) {
		    	System.out.println("Selected option 1: Customer\n");
		    	name = getInput("Please enter your name:", in);
		    	email = getInput("Please enter your email:", in);
		    	Customer customer = initCustomer(name, email);
		    	customerGUI(customer, MMS, OMS);
		    } else if (option.equals("2")) {
		    	System.out.println("Selected option 2: Employee\n");
		    	employeeGUI(MMS, OMS, authenticate);
		    } else if (option.equals("3")) {
		    	System.out.println("Selected option 2: Exit\n");
		    	break;
		    } else {
		    	System.out.println("Invalid option\n");
		    }
		    
		}
		System.out.println("Goodbye!");
		System.exit(0);
	}

	// Customer
	public static void customerGUI(Customer customer, MenuManagementSystem MMS, OrderManagementSystem OMS) {
		Scanner in = new Scanner(System.in);
		while (true) {
			System.out.println("Select an option:\n1: View Available Menu Titles\n"
					+ "2: View Full List of Menus\n3: Place Order\n4: Exit");
			String option = in.nextLine();
			if (option.equals("1")) {
				System.out.println("Selected option 1: View Available Menu Titles");
				System.out.println(MMS.getAvailableMenuNames());
			} else if (option.equals("2")) {
				System.out.println("Selected option 2: View Full List of Menus");
				customer.viewMenu(MMS);
			} else if (option.equals("3")) {
				System.out.println("Selected option 3: Place Order");
				selectMenu(customer, MMS, OMS);
			} else if (option.equals("4")) {
				System.out.println("Selected option 4: Exit");
				break;
			} else {
				System.out.println("Invalid option");
			}
		}
	}
	// Customer
	public static void selectMenu(Customer customer, MenuManagementSystem MMS, OrderManagementSystem OMS) {
		Scanner in = new Scanner(System.in);
		boolean ordering = true;
		while (ordering) {
			System.out.println("Type in the menu you want to order from");
			String selection = in.nextLine().toLowerCase();
			if (MMS.isValidMenu(selection)) {
				System.out.println("Selected " + selection + " menu");
			
				for (Menu menu: MMS.getMenus()) {
					if (menu.getStatus().toLowerCase().equals("available") && menu.getName().toLowerCase().equals(selection.toLowerCase())) {
						List<MenuItem> menuItems = selectMenuItems(menu);
						if (!menuItems.isEmpty()) {
							customer.placeOrder(OMS, menu, menuItems);
						}
						ordering = false;
						break;
					} else if (menu.getStatus().toLowerCase().equals("not available") && menu.getName().toLowerCase().equals(selection.toLowerCase())) {
						System.out.println("Sorry, " + menu + " menu is not available.");
					}
				}
			} else {
				System.out.println("Invalid option");
			}
		}
	}
	// Customer
	public static List<MenuItem> selectMenuItems(Menu menu) {
		Scanner in = new Scanner(System.in);
		List<MenuItem> menuItems = new ArrayList<>();
		boolean addItem = false;
		boolean ordering = true;
		
		while (ordering) {
			System.out.println("\nOptions:\n1: View Menu Items\n2: Add to Order\n"
					+ "3: Review Added Items\n4: Place Order\n5: Cancel");
			String option = in.nextLine();
			if (option.toLowerCase().equals("1")) {
				System.out.println("Selected " + option + ": View Menu Items");
				for (MenuItem menuItem: menu.getMenuItems()) {
					System.out.println("#" + menuItem.getID() + " " + menuItem.getName());
				}
			} else if (option.toLowerCase().equals("2")) {
				System.out.println("Selected " + option + ": Add to Order");
				addItem = true;
				System.out.println("Type in the menu item number you want to Add to Order");
				while (addItem) {
					String selection = in.nextLine();
					System.out.println(selection);
					for (MenuItem menuItem: menu.getMenuItems()) {
						if (selection.equals(String.valueOf(menuItem.getID()))) {
							menuItems.add(menuItem);
							System.out.println(menuItem.getName() + " was added to your order.");
							addItem = false;
						}
					}
					if (addItem == true) {
						System.out.println("Invalid selection");
						addItem = false;
					}
				}
			} else if (option.toLowerCase().equals("3")) {
				System.out.println("Selected " + option + ": Review Added Items");
				if (!menuItems.isEmpty()) {
					for (MenuItem menuItem: menuItems) {
						System.out.println("#" + menuItem.getID() + " " + menuItem.getName());
					}
				} else {
					System.out.println("No items have been added yet.");
				}
			} else if (option.toLowerCase().equals("4")) {
				System.out.println("Selected " + option + ": Place Order");
				if (!menuItems.isEmpty()) {
					System.out.println("Order placed:");
					for (MenuItem menuItem: menuItems) {
						System.out.println("#" + menuItem.getID() + " " + menuItem.getName() + "\n");
					}
					ordering = false;
					return menuItems;
				} else {
					System.out.println("No items have been added yet.");
				}
			} else if (option.toLowerCase().equals("5")) {
				System.out.println("Selected " + option + ": Cancel");
				ordering = false;
				break;
			} else {
				System.out.println("Invalid option");
			}
		}
		return menuItems;
	}
	// Employee
	public static void employeeGUI(MenuManagementSystem MMS, OrderManagementSystem OMS, Authenticate authenticate) {
		Scanner in = new Scanner(System.in);
		String username;
		String password;
		boolean auth = false;
		System.out.println("For testing:\n");
		System.out.println(employeeDataToString());
		username = getInput("Enter username:", in);
		password = getInput("Enter password:", in);
		// authenticate
		Employee eAuth = authenticate.login(username, password);
		if (eAuth != null) {
			Employee employee = getEmployee(username, password);
			if (employee != null) {
				if (employee.getEmployeeData().getRole().equals("operator")) {
					System.out.println("Operator login\n");
					operatorGUI(employee, OMS);
				} else if (employee.getEmployeeData().getRole().equals("manager")) {
					System.out.println("Manager login\n");
					managerGUI(employee, MMS);
				}
			} else {
				System.out.println("Error: issue with login");
			}
			
		} else {
			System.out.println("Invalid login");
		}
	}
	// Operator
	public static void operatorGUI(Employee employee, OrderManagementSystem OMS) {
		Scanner in = new Scanner(System.in);
		while (true) {
			System.out.println("Select an option:\n1: View Orders\n2: Update Order Status\n3: Log Out");
			String option = in.nextLine();
			if (option.equals("1")) {
				System.out.println("Selected " + option + ": View Orders");
				System.out.println(OMS.ordersToString());
			} else if (option.equals("2")) {
				System.out.println("Selected " + option + ": Update Order Status");
				String orderId = getInput("Please provide the order id:", in);
				String status = getInput("Please provide the new order status:", in);
				OMS.updateOrderStatusById(Integer.parseInt(orderId), status);
			} else if (option.equals("3")) {
				System.out.println("Selected " + option + ": Log Out");
				authenticate.logout();
				break;
			} else {
				System.out.println("Invalid option");
			}
		}
	}
	// Manager
	public static void managerGUI(Employee employee, MenuManagementSystem MMS) {
		Scanner in = new Scanner(System.in);
		while (true) {
			System.out.println("Select an option:\n1: View Menus\n2: View Menu Items\n3: Add Menu\n4: Add Menu Item\n5: Remove Menu\n6: Remove Menu Item\n7: Update Menu Status\n8: Update Menu Item Status\n9: Log Out");
			String option = in.nextLine();
			if (option.equals("1")) {
				System.out.println("Selected " + option + ": View Menus");
				System.out.println(MMS.menusToString());
			} else if (option.equals("2")) {
				System.out.println("Selected " + option + ": View Menu Items");
				System.out.println(MMS.menuItemsToString());
			} else if (option.equals("3")) {
				System.out.println("Selected " + option + ": Add Menu");
				String menuName = getInput("Please enter the name of the menu you want to add:", in);
				MMS.addMenuByMenuName(menuName);
			} else if (option.equals("4")) {
				System.out.println("Selected " + option + ": Add Menu Item");
				String menuName = getInput("Please enter the name of the menu you want to add a menu item to:", in);
				String menuItemName = getInput("Please enter the name of the menu item:", in);
				String menuItemDescription = getInput("Please enter the menu item description:", in);
				MMS.addMenuItemByMenuName(menuName, menuItemName, menuItemDescription);
			} else if (option.equals("5")) {
				System.out.println("Selected " + option + ": Remove Menu");
				String menuID = getInput("Please enter the menuID of the menu to remove:", in);
				MMS.removeMenuByMenuID(Integer.parseInt(menuID));
			} else if (option.equals("6")) {
				System.out.println("Selected " + option + ": Remove Menu Item");
				String menuItemID = getInput("Please enter the menuItemID of the menu item to remove:", in);
				MMS.removeMenuItemByMenuItemID(Integer.parseInt(menuItemID));
			} else if (option.equals("7")) {
				System.out.println("Selected " + option + ": Update Menu Status");
				String menuName = getInput("Please provide name of menu you want to update the status for:", in);
				String status = getInput("Please provide status you want to update the menu to:", in);
				MMS.updateMenuStatusByMenuName(menuName, status);
			} else if (option.equals("8")) {
				System.out.println("Selected " + option + ": Update Menu Item Status");
				// TODO error handling
				String menuItemID = getInput("Please provide menuItemID of menu item to update the status for:", in);
				String status = getInput("Please provide status you want to update the menu item to:", in);
				MMS.updateMenuItemStatusByMenuItemID(Integer.parseInt(menuItemID), status);
			} else if (option.equals("9")) {
				System.out.println("Selected " + option + ": Log Out\n");
				System.out.println("Logging out... Goodbye!\n");
				break;
			}
			else {
				System.out.println("Invalid option");
			}
		}
	}
	// Helper for scanner input
	public static String getInput(String message, Scanner in) {
		String input;
		while(true) {
			System.out.println(message);
			input = in.nextLine();
			if (!input.isEmpty()) {
				return input;
			}
		}
	}
	// Helper for scanner input
	public static int getIntInput(String message, Scanner in) {
		int input = -1;
		while(true) {
			System.out.println(message);
			input = in.nextInt();
			if (input != -1) {
				return input;
			}
		}
	}
	
	// User data
	public static void loadUsersFromDB() {
		// load user data
		  try (Connection connection = connectToDatabase()) {
	            String userQuery = "SELECT * FROM UserData;";
	            Statement userStmt = connection.createStatement();
	            ResultSet userRs = userStmt.executeQuery(userQuery);
	            while (userRs.next()) {
	                int userID = userRs.getInt("userID");
	                String name = userRs.getString("name");
	                String email = userRs.getString("email");
	                UserData userData = new UserData(userID, name, email);
	                User user = new User(userData);
                	users.add(user);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	}
	
	public static void saveUsersToDB() {
		
	}
	
	public static void initEmployees() {
		// load employee data
		  try (Connection connection = connectToDatabase()) {
	            String employeeQuery = "SELECT * FROM EmployeeData;";
	            Statement employeeStmt = connection.createStatement();
	            ResultSet employeeRs = employeeStmt.executeQuery(employeeQuery);
	            while (employeeRs.next()) {
	                int empID = employeeRs.getInt("empID");
	                String username = employeeRs.getString("username");
	                String password = employeeRs.getString("password");
	                String role = employeeRs.getString("role");
	                int userID = employeeRs.getInt("userID");
	                for (User u: users) {
	                	UserData userData = u.getUserData();
	                	if (userID == userData.getUserID()) {
	                		EmployeeData employeeData = new EmployeeData(userData, empID, role, username, password);
	                		Employee employee = new Employee(userData, employeeData);
	                		employees.add(employee);
	                	}
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	}
	
	public static void initCustomers() {
		 for (User u : users) {
	        boolean isEmployee = false;

	        for (Employee e : employees) {
	            if (u.getUserData().getUserID() == e.getUserData().getUserID()) {
	                isEmployee = true;
	                break;
	            }
	        }
	        if (!isEmployee) {
	            customers.add(new Customer(u.getUserData()));
	        }
	    }
	}
	
	// Customer
	public static Customer initCustomer(String name, String email) {
		// Customer
		for (User u: users) {
			if (u.getUserData().getName().equals(name) && u.getUserData().getEmail().equals(email)) {
				return new Customer(u.getUserData());
			}
		}
		
		// add user to DB:
		UserData userData = createNewUserAndSaveToDB(name, email);
		return new Customer(userData);
	}
	
	public static UserData createNewUserAndSaveToDB(String name, String email) {
	    try (Connection connection = connectToDatabase()) {
	        String insertQuery = "INSERT IGNORE INTO UserData (name, email) VALUES (?, ?)";
	        try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
	            insertStmt.setString(1, name);
	            insertStmt.setString(2, email);
	            insertStmt.executeUpdate();
	        }

	        String selectQuery = "SELECT * FROM UserData WHERE name = ? AND email = ?";
	        try (PreparedStatement selectStmt = connection.prepareStatement(selectQuery)) {
	            selectStmt.setString(1, name);
	            selectStmt.setString(2, email);
	            try (ResultSet rs = selectStmt.executeQuery()) {
	                if (rs.next()) {
	                    int userID = rs.getInt("userID");
	                    String retrievedName = rs.getString("name");
	                    String retrievedEmail = rs.getString("email");
	                    return new UserData(userID, retrievedName, retrievedEmail);
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}

	
	// Employee
	public static void addEmployee(Employee employee) {
		employees.add(employee);
	}
	// Customer
	public static void addCustomer(Customer customer) {
		customers.add(customer);
	}
	// Employee
	public static String employeeDataToString() {
		String result = "";
		for (Employee employee: employees) {
			EmployeeData employeeData = employee.getEmployeeData();
			result += "username: " + employeeData.getUsername() + "\n"
			+ "password: " + employeeData.getPassword() + "\n\n";
		}
		return result;
	}
	// Employee
	public static Employee getEmployee(String username, String password) {
		for (Employee employee: employees) {
			EmployeeData employeeData = employee.getEmployeeData();
			if (employeeData.getUsername().equals(username) && employeeData.getPassword().equals(password)) {
				return employee;
			}
		}
		return null;
	}
}
