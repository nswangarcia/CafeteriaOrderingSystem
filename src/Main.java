import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
	private static List<Employee> employees;
	private static List<Customer> customers;
	
	public static void main(String[] args) {
		employees = new ArrayList<>();
		customers = new ArrayList<>();
		
		run();
		System.exit(0);
	}
	// User
	public static void run() {
		System.out.println("*************************\nSystem Initialization\n*************************");
		MenuManagementSystem MMS = initMMS();
		OrderManagementSystem OMS = initOMS();
		Manager defaultManager = initManager();
		Operator defaultOperator = initOperator();
		Customer defaultCustomer = initCustomer();
		initOrder(OMS, MMS, defaultCustomer);
		Authenticate authenticate = new Authenticate();
		String name;
		String email;
		// TODO: update id
		String id = "1";
		
		//new DatabaseConnection();
		
		Scanner in = new Scanner(System.in);
		
		while (true) {
		System.out.println("*************************\nCafeteria Ordering System\n*************************\n"
				+ "Please select an option:\n1: Customer\n2: Employee\n3: Exit");
	    String option = in.nextLine();
		    if (option.equals("1")) {
		    	System.out.println("Selected option 1: Customer\n");
		    	name = getInput("Please enter your name:", in);
		    	email = getInput("Please enter your email:", in);
		    	UserData userData = new UserData(id, name, email);
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
			if (selection.equals("breakfast") || selection.equals("lunch") || 
					selection.equals("dinner") || selection.equals("beverages")) {
				System.out.println("Selected " + selection + " menu");
			
				for (Menu menu: MMS.getMenus()) {
					if (menu.getStatus().equals("available") && menu.getName().toLowerCase().equals(selection.toLowerCase())) {
						
						List<MenuItem> menuItems = selectMenuItems(menu);
						if (!menuItems.isEmpty()) {
							customer.placeOrder(OMS, menu, menuItems);
						}
						
						ordering = false;
						break;
					} else if (menu.getStatus().equals("not available") && menu.getName().toLowerCase().equals(selection.toLowerCase())) {
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
		while (true) {
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
						if (selection.equals(menuItem.getID())) {
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
					return menuItems;
				} else {
					System.out.println("No items have been added yet.");
				}
			} else if (option.toLowerCase().equals("5")) {
				System.out.println("Selected " + option + ": Cancel");
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
		auth = authenticate.login(username, password, employees);
		if (auth == true) {
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
			System.out.println("Select an option:\n1: View Orders\n2: Update Order Status\n3: Exit");
			String option = in.nextLine();
			if (option.equals("1")) {
				System.out.println("Selected " + option + ": View Orders");
				System.out.println(OMS.ordersToString());
			} else if (option.equals("2")) {
				System.out.println("Selected " + option + ": Update Order Status");
				String orderId = getInput("Please provide the order id:", in);
				String status = getInput("Please provide the new order status:", in);
				OMS.updateOrderStatusById(orderId, status);
			} else if (option.equals("3")) {
				System.out.println("Selected " + option + ": Exit");
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
			System.out.println("Select an option:\n1: View Menu Items\n2: Add Menu Item\n3: Remove Menu Item\n4: Update Menu Status\n5: Exit");
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
				MMS.removeMenuItemByMenuName(menuName, menuItemId);
			} else if (option.equals("4")) {
				System.out.println("Selected " + option + ": Update Menu Status");
				String menuName = getInput("Please provide name of menu you want to update the status for:", in);
				String status = getInput("Please provide status you want to update the menu to:", in);
				MMS.updateMenuStatusByName(menuName, status);
			} else if (option.equals("5")) {
				System.out.println("Selected " + option + ": Exit");
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
	// Default Manager
	public static Manager initManager() {
		// Manager
		UserData managerData = new UserData("M001", "Pat Jones", "pjones@email.com");
		EmployeeData managerEmployeeData = new EmployeeData(managerData, "manager", "manager", "123");
		System.out.println("Default Manager initialized");
		Manager manager = new Manager(managerData, managerEmployeeData);
		addEmployee(manager);
		return manager;
	}
	// Default Operator
	public static Operator initOperator() {
		// Operator
		UserData operatorData = new UserData("O001", "Tris Dawson", "tdaw@email.com");
		EmployeeData operatorEmployeeData = new EmployeeData(operatorData, "operator", "operator", "321");
		System.out.println("Default Operator initialized");
		Operator operator = new Operator(operatorData, operatorEmployeeData);
		addEmployee(operator);
		return operator;
	}
	// Default Customer
	public static Customer initCustomer() {
		// Customer
		UserData customerData = new UserData("C001", "Kit Mint", "kmint@email.com");
		System.out.println("Default Customer initialized");
		Customer customer = new Customer(customerData);
		addCustomer(customer);
		return customer;
	}
	// Initialize Menu Management System
	public static MenuManagementSystem initMMS() {
		// Breakfast
		Menu breakfast = new Menu("1", "available", "Breakfast");
		breakfast.addMenuItem(new MenuItem("1", "available", "Pancakes", "Fluffy pancakes served with butter and maple syrup, often accompanied by bacon or "
				+ " sausage."));
		breakfast.addMenuItem(new MenuItem("2", "available", "Eggs Benedict", "Poached eggs served on English muffins with Canadian bacon or ham, topped "
				+ " with hollandaise sauce."));
		breakfast.addMenuItem(new MenuItem("3", "available", "Omelets", "Fluffy eggs folded over a variety of fillings such as cheese, vegetables, bacon, ham, or "
				+ " mushrooms."));
		breakfast.addMenuItem(new MenuItem("4", "available", "French Toast", "Slices of bread dipped in a mixture of eggs and milk, then grilled until golden "
				+ " brown and served with syrup and powdered sugar."));
		breakfast.addMenuItem(new MenuItem("5", "available", "Breakfast Burrito", "Tortilla filled with scrambled eggs, cheese, potatoes, and a choice of bacon, "
				+ "	 sausage, or chorizo, often served with salsa and sour cream."));
		breakfast.addMenuItem(new MenuItem("6", "available", "Bagel with Lox", "Toasted bagel topped with cream cheese, smoked salmon (lox), capers, red "
				+ " onion, and sometimes tomato slices."));
		breakfast.addMenuItem(new MenuItem("7", "available", "Biscuits and Gravy", "Flaky biscuits topped with creamy sausage gravy, served hot and often "
				+ " accompanied by eggs."));
		breakfast.addMenuItem(new MenuItem("8", "available", "Belgian Waffles", "Crispy waffles topped with whipped cream, fresh berries, and maple syrup. "
				+ " Breakfast Sandwich: English muffin, biscuit, or croissant filled with eggs, cheese, and a choice "
				+ " of sausage, bacon, or ham."));
		breakfast.addMenuItem(new MenuItem("9", "available", "Greek Yogurt Parfait", "Layers of Greek yogurt, granola, and fresh berries or fruit, sometimes "
				+ " drizzled with honey or maple syrup."));
		
		// Lunch
		Menu lunch = new Menu("2", "available", "Lunch");
		lunch.addMenuItem(new MenuItem("10", "available", "Cheeseburger", "Classic American favorite, typically served with lettuce, tomato, onion, pickles, "
				+ " and condiments on a bun."));
		lunch.addMenuItem(new MenuItem("11", "available", "Club Sandwich", "Triple-decker sandwich with layers of turkey or chicken, bacon, lettuce, tomato, "
				+ " and mayo on toasted bread."));
		lunch.addMenuItem(new MenuItem("12", "available", "Caesar Salad", "Romaine lettuce tossed with Caesar dressing, croutons, and Parmesan cheese, "
				+ " often topped with grilled chicken or shrimp."));
		lunch.addMenuItem(new MenuItem("13", "available", "BBQ Ribs", "Slow-cooked pork or beef ribs slathered in barbecue sauce, served with coleslaw "
				+ " and cornbread."));
		lunch.addMenuItem(new MenuItem("14", "available", "Philly Cheesesteak", "Sliced steak, often with grilled onions and peppers, topped with melted "
				+ " cheese on a hoagie roll."));
		lunch.addMenuItem(new MenuItem("15", "available", "New England Clam Chowder", "Cream-based soup with clams, potatoes, onions, and celery, "
				+ " seasoned with herbs and spices."));
		lunch.addMenuItem(new MenuItem("16", "available", "Cobb Salad", "Mixed greens topped with grilled chicken, bacon, avocado, hard-boiled eggs, "
				+ " tomatoes, blue cheese, and vinaigrette dressing."));
		lunch.addMenuItem(new MenuItem("17", "available", "Pulled Pork Sandwich", "Slow-cooked pork shoulder shredded and mixed with barbecue sauce, "
				+ "	served on a bun with pickles and coleslaw."));
		lunch.addMenuItem(new MenuItem("18", "available", "Fish Tacos", "Soft corn tortillas filled with grilled or fried fish, cabbage slaw, avocado, salsa, and "
				+ " lime."));

		// Dinner
		Menu dinner = new Menu("3", "available", "Dinner");
		dinner.addMenuItem(new MenuItem("19", "available", "Shrimp Scampi", "Shrimp sautéed in garlic butter and white wine sauce, served over linguine "
				+ "	pasta with a sprinkle of Parmesan cheese and parsley."));
		dinner.addMenuItem(new MenuItem("20", "available", "BBQ Brisket", "Slow-smoked beef brisket served with coleslaw, baked beans, cornbread, and "
				+ "	pickles."));
		dinner.addMenuItem(new MenuItem("21", "available", "Vegetarian Lasagna", "Layers of pasta filled with ricotta cheese, spinach, mushrooms, and "
				+ "	marinara sauce, topped with mozzarella cheese and baked until golden."));
		dinner.addMenuItem(new MenuItem("22", "available", "Stuffed Bell Peppers", "Bell peppers stuffed with a mixture of ground beef or turkey, rice, "
				+ "	tomatoes, and cheese, baked until tender."));
		dinner.addMenuItem(new MenuItem("23", "available", "Lobster Tail Dinner", "Broiled or grilled lobster tail served with drawn butter, roasted potatoes or "
				+ "	rice, steamed vegetables, and a wedge of lemon."));
		dinner.addMenuItem(new MenuItem("24", "available", "Taco Night", "Build-your-own taco dinner with seasoned ground beef or chicken, soft or crispy "
				+ "	taco shells, lettuce, tomatoes, cheese, salsa, sour cream, and guacamole."));
		dinner.addMenuItem(new MenuItem("25", "available", "Buffalo Wings", "Deep-fried chicken wings coated in spicy buffalo sauce, served with celery sticks "
				+ "	and blue cheese dressing."));
		dinner.addMenuItem(new MenuItem("26", "available", "Steakhouse Dinner", "A classic American favorite featuring a grilled steak (such as ribeye, filet "
				+ "	mignon, or New York strip) served with sides like mashed potatoes, steamed vegetables, and a salad."));
		dinner.addMenuItem(new MenuItem("27", "available", "Grilled Salmon", "Fresh salmon fillet grilled and served with rice pilaf or roasted potatoes, "
				+ "	steamed asparagus, and a lemon wedge."));
		dinner.addMenuItem(new MenuItem("28", "available", "Roast Chicken", "Herb-roasted or rotisserie chicken served with mashed potatoes, gravy, roasted "
				+ "	vegetables, and a side of cranberry sauce."));
		dinner.addMenuItem(new MenuItem("29", "available", "Spaghetti and Meatballs", "Spaghetti pasta topped with marinara sauce and meatballs, served "
				+ "	with garlic bread and a side salad."));
		
		// Beverages		
		Menu beverages = new Menu("4", "available", "Beverages");
		beverages.addMenuItem(new MenuItem("30", "available", "Coffee", "Whether black, with cream and sugar, or as specialty drinks like lattes and cappuccinos."));
		beverages.addMenuItem(new MenuItem("31", "available", "Iced Tea", "Served with sweetened or unsweetened with lemon."));
		beverages.addMenuItem(new MenuItem("32", "available", "Soda (Soft Drinks)", "Varieties like cola (Coca-Cola, Pepsi), lemon-lime (Sprite, 7UP), root beer."));
		beverages.addMenuItem(new MenuItem("33", "available", "Craft Beer", "With a growing craft brewery scene."));
		beverages.addMenuItem(new MenuItem("34", "available", "Bottled Water", "both still or sparkling."));
		System.out.println("Default Menu initialized");
		System.out.println("Default MenuItems initialized");
		MenuManagementSystem MMS = new MenuManagementSystem();
		MMS.addMenu(breakfast);
		MMS.addMenu(lunch);
		MMS.addMenu(dinner);
		MMS.addMenu(beverages);
		System.out.println("Default MMS initialized");
		MMS.resetMenuItemIds();
		System.out.println("Default MenuItem ids reset");
		return MMS;
	}
	// Default Order
	public static void initOrder(OrderManagementSystem OMS, MenuManagementSystem MMS, Customer customer) {
		UserData userData = customer.getUserData();
		Menu menu = MMS.getMenuByName("breakfast");
		List<MenuItem> menuItems = menu.getMenuItems();
		Order order = new Order("" + (OMS.getOrders().size() + 1), menu, menuItems, userData);
		OMS.addOrder(order);
		System.out.println("Default Order initialized");
	}
	// Initialize Order Management System
	public static OrderManagementSystem initOMS() {
		OrderManagementSystem OMS = new OrderManagementSystem();
		System.out.println("Default OMS initialized");
		return OMS;
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
