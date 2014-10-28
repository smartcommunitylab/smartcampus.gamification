package eu.trentorise.game.core;

public abstract class GameTask {

	private TaskSchedule schedule;

	public abstract void execute(GameContext ctx);

	public GameTask(TaskSchedule schedule) {
		this.setSchedule(schedule);
	}

	public TaskSchedule getSchedule() {
		return schedule;
	}

	public void setSchedule(TaskSchedule schedule) {
		this.schedule = schedule;
	}

}
