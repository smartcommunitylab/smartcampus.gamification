package eu.trentorise.game.challenges.rest;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A facade for handling logic for Gamification Engine Rest api
 * 
 * @see <a
 *      href="https://github.com/smartcommunitylab/smartcampus.gamification">Gamification
 *      Engine</a>
 */
public class GamificationEngineRestFacade {

    private static final String STATE = "state";
    private static final String GAME = "game";
    private static final Logger logger = LogManager
	    .getLogger(GamificationEngineRestFacade.class);
    private static final String RULE = "rule";
    private static final String DB = "db";

    private final String endpoint;

    public GamificationEngineRestFacade(final String endpoint) {
	if (endpoint == null) {
	    throw new NullPointerException("Endpoint cannot be null");
	}
	logger.debug("GamificationEngineRestFacade created");
	this.endpoint = endpoint;
    }

    /**
     * @return {@link WebTarget} to be used by facade
     */
    private WebTarget createEndpoint() {
	logger.debug("created endpoint for " + endpoint);
	Client client = ClientBuilder.newClient();
	return client.target(endpoint);
    }

    /**
     * Read game state
     * 
     * @param gameId
     * @return {@link Paginator} a page of results
     */
    public Paginator readGameState(String gameId) {
	if (gameId == null) {
	    throw new IllegalArgumentException("gameId cannot be null");
	}
	WebTarget target = createEndpoint().path(STATE).path(gameId);
	Paginator response = target.request().get(Paginator.class);
	if (response != null) {
	    logger.error("response code: ");
	    return response;
	} else {
	    logger.error("response code: ");
	    return null;
	}

    }

    /**
     * Insert given rule inside gamification engine
     * 
     * @param gameId
     *            unique id for game
     * @param rule
     *            to insert
     * @return {@link RuleDto} instance, null if error
     */
    public RuleDto insertGameRule(String gameId, RuleDto rule) {
	if (gameId == null || rule == null) {
	    throw new IllegalArgumentException("input cannot be null");
	}
	WebTarget target = createEndpoint().path(GAME).path(gameId).path(RULE)
		.path(DB);
	Response response = target.request().post(Entity.json(rule));

	if (response.getStatus() == Response.Status.OK.getStatusCode()) {
	    logger.error("response code: " + response.getStatus());
	    return response.readEntity(RuleDto.class);
	} else {
	    logger.error("response code: " + response.getStatus());
	    return null;
	}
    }

    public boolean deleteGameRule(String gameId, String id) {
	if (gameId == null || id == null) {
	    throw new IllegalArgumentException("input cannot be null");
	}
	String ruleUrl = StringUtils.removeStart(id, "db://");
	WebTarget target = createEndpoint().path(GAME).path(gameId).path(RULE)
		.path(DB).path(ruleUrl);
	Response response = target.request().delete();
	if (response.getStatus() == Response.Status.OK.getStatusCode()) {
	    logger.error("response code: " + response.getStatus());
	    return true;
	} else {
	    logger.error("response code: " + response.getStatus());
	    return false;
	}

    }
}
