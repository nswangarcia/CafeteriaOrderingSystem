public class MenuItem {
	private int id;
	private String name;
	private String description;
	private String status;
	private double price;
	private int menuID;
	
	public MenuItem(int id, String status, String name, String description, int menuID) {
		this.id = id;
		this.status = status;
		this.name = name;
		this.description = description;
		this.menuID = menuID;
//		this.price = price;  
	}
	
	// Getters
	public int getID() {
		return this.id;
	}
	public String getStatus() {
		return this.status;
	}
	public String getName() {
		return this.name;
	}
	public String getDescription() {
		return this.description;
	}
	public int getMenuID() {
		return this.menuID;
	}
//	public double getPrice() {
//		return this.price;
//	}
	// Setters
	public void setID(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public void setMenuID(int id) {
		this.menuID = id;
	}
//	public void setID(String id) {
//		this.id = id;
//	}
}
