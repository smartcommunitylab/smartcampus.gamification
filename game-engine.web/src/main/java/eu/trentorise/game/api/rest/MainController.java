package eu.trentorise.game.api.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.trentorise.game.bean.PlayerStateDTO;
import eu.trentorise.game.managers.NotificationManager;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.Notification;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.services.PlayerService;
import eu.trentorise.game.services.Workflow;
import eu.trentorise.game.utils.Converter;

@RestController
@RequestMapping(value = "/gengine")
public class MainController {

	private static Logger logger = org.slf4j.LoggerFactory
			.getLogger(MainController.class);

	@Autowired
	Workflow workflow;

	@Autowired
	PlayerService playerSrv;

	@Autowired
	GameService gameSrv;

	@Autowired
	NotificationManager notificationSrv;

	@Autowired
	Converter converter;

	@RequestMapping(method = RequestMethod.POST, value = "/execute")
	public void executeAction(@RequestBody ExecutionData data,
			HttpServletResponse res) {
		Game game = gameSrv.loadGameDefinitionByAction(data.getActionId());
		if (game != null && game.isTerminated()) {
			try {
				res.sendError(403,
						String.format("game %s is expired", game.getId()));
			} catch (IOException e1) {
				logger.error("Exception sendError to client", e1);
			}
		} else {
			workflow.apply(data.getActionId(), data.getUserId(), data.getData());
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/state/{gameId}/{playerId}")
	public PlayerStateDTO readPlayerState(@PathVariable String gameId,
			@PathVariable String playerId) {
		return converter.convertPlayerState(playerSrv.loadState(playerId,
				gameId));
	}

	@RequestMapping(method = RequestMethod.GET, value = "/state/{gameId}")
	public List<PlayerStateDTO> readPlayerState(@PathVariable String gameId) {
		List<PlayerStateDTO> resList = new ArrayList<PlayerStateDTO>();
		for (PlayerState ps : playerSrv.loadStates(gameId)) {
			resList.add(converter.convertPlayerState(ps));
		}
		return resList;
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