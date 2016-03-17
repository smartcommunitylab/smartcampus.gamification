package eu.trentorise;

import java.io.File;
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

	private static final String GAME = "long_game";
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
		concepts.add(new BadgeCollectionConcept("bike aficionado"));
		concepts.add(new BadgeCollectionConcept("public transport aficionado"));
		concepts.add(new BadgeCollectionConcept("park and ride pioneer"));
		concepts.add(new BadgeCollectionConcept("leaderboard top 3"));
		
		defineGameHelper(GAME, Arrays.asList(ACTION), concepts);

		String rootProjFolder = new File(System.getProperty("user.dir"))
				.getParent();
		String pathGame = rootProjFolder
				+ "/game-engine.games/rovereto-longgame";

		loadFilesystemRules(GAME, Arrays.asList(pathGame + "/constants",
				pathGame + "/greenBadges.drl", 
				pathGame + "/greenPoints.drl",
				pathGame + "/mode-counters.drl", 
				pathGame + "/finalClassificationBadges.drl",
				pathGame + "/specialBadges.drl",
  				pathGame + "/weekClassificationBadges.drl"));  
	}

	@Override
	public void defineExecData(List<ExecData> execList) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("walkDistance", 0.4d);
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
		data.put("bikeDistance", 9.47d);
		data.put("bikesharing", true);
		data.put("sustainable", true);
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
		 * this "reset" action is a fake action used in
		 * combination with a stub rule in the .drl file to force
		 * a reset of the "_past" counters.
		 */
		data = new HashMap<String, Object>();
		data.put("reset", new Boolean(true));
		ex = new ExecData(GAME, ACTION, PLAYER_ID, data);
		execList.add(ex);

		data = new HashMap<String, Object>();
		data.put("bikeDistance", 1d);
		data.put("bikesharing", true);
		data.put("walkDistance", 2.3d);
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
		
		//Check point totals
		assertionPoint(GAME, 492d, PLAYER_ID, "green leaves");
		
		//Check cumulative counters for Km
		Assert.assertEquals(22.7d, s.getCustomData().get("walk_km"));
		Assert.assertEquals(40.57d, s.getCustomData().get("bike_km"));
		Assert.assertEquals(10.47d, s.getCustomData().get("bikesharing_km"));
		Assert.assertEquals(5d, s.getCustomData().get("car_km"));
		Assert.assertEquals(20d, s.getCustomData().get("bus_km"));
		Assert.assertEquals(66d, s.getCustomData().get("train_km"));

		// Check cumulative counters for trips
		Assert.assertEquals(4, s.getCustomData().get("walk_trips"));
		Assert.assertEquals(4, s.getCustomData().get("bike_trips"));
		Assert.assertEquals(2, s.getCustomData().get("bikesharing_trips"));
		Assert.assertEquals(2, s.getCustomData().get("car_trips"));
		Assert.assertEquals(2, s.getCustomData().get("bus_trips"));
		Assert.assertEquals(2, s.getCustomData().get("train_trips"));

		// Check period counters for Km
		Assert.assertEquals(2.3d, s.getCustomData().get("walk_km_past"));
		Assert.assertEquals(1d, s.getCustomData().get("bike_km_past"));
		Assert.assertEquals(1d, s.getCustomData().get("bikesharing_km_past"));
		Assert.assertEquals(3d, s.getCustomData().get("car_km_past"));
		Assert.assertEquals(5d, s.getCustomData().get("bus_km_past"));
		Assert.assertEquals(40d, s.getCustomData().get("train_km_past"));

		// Check period counters for trips
		Assert.assertEquals(1, s.getCustomData().get("walk_trips_past"));
		Assert.assertEquals(1, s.getCustomData().get("bike_trips_past"));
		Assert.assertEquals(1, s.getCustomData().get("bikesharing_trips_past"));
		Assert.assertEquals(1, s.getCustomData().get("car_trips_past"));
		Assert.assertEquals(1, s.getCustomData().get("bus_trips_past"));
		Assert.assertEquals(1, s.getCustomData().get("train_trips_past"));

	}
}
