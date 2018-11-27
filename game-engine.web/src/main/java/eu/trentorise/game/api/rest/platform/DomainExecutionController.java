package eu.trentorise.game.api.rest.platform;

import static eu.trentorise.game.api.rest.ControllerUtils.decodePathVariable;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eu.trentorise.game.bean.ExecutionDataDTO;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.services.Workflow;
import io.swagger.annotations.ApiOperation;

@RestController
@Profile("platform")
public class DomainExecutionController {

    private static final Logger logger = LoggerFactory.getLogger(DomainExecutionController.class);

    @Autowired
    private GameService gameSrv;

    @Autowired
    private Workflow workflow;

    // Execute
    // POST /exec/game/{id}/action/{actionId}

    @RequestMapping(method = RequestMethod.POST,
            value = "/api/{domain}/exec/game/{gameId}/action/{actionId}",
            consumes = {"application/json"}, produces = {"application/json"})
    @ApiOperation(value = "Execute an action")
    public void executeAction(@PathVariable String domain, @PathVariable String gameId,
            @PathVariable String actionId, @RequestBody ExecutionDataDTO data,
            HttpServletResponse res) {
        gameId = decodePathVariable(gameId);
        actionId = decodePathVariable(actionId);

        Game game = gameSrv.loadGameDefinitionByAction(actionId);
        if (game != null && game.isTerminated()) {
            try {
                res.sendError(403, String.format("game %s is expired", game.getId()));
            } catch (IOException e1) {
                logger.error("Exception sendError to client", e1);
            }
        } else {
            if (data.getExecutionMoment() == null) {
                workflow.apply(gameId, actionId, data.getPlayerId(), data.getData(), null);
            } else {
                workflow.apply(gameId, actionId, data.getPlayerId(),
                        data.getExecutionMoment().getTime(), data.getData(), null);
            }
        }
    }
}
