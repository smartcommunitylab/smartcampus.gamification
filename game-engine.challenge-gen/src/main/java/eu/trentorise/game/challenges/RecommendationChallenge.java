package eu.trentorise.game.challenges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import eu.trentorise.game.challenges.exception.UndefinedChallengeException;
import eu.trentorise.game.challenges.model.Challenge;
import eu.trentorise.game.challenges.model.ChallengeType;

public class RecommendationChallenge extends Challenge {
	private Integer prize = null;
	private String pointType = null;
	private int recommendations = 0;

	public RecommendationChallenge() {
		generateChId();
		templateName = "GameRecommendationTemplate.drt";
		type = ChallengeType.RECOMMENDATION;
	}

	@Override
	public void setTemplateParams(HashMap<String, Object> tp) throws UndefinedChallengeException {
		templateParams = new HashMap<String, Object>();
		templateParams.put ("ch_ID", this.chId);
		
		if (! tp.containsKey("point_type"))
			throw new UndefinedChallengeException("undefined challenge!");
		this.pointType = (String) tp.get("point_type");
		templateParams.put("ch_point_type", this.pointType);
		
		setCustomData(tp);
	}
	
	@Override
	protected void setCustomData (HashMap<String, Object> tp)
			throws UndefinedChallengeException {
			super.setCustomData(tp);
			
			customData.put("ch_" + this.chId + "point_type", this.pointType);
			
			if (!tp.containsKey("bonus"))
				throw new UndefinedChallengeException("undefined challenge!");
			this.prize = (Integer) tp.get("bonus");
			customData.put("ch_" + this.chId + "_bonus", this.prize);
			
			if (! tp.containsKey("recommendations"))
				throw new UndefinedChallengeException("undefined challenge!");
			this.recommendations = ((Integer)tp.get("recommendations")).intValue();
			customData.put("ch_" + this.chId + "_target", this.recommendations);
			
			customData.put("ch_" + this.chId + "_recommendations_sent_during_challenges", new Integer(0));
	}
	@Override
	public void compileChallenge()
			throws UndefinedChallengeException {
		if (recommendations <=0 || prize == null)
			throw new UndefinedChallengeException("undefined challenge!");
		
		// here find the players affected by this one challenge
		players = selectPlayers(getAllPlayers());
		if (players.isEmpty())
				return;						
				
		for (Object o : players) {
			templateParams.put("ch_player", o.toString());
			try {
				generatedRules += generateRules(locateTemplate());
			} catch (IOException ioe) {
				throw new UndefinedChallengeException("challenge cannot be compiled for user " + o.toString());
			}
		return;
		}
	}
	
	/*
	 * TODO: this is a stub method to be replaced with a query to GE
	 * 
	 */
	private Collection<Object> getAllPlayers() {
		ArrayList<Object> p = new ArrayList<Object>();
		p.add(new Object());
		return p;
	}
	
	/*
	 * TODO: this is a stub method to be replaced by an algorithm
	 * that filters over p
	 */
	private Collection<Object> selectPlayers(Collection<Object> p) {
		return p;	
	}

	
}
