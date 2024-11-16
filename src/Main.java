import java.sql.Connection;
import java.sql.DriverManager;
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
	
	public static void main(String[] args) {
		users = new ArrayList<>();
		employees = new ArrayList<>();
		customers = new ArrayList<>();
		
		loadUsersFromDB();
		initEmployees();
		initCustomers();
		
		run();
		System.exit(0);
	}
	
	// db connection
	private static Connection connectToDatabase() throws SQLException {
        return DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
    }
	// User
	public static void run() {
		//System.out.println("*************************\nSystem Initialization\n*************************");
		MenuManagementSystem MMS = new MenuManagementSystem();
		OrderManagementSystem OMS = new OrderManagementSystem();
//		Manager defaultManager = initManager();
//		Operator defaultOperator = initOperator();
//		Customer defaultCustomer = initCustomer();
		//initOrder(OMS, MMS, defaultCustomer);
		Authenticate authenticate = new Authenticate();
		String name;
		String email;
		
		//new DatabaseConnection();
		
		Scanner in = new Scanner(System.in);
		
		while (true) {
		System.out.println("\n********************************\nWelcome to the Cafeteria System!\n********************************\n"
				+ "Please select an option:\n1: Customer\n2: Employee\n3: Exit");
	    String option = in.nextLine();
		    if (option.equals("1")) {
		    	System.out.println("Selected option 1: Customer\n");
		    	name = getInput("Please enter your name:", in);
		    	email = getInput("Please enter your email:", in);
		    	UserData userData = new UserData(customers.size() + 1, name, email);
		    	Customer customer = new Customer(userData);
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
		// TODO: actually authenticate
		Employee eAuth = authenticate.login(username, password, employees);
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
				System.out.println("Logging out... Goodbye!\n");
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
			System.out.println("Select an option:\n1: View Menu Items\n2: Add Menu Item\n3: Remove Menu Item\n4: Update Menu Status\n5: Log Out");
			String option = in.nextLine();
			if (option.equals("1")) {
				System.out.println("Selected " + option + ": View Menu Items");
				System.out.println(MMS.menuItemsToString());
			} else if (option.equals("2")) {
				System.out.println("Selected " + option + ": Add Menu Item");
				String menuName = getInput("Please enter to name of the menu you want to add a menu item to:", in);
				String menuItemName = getInput("Please enter the name of the menu item:", in);
				String menuItemDescription = getInput("Please enter the menu item description:", in);
				MMS.addMenuItemByMenuName(menuName, menuItemName, menuItemDescription);
			} else if (option.equals("3")) {
				System.out.println("Selected " + option + ": Remove Menu Item");
				String menuName = getInput("Please enter the name of the menu you want to remove a menu item from:", in);
				String menuItemId = getInput("Please enter the id of the menu item to remove:", in);
				MMS.removeMenuItemByMenuName(menuName, Integer.parseInt(menuItemId));
			} else if (option.equals("4")) {
				System.out.println("Selected " + option + ": Update Menu Status");
				String menuName = getInput("Please provide name of menu you want to update the status for:", in);
				String status = getInput("Please provide status you want to update the menu to:", in);
				MMS.updateMenuStatusByName(menuName, status);
			} else if (option.equals("5")) {
				System.out.println("Selected " + option + ": Log Out\n");
				System.out.println("Logging out... Goodbye!\n");
				break;
			} else {
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
	
	// Default Manager
//	public static Manager initManager() {
//		// Manager
//		UserData managerData = new UserData(employees.size() + 1, "Pat Jones", "pjones@email.com");
//		EmployeeData managerEmployeeData = new EmployeeData(managerData, "manager", "manager", "123");
//		//System.out.println("Default Manager initialized");
//		Manager manager = new Manager(managerData, managerEmployeeData);
//		addEmployee(manager);
//		return manager;
//	}
//	
//	// Default Operator
//	public static Operator initOperator() {
//		// Operator
//		UserData operatorData = new UserData(employees.size() + 1, "Tris Dawson", "tdaw@email.com");
//		EmployeeData operatorEmployeeData = new EmployeeData(operatorData, "operator", "operator", "321");
//		//System.out.println("Default Operator initialized");
//		Operator operator = new Operator(operatorData, operatorEmployeeData);
//		addEmployee(operator);
//		return operator;
//	}
	// Default Customer
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
	
	public static UserData createNewUserAndSaveToDB(String n, String em) {
		boolean exists = false;
		try (Connection connection = connectToDatabase()) {
            String userQuery = "INSERT IGNORE INTO UserData (name, email) VALUES ('" + n + "', '" + em + "');";
            Statement userStmt = connection.createStatement();
            ResultSet userRs = userStmt.executeQuery(userQuery);
            userQuery = "SELECT * FROM UserData WHERE name = '" + n + "', AND email = '" + em + "';";
            userStmt = connection.createStatement();
            userRs = userStmt.executeQuery(userQuery);
            while (userRs.next()) {
                int userID = userRs.getInt("userID");
                String name = userRs.getString("name");
                String email = userRs.getString("email");
                UserData userData = new UserData(userID, name, email);
                User user = new User(userData);
                return userData;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
		return null;
	}
	// Default Order
//	public static void initOrder(OrderManagementSystem OMS, MenuManagementSystem MMS, Customer customer) {
//		UserData userData = customer.getUserData();
//		Menu menu = MMS.getMenuByName("breakfast");
//		List<MenuItem> menuItems = menu.getMenuItems();
//		Order order = new Order(OMS.getOrders().size() + 1, menu, menuItems, userData);
//		OMS.addOrder(order);
//		//System.out.println("Default Order initialized");
//	}
	// Initialize Order Management System
//	public static OrderManagementSystem initOMS() {
//		OrderManagementSystem OMS = new OrderManagementSystem();
//		//System.out.println("Default OMS initialized");
//		return OMS;
//	}
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
