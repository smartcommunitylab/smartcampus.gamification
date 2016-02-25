package eu.trentorise;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.trentorise.game.model.BadgeCollectionConcept;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.core.GameConcept;

public class Ch3Test extends GameTest {

	private static final String GAME = "challenge";
	private static final String ACTION = "myAction";

	private static final String PLAYER_1 = "dick_grayson";

	@Override
	public void initEnv() {
		Map<String, Object> customData = new HashMap<String, Object>();
		Calendar calendar = new GregorianCalendar();
		calendar.add(Calendar.DAY_OF_MONTH, -1);

		customData.put("ch-ID-startChTs", calendar.getTimeInMillis());

		calendar.add(Calendar.DAY_OF_MONTH, 2);
		customData.put("ch-ID-endChTs", calendar.getTimeInMillis());

		savePlayerState(
				GAME,
				PLAYER_1,
				Arrays.asList(new PointConceptBuilder().setName("green leaves")
						.setScore(0d).build()), customData);
	}

	@Override
	public void defineGame() {
		List<GameConcept> concepts = new ArrayList<GameConcept>();
		concepts.add(new PointConcept("green leaves"));
		concepts.add(new BadgeCollectionConcept("green leaves"));

		defineGameHelper(GAME, Arrays.asList(ACTION), concepts);

		loadClasspathRules(
				GAME,
				Arrays.asList("rules/" + GAME + "/constants", "rules/" + GAME
						+ "/ch3.drl", "rules/" + GAME + "/greenPoints.drl"));

	}

	@Override
	public void defineExecData(List<ExecData> execList) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("walkDistance", 10d);
		data.put("bikeDistance", 5d);
		data.put("bikesharing", true);
		ExecData input = new ExecData(GAME, ACTION, PLAYER_1, data);
		execList.add(input);

		data = new HashMap<String, Object>();
		input = new ExecData(GAME, ACTION, PLAYER_1, data);
		execList.add(input);

		data = new HashMap<String, Object>();
		data.put("bikeDistance", 15d);
		data.put("bikesharing", true);
		input = new ExecData(GAME, ACTION, PLAYER_1, data);
		execList.add(input);

		data = new HashMap<String, Object>();
		data.put("walkDistance", 2d);
		input = new ExecData(GAME, ACTION, PLAYER_1, data);
		execList.add(input);
	}

	@Override
	public void analyzeResult() {
		assertionPoint(GAME, 210d, PLAYER_1, "green leaves");
	}
}
