package eu.trentorise.game.bean;

public class GeneralClassificationDTO extends ClassificationDTO {
	private String cronExpression;

	public GeneralClassificationDTO() {
		super.setType("general");
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
}
