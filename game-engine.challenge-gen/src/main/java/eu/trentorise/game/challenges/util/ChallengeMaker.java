package eu.trentorise.game.challenges.util;

import eu.trentorise.game.challenges.ChallengeFactory;
import eu.trentorise.game.challenges.api.ChallengeFactoryInterface;


public class ChallengeMaker {
	private ChallengeFactoryInterface chFactory;
	

	public static void main(String[] args) {
		ChallengeMaker cm = new ChallengeMaker();
		cm.chFactory = new ChallengeFactory();	
		
	}

}
