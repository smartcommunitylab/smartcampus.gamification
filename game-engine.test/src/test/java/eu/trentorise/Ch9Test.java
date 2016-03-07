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

public class Ch9Test extends GameTest {

	private static final String GAME = "challenge";
	private static final String ACTION = "app_sent_recommendation";

	private static final String PLAYER_1 = "damian_wayne";
	private static final Integer TARGET = 10;
	private static final Integer BONUS = 63;

	@Override
	public void initEnv() {
		Map<String, Object> customData = new HashMap<String, Object>();
		Calendar calendar = new GregorianCalendar();
		calendar.add(Calendar.DAY_OF_MONTH, -1);

		customData.put("ch_ID_startChTs", calendar.getTimeInMillis());

		calendar.add(Calendar.DAY_OF_MONTH, 2);
		customData.put("ch_ID_endChTs", calendar.getTimeInMillis());
		customData.put("ch_ID_target", TARGET);
		customData.put("ch_ID_bonus", BONUS);

		// it MUST be defined
		customData.put("ch_ID_success", false);
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
						+ "/ch9.drl"));

	}

	@Override
	public void defineExecData(List<ExecData> execList) {
		Map<String, Object> data = new HashMap<String, Object>();
		for (int i = 0; i < TARGET + 5; i++) {
			ExecData input = new ExecData(GAME, ACTION, PLAYER_1, data);
			execList.add(input);
		}

	}

	@Override
	public void analyzeResult() {
		assertionPoint(GAME, 0d + BONUS, PLAYER_1, "green leaves");
	}
}
