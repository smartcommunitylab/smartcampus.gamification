package eu.trentorise.smartcampus.gamification_web.models.status;

public class ChallengesData {
	
	private String challId = "";
	private String challDesc = "";
	private String challCompleteDesc = "";
	private int challTarget = 0;
	private int status = 0;
	private double row_status = 0L;
	private String type = "";
	private Boolean active = false;
	private Boolean success = false;
	private long startDate = 0L;
	private long endDate = 0L;
	private int daysToEnd = 0;
	
	public ChallengesData(){
		super();
	}

	public ChallengesData(String challId, String challDesc, String challCompleteDesc, int challTarget, int status, double row_status,
			String type, Boolean active, Boolean success, long startDate, long endDate, int daysToEnd) {
		super();
		this.challId = challId;
		this.challDesc = challDesc;
		this.challCompleteDesc = challCompleteDesc;
		this.challTarget = challTarget;
		this.status = status;
		this.row_status = row_status;
		this.type = type;
		this.active = active;
		this.success = success;
		this.startDate = startDate;
		this.endDate = endDate;
		this.daysToEnd = daysToEnd;
	}

	public String getChallId() {
		return challId;
	}

	public String getChallDesc() {
		return challDesc;
	}

	public int getChallTarget() {
		return challTarget;
	}

	public void setChallId(String challId) {
		this.challId = challId;
	}

	public void setChallDesc(String challDesc) {
		this.challDesc = challDesc;
	}

	public void setChallTarget(int challTarget) {
		this.challTarget = challTarget;
	}
	
	public String getType() {
		return type;
	}

	public int getStatus() {
		return status;
	}

	public Boolean getActive() {
		return active;
	}

	public Boolean getSuccess() {
		return success;
	}

	public long getStartDate() {
		return startDate;
	}

	public long getEndDate() {
		return endDate;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}

	public String getChallCompleteDesc() {
		return challCompleteDesc;
	}

	public void setChallCompleteDesc(String challCompleteDesc) {
		this.challCompleteDesc = challCompleteDesc;
	}

	public int getDaysToEnd() {
		return daysToEnd;
	}

	public void setDaysToEnd(int daysToEnd) {
		this.daysToEnd = daysToEnd;
	}

	public double getRow_status() {
		return row_status;
	}

	public void setRow_status(double row_status) {
		this.row_status = row_status;
	}

	@Override
	public String toString() {
		return "ChallengesData [challId=" + challId + ", challDesc=" + challDesc + ", challCompleteDesc="
				+ challCompleteDesc + ", challTarget=" + challTarget + ", status=" + status + ", type=" + type
				+ ", active=" + active + ", success=" + success + ", startDate=" + startDate + ", endDate=" + endDate
				+ "]";
	}

}
