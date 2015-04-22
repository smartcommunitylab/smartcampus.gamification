/**
 *    Copyright 2015 Fondazione Bruno Kessler - Trento RISE
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package eu.trentorise;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import eu.trentorise.game.task.ClassificationTask;

public class GreenGameTest extends GameTest {

	private static final String GAME = "gameTest";
	private static final String ACTION = "save_itinerary";

	@Override
	public void initEnv() {
		savePlayerState(GAME, "1", Arrays.asList(new PointConceptBuilder()
				.setName("green leaves").setScore(15d).build(),
				new PointConceptBuilder().setName("health").setScore(2d)
						.build(), new PointConceptBuilder().setName("p+r")
						.setScore(5d).build()));

		savePlayerState(GAME, "2", Arrays.asList(new PointConceptBuilder()
				.setName("green leaves").setScore(12d).build(),
				new PointConceptBuilder().setName("health").setScore(50d)
						.build(), new PointConceptBuilder().setName("p+r")
						.setScore(12d).build()));

		savePlayerState(GAME, "11", Arrays.asList(new PointConceptBuilder()
				.setName("green leaves").setScore(112d).build(),
				new PointConceptBuilder().setName("health").setScore(52d)
						.build(), new PointConceptBuilder().setName("p+r")
						.setScore(1d).build()));

		savePlayerState(GAME, "122", Arrays.asList(new PointConceptBuilder()
				.setName("green leaves").setScore(2d).build(),
				new PointConceptBuilder().setName("health").setScore(20d)
						.build(), new PointConceptBuilder().setName("p+r")
						.setScore(11d).build()));

	}

	@Override
	public void defineGame() {

		/**
		 * IMPORTANT: if you use ClassificationTask in your game, you have to
		 * insert 'classification' into your game actions.
		 */
		defineGameHelper(GAME, Arrays.asList(ACTION));

		loadClasspathRules(GAME, Arrays.asList("rules/" + GAME + "/constants",
				"rules/" + GAME + "/initState.drl", "rules/" + GAME
						+ "/greenBadges.drl", "rules/" + GAME
						+ "/greenPoints.drl", "rules/" + GAME
						+ "/healthBadges.drl", "rules/" + GAME
						+ "/healthPoints.drl", "rules/" + GAME
						+ "/prBadges.drl", "rules/" + GAME + "/prPoints.drl",
				"rules/" + GAME + "/specialBadges.drl", "rules/" + GAME
						+ "/weekClassificationBadges.drl", "rules/" + GAME
						+ "/finalClassificationBadges.drl"));

		addGameTask(GAME, new ClassificationTask(null, 3, "green leaves",
				"final classification green"));
		addGameTask(GAME, new ClassificationTask(null, 3, "health",
				"final classification health"));
		addGameTask(GAME, new ClassificationTask(null, 3, "p+r",
				"final classification p+r"));
		addGameTask(GAME, new ClassificationTask(null, 1, "green leaves",
				"week classification green"));
		addGameTask(GAME, new ClassificationTask(null, 1, "health",
				"week classification health"));
		addGameTask(GAME, new ClassificationTask(null, 1, "p+r",
				"week classification p+r"));
	}

	@Override
	public void defineExecData(List<ExecData> execList) {
	}

	@Override
	public void analyzeResult() {

		// player 1
		assertionPoint(GAME, 15d, "1", "green leaves");
		assertionBadge(GAME,
				Arrays.asList("silver-medal-green", "10-point-green"), "1",
				"green leaves");
		assertionBadge(GAME, Arrays.asList("bronze-medal-pr"), "1", "p+r");
		assertionBadge(GAME, Collections.<String> emptyList(), "1", "health");
		assertionBadge(GAME, Collections.<String> emptyList(), "1", "special");

		// player 2
		assertionPoint(GAME, 12d, "2", "green leaves");
		assertionBadge(GAME,
				Arrays.asList("bronze-medal-green", "10-point-green"), "2",
				"green leaves");
		assertionBadge(GAME,
				Arrays.asList("10-point-pr", "king-week-pr", "gold-medal-pr"),
				"2", "p+r");
		assertionBadge(GAME, Arrays.asList("10-point-health",
				"25-point-health", "50-point-health", "silver-medal-health"),
				"2", "health");
		assertionBadge(GAME, Collections.<String> emptyList(), "2", "special");

		// player 11
		assertionPoint(GAME, 112d, "11", "green leaves");
		assertionBadge(GAME, Arrays.asList("gold-medal-green",
				"10-point-green", "50-point-green", "100-point-green",
				"king-week-green"), "11", "green leaves");
		assertionBadge(GAME, Collections.<String> emptyList(), "11", "p+r");
		assertionBadge(GAME, Arrays.asList("10-point-health",
				"25-point-health", "50-point-health", "king-week-health",
				"gold-medal-health"), "11", "health");
		assertionBadge(GAME, Collections.<String> emptyList(), "11", "special");

		// player 122
		assertionPoint(GAME, 2d, "122", "green leaves");
		assertionBadge(GAME, Collections.<String> emptyList(), "122",
				"green leaves");
		assertionBadge(GAME, Arrays.asList("10-point-pr", "silver-medal-pr"),
				"122", "p+r");
		assertionBadge(GAME,
				Arrays.asList("bronze-medal-health", "10-point-health"), "122",
				"health");
		assertionBadge(GAME, Collections.<String> emptyList(), "122", "special");

	}
}
