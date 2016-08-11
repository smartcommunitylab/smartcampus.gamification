package eu.trentorise.game.bean;

public class IncrementalClassificationDTO extends ClassificationDTO {
	private String periodName;

	public IncrementalClassificationDTO() {
		super.setType("incremental");
	}

	public String getPeriodName() {
		return periodName;
	}

	public void setPeriodName(String periodName) {
		this.periodName = periodName;
	}
}
