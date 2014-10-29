package eu.trentorise.game.core;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.services.PlayerService;
import eu.trentorise.game.services.Workflow;

@Component("gameCtx")
@Scope("prototype")
public class GameContext {

	private final Logger logger = LoggerFactory.getLogger(GameContext.class);
	private String gameRefId;

	@Autowired
	@Qualifier("dbPlayerManager")
	PlayerService playerSrv;

	@Autowired
	Workflow gameWorkflow;

	public GameContext(String gameRefId) {
		this.gameRefId = gameRefId;
	}

	public GameContext() {

	}

	public void sendAction(String action, String playerId) {
		gameWorkflow.apply(action, playerId, null);
	}

	public PlayerState readStatus(String playerId) {
		return playerSrv.loadState(playerId, gameRefId);
	}

	public List<String> readPlayers() {
		return playerSrv.readPlayers(gameRefId);
	}

}
