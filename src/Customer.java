import java.util.ArrayList;
import java.util.List;

public class Customer extends User {
	private UserData userData;
	private List<Order> orders;
	
	public Customer(UserData userData) {
		super(userData);
		this.userData = userData;
		this.orders = new ArrayList<>();
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
		OMS.createOrder(userData, menu, menuItems);
	};
	
	// todo
	public List<Order> getOrders() {
		return this.orders;
	}
	
	// todo
	public void addOrder(Order order) {
		this.orders.add(order);
	}
	
	
}