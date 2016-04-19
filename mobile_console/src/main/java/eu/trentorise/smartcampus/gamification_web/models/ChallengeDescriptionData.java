package eu.trentorise.smartcampus.gamification_web.models;

import java.io.Serializable;

public class ChallengeDescriptionData implements Serializable {
	
	private static final long serialVersionUID = 5614097311629963751L;
	private String id;
	private String type;
	private String mobilitymode;
	private String description;
	
	public String getId() {
		return id;
	}
	
	public String getType() {
		return type;
	}
	
	public String getMobilitymode() {
		return mobilitymode;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public void setMobilitymode(String mobilitymode) {
		this.mobilitymode = mobilitymode;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return "ChallengeDescriptionData [id=" + id + ", type=" + type + ", mobilitymode=" + mobilitymode
				+ ", description=" + description + "]";
	}
	

}
