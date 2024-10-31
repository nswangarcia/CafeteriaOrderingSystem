import java.util.List;

public class Order {
	private String id;
	private Menu menu;
	private List<MenuItem> menuItems;
	private String status;
	private UserData userData;
	
	// TODO: incorporate payment system
	
	// Order status' Pending, Processing, Completed, Delivered
	
	public Order(String id, Menu menu, List<MenuItem> menuItems, UserData userData) {
		this.id = id;
		this.menu = menu;
		this.menuItems = menuItems;
		this.status = "Pending";
		this.userData = userData;
	}
	
	// Getters
	public String getId() {
		return this.id;
	}
	public Menu getMenu() {
		return this.menu;
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
	// Setters
	public void setId(String id) {
		this.id = id;
	}
	public void setMenu(Menu menu) {
		this.menu = menu;
	}
	public void setMenuItems(List<MenuItem> menuItems) {
		this.menuItems = menuItems;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setUserData(UserData userData) {
		this.userData = userData;
	}
}
