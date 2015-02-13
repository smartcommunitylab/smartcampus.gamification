package eu.trentorise.game.api.rest;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.trentorise.game.managers.NotificationManager;
import eu.trentorise.game.model.Notification;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.services.PlayerService;
import eu.trentorise.game.services.Workflow;

@RestController
public class MainController {

	@Autowired
	Workflow workflow;

	@Autowired
	PlayerService playerSrv;

	@Autowired
	NotificationManager notificationSrv;

	@RequestMapping(method = RequestMethod.POST, value = "/execute")
	public void executeAction(@RequestBody ExecutionData data) {
		workflow.apply(data.getActionId(), data.getUserId(), data.getData());
	}

	@RequestMapping(method = RequestMethod.GET, value = "/state/{gameId}/{playerId}")
	public PlayerState readPlayerState(@PathVariable String gameId,
			@PathVariable String playerId) {
		return playerSrv.loadState(playerId, gameId);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/state/{gameId}")
	public List<PlayerState> readPlayerState(@PathVariable String gameId) {
		return playerSrv.loadStates(gameId);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/notification/{gameId}")
	public List<Notification> readNotification(@PathVariable String gameId,
			@RequestParam(required = false) Long timestamp) {
		if (timestamp != null) {
			return notificationSrv.readNotifications(gameId, timestamp);
		} else {
			return notificationSrv.readNotifications(gameId);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/notification/{gameId}/{playerId}")
	public List<Notification> readNotification(@PathVariable String gameId,
			@PathVariable String playerId,
			@RequestParam(required = false) Long timestamp) {
		if (timestamp != null) {
			return notificationSrv.readNotifications(gameId, playerId,
					timestamp);
		} else {
			return notificationSrv.readNotifications(gameId, playerId);
		}
	}

}

class ExecutionData {
	private String actionId;
	private String userId;
	private Map<String, Object> data;

	public ExecutionData() {
	}

	public String getActionId() {
		return actionId;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

}