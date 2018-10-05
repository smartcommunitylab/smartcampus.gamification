package eu.trentorise.game.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import eu.trentorise.game.model.ChallengeModel;
import eu.trentorise.game.model.GroupChallenge.PointConceptRef;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.Reward;
import eu.trentorise.game.model.core.GameConcept;

public class RewardAssignmentTest extends GameTest {

    private static final String GAME = "challengeTest";
    private static final String ACTION = "save_itinerary";

    private static final String DOMAIN = "my-domain";

    private static final String PLAYER_1 = "Alice";

    // TODO to remove extending GameTest with operation on ChallengeModels
    @Autowired
    private MongoTemplate mongo;

    @Override
    public void initEnv() {
    		List<GameConcept> concepts = new ArrayList<GameConcept>();
    		PointConcept p = new PointConcept("green leaves");
    		PointConcept p2 = new PointConcept("points");
    		
    		long dayDurationInMillis = 24 * 60 * 60 * 1000;
    	
    		Date todayDate = new Date(); // today
		Date tomorrowDate = new Date(); // today
		tomorrowDate.setTime(todayDate.getTime() + dayDurationInMillis);
		Date yesterdayDate = new Date(); // yesterday
		yesterdayDate.setTime(todayDate.getTime() - dayDurationInMillis);
		Date dayBeforeYesterdayDate = new Date(); // the day before yesterday
		dayBeforeYesterdayDate.setTime(yesterdayDate.getTime() - dayDurationInMillis);
		
		p.addPeriod("daily", dayBeforeYesterdayDate, dayDurationInMillis);
		p.increment(0d);
		p.addPeriod("daily", todayDate, dayDurationInMillis);
		p.increment(20d);
		
		//p.addPeriod("weekly", todayDate, dayDurationInMillis*7);
		//p.increment(20d); 
		
		concepts.add(p);
		
		/*p2.addPeriod("daily", dayBeforeYesterdayDate, dayDurationInMillis);
		p2.increment(30d);
		p2.addPeriod("daily", yesterdayDate, dayDurationInMillis);
		p2.increment(100d);
		p2.addPeriod("daily", todayDate, dayDurationInMillis);
		p2.increment(20d);*/
		
		p2.addPeriod("weekly", todayDate, dayDurationInMillis*7);
		p2.increment(150d); //100+20+30
		
		concepts.add(p2);
		
		savePlayerState(GAME, PLAYER_1, concepts);

    }

    @Override
    public void defineGame() {
        mongo.dropCollection(ChallengeModel.class);

        List<GameConcept> concepts = new ArrayList<GameConcept>();
        concepts.add(new PointConcept("green leaves"));

        defineGameHelper(DOMAIN, GAME, Arrays.asList(ACTION), concepts);

        try {
            loadClasspathRules(GAME, "rules/" + GAME);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void defineExecData(List<ExecData> execList) {
        Map<String, Object> data = new HashMap<>();
        Reward r = new Reward();
        data = new HashMap<>();
        
        r.setPercentage(50);
        r.setCalculationPointConcept(new PointConceptRef("points", "weekly"));
        r.setTargetPointConcept(new PointConceptRef("green leaves", "daily"));
        data.put("reward", r);
        
        execList.add(new ExecData(GAME, ACTION, PLAYER_1, data));

    }

    @Override
    public void analyzeResult() {
	    	assertionPoint(GAME, 150d, PLAYER_1, "points"); //do not change
	    	double newscore = 20 + (150 * 0.5); //increase current gl of the percentage
	    	assertionPoint(GAME, newscore, PLAYER_1, "green leaves"); //+50%
    }

}
