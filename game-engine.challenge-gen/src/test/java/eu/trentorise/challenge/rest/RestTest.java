package eu.trentorise.challenge.rest;

import org.junit.Test;

import eu.trentorise.game.challenges.rest.GamificationEngineRestFacade;
import eu.trentorise.game.challenges.rest.Paginator;

public class RestTest {

    @Test
    public void gameReadTest() {
	GamificationEngineRestFacade facade = new GamificationEngineRestFacade(
		"http://localhost:8080/gamification/gengine");
	Paginator result = facade.gameState("56e7bf3b570ac89331c37262");
	System.out.println();

    }
}
