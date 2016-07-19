package eu.trentorise.smartcampus.gamification_web.models;

public class UserCheck {

	private boolean registered;

	public boolean isRegistered() {
		return registered;
	}

	public void setRegistered(boolean registered) {
		this.registered = registered;
	}

	public UserCheck() {
		super();
	}

	public UserCheck(boolean registered) {
		super();
		this.registered = registered;
	}
}
