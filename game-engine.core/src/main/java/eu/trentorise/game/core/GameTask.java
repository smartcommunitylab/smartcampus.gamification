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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GameTask other = (GameTask) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
