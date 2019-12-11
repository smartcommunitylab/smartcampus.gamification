package eu.trentorise.game.api.rest;

import static eu.trentorise.game.api.rest.ControllerUtils.decodePathVariable;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import eu.trentorise.game.model.ChallengeConcept;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.services.PlayerService;

@RestController
public class ChallengeController {

    @Autowired
    private PlayerService playerSrv;

    @DeleteMapping("/data/game/{gameId}/player/{playerId}/challenge/{instanceName}")
    public ChallengeConcept deleteChallenge(@PathVariable String gameId,
            @PathVariable String playerId, @PathVariable String instanceName) {
        gameId = decodePathVariable(gameId);
        final String decodedPlayerId = decodePathVariable(playerId);
        final String decodedInstanceName = decodePathVariable(instanceName);

        PlayerState state = playerSrv.loadState(gameId, playerId, false, false);
        Optional<ChallengeConcept> removed = state.removeChallenge(decodedInstanceName);
        if (removed.isPresent()) {
            playerSrv.saveState(state);
        }
        return removed.orElseThrow(() -> new IllegalArgumentException(String.format(
                "challenge %s doesn't exist in state of player %s", decodedInstanceName,
                decodedPlayerId)));
    }
}
