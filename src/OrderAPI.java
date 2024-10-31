import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OrderAPI implements Observe {
	private OrderManagementSystem orderManagementSystem;

	public boolean createOrder() {
		
		return true;
	}

	public List<Order> getOrdersByUser(String userId, OrderManagementSystem OMS) {
		List<Order> orders = new ArrayList<>();
		for (Order order: OMS.getOrders()) {
			UserData userData = order.getUserData();
			if (userData.getId().equals(userId)) {
				orders.add(order);
			}
		}
		return orders;
	}


	/**
	 * @see Observe#registerObserver(Observer)
	 */
	public void registerObserver(Observer observer) {

	}


	/**
	 * @see Observe#unregisterObserver(Observer)
	 */
	public void unregisterObserver(Observer observer) {

	}


	/**
	 * @see Observe#nofityObservers()
	 */
	public void nofityObservers() {

	}

}
