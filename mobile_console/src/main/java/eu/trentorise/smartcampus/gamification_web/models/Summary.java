package eu.trentorise.smartcampus.gamification_web.models;

public class Summary {

	private String user;
	private String scores;
	private String notifications;
	
	
	public Summary() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Summary(String user, String scores, String notifications) {
		super();
		this.user = user;
		this.scores = scores;
		this.notifications = notifications;
	}
	
	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getScores() {
		return scores;
	}
	
	public String getNotifications() {
		return notifications;
	}
	
	public void setScores(String scores) {
		this.scores = scores;
	}
	
	public void setNotifications(String notifications) {
		this.notifications = notifications;
	}

	@Override
	public String toString() {
		return "Summary [user:" + user + ", scores:" + scores
				+ ", notifications:" + notifications + "]";
	}

	
}
