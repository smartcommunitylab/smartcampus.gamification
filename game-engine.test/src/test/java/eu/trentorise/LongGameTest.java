package eu.trentorise;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import eu.trentorise.game.model.BadgeCollectionConcept;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.services.PlayerService;

public class LongGameTest extends GameTest {

	@Autowired
	PlayerService playerSrv;

	private static final String GAME = "long-game";
	private static final String ACTION = "save_itinerary";

	private static final String PLAYER_ID = "daken";

	@Override
	public void initEnv() {
	}

	@Override
	public void defineGame() {
		List<GameConcept> concepts = new ArrayList<GameConcept>();
		concepts.add(new PointConcept("green leaves"));
		concepts.add(new BadgeCollectionConcept("green leaves"));

		defineGameHelper(GAME, Arrays.asList(ACTION), concepts);

		String pathGame = "/home/mirko/data/git/smartcampus.gamification/game-engine.games/rovereto-longgame";
		loadFilesystemRules(GAME, Arrays.asList(pathGame + "/constants",
				pathGame + "/greenBadges.drl", pathGame + "/greenPoints.drl",
				pathGame + "/mode-counters.drl", pathGame
						+ "/finalClassificationBadges.drl", pathGame
						+ "/specialBadges.drl", pathGame
						+ "/weekClassificationBadges.drl"));

	}

	@Override
	public void defineExecData(List<ExecData> execList) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("walkDistance", 4d);
		ExecData ex = new ExecData(GAME, ACTION, PLAYER_ID, data);
		execList.add(ex);

		data = new HashMap<String, Object>();
		data.put("walkDistance", 10d);
		ex = new ExecData(GAME, ACTION, PLAYER_ID, data);
		execList.add(ex);

		data = new HashMap<String, Object>();
		data.put("bikeDistance", 30d);
		ex = new ExecData(GAME, ACTION, PLAYER_ID, data);
		execList.add(ex);

		data = new HashMap<String, Object>();
		data.put("bikeDistance", 10d);
		data.put("bikesharing", true);
		ex = new ExecData(GAME, ACTION, PLAYER_ID, data);
		execList.add(ex);

		data = new HashMap<String, Object>();
		data.put("walkDistance", 10d);
		data.put("busDistance", 15d);
		ex = new ExecData(GAME, ACTION, PLAYER_ID, data);
		execList.add(ex);

		data = new HashMap<String, Object>();
		data.put("carDistance", 2d);
		data.put("trainDistance", 26d);
		ex = new ExecData(GAME, ACTION, PLAYER_ID, data);
		execList.add(ex);

		data = new HashMap<String, Object>();
		data.put("walkDistance", 0.09d);
		data.put("bikeDistance", 0.1d);
		ex = new ExecData(GAME, ACTION, PLAYER_ID, data);
		execList.add(ex);

		/*
		 * this "reset" action is a fake action used in combination with a stub
		 * rule in the .drl file to force a rese4t of the "-past" counters.
		 */
		data = new HashMap<String, Object>();
		data.put("reset", new Boolean(true));
		ex = new ExecData(GAME, ACTION, PLAYER_ID, data);
		execList.add(ex);

		data = new HashMap<String, Object>();
		data.put("bikeDistance", 1d);
		data.put("bikesharing", true);
		data.put("walkDistance", 2d);
		data.put("carDistance", 3d);
		data.put("trainDistance", 40d);
		data.put("busDistance", 5d);
		ex = new ExecData(GAME, ACTION, PLAYER_ID, data);
		execList.add(ex);

	}

	@Override
	public void analyzeResult() {
		PlayerState s = playerSrv.loadState(GAME, PLAYER_ID, false);
		Assert.assertNotNull(s);

		// Check cumulative counters for Km
		Assert.assertEquals(26d, s.getCustomData().get("walk-km"));
		Assert.assertEquals(41.1d, s.getCustomData().get("bike-km"));
		Assert.assertEquals(11d, s.getCustomData().get("bikesharing-km"));
		Assert.assertEquals(5d, s.getCustomData().get("car-km"));
		Assert.assertEquals(20d, s.getCustomData().get("bus-km"));
		Assert.assertEquals(66d, s.getCustomData().get("train-km"));

		// Check cumulative counters for trips
		Assert.assertEquals(4, s.getCustomData().get("walk-trips"));
		Assert.assertEquals(4, s.getCustomData().get("bike-trips"));
		Assert.assertEquals(2, s.getCustomData().get("bikesharing-trips"));
		Assert.assertEquals(2, s.getCustomData().get("car-trips"));
		Assert.assertEquals(2, s.getCustomData().get("bus-trips"));
		Assert.assertEquals(2, s.getCustomData().get("train-trips"));

		// Check period counters for Km
		Assert.assertEquals(2d, s.getCustomData().get("walk-km-past"));
		Assert.assertEquals(1d, s.getCustomData().get("bike-km-past"));
		Assert.assertEquals(1d, s.getCustomData().get("bikesharing-km-past"));
		Assert.assertEquals(3d, s.getCustomData().get("car-km-past"));
		Assert.assertEquals(5d, s.getCustomData().get("bus-km-past"));
		Assert.assertEquals(40d, s.getCustomData().get("train-km-past"));

		// Check period counters for trips
		Assert.assertEquals(1, s.getCustomData().get("walk-trips-past"));
		Assert.assertEquals(1, s.getCustomData().get("bike-trips-past"));
		Assert.assertEquals(1, s.getCustomData().get("bikesharing-trips-past"));
		Assert.assertEquals(1, s.getCustomData().get("car-trips-past"));
		Assert.assertEquals(1, s.getCustomData().get("bus-trips-past"));
		Assert.assertEquals(1, s.getCustomData().get("train-trips-past"));

	}
}
