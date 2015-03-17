package eu.trentorise.game.bean;

public class TaskDTO {
	private String name;
	private String gameId;
	private String cronExpression;
	private int itemsToNotificate;
	private String itemType;
	private String classificationName;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public int getItemsToNotificate() {
		return itemsToNotificate;
	}

	public void setItemsToNotificate(int itemToNotificate) {
		this.itemsToNotificate = itemToNotificate;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getClassificationName() {
		return classificationName;
	}

	public void setClassificationName(String classificationName) {
		this.classificationName = classificationName;
	}

}
