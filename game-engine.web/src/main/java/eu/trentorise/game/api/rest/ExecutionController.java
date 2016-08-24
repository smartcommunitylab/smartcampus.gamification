package eu.trentorise.game.api.rest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eu.trentorise.game.bean.ExecutionDataDTO;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.services.Workflow;

@RestController
public class ExecutionController {

	private static final Logger logger = LoggerFactory
			.getLogger(ExecutionController.class);

	@Autowired
	private GameService gameSrv;

	@Autowired
	private Workflow workflow;

	// Execute
	// POST /exec/game/{id}/action/{actionId}

	@RequestMapping(method = RequestMethod.POST, value = "/exec/game/{gameId}/action/{actionId}")
	public void executeAction(@PathVariable String gameId,
			@PathVariable String actionId, @RequestBody ExecutionDataDTO data,
			HttpServletResponse res) {
		try {
			gameId = URLDecoder.decode(gameId, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("gameId is not UTF-8 encoded");
		}

		try {
			actionId = URLDecoder.decode(actionId, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("gameId is not UTF-8 encoded");
		}

		Game game = gameSrv.loadGameDefinitionByAction(actionId);
		if (game != null && game.isTerminated()) {
			try {
				res.sendError(403,
						String.format("game %s is expired", game.getId()));
			} catch (IOException e1) {
				logger.error("Exception sendError to client", e1);
			}
		} else {
			workflow.apply(gameId, actionId, data.getPlayerId(),
					data.getData(), null);
		}
	}
}
