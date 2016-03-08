package eu.trentorise.game.challenges;

import java.util.HashMap;

import java.util.UUID;
import java.util.Map;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

// import eu.trentorise.game.model.Player;

public class PercentMobilityChallenge 
	extends Challenge {
	private String mode = null;
	private Double percent = 0.0;
	private Double baseline = 0.0;
	private Integer prize = null;
	private String pointType = null;
	
	public PercentMobilityChallenge() {
		generateChId();
		templateName = "MobilityPercentImproveTemplate.drt";
		type = ChallengeType.PERCENT;
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
		
		if (! tp.containsKey("percent"))
			throw new UndefinedChallengeException("undefined challenge!");
		this.percent = ((Double)tp.get("percent")).doubleValue();		
		if (! tp.containsKey("baseline"))
			throw new UndefinedChallengeException("undefined challenge!");
		this.baseline = ((Double)tp.get("baseline")).doubleValue();
		customData.put("ch_target", this.baseline * (1.0+this.percent));
		
		if (! tp.containsKey("bonus"))
			throw new UndefinedChallengeException("undefined challenge!");
		this.prize = ((Integer)tp.get("bonus")).intValue();
		customData.put("ch_" + this.chId + "_bonus", this.prize);
		
		customData.put("ch_" + this.chId + "_Km_traveled_during_challenge", new Double(0.0));		
	}

	@Override
	public void compileChallenge() throws UndefinedChallengeException{
		if (mode == null || prize == null || percent <= 0 || baseline <=0)
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
	 * TODO: this is a stub method to be replaced by an algo
	 * that filters over p
	 */
	private Collection<Object> selectPlayers(Collection<Object> p) {
		return p;	
	}
}
