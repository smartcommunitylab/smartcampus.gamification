package eu.trentorise.game.challenges.rest;

import java.util.ArrayList;
import java.util.List;

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

    private static final String RULE_PREFIX = "db://";

    private static final Logger logger = LogManager
	    .getLogger(GamificationEngineRestFacade.class);

    private static final String STATE = "state";
    private static final String GAME = "game";
    private static final String RULE = "rule";
    private static final String DB = "db";
    private static final String EXECUTE = "execute";

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
     * @return a list of {@link Content}, null if error
     */
    public List<Content> readGameState(String gameId) {
	if (gameId == null) {
	    throw new IllegalArgumentException("gameId cannot be null");
	}
	WebTarget target = createEndpoint().path(STATE).path(gameId);
	// for testing pagination: target.queryParam("page",
	// 1).queryParam("size", 1).request().get(Paginator.class);
	Paginator response = target.request().get(Paginator.class);
	if (response == null) {
	    logger.error("error in reading game state");
	    return null;
	}
	if (response.getLast()) {
	    logger.error("service return only one page of result");
	    return response.getContent();
	}
	List<Content> result = new ArrayList<Content>();
	int page = 2;
	boolean end = false;
	Paginator pageResponse;
	result.addAll(response.getContent());
	while (!end) {
	    pageResponse = target.request().get(Paginator.class);
	    if (pageResponse != null) {
		result.addAll(pageResponse.getContent());
		if (pageResponse.getLast()) {
		    end = true;
		} else {
		    page++;
		}
	    }
	}
	logger.error("service return " + page + " pages of result");
	return result;
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
	    logger.debug("response code: " + response.getStatus());
	    return response.readEntity(RuleDto.class);
	}
	logger.error("response code: " + response.getStatus());
	return null;
    }

    /**
     * Delete rule from game
     * 
     * @param gameId
     *            - unique id for game
     * @param ruleId
     *            - unique id for rule
     * @return
     */
    public boolean deleteGameRule(String gameId, String ruleId) {
	if (gameId == null || ruleId == null) {
	    throw new IllegalArgumentException("input cannot be null");
	}
	String ruleUrl = StringUtils.removeStart(ruleId, RULE_PREFIX);
	WebTarget target = createEndpoint().path(GAME).path(gameId).path(RULE)
		.path(DB).path(ruleUrl);
	Response response = target.request().delete();
	if (response.getStatus() == Response.Status.OK.getStatusCode()) {
	    logger.debug("response code: " + response.getStatus());
	    return true;
	}
	logger.error("response code: " + response.getStatus());
	return false;
    }

    public boolean saveItinerary(ExecutionDataDTO input) {
	if (input == null) {
	    throw new IllegalArgumentException("input cannot be null");
	}
	WebTarget target = createEndpoint().path(EXECUTE);
	Response response = target.request().post(Entity.json(input));
	if (response.getStatus() == Response.Status.OK.getStatusCode()) {
	    logger.debug("response code: " + response.getStatus());
	    return true;
	}
	logger.error("response code: " + response.getStatus());
	return false;
    }
}
