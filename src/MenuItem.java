public class MenuItem {
	private String id;
	private String name;
	private String description;
	private String status;
	private double price;
	
	public MenuItem(String id, String status, String name, String description) {
		this.id = id;
		this.status = status;
		this.name = name;
		this.description = description;
//		this.price = price;  
	}
	
	// Getters
	public String getID() {
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
//	public double getPrice() {
//		return this.price;
//	}
	// Setters
	public void setID(String id) {
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
//	public void setID(String id) {
//		this.id = id;
//	}
}
