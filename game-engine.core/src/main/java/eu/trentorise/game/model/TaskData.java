package eu.trentorise.game.model;


public class TaskData {

	private String id;
	private String gameId;
	private String taskName;
	private long timestamp;
	private Object data;
	private String dataClassname;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getDataClassname() {
		return dataClassname;
	}

	public void setDataClassname(String dataClassname) {
		this.dataClassname = dataClassname;
	}
}
