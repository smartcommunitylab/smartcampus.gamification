package eu.trentorise.smartcampus.gamification_web.models;

import java.io.Serializable;

public class SponsorBannerData implements Serializable {

	private static final long serialVersionUID = -7371129192199527893L;
	private String id;
	private long start_timestamp;
	private long end_timestamp;
	private String week_theme;
	private String prize;
	private String sponsor;
	
	public String getId() {
		return id;
	}
	
	public long getStart_timestamp() {
		return start_timestamp;
	}
	
	public long getEnd_timestamp() {
		return end_timestamp;
	}
	
	public String getWeek_theme() {
		return week_theme;
	}
	
	public String getPrize() {
		return prize;
	}
	
	public String getSponsor() {
		return sponsor;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setStart_timestamp(long start_timestamp) {
		this.start_timestamp = start_timestamp;
	}
	
	public void setEnd_timestamp(long end_timestamp) {
		this.end_timestamp = end_timestamp;
	}
	
	public void setWeek_theme(String week_theme) {
		this.week_theme = week_theme;
	}
	
	public void setPrize(String prize) {
		this.prize = prize;
	}
	
	public void setSponsor(String sponsor) {
		this.sponsor = sponsor;
	}
	
	@Override
	public String toString() {
		return "SponsorBannerData [id=" + id + ", start_timestamp=" + start_timestamp + ", end_timestamp="
				+ end_timestamp + ", week_theme=" + week_theme + ", prize=" + prize + ", sponsor=" + sponsor + "]";
	}

}
