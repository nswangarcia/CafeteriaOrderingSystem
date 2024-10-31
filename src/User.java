public class User {
	private UserData userData;
	
	public User(UserData userData) {
		// TODO Auto-generated constructor stub
		this.userData = userData;
	}

	public UserData getUserData() {
		return this.userData;
	}

	public void updateUserData(UserData userData) {
		this.userData = userData;
	}
}
