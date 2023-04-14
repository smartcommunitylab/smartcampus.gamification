package eu.trentorise.game.bean;

public class IncrementalClassificationDTO extends ClassificationDTO {
	private String periodName;
	private int delayValue;
	private String delayUnit;
	private String cronExpression;

	public IncrementalClassificationDTO() {
		super.setType("incremental");
	}

	public String getPeriodName() {
		return periodName;
	}

	public void setPeriodName(String periodName) {
		this.periodName = periodName;
	}

	public int getDelayValue() {
		return delayValue;
	}

	public void setDelayValue(int delayValue) {
		this.delayValue = delayValue;
	}

	public String getDelayUnit() {
		return delayUnit;
	}

	public void setDelayUnit(String delayUnit) {
		this.delayUnit = delayUnit;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

}
