package eu.trentorise.game.challenges;

public class ChallengeMaker {
	private ChallengeFactory chFactory;
	

	public static void main(String[] args) {
		ChallengeMaker cm = new ChallengeMaker();
		cm.chFactory = new ChallengeFactory();	
		
	}

}
