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
import eu.trentorise.game.model.Reward;
import eu.trentorise.game.model.GroupChallenge.PointConceptRef;
import eu.trentorise.game.model.core.ChallengeAssignment;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.services.PlayerService;
import eu.trentorise.game.test.GameTest.ExecData;

public class MPIncentiveChallengeGameTest extends GameTest {

    private static final String GAME = "incentive";
    private static final String ACTION = "play";

    private static final String DOMAIN = "my-domain";

    private static final String PLAYER_1 = "rick grames";
    private static final String PLAYER_2 = "ricky";

    @Autowired
    private GameService gameSrv;

    @Autowired
    private PlayerService playerSrv;

    @Autowired
    private MongoTemplate mongo;

    @Override
    public void initEnv() {

        savePlayerState(GAME, PLAYER_1, new ArrayList<GameConcept>());
        savePlayerState(GAME, PLAYER_2, new ArrayList<GameConcept>());

        // assign a challenge to PLAYER_1
        LocalDate today = new LocalDate();
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("bonusScore", 250d);
        data.put("bonusPointType", "green leaves");
        playerSrv.assignChallenge(GAME, PLAYER_1,
                new ChallengeAssignment("incentiveGroupChallengeReward", 
                		"specialInstance", 
                		data, 
                		null, 
                		today.toDate(),
                		today.dayOfMonth().addToCopy(1).toDate()));
        
        // assign a challenge to PLAYER_2
        today = new LocalDate();
        data = new HashMap<String, Object>();
        data.put("bonusScore", 250d);
        data.put("bonusPointType", "green leaves");
        playerSrv.assignChallenge(GAME, PLAYER_2,
                new ChallengeAssignment("incentiveGroupChallengeReward", 
                		"specialInstance", 
                		data, 
                		null, 
                		today.toDate(),
                		today.dayOfMonth().addToCopy(1).toDate()));
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
        model.setName("incentiveGroupChallengeReward");
        model.setVariables(new HashSet<String>());
        model.getVariables().add("bonusScore");
        model.getVariables().add("bonusPointType");
        gameSrv.saveChallengeModel(GAME, model);

        gameSrv.saveChallengeModel(GAME, model);

    }

    @Override
    public void defineExecData(List<ExecData> execList) {
    	
    		List<Object> list = new ArrayList<>();
        Reward r = new Reward(); // with threshold, and threshold isn't applied
        
        r.setPercentage(50);
        r.setThreshold(250);
        r.setCalculationPointConcept(new PointConceptRef("green leaves", "weekly"));
        r.setTargetPointConcept(new PointConceptRef("green leaves", "daily"));
        list.add(r);
        execList.add(new ExecData(GAME, ACTION, PLAYER_1, new HashMap<>(), list));
       
    }

    @Override
    public void analyzeResult() {
    		assertionPoint(GAME, 250d, PLAYER_1, "green leaves");
    		assertionPoint(GAME, 0d, PLAYER_2, "green leaves");
    }

}
