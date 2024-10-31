import java.util.ArrayList;
import java.util.List;

public class Menu {
	private String id;
	private String status;
	private String name;
	private List<MenuItem> menuItems;

	public Menu(String id, String status, String name) {
		this.id = id;
		this.status = status;
		this.name = name;
		this.menuItems = new ArrayList<>();
		
	}
	
	public void addMenuItem(MenuItem menuItem) {
		menuItems.add(menuItem);
		System.out.println("Menu item: " + menuItem.getName() + " added to Menu: " + getName());
	}
	
	public void removeMenuItem(MenuItem menuItem) {
		menuItems.remove(menuItem);
		System.out.println("Menu item: " + menuItem.getName() + " removed from Menu: " + getName());
	}
	
	// Getters
	public int getNumberOfItems() {
		if (!menuItems.isEmpty()) {
			return this.menuItems.size();
		}
		return 0;
	}
	public String getId() {
		return this.id;
	}
	public String getStatus() {
		return this.status;
	}
	public String getName() {
		return this.name;
	}
	public List<MenuItem> getMenuItems() {
		return this.menuItems;
	}
	// Setters
	public void setId(String id) {
		this.id = id;
	}
	public void setStatus(String status) {
		this.status = status;
		System.out.println("Menu: " + this.getName() + " status updated to: " + this.getStatus());
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setMenuItems(List<MenuItem> menuItems) {
		this.menuItems = menuItems;
	}
}
