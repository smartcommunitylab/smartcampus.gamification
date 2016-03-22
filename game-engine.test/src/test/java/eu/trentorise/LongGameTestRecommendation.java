package eu.trentorise;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import eu.trentorise.GameTest.ExecData;
import eu.trentorise.game.model.BadgeCollectionConcept;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.services.PlayerService;

public class LongGameTestRecommendation extends GameTest {

	@Autowired
	PlayerService playerSrv;

	private static final String GAME = "long_game";
	private static final String ACTION = "app_sent_recommandation";

	private static final String PLAYER_ID = "annap";

	@Override
	public void initEnv() {
	}

	@Override
	public void defineGame() {
		List<GameConcept> concepts = new ArrayList<GameConcept>();
		concepts.add(new PointConcept("green leaves"));
		concepts.add(new BadgeCollectionConcept("green leaves"));
		concepts.add(new BadgeCollectionConcept("leaderboard top 3"));
		concepts.add(new BadgeCollectionConcept("recommendations"));
		
		defineGameHelper(GAME, Arrays.asList(ACTION), concepts);

		String rootProjFolder = new File(System.getProperty("user.dir"))
				.getParent();
		String pathGame = rootProjFolder
				+ "/game-engine.rules/src/main/resources/rules";

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
		//data.put("walkDistance", 0.4d);
		ExecData ex = new ExecData(GAME, ACTION, PLAYER_ID, data);
		execList.add(ex);
		
		data = new HashMap<String, Object>();
		ex = new ExecData(GAME, ACTION, PLAYER_ID, data);
		execList.add(ex);
		
		data = new HashMap<String, Object>();
		ex = new ExecData(GAME, ACTION, PLAYER_ID, data);
		execList.add(ex);
		
		//SO FAR 3 INVITED..
		
		data = new HashMap<String, Object>();
		ex = new ExecData(GAME, ACTION, PLAYER_ID, data);
		execList.add(ex);
		
		data = new HashMap<String, Object>();
		ex = new ExecData(GAME, ACTION, PLAYER_ID, data);
		execList.add(ex);
		
		//SO FAR 5 INVITED..
		
		data = new HashMap<String, Object>();
		ex = new ExecData(GAME, ACTION, PLAYER_ID, data);
		execList.add(ex);
		
		data = new HashMap<String, Object>();
		ex = new ExecData(GAME, ACTION, PLAYER_ID, data);
		execList.add(ex);
		
		data = new HashMap<String, Object>();
		ex = new ExecData(GAME, ACTION, PLAYER_ID, data);
		execList.add(ex);
		
		data = new HashMap<String, Object>();
		ex = new ExecData(GAME, ACTION, PLAYER_ID, data);
		execList.add(ex);
		
		data = new HashMap<String, Object>();
		ex = new ExecData(GAME, ACTION, PLAYER_ID, data);
		execList.add(ex);
		
		//SO FAR 10 INVITED..
		
		data = new HashMap<String, Object>();
		ex = new ExecData(GAME, ACTION, PLAYER_ID, data);
		execList.add(ex);
		
		data = new HashMap<String, Object>();
		ex = new ExecData(GAME, ACTION, PLAYER_ID, data);
		execList.add(ex);
		
		data = new HashMap<String, Object>();
		ex = new ExecData(GAME, ACTION, PLAYER_ID, data);
		execList.add(ex);
		
		data = new HashMap<String, Object>();
		ex = new ExecData(GAME, ACTION, PLAYER_ID, data);
		execList.add(ex);
		
		data = new HashMap<String, Object>();
		ex = new ExecData(GAME, ACTION, PLAYER_ID, data);
		execList.add(ex);
		
		data = new HashMap<String, Object>();
		ex = new ExecData(GAME, ACTION, PLAYER_ID, data);
		execList.add(ex);
		
		
		data = new HashMap<String, Object>();
		ex = new ExecData(GAME, ACTION, PLAYER_ID, data);
		execList.add(ex);
		
		data = new HashMap<String, Object>();
		ex = new ExecData(GAME, ACTION, PLAYER_ID, data);
		execList.add(ex);
		
		data = new HashMap<String, Object>();
		ex = new ExecData(GAME, ACTION, PLAYER_ID, data);
		execList.add(ex);
		
		data = new HashMap<String, Object>();
		ex = new ExecData(GAME, ACTION, PLAYER_ID, data);
		execList.add(ex);
		
		data = new HashMap<String, Object>();
		ex = new ExecData(GAME, ACTION, PLAYER_ID, data);
		execList.add(ex);
		
		data = new HashMap<String, Object>();
		ex = new ExecData(GAME, ACTION, PLAYER_ID, data);
		execList.add(ex);
		
		data = new HashMap<String, Object>();
		ex = new ExecData(GAME, ACTION, PLAYER_ID, data);
		execList.add(ex);
		
		data = new HashMap<String, Object>();
		ex = new ExecData(GAME, ACTION, PLAYER_ID, data);
		execList.add(ex);
		
		data = new HashMap<String, Object>();
		ex = new ExecData(GAME, ACTION, PLAYER_ID, data);
		execList.add(ex);
		
		//SO FAR 25 INVITED..
		
		data = new HashMap<String, Object>();
		ex = new ExecData(GAME, ACTION, PLAYER_ID, data);
		execList.add(ex);
		
	}

	@Override
	public void analyzeResult() {
		PlayerState s = playerSrv.loadState(GAME, PLAYER_ID, false);
		Assert.assertNotNull(s);
		
		//Check point totals
		assertionPoint(GAME, 1170d, PLAYER_ID, "green leaves");
		
		//Check cumulative counters for Km
		Assert.assertEquals(26, s.getCustomData().get("recommendations"));


	}
}


