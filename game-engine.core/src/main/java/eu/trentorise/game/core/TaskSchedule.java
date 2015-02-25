package eu.trentorise.game.core;

public class TaskSchedule {
	private String cronExpression;

	private CronFrequency frequency;

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public CronFrequency getFrequency() {
		return frequency;
	}

	public void setFrequency(CronFrequency frequency) {
		this.frequency = frequency;
	}

	public enum CronFrequency {
		DAILY("0 0 0 * * *"), WEEKLY("0 0 0 ? 0 *"), MONTHLY("0 0 0 1 * *");

		CronFrequency(String cronExpr) {
			this.cronExpr = cronExpr;
		}

		private final String cronExpr;

		public String getCronExpr() {
			return cronExpr;
		}

	}

}
