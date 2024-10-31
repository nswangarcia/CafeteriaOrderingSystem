import java.util.ArrayList;
import java.util.List;

public class OrderManagementSystem {
	private List<Order> orders;
	private OrderAPI orderAPI;
	
	public OrderManagementSystem() {
		this.orders = new ArrayList<>();
	}
	
	public void addOrder(Order order) {
		this.orders.add(order);
	}
	
	public List<Order> getOrders() {
		return this.orders;
	}
	
	public void updateOrderStatusById(String id, String status) {
		if (!orders.isEmpty()) {
			for (Order order : orders) {
				if (order.getId().equals(id)) {
					order.setStatus(status);
					System.out.println("Order status updated to: " + order.getStatus());
					return;
				}
			}
			System.out.println("Invalid order id: " + id);
		} else {
			System.out.println("There are no active orders in system");
		}
	}
	
	public String ordersToString() {
		String ordersStr = "";
		for (Order order : orders) {
			if (!order.getStatus().equals("delivered")) {
				ordersStr += "\n*************************\nOrder ID: " + order.getId()
						+ "\nOrder status: " + order.getStatus()
						+ "\nCustomer name: " + order.getUserData().getName() + "\n"
						+ "Customer email: " + order.getUserData().getEmail() + "\n"
						+ "Menu: " + order.getMenu().getName()  + "\nOrder items:\n";
				List<MenuItem> menuItems = order.getMenuItems();
				for (MenuItem menuItem : menuItems) {
					ordersStr += "#" + menuItem.getID() + ": " + menuItem.getName() + "\n";
				}
				
			}
		}
		ordersStr += "*************************\n";
		return ordersStr;
	}
	
	// TODO: database or file system
	public void loadOrders() {
		
	}
	
	public void saveOrders() {
		
	}
}
