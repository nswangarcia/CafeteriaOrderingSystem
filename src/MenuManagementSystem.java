import java.util.ArrayList;
import java.util.List;

public class MenuManagementSystem {

	private List<Menu> menus;
	private int count;
	
	public MenuManagementSystem() {
		this.menus = new ArrayList<>();
		count = 0;
	}
	
	public void addMenu(Menu menu) {
		this.menus.add(menu);
		this.count += menu.getNumberOfItems();
	}
	
	//TODO?? depends on database system
	public void resetMenuItemIds() {
		int id = 0;
		for (Menu menu : menus) {
			List<MenuItem> menuItems = menu.getMenuItems();
			for (MenuItem menuItem : menuItems) {
				id++;
				menuItem.setID(String.valueOf(id));
			}
		}
	}
	
	public void addMenuItemByMenuName(String menuName, String menuItemName, String menuItemDescirption) {
		Menu menu = this.getMenuByName(menuName);
		if (menu != null) {
			String status = "Available";
			MenuItem menuItem = new MenuItem(String.valueOf(getMenuItemCount() + 1), status, menuItemName, menuItemDescirption);
			menu.addMenuItem(menuItem);
		}
	}
	public void removeMenuItemByMenuName(String menuName, String menuItemId) {
		Menu menu = this.getMenuByName(menuName);
			if (menu != null) {
			List<MenuItem> menuItems = menu.getMenuItems();
			for (MenuItem menuItem : menuItems) {
				if (menuItem.getID().equals(menuItemId)) {
					menu.removeMenuItem(menuItem);
					// TODO: may remove when db added
					resetMenuItemIds();
					return;
				}
			}
		}
	}
	
	public int getMenuItemCount() {
		return this.count;
	}
	
	public List<Menu> getMenus() {
		return this.menus;
	}
	
	public Menu getMenuByName(String name) {
		for (Menu menu: menus) {
			if (menu.getName().toLowerCase().equals(name.toLowerCase())) {
				return menu;
			}
		}
		return null;
	}
	
	public String getAvailableMenuNames() {
		String result = "";
		for (Menu menu : menus) {
			if (menu.getStatus().equals("available")) {
//				System.out.println(menu.getName());
				result += menu.getName() + "\n";
			}
		}
		return result;
	}
	
	public String menuItemsToString() {
		String result = "";
		for (Menu menu: menus) {
			result += "Menu: " + menu.getName() + "\n";
			result += "Status: " + menu.getStatus() + "\n";
			List<MenuItem> menuItems = menu.getMenuItems();
			result += "Menu Items:\n";
			for (MenuItem menuItem : menuItems) {
				result += "#" + menuItem.getID() + " " + menuItem.getName() + "\n";
			}
			result += "\n";
		}
		return result;
	}
	
	public void updateMenuStatusByName(String menuName, String status) {
		Menu menu = this.getMenuByName(menuName) ;
		if (menu != null) {
			menu.setStatus(status);
			
		}
	}
	
	// TODO: database or file system
	public void loadMenus() {
		// load menu lists
	}
	
	public void saveMenus() {
		// save menu lists
	}
}
