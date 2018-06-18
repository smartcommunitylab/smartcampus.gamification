/**
 * Copyright 2015 Fondazione Bruno Kessler - Trento RISE
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package eu.trentorise.game.api.rest.platform;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
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
import eu.trentorise.game.core.LogHub;
import eu.trentorise.game.managers.NotificationManager;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.core.Notification;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.services.PlayerService;
import eu.trentorise.game.services.Workflow;
import eu.trentorise.game.utils.Converter;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(value = "/gengine/{domain}")
@Profile("platform")
public class DomainMainController {

    private static Logger logger = org.slf4j.LoggerFactory.getLogger(DomainMainController.class);

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

    @RequestMapping(method = RequestMethod.POST, value = "/execute",
            consumes = {"application/json"}, produces = {"application/json"})
    @ApiOperation(value = "Execute an action", notes = "Execute an action in a game")
    public void executeAction(@PathVariable String domain, @RequestBody ExecutionDataDTO data,
            HttpServletResponse res) {
        Game game = gameSrv.loadGameDefinitionByAction(data.getActionId());
        if (game != null && game.isTerminated()) {
            try {
                res.sendError(403, String.format("game %s is expired", game.getId()));
            } catch (IOException e1) {
                LogHub.error(game.getId(), logger, "Exception sendError to client", e1);
            }
        } else {
            if (data.getExecutionMoment() == null) {
                workflow.apply(data.getGameId(), data.getActionId(), data.getPlayerId(),
                        data.getData(), null);
            } else {
                workflow.apply(data.getGameId(), data.getActionId(), data.getPlayerId(),
                        data.getExecutionMoment().getTime(), data.getData(), null);
            }
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/state/{gameId}/{playerId}",
            produces = {"application/json"})
    @ApiOperation(value = "Get player state", notes = "Get the state of a player in a game")
    public PlayerStateDTO readPlayerState(@PathVariable String domain, @PathVariable String gameId,
            @PathVariable String playerId) {
        try {
            gameId = URLDecoder.decode(gameId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("gameId is not UTF-8 encoded");
        }

        try {
            playerId = URLDecoder.decode(playerId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("playerId is not UTF-8 encoded");
        }

        return converter.convertPlayerState(playerSrv.loadState(gameId, playerId, true));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/state/{gameId}",
            produces = {"application/json"})
    @ApiOperation(value = "Get player states",
            notes = "Get the state of players in a game filter by optional player name")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve "),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page."),})
    public Page<PlayerStateDTO> readPlayerState(@PathVariable String domain,
            @PathVariable String gameId, @ApiIgnore Pageable pageable,
            @RequestParam(required = false) String playerFilter) {

        try {
            gameId = URLDecoder.decode(gameId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("gameId is not UTF-8 encoded");
        }

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

        PageImpl<PlayerStateDTO> res =
                new PageImpl<PlayerStateDTO>(resList, pageable, page.getTotalElements());

        return res;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/notification/{gameId}",
            produces = {"application/json"})
    @ApiOperation(value = "Get notifications", notes = "Get the notifications of a game")
    @Deprecated
    public List<Notification> readNotification(@PathVariable String domain,
            @PathVariable String gameId, @RequestParam(required = false) Long timestamp) {
        try {
            gameId = URLDecoder.decode(gameId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("gameId is not UTF-8 encoded");
        }

        if (timestamp != null) {
            return notificationSrv.readNotifications(gameId, timestamp, -1);
        } else {
            return notificationSrv.readNotifications(gameId);
        }
    }

    @RequestMapping(method = RequestMethod.GET,
            value = "/notification/{gameId}/{playerId}", produces = {"application/json"})
    @ApiOperation(value = "Get player notifications",
            notes = "Get the player notifications of a game")
    @Deprecated
    public List<Notification> readNotification(@PathVariable String domain,
            @PathVariable String gameId, @PathVariable String playerId,
            @RequestParam(required = false) Long timestamp) {

        try {
            gameId = URLDecoder.decode(gameId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("gameId is not UTF-8 encoded");
        }

        try {
            playerId = URLDecoder.decode(playerId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("playerId is not UTF-8 encoded");
        }
        if (timestamp != null) {
            return notificationSrv.readNotifications(gameId, playerId, timestamp, -1);
        } else {
            return notificationSrv.readNotifications(gameId, playerId);
        }
    }
}
