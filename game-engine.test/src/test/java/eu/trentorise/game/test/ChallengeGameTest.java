package eu.trentorise.game.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import eu.trentorise.game.model.ChallengeModel;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.services.PlayerService;

public class ChallengeGameTest extends GameTest {

    private static final String GAME = "challengeTest";
    private static final String ACTION = "play";

    private static final String DOMAIN = "my-domain";

    private static final String PLAYER_1 = "rick grames";
    private static final String PLAYER_2 = "Bob Stookey";

    @Autowired
    private GameService gameSrv;

    @Autowired
    private PlayerService playerSrv;

    // TODO to remove extending GameTest with operation on ChallengeModels
    @Autowired
    private MongoTemplate mongo;

    @Override
    public void initEnv() {

        savePlayerState(GAME, PLAYER_1, new ArrayList<GameConcept>());
        savePlayerState(GAME, PLAYER_2, new ArrayList<GameConcept>());

        // assign a challenge to PLAYER_1
        LocalDate today = new LocalDate();
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("target", 100d);
        data.put("bonusScore", 100d);
        data.put("bonusPointType", "green leaves");
        playerSrv.assignChallenge(GAME, PLAYER_1, "score", "specialInstance", data, today.toDate(),
                today.dayOfMonth().addToCopy(1).toDate());

        // assign a challenge to PLAYER_2
        data = new HashMap<String, Object>();
        data.put("counter", 0);
        data.put("target", 2);
        data.put("bonusScore", 50d);
        data.put("bonusPointType", "green leaves");
        playerSrv.assignChallenge(GAME, PLAYER_2, "zeroImpact", null, data, today.toDate(),
                today.dayOfMonth().addToCopy(1).toDate());

    }

    @Override
    public void defineGame() {
        // TODO to remove extending GameTest with operation on ChallengeModels
        mongo.dropCollection(ChallengeModel.class);

        List<GameConcept> concepts = new ArrayList<GameConcept>();
        concepts.add(new PointConcept("green leaves"));

        defineGameHelper(DOMAIN, GAME, Arrays.asList(ACTION), concepts);

        try {
            loadClasspathRules(GAME, "rules/" + GAME);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // define challenge models
        ChallengeModel model = new ChallengeModel();
        model.setName("score");
        model.setVariables(new HashSet<String>());
        model.getVariables().add("target");
        model.getVariables().add("bonusScore");
        model.getVariables().add("bonusPointType");
        gameSrv.saveChallengeModel(GAME, model);

        model = new ChallengeModel();
        model.setName("zeroImpact");
        model.setVariables(new HashSet<String>());
        model.getVariables().add("counter");
        model.getVariables().add("target");
        model.getVariables().add("bonusScore");
        model.getVariables().add("bonusPointType");
        gameSrv.saveChallengeModel(GAME, model);

    }

    @Override
    public void defineExecData(List<ExecData> execList) {
        Map<String, Object> data = new HashMap<>();
        // data.put("walkDistance", 10d);
        // execList.add(new ExecData(GAME, ACTION, PLAYER_1, data));
        //
        // data = new HashMap<>();
        // data.put("walkDistance", 60d);
        // execList.add(new ExecData(GAME, ACTION, PLAYER_1, data));
        //
        // data = new HashMap<>();
        // data.put("walkDistance", 30d);
        // execList.add(new ExecData(GAME, ACTION, PLAYER_1, data));

        data = new HashMap<>();
        data.put("walkDistance", 1d);
        execList.add(new ExecData(GAME, ACTION, PLAYER_2, data));

        data = new HashMap<>();
        data.put("walkDistance", 2d);
        execList.add(new ExecData(GAME, ACTION, PLAYER_2, data));

        data = new HashMap<>();
        data.put("walkDistance", 3d);
        execList.add(new ExecData(GAME, ACTION, PLAYER_2, data));

    }

    @Override
    public void analyzeResult() {}

}
