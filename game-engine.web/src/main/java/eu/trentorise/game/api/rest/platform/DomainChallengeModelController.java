package eu.trentorise.game.api.rest.platform;

import static eu.trentorise.game.api.rest.ControllerUtils.decodePathVariable;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eu.trentorise.game.model.ChallengeModel;
import eu.trentorise.game.services.GameService;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@Profile("platform")
public class DomainChallengeModelController {

    @Autowired
    private GameService gameSrv;

    @RequestMapping(method = RequestMethod.POST,
            value = "/api/{domain}/model/game/{gameId}/challenge",
            consumes = {"application/json"}, produces = {"application/json"})
    @Operation(summary = "Add challenge model")
    public ChallengeModel saveGame(@RequestBody ChallengeModel challengeModel,
            @PathVariable String domain, @PathVariable String gameId) {
        gameId = decodePathVariable(gameId);
        return gameSrv.saveChallengeModel(gameId, challengeModel);
    }

    @RequestMapping(method = RequestMethod.GET,
            value = "/api/{domain}/model/game/{gameId}/challenge",
            produces = {"application/json"})
    @Operation(summary = "Get challenge models")
    public Set<ChallengeModel> readChallengeModels(@PathVariable String domain,
            @PathVariable String gameId) {
        gameId = decodePathVariable(gameId);
        return gameSrv.readChallengeModels(gameId);
    }

    @RequestMapping(method = RequestMethod.DELETE,
            value = "/api/{domain}/model/game/{gameId}/challenge/{modelId}",
            produces = {"application/json"})
    @Operation(summary = "Delete challenge model")
    public void deleteChallengeModels(@PathVariable String domain, @PathVariable String gameId,
            @PathVariable String modelId) {
        gameId = decodePathVariable(gameId);
        gameSrv.deleteChallengeModel(gameId, modelId);
    }

}
