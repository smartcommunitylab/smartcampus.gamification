package eu.trentorise.game.bean;

public class IncrementalClassificationDTO extends ClassificationDTO {
	private String periodName;
	private int delayValue;
	private String delayUnit;

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

}
