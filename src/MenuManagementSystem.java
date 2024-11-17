import config.DBConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MenuManagementSystem {

	private List<Menu> menuData;
	private int count;
	
	public MenuManagementSystem() {
		this.menuData = new ArrayList<>();
		count = 0;
		//loadMenus(); // load from db
		loadMenuData();
	}
	
	private Connection connectToDatabase() throws SQLException {
        return DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
    }
	
	// for main
	public void addMenuByMenuName(String menuName) {
		String status = "Available";
		try (Connection connection = connectToDatabase()) {
	        String insertQuery = "INSERT IGNORE INTO MenuData (name, status) VALUES (?, ?)";

	        try (PreparedStatement stmt = connection.prepareStatement(insertQuery)) {
	            stmt.setString(1, menuName);
	            stmt.setString(2, status);

	            int rowsAffected = stmt.executeUpdate();

	            if (rowsAffected > 0) {
	                System.out.println("Menu added successfully.");
	                loadMenuData();
	            } else {
	                System.out.println("Failed to add menu.");
	            }
	        }
	        loadMenuData();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	// for main
	public void addMenuItemByMenuName(String menuName, String menuItemName, String menuItemDescription) {
		loadMenuData();
		Menu menu = this.getMenuByName(menuName);
		if (menu != null) {
			String status = "Available";
			try (Connection connection = connectToDatabase()) {
		        String insertQuery = "INSERT INTO MenuItemData (name, status, description, menuID) VALUES (?, ?, ?, ?)";

		        try (PreparedStatement stmt = connection.prepareStatement(insertQuery)) {
		            stmt.setString(1, menuItemName);
		            stmt.setString(2, status);
		            stmt.setString(3, menuItemDescription);
		            stmt.setInt(4, menu.getId());

		            int rowsAffected = stmt.executeUpdate();

		            if (rowsAffected > 0) {
		                System.out.println("Menu item added successfully.");
		                loadMenuData();
		            } else {
		                System.out.println("Failed to add menu item.");
		            }
		        }
		        loadMenuData();
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		}		
	}
	
	//for main
	public void removeMenuByMenuID(int menuID) {
		loadMenuData();
		Menu menu = this.getMenuByID(menuID);
			if (menu != null) {
				String deleteQuery = "DELETE FROM MenuData WHERE menuID = ?";
			        
		        try (Connection connection = connectToDatabase();
		            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
		            preparedStatement.setInt(1, menuID);
		            
		            int rowsDeleted = preparedStatement.executeUpdate();
		            if (rowsDeleted > 0) {
		            	System.out.println("Successfully removed menu.");
		            }
		            loadMenuData();
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		}
	}
	
	//for main
	public void removeMenuItemByMenuItemID(int menuItemID) {
		loadMenuData();
		String deleteQuery = "DELETE FROM MenuItemData WHERE menuItemID = ?";
	        
        try (Connection connection = connectToDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.setInt(1, menuItemID);
            
            int rowsDeleted = preparedStatement.executeUpdate();
            System.out.println("Rows deleted: " + rowsDeleted);
            loadMenuData();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public List<Menu> getMenus() {
		loadMenuData();
		return this.menuData;
	}
	
//	public Menu getMenuById(int id) {
//		for (Menu m: menuData) {
//			if (m.getId() == id) {
//				return m;
//			}
//		}
//		return null;
//	}
	
	public Menu getMenuByName(String name) {
		loadMenuData();
		for (Menu menu: menuData) {
			if (menu.getName().toLowerCase().equals(name.toLowerCase())) {
				return menu;
			}
		}
		return null;
	}
	public Menu getMenuByID(int menuID) {
		loadMenuData();
		for (Menu menu: menuData) {
			if (menu.getId() == menuID) {
				return menu;
			}
		}
		return null;
	}
	
	public String getAvailableMenuNames() {
		String result = "";
		loadMenuData();
		for (Menu menu : menuData) {
			if (menu.getStatus().toLowerCase().equals("available")) {
				result += menu.getName() + "\n";
			}
		}
		return result;
	}
	
//	public List<String> getAvailableMenuNamesList() {
//		List<String> result = new ArrayList<>();
//		for (Menu menu : menuData) {
//			if (menu.getStatus().equals("available")) {
//				result.add(menu.getName());
//			}
//		}
//		return result;
//	}
	
	public boolean isValidMenu(String name) {
		loadMenuData();
		for (Menu menu : menuData) {
			if (menu.getName().toLowerCase().equals(name.toLowerCase())) {
				return true;
			}
		}
		return false;
	}
	
	public String menusToString() {
		String result = "";
		loadMenuData();
		for (Menu menu: menuData) {
			result += "#" + menu.getId() + " " + menu.getName() + ": " + menu.getStatus() + "\n";
		}
		return result;
	}
	
	public String menuItemsToString() {
		String result = "";
		loadMenuData();
		for (Menu menu: menuData) {
			result += "Menu: " + menu.getName() + "\n";
			result += "MenuID: " + menu.getId() + "\n";
			result += "Status: " + menu.getStatus() + "\n";
			List<MenuItem> menuItems = menu.getMenuItems();
			result += "Menu Items:\n";
			for (MenuItem menuItem : menuItems) {
				result += "#" + menuItem.getID() + " " + menuItem.getName() + ": " + menuItem.getStatus() + "\n";
			}
			result += "\n";
		}
		return result;
	}
	
	// TODO: update menu item status as well.
	public void updateMenuStatusByMenuName(String menuName, String status) {
		loadMenuData();
		Menu menu = this.getMenuByName(menuName);
		if (menu != null) {
			String updateQuery = "UPDATE MenuData SET status = ? WHERE name = ?";
			    
		    try (Connection connection = connectToDatabase();
		         PreparedStatement stmt = connection.prepareStatement(updateQuery)) {

		        stmt.setString(1, status);
		        stmt.setString(2, menuName);

		        int rowsAffected = stmt.executeUpdate();
		        if (rowsAffected > 0) {
		        	updateMenuItemStatusByMenuName(menuName, status);
		            System.out.println("Menu status updated successfully.");
		            loadMenuData();
		        } else {
		            System.out.println("No menu found with the given name.");
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
			
		}
	}
	public void updateMenuItemStatusByMenuName(String menuName, String status) {
		loadMenuData();
		Menu menu = this.getMenuByName(menuName);
		if (menu != null) {
			String updateQuery = "UPDATE MenuItemData SET status = ? WHERE menuID = ?";
			    
		    try (Connection connection = connectToDatabase();
		         PreparedStatement stmt = connection.prepareStatement(updateQuery)) {

		        stmt.setString(1, status);
		        stmt.setInt(2, menu.getId());

		        int rowsAffected = stmt.executeUpdate();
		        if (rowsAffected > 0) {
		            System.out.println("Menu item status updated successfully.");
		            loadMenuData();
		        } else {
		            System.out.println("No menu item found with the given menu name.");
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
			
		}
	}
	public void updateMenuItemStatusByMenuItemID(int menuItemID, String status) {
		String updateQuery = "UPDATE MenuItemData SET status = ? WHERE menuItemID = ?";
		loadMenuData();
	    try (Connection connection = connectToDatabase();
	         PreparedStatement stmt = connection.prepareStatement(updateQuery)) {

	        stmt.setString(1, status);
	        stmt.setInt(2, menuItemID);

	        int rowsAffected = stmt.executeUpdate();
	        if (rowsAffected > 0) {
	            System.out.println("Menu item status updated successfully.");
	            loadMenuData();
	        } else {
	            System.out.println("No menu item found with the given name.");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	// for main.java
	public void loadMenuData() {
		// load menu data
		  try (Connection connection = connectToDatabase()) {
			    menuData = new ArrayList<>();
	            String menuQuery = "SELECT * FROM MenuData;";
	            Statement menuStmt = connection.createStatement();
	            ResultSet menuRs = menuStmt.executeQuery(menuQuery);
	            while (menuRs.next()) {
	                int menuID = menuRs.getInt("menuID");
	                String name = menuRs.getString("name");
	                String status = menuRs.getString("status");
	                
	                Menu menu = new Menu(menuID, status, name);
                	loadMenuItemData(menu);
                	this.menuData.add(menu);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	}
	
	// for main.java
	public void loadMenuItemData(Menu menu) {
		// load menu item data
		try (Connection connection = connectToDatabase()) {
			int mid = menu.getId();
			String menuQuery = "SELECT * FROM MenuItemData WHERE menuID = " + mid + ";";
			Statement menuStmt = connection.createStatement();
	        ResultSet menuRs = menuStmt.executeQuery(menuQuery);
	        while (menuRs.next()) {
	            int menuItemID = menuRs.getInt("menuItemID");
	            String menuItemName = menuRs.getString("name");
	            String menuItemStatus = menuRs.getString("status");
	            String menuItemDescription = menuRs.getString("description");
	            int menuID = menuRs.getInt("menuID");
	            
	            MenuItem menuItem = new MenuItem(menuItemID, menuItemStatus, menuItemName, menuItemDescription, menuID);
	        	menu.addMenuItem(menuItem);
	        }
	      } catch (SQLException e) {
	    	  e.printStackTrace();
	      }
	}
	
	public void saveMenusToDB() {
		loadMenuData();
	    try (Connection connection = connectToDatabase()) {
	        connection.setAutoCommit(false);

	        for (Menu menu : menuData) {
	            saveMenu(menu, connection);
	            saveMenuItems(menu, connection);
	        }
	        
	        connection.commit();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	// Helper method to insert or update a Menu record
	private void saveMenu(Menu menu, Connection connection) throws SQLException {
	    String query = "INSERT INTO MenuData (name, status) VALUES (?, ?) " +
	                   "ON DUPLICATE KEY UPDATE name = VALUES(name), status = VALUES(status)";
	    loadMenuData();
	    try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
	        stmt.setString(1, menu.getName());
	        stmt.setString(2, menu.getStatus());
	        stmt.executeUpdate();

	        // Retrieve the generated menuID if it's a new entry
	        ResultSet generatedKeys = stmt.getGeneratedKeys();
	        if (generatedKeys.next()) {
	            menu.setId(generatedKeys.getInt(1)); // Save the menuID for future use
	        }
	    }
	}

	// Helper method to insert or update MenuItem records
	private void saveMenuItems(Menu menu, Connection connection) throws SQLException {
	    String query = "INSERT INTO MenuItemData (menuID, name, description, status) VALUES (?, ?, ?, ?) " +
	                   "ON DUPLICATE KEY UPDATE name = VALUES(name), description = VALUES(description), status = VALUES(status)";
	    loadMenuData();
	    try (PreparedStatement stmt = connection.prepareStatement(query)) {
	        for (MenuItem item : menu.getMenuItems()) {
	            stmt.setInt(1, menu.getId());
	            stmt.setString(2, item.getName());
	            stmt.setString(3, item.getDescription());
	            stmt.setString(4, item.getStatus());
	            stmt.addBatch();
	        }
	        stmt.executeBatch();
	    }
	}

}
