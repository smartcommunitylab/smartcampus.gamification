package eu.trentorise.game.api.rest;

import static eu.trentorise.game.api.rest.ControllerUtils.decodePathVariable;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import eu.trentorise.game.core.LogHub;
import eu.trentorise.game.managers.ChallengeManager;
import eu.trentorise.game.model.ChallengeConcept;
import eu.trentorise.game.model.ChallengeUpdate;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.repo.ChallengeConceptPersistence;
import eu.trentorise.game.repo.ChallengeConceptRepo;
import eu.trentorise.game.services.PlayerService;

@RestController
public class ChallengeController {

    private static final Logger logger = LoggerFactory.getLogger(ChallengeController.class);

    @Autowired
    private PlayerService playerSrv;

    @Autowired
    private ChallengeManager challengeSrv;
    
    @Autowired
   	private ChallengeConceptRepo challengeConceptRepo;

    @DeleteMapping("/data/game/{gameId}/player/{playerId}/challenge/{instanceName}")
    public ChallengeConcept deleteChallenge(@PathVariable String gameId,
            @PathVariable String playerId, @PathVariable String instanceName) {
        gameId = decodePathVariable(gameId);
        final String decodedPlayerId = decodePathVariable(playerId);
        final String decodedInstanceName = decodePathVariable(instanceName);

        PlayerState state = playerSrv.loadState(gameId, playerId, false, false);
        List<ChallengeConceptPersistence> listCcs = challengeConceptRepo.findByGameIdAndPlayerId(gameId, playerId); 
        state.loadChallengeConcepts(listCcs);
        Optional<ChallengeConcept> removed = state.removeChallenge(decodedInstanceName);
        ChallengeConceptPersistence saved = challengeConceptRepo.findByGameIdAndPlayerIdAndName(gameId, playerId, instanceName);
        challengeConceptRepo.delete(saved);
        if (removed.isPresent()) {
            playerSrv.saveState(state);
            LogHub.info(gameId, logger, "removed challenge {} of player {}", instanceName,
                    playerId);
        }
        return removed.orElseThrow(() -> new IllegalArgumentException(String.format(
                "challenge %s doesn't exist in state of player %s", decodedInstanceName,
                decodedPlayerId)));
    }


    @PutMapping("/data/game/{gameId}/player/{playerId}/challenge/{instanceName}")
    public ChallengeConcept updateChallenge(@PathVariable String gameId,
            @PathVariable String playerId, @PathVariable String instanceName,
            @RequestBody ChallengeUpdate changes) {
        gameId = decodePathVariable(gameId);
        final String decodedPlayerId = decodePathVariable(playerId);
        final String decodedInstanceName = decodePathVariable(instanceName);

        changes.setName(decodedInstanceName);
        return challengeSrv.update(gameId, decodedPlayerId, changes);
    }
}
