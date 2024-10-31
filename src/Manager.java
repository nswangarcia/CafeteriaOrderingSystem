public class Manager extends Employee {
	public Manager(UserData userData, EmployeeData employeeData) {
	        super(userData, employeeData);
	}
	public void manageMenu(MenuAPI menuAPI, MenuItem menuItem, String action) {
		switch (action.toLowerCase()) {
			case "add":
				menuAPI.addMenuItem(menuItem);
				System.out.println("menuItem added to menu: " + menuItem.getName());
				break;
			case "remove":
				menuAPI.removeMenuItem(menuItem);
				System.out.println("menuItem added to menu: " + menuItem.getName());
				break;
			default:
				System.out.println("Invalid option. Use 'add' or 'remove'");
				break;
		}
	}
}
