package eu.trentorise.smartcampus.gamification_web.models.status;

public class ServerChallengesData {

	private String name = "";
	private String modelName = "";
	private long start = 0L;
	private long end = 0L;
	private Boolean completed = false;
	private long dateCompleted = 0L;
	// fields for specific challenges
	private String periodName = "";		// how can I use it (weekly)
	private String bonusPointType = "";
	private String counterName = "";
	private String badgeCollectionName = "";
	private int bonusScore = 0;
	private int baseline = 0;
	private int target = 0;
	private int initialBadgeNum = 0;

	public String getName() {
		return name;
	}

	public String getModelName() {
		return modelName;
	}

	public long getStart() {
		return start;
	}

	public long getEnd() {
		return end;
	}

	public Boolean getCompleted() {
		return completed;
	}

	public long getDateCompleted() {
		return dateCompleted;
	}

	public String getPeriodName() {
		return periodName;
	}

	public String getBonusPointType() {
		return bonusPointType;
	}

	public String getCounterName() {
		return counterName;
	}

	public String getBadgeCollectionName() {
		return badgeCollectionName;
	}

	public int getBonusScore() {
		return bonusScore;
	}

	public int getBaseline() {
		return baseline;
	}

	public int getTarget() {
		return target;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public void setEnd(long end) {
		this.end = end;
	}

	public void setCompleted(Boolean completed) {
		this.completed = completed;
	}

	public void setDateCompleted(long dateCompleted) {
		this.dateCompleted = dateCompleted;
	}

	public void setPeriodName(String periodName) {
		this.periodName = periodName;
	}

	public void setBonusPointType(String bonusPointType) {
		this.bonusPointType = bonusPointType;
	}

	public void setCounterName(String counterName) {
		this.counterName = counterName;
	}

	public void setBadgeCollectionName(String badgeCollectionName) {
		this.badgeCollectionName = badgeCollectionName;
	}

	public void setBonusScore(int bonusScore) {
		this.bonusScore = bonusScore;
	}

	public void setBaseline(int baseline) {
		this.baseline = baseline;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	public int getInitialBadgeNum() {
		return initialBadgeNum;
	}

	public void setInitialBadgeNum(int initialBadgeNum) {
		this.initialBadgeNum = initialBadgeNum;
	}

	public ServerChallengesData() {
		super();
	}

	public ServerChallengesData(String name, String modelName, long start, long end, Boolean completed,
			long dateCompleted, String periodName, String bonusPointType, String counterName,
			String badgeCollectionName, int bonusScore, int baseline, int target, int initialBadgeNum) {
		super();
		this.name = name;
		this.modelName = modelName;
		this.start = start;
		this.end = end;
		this.completed = completed;
		this.dateCompleted = dateCompleted;
		this.periodName = periodName;
		this.bonusPointType = bonusPointType;
		this.counterName = counterName;
		this.badgeCollectionName = badgeCollectionName;
		this.bonusScore = bonusScore;
		this.baseline = baseline;
		this.target = target;
		this.initialBadgeNum = initialBadgeNum;
	}

}
