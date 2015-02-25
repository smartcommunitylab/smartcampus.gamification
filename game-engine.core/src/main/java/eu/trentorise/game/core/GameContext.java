package eu.trentorise.game.core;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.TaskDataConverter;
import eu.trentorise.game.services.PlayerService;
import eu.trentorise.game.services.TaskService;
import eu.trentorise.game.services.Workflow;

@Component("gameCtx")
@Scope("prototype")
public class GameContext {

	private final Logger logger = LoggerFactory.getLogger(GameContext.class);

	private String gameRefId;
	private GameTask task;

	@Autowired
	private PlayerService playerSrv;

	@Autowired
	private Workflow workflow;

	@Autowired
	private TaskService taskSrv;

	TaskDataConverter converter = new TaskDataConverter();

	public enum Order {
		ASC, DESC
	}

	public GameContext(String gameRefId, GameTask task) {
		this.gameRefId = gameRefId;
		this.task = task;
	}

	public GameContext() {

	}

	public synchronized void sendAction(String action, String playerId,
			Map<String, Object> params) {
		workflow.apply(action, playerId, params);
	}

	public PlayerState readStatus(String playerId) {
		return playerSrv.loadState(playerId, gameRefId);
	}

	public List<String> readPlayers() {
		return playerSrv.readPlayers(gameRefId);
	}

	public String getGameRefId() {
		return gameRefId;
	}

	public GameTask getTask() {
		return task;
	}

	public String writeTaskData(Object data) {
		return taskSrv.saveData(gameRefId, task.getName(), data);
	}

	public Object readTaskData() {
		List<Object> res = taskSrv.readData(gameRefId, task.getName());
		return res.isEmpty() ? null : res.get(0);
	}

	public <T> T readTaskData(Class<T> classType) {
		return converter.convert(readTaskData(), classType);
	}

	// public Object readTaskDataById(String id) {
	// return taskSrv.readData(gameRefId, task.getName(),
	// new TaskDataQueryBuilder().setTaskDataId(id).build());
	// }
	//
	// public <T> T readTaskDataById(String id, Class<T> classType) {
	// return converter.convert(readTaskDataById(id), classType);
	// }
	//
	// public List<Object> readTaskDataByTimestamp(long timestamp, Order order)
	// {
	// switch (order) {
	// case ASC:
	// default:
	// return taskSrv.readData(gameRefId, task.getName(),
	// new TaskDataQueryBuilder().setTimestamp(timestamp).build());
	// case DESC:
	// return taskSrv.readData(gameRefId, task.getName(),
	// new TaskDataQueryBuilder().setTimestamp(timestamp).build());
	//
	// }
	// }
	//
	// /*
	// * public List<?> readTaskDataByClassname(String classname) { return
	// * taskSrv.readData(gameRefId, task.getName(), new
	// * TaskDataQueryBuilder().setTaskDataClassname(classname) .build()); }
	// */
	//
	// public <T> List<T> readTaskDataByClassname(Class<T> classtype) {
	// return converter.convert(taskSrv.readData(gameRefId, task.getName(),
	// new TaskDataQueryBuilder().setTaskDataClassname(classtype)
	// .build()), classtype);
	// }

}
