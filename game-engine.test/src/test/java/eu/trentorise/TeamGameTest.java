package eu.trentorise;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import eu.trentorise.game.model.BadgeCollectionConcept;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.TeamState;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.services.PlayerService;

public class TeamGameTest extends GameTest {

	@Autowired
	PlayerService playerSrv;

	private static final String GAME = "teams";
	private static final String ACTION = "save_itinerary";

	@Override
	public void initEnv() {
		TeamState team = new TeamState(GAME, "fuorilegge");
		team.setName("fuorilegge");
		team.setMembers(Arrays.asList("prowler", "rocket racer"));
		playerSrv.saveTeam(team);

	}

	@Override
	public void defineGame() {
		List<GameConcept> concepts = new ArrayList<GameConcept>();
		concepts.add(new PointConcept("steps"));
		concepts.add(new BadgeCollectionConcept("itinerary"));
		concepts.add(new BadgeCollectionConcept("my-badges"));

		defineGameHelper(GAME, Arrays.asList(ACTION), concepts);

		loadClasspathRules(GAME, Arrays.asList("rules/" + GAME + "/constants",
				"rules/" + GAME + "/rulePoint.drl", "rules/" + GAME
						+ "/ruleBadges.drl"));
	}

	@Override
	public void defineExecData(List<ExecData> execList) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("meters-walked", 500d); // 750steps
		ExecData input = new ExecData(GAME, ACTION, "prowler", data);
		execList.add(input);

		data = new HashMap<String, Object>();
		data.put("meters-walked", 1000d); // 1500steps
		input = new ExecData(GAME, ACTION, "fuorilegge", data);
		execList.add(input);

		data = new HashMap<String, Object>();
		data.put("meters-walked", 400d); // 600steps
		input = new ExecData(GAME, ACTION, "prowler", data);
		execList.add(input);

		data = new HashMap<String, Object>();
		data.put("meters-walked", 100d); // 150steps
		input = new ExecData(GAME, ACTION, "rocket racer", data);
		execList.add(input);

	}

	@Override
	public void analyzeResult() {
		assertionPoint(GAME, 1501d, "prowler", "steps");
		assertionPoint(GAME, 301d, "rocket racer", "steps");
		assertionPoint(GAME, 2270d, "fuorilegge", "steps");

	}

}
