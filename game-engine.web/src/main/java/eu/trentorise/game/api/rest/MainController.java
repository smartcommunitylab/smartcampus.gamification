/**
 *    Copyright 2015 Fondazione Bruno Kessler - Trento RISE
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package eu.trentorise.game.api.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.trentorise.game.bean.ExecutionDataDTO;
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
	public void executeAction(@RequestBody ExecutionDataDTO data,
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
			workflow.apply(data.getGameId(), data.getActionId(),
					data.getPlayerId(), data.getData(), null);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/state/{gameId}/{playerId}")
	public PlayerStateDTO readPlayerState(@PathVariable String gameId,
			@PathVariable String playerId) {
		return converter.convertPlayerState(playerSrv.loadState(gameId,
				playerId, true));
	}

	@RequestMapping(method = RequestMethod.GET, value = "/state/{gameId}")
	public Page<PlayerStateDTO> readPlayerState(@PathVariable String gameId,
			Pageable pageable,
			@RequestParam(required = false) String playerFilter) {

		List<PlayerStateDTO> resList = new ArrayList<PlayerStateDTO>();
		Page<PlayerState> page = null;
		if (playerFilter == null) {
			page = playerSrv.loadStates(gameId, pageable);
		} else {
			page = playerSrv.loadStates(gameId, playerFilter, pageable);
		}
		for (PlayerState ps : page) {
			resList.add(converter.convertPlayerState(ps));
		}

		PageImpl<PlayerStateDTO> res = new PageImpl<PlayerStateDTO>(resList,
				pageable, page.getTotalElements());

		return res;
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
