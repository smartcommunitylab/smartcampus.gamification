package eu.trentorise.game.challenges;

import java.util.HashMap;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import eu.trentorise.game.challenges.exception.UndefinedChallengeException;
import eu.trentorise.game.challenges.model.Challenge;
import eu.trentorise.game.challenges.model.ChallengeType;

// import eu.trentorise.game.model.Player;

public class TripNumberChallenge 
	extends Challenge {
	private int nTrips = 0;
	private String mode = null;
	private Integer prize = null;
	private String pointType = null;
	
	public TripNumberChallenge() {
		generateChId();
		templateName = "TravelModeTemplate.drt";
		type = ChallengeType.TRIPNUMBER;
		
	}
	
	@Override 
	public void setTemplateParams(HashMap<String, Object> tp) 
			throws UndefinedChallengeException {
		templateParams = new HashMap<String, Object>();
		templateParams.put ("ch_ID", this.chId);
		
		if (! tp.containsKey("mode"))
			throw new UndefinedChallengeException("undefined challenge!");
		this.mode = (String) tp.get("mode");
		templateParams.put("ch_mode", this.mode);
		
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
		
		customData.put("ch_" + this.chId + "_mode", this.mode);
		customData.put("ch_" + this.chId + "point_type", this.pointType);
		
		if (! tp.containsKey("trips"))
			throw new UndefinedChallengeException("undefined challenge!");
		this.nTrips = ((Integer)tp.get("trips")).intValue();
		customData.put("ch_" + this.chId + "_target", this.nTrips);

		if (!tp.containsKey("bonus"))
			throw new UndefinedChallengeException("undefined challenge!");
		this.prize = (Integer) tp.get("bonus");
		customData.put("ch_" + this.chId + "_bonus", this.prize);
		
		customData.put("ch_" + this.chId + "_counter", new Integer(0));
	}

	
	@Override
	public void compileChallenge() throws UndefinedChallengeException {
		if (nTrips <=0 || mode == null || prize == null)
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