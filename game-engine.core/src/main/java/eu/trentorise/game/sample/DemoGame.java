package eu.trentorise.game.sample;

import java.util.Arrays;
import java.util.HashSet;

import org.springframework.stereotype.Component;

import eu.trentorise.game.model.BadgeCollectionConcept;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.GameConcept;
import eu.trentorise.game.model.PointConcept;

@Component
public class DemoGame {

	private Game game;

	private static final String GAME_ID = "";
	private static final String GAME_NAME = "";
	private static final String GAME_OWNER = "";

	public DemoGame() {
		game = new Game(GAME_ID);
		game.setName(GAME_NAME);
		game.setOwner(GAME_OWNER);

		game.setActions(new HashSet<String>(Arrays.asList("save_itinerary")));

		game.setConcepts(new HashSet<GameConcept>(Arrays.asList(
				new PointConcept("green leaves"), new PointConcept("health"),
				new PointConcept("p+r"), new BadgeCollectionConcept(
						"green leaves"), new BadgeCollectionConcept("health"),
				new BadgeCollectionConcept("p+r"), new BadgeCollectionConcept(
						"special"))));

	}
}
