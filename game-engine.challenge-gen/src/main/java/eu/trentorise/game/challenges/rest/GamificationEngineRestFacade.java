package eu.trentorise.game.challenges.rest;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A facade for handling logic for Gamification Engine Rest api
 */
public class GamificationEngineRestFacade {

    private static final String STATE = "state";

    private static final Logger logger = LogManager
	    .getLogger(GamificationEngineRestFacade.class);
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
    public Paginator gameState(String gameId) {
	if (gameId != null) {
	    WebTarget endpoint = createEndpoint().path(STATE).path(gameId);
	    return endpoint.request().get(Paginator.class);
	} else {
	    throw new IllegalArgumentException("gameId cannot be null");
	}
    }
}
