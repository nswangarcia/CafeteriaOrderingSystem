import java.util.List;

public class MenuAPI implements Observe {
	private Menu menu;

	private MenuManagementSystem menuManagementSystem;
	
	public MenuAPI(Menu menu) {
		this.menu = menu;
	}

	public Menu getAvailableMenu() {
		return this.menu;
	}

	public boolean updateMenuStatus(String status) {
		this.menu.setStatus(status);
		return true;
	}

	public boolean addMenuItem(MenuItem menuItem) {
		this.menu.addMenuItem(menuItem);
		return true;
	}
	
	public boolean removeMenuItem(MenuItem menuItem) {
		this.menu.addMenuItem(menuItem);
		return true;
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
