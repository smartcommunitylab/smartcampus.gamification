package eu.trentorise.game.api.rest;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eu.trentorise.game.bean.ChallengeDataDTO;
import eu.trentorise.game.services.PlayerService;

@RestController
public class PlayerController {

	@Autowired
	private PlayerService playerSrv;

	@RequestMapping(method = RequestMethod.POST, value = "/data/game/{gameId}/player/{playerId}/challenges")
	public void assignChallenge(@RequestBody ChallengeDataDTO challengeData,
			@PathVariable String gameId, @PathVariable String playerId) {

		try {
			gameId = URLDecoder.decode(gameId, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("gameId is not UTF-8 encoded");
		}
		playerSrv.assignChallenge(gameId, playerId,
				challengeData.getModelName(), challengeData.getInstanceName(),
				challengeData.getData(), challengeData.getStart(),
				challengeData.getEnd());
	}
}
