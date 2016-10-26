package eu.trentorise.smartcampus.gamification_web.models;

public class Event {
	
	private String name;
	private String type;
	private Long timestamp;
	
	public Event() {
		super();
	}

	public Event(String name, String type, Long timestamp) {
		super();
		this.name = name;
		this.type = type;
		this.timestamp = timestamp;
	}

	public String getName() {
		return name;
	}
	
	public String getType() {
		return type;
	}
	
	public Long getTimestamp() {
		return timestamp;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	

}
