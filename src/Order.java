import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
	private int orderID;
	private String menuName;
	private List<MenuItem> menuItems;
	private String status;
	private UserData userData;
	private LocalDateTime timestamp;
	
	// TODO: incorporate payment system
	
	// Order status' Pending, Processing, Completed, Delivered
	
	public Order(LocalDateTime timestamp, int orderID, String menuName, String status, List<MenuItem> menuItems, UserData userData) {
		this.timestamp = timestamp;
		this.orderID = orderID;
		this.menuName = menuName;
		this.status = status;
		this.menuItems = menuItems;
		this.userData = userData;
		this.menuItems = new ArrayList<>();
	}
	
	// Getters
	public LocalDateTime getTimestamp() {
		return this.timestamp;
	}
	public int getOrderID() {
		return this.orderID;
	}
	public String getMenuName() {
		return this.menuName;
	}
	public List<MenuItem> getMenuItems() {
		return this.menuItems;
	}
	public String getStatus() {
		return this.status;
	}
	public UserData getUserData() {
		return this.userData;
	}
	public LocalDateTime getTimeStamp() {
		return this.timestamp;
	}
	// Setters
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}
	public void setMenuItems(List<MenuItem> menuItems) {
		this.menuItems = menuItems;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setUserData(UserData userData) {
		this.userData = userData;
	}
	
	public void addMenuItem(MenuItem menuItem) {
		this.menuItems.add(menuItem);
	}
	public void setTimestamp() {
		this.timestamp = LocalDateTime.now();
	}
}
