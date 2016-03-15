package eu.trentorise.challenge.test;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

import eu.trentorise.game.challenges.exception.UndefinedChallengeException;
import eu.trentorise.game.challenges.model.Challenge;
import eu.trentorise.game.challenges.model.ChallengeType;
import eu.trentorise.game.challenges.util.ChallengeFactory;

public class FactoryTest {
	private ChallengeFactory chFactory;
	
	@Test
	public void buildChallenges() {
		// simulates Factory client
		chFactory = new ChallengeFactory();
		Challenge c = null;
		
		//PERCENT Challenge building
		HashMap<String, Object> params = new HashMap<String, Object>();
		try {
				c = chFactory.createChallenge(ChallengeType.PERCENT);
				params = new HashMap<String, Object>();
				params.put("percent", new Double(15));
				params.put("mode", "walkDistance");
				params.put("bonus", new Integer(50));
				params.put("point_type", "green leaves");
				params.put("baseline", new Double(100.0));
				c.setTemplateParams(params);
				c.compileChallenge();
		} catch (UndefinedChallengeException uce) {
			uce.printStackTrace();
		}
		Assert.assertTrue("Challenge " + ChallengeType.PERCENT + " created", c != null);
		Assert.assertTrue(c.getType().equals(ChallengeType.PERCENT));
		Assert.assertTrue(c.getGeneratedRules() !=  null && !c.getGeneratedRules().equals(""));
		System.out.println(c.getGeneratedRules()+ "\n\n");

		
		//TRIPNUMBER Challenge building
		try {
			c = chFactory.createChallenge(ChallengeType.TRIPNUMBER);
			params = new HashMap<String, Object>();
			params.put("trips", new Integer(1));
			params.put("mode", "bikesharing");
			params.put("bonus", new Integer(50));
			params.put("point_type", "green leaves");
			c.setTemplateParams(params);
			c.compileChallenge();
		} catch (UndefinedChallengeException uce) {
			// TODO Auto-generated catch block
			uce.printStackTrace();
		}
		Assert.assertTrue("Challenge " + ChallengeType.TRIPNUMBER + " created", c != null);
		Assert.assertTrue(c.getType().equals(ChallengeType.TRIPNUMBER));
		Assert.assertTrue(c.getGeneratedRules() !=  null && !c.getGeneratedRules().equals(""));
		System.out.println(c.getGeneratedRules() + "\n\n");
		
		//BADGE COLLECTION COMPLETION Challenge building
			try {
				c = chFactory.createChallenge(ChallengeType.BADGECOLLECTION);
				params = new HashMap<String, Object>();
				params.put("badge", "badge_foo");
				params.put("badge_collection", "green leaves");
				params.put("bonus", new Integer(50));
				params.put("point_type", "green leaves");
				c.setTemplateParams(params);
				c.compileChallenge();
			} catch (UndefinedChallengeException uce) {
				// TODO Auto-generated catch block
				uce.printStackTrace();
			}
			Assert.assertTrue("Challenge " + ChallengeType.BADGECOLLECTION + " created", c != null);
			Assert.assertTrue(c.getType().equals(ChallengeType.BADGECOLLECTION));
			Assert.assertTrue(c.getGeneratedRules() !=  null && !c.getGeneratedRules().equals(""));
			System.out.println(c.getGeneratedRules() + "\n\n");		

			//GAME RECOMMENDATION Challenge building
			try {
				c = chFactory.createChallenge(ChallengeType.RECOMMENDATION);
				params = new HashMap<String, Object>();
				params.put("recommendations", new Integer(10));
				params.put("point_type", "green leaves");
				params.put("bonus", new Integer(50));
				c.setTemplateParams(params);
				c.compileChallenge();
			} catch (UndefinedChallengeException uce) {
				// TODO Auto-generated catch block
				uce.printStackTrace();
			}
			Assert.assertTrue("Challenge " + ChallengeType.RECOMMENDATION + " created", c != null);
			Assert.assertTrue(c.getType().equals(ChallengeType.RECOMMENDATION));
			Assert.assertTrue(c.getGeneratedRules() !=  null && !c.getGeneratedRules().equals(""));
			System.out.println(c.getGeneratedRules() + "\n\n");	
	}
}

	
	
	
