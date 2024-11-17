import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import config.DBConfig;

public class OrderManagementSystem {
	private List<Order> orderData;
	
	public OrderManagementSystem() {
		loadOrders();
	}
	
	private Connection connectToDatabase() throws SQLException {
        return DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
    }
	
	public void createOrder(UserData userData, Menu menu, List<MenuItem> menuItems) {
	    try (Connection connection = connectToDatabase()) {
	        connection.setAutoCommit(false);

	        String insertOrderQuery = "INSERT INTO OrderData (name, email, menuName, status, userID, timestamp) "
	                                + "VALUES (?, ?, ?, ?, ?, ?)";
	        LocalDateTime timestamp = LocalDateTime.now();
	        int generatedOrderID = -1;

	        try (PreparedStatement stmt = connection.prepareStatement(insertOrderQuery, Statement.RETURN_GENERATED_KEYS)) {
	            stmt.setString(1, userData.getName());
	            stmt.setString(2, userData.getEmail());
	            stmt.setString(3, menu.getName());
	            stmt.setString(4, "Pending");
	            stmt.setInt(5, userData.getUserID());
	            stmt.setTimestamp(6, Timestamp.valueOf(timestamp));

	            int rowsAffected = stmt.executeUpdate();
	            if (rowsAffected > 0) {
	                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
	                    if (generatedKeys.next()) {
	                        generatedOrderID = generatedKeys.getInt(1); // Get the generated orderID
	                    }
	                }
	            }
	            if (generatedOrderID > 0) {
	                for (MenuItem menuItem : menuItems) {
	                    addOrderMenuItemDataToDB(connection, generatedOrderID, menuItem);
	                }

	                connection.commit();
	                System.out.println("Order placed successfully with orderID: " + generatedOrderID);
	            } else {
	                connection.rollback();
	                System.out.println("Failed to add order.");
	            }
	        } catch (SQLException e) {
	            connection.rollback();
	            throw e;
	        }
	        loadOrders();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	private void addOrderMenuItemDataToDB(Connection connection, int orderID, MenuItem menuItem) throws SQLException {
	    String insertQuery = "INSERT INTO OrderMenuItemData (orderID, menuItemID) VALUES (?, ?)";

	    try (PreparedStatement stmt = connection.prepareStatement(insertQuery)) {
	        stmt.setInt(1, orderID);
	        stmt.setInt(2, menuItem.getID());

	        int rowsAffected = stmt.executeUpdate();
	        if (rowsAffected > 0) {
	            System.out.println("Menu item with ID " + menuItem.getID() + " added to order " + orderID);
	        } else {
	            System.out.println("Failed to add menu item with ID " + menuItem.getID() + " to order " + orderID);
	        }
	    }
	}
	
	public List<Order> getOrders() {
		loadOrders();
		return this.orderData;
	}
	
	public void updateOrderStatusById(int id, String status) {
		if (!orderData.isEmpty()) {
			if (updateOrderStatusInDB(id, status) > 0) {
				System.out.println("Order status updated to: " + status);
                loadOrders();
                return;
            } else {
            	System.out.println("Invalid order id: " + id);
            }
		} else {
			System.out.println("There are no orders in system");
		}
	}
	
	public int updateOrderStatusInDB(int orderID, String status) {
		 try (Connection connection = connectToDatabase()) {
		        String updateQuery = "UPDATE OrderData SET status = ? WHERE orderID = ?";

		        try (PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
		            stmt.setString(1, status);
		            stmt.setInt(2, orderID);
		            
		            int rowsAffected = stmt.executeUpdate();
		            loadOrders();
		            return rowsAffected;
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		 return 0;
	}
	
	public String ordersToString() {
		String ordersStr = "";
		loadOrders();
		for (Order order : orderData) {
			if (!order.getStatus().equals("delivered")) {
				ordersStr += "\n*************************\nOrder ID: " + order.getOrderID()
						+ "\nOrder Time: " + order.getTimestamp()
						+ "\nOrder status: " + order.getStatus()
						+ "\nCustomer name: " + order.getUserData().getName() + "\n"
						+ "Customer email: " + order.getUserData().getEmail() + "\n"
						+ "\nOrder items:\n";
				List<MenuItem> menuItems = order.getMenuItems();
				for (MenuItem menuItem : menuItems) {
					ordersStr += "#" + menuItem.getID() + ": " + menuItem.getName() + "\n";
				}
			}
		}
		ordersStr += "*************************\n";
		return ordersStr;
	}
	
	// load orders from DB
	public void loadOrders() {
	    orderData = new ArrayList<>();
	    try (Connection connection = connectToDatabase()) {
	        String orderQuery = "SELECT o.orderID, o.menuName, o.status, o.userID, o.timestamp, o.name, o.email, " +
	                            "omi.menuItemID, mi.status AS itemStatus, mi.name AS itemName, " +
	                            "mi.description, mi.menuID " +
	                            "FROM OrderData AS o " +
	                            "JOIN OrderMenuItemData AS omi ON o.orderID = omi.orderID " +
	                            "JOIN MenuItemData AS mi ON omi.menuItemID = mi.menuItemID";

	        Statement orderStmt = connection.createStatement();
	        ResultSet orderRs = orderStmt.executeQuery(orderQuery);

	        Map<Integer, Order> orderMap = new HashMap<>();
	        Map<Integer, UserData> userMap = new HashMap<>();

	        while (orderRs.next()) {
	            int orderID = orderRs.getInt("orderID");
	            String menuName = orderRs.getString("menuName");
	            String status = orderRs.getString("status");
	            int userID = orderRs.getInt("userID");
	            Timestamp time = orderRs.getTimestamp("timestamp");
	            String name = orderRs.getString("name");
	            String email = orderRs.getString("email");
	            LocalDateTime timestamp = time != null ? time.toLocalDateTime() : null;
	            int menuItemID = orderRs.getInt("menuItemID");
	            String itemStatus = orderRs.getString("itemStatus");
	            String itemName = orderRs.getString("itemName");
	            String itemDescription = orderRs.getString("description");
	            int menuID = orderRs.getInt("menuID");

	            UserData userData = userMap.computeIfAbsent(userID, id -> new UserData(userID, name, email));

	            Order order = orderMap.computeIfAbsent(orderID, id -> new Order(timestamp, orderID, menuName, status, null, userData));

	            MenuItem menuItem = new MenuItem(menuItemID, itemStatus, itemName, itemDescription, menuID);
	            order.addMenuItem(menuItem);
	        }

	        orderData.addAll(orderMap.values());

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

}
