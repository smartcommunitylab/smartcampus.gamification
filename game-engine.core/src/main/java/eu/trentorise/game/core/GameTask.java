package eu.trentorise.game.core;

public abstract class GameTask {

	private String name;

	private TaskSchedule schedule;

	public abstract void execute(GameContext ctx);

	public GameTask(String name, TaskSchedule schedule) {
		this.name = name;
		this.setSchedule(schedule);
	}

	public GameTask() {

	}

	public TaskSchedule getSchedule() {
		return schedule;
	}

	public void setSchedule(TaskSchedule schedule) {
		this.schedule = schedule;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
