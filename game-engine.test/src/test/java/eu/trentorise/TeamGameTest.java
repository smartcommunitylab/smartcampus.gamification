package eu.trentorise;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.Team;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.services.PlayerService;

public class TeamGameTest extends GameTest {

	@Autowired
	PlayerService playerSrv;

	private static final String GAME = "teams";
	private static final String ACTION = "save_itinerary";

	@Override
	public void initEnv() {
		Team team = new Team(GAME, "fuorileggeId");
		team.setName("fuorilegge");
		team.setMembers(Arrays.asList("prowler"));
		playerSrv.saveTeam(team);
	}

	@Override
	public void defineGame() {
		List<GameConcept> concepts = new ArrayList<GameConcept>();
		concepts.add(new PointConcept("steps"));

		defineGameHelper(GAME, Arrays.asList(ACTION), concepts);

		loadClasspathRules(
				GAME,
				Arrays.asList("rules/" + GAME + "/constants", "rules/" + GAME
						+ "/rulePoint.drl"));
	}

	@Override
	public void defineExecData(List<ExecData> execList) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("meters-walked", 500d);
		ExecData input = new ExecData(ACTION, "prowler", data);
		input.setGameId(GAME);
		execList.add(input);

		data = new HashMap<String, Object>();
		data.put("meters-walked", 100d);
		input = new ExecData(ACTION, "fuorileggeId", data);
		input.setGameId(GAME);
		execList.add(input);
	}

	@Override
	public void analyzeResult() {
		assertionPoint(GAME, 750d, "prowler", "steps");
		assertionPoint(GAME, 525d, "fuorileggeId", "steps");
	}

}
