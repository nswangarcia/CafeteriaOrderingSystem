import java.util.ArrayList;
import java.util.List;

public class Customer extends User {
//	private String username;
//	private String password;
	private UserData userData;
	
	public Customer(UserData userData) {
		super(userData);
//		this.username = username;
//		this.password = password;
		this.userData = userData;
	}

	public void viewMenu(MenuManagementSystem MMS) {
		for (Menu menu: MMS.getMenus()) {
			System.out.println("*****************************************************\n"
					+ "Menu: " + menu.getName() + "\n"
					+ "Status: " + menu.getStatus() + "\n"
					+ "\n\nMenu Items:");
			for (MenuItem menuItem: menu.getMenuItems()) {
				System.out.println("#" + menuItem.getID() + " "
						+ menuItem.getName() + ": " + menuItem.getDescription() + "\n"
						+ "Status: " + menuItem.getStatus() + "\n");
			}
		}
	};

	public void placeOrder(OrderManagementSystem OMS, Menu menu, List<MenuItem> menuItems) {
		Order order = new Order("" + (OMS.getOrders().size() + 1), menu, menuItems, userData);
		OMS.addOrder(order);
		System.out.println("Order placed successfully. Order ID: " + order.getId());
	};
}