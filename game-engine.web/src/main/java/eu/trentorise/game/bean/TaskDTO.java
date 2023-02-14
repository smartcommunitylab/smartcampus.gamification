package eu.trentorise.game.bean;

import eu.trentorise.game.task.GeneralClassificationTask;
import eu.trentorise.game.task.IncrementalClassificationTask;

public class TaskDTO {
	private String id;
	private String type;
	private String pointConceptName;
	private Object task;

	public TaskDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TaskDTO(String id, IncrementalClassificationTask task) {
		super();
		this.id = id;
		this.task = task;
		this.type = "incremental";
		this.pointConceptName = task.getPointConceptName();
	}

	public TaskDTO(String id, GeneralClassificationTask task) {
		super();
		this.id = id;
		this.task = task;
		this.type = "general";
		this.pointConceptName = task.getItemType();
	}
	
	public TaskDTO(String id, IncrementalClassificationDTO task) {
		super();
		this.id = id;
		this.task = task;
		this.type = "incremental";
		this.pointConceptName = task.getItemType();
	}
	
	public TaskDTO(String id, GeneralClassificationDTO task) {
		super();
		this.id = id;
		this.task = task;
		this.type = "general";
		this.pointConceptName = task.getItemType();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Object getTask() {
		return task;
	}

	public void setTask(Object task) {
		this.task = task;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPointConceptName() {
		return pointConceptName;
	}

	public void setPointConceptName(String pointConceptName) {
		this.pointConceptName = pointConceptName;
	}

}
