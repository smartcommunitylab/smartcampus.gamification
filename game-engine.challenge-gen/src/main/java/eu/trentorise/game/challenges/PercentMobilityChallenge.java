package eu.trentorise.game.challenges;

import java.io.IOException;
import java.util.HashMap;

import eu.trentorise.game.challenges.exception.UndefinedChallengeException;
import eu.trentorise.game.challenges.model.Challenge;
import eu.trentorise.game.challenges.model.ChallengeType;

// import eu.trentorise.game.model.Player;

public class PercentMobilityChallenge extends Challenge {
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
	templateParams.put(Constants.CH_ID, this.chId);

	if (!tp.containsKey(Constants.MODE))
	    throw new UndefinedChallengeException("undefined challenge!");
	this.mode = (String) tp.get(Constants.MODE);
	templateParams.put(Constants.CH_MODE, this.mode);

	if (!tp.containsKey(Constants.POINT_TYPE))
	    throw new UndefinedChallengeException("undefined challenge!");
	this.pointType = (String) tp.get(Constants.POINT_TYPE);
	templateParams.put(Constants.CH_POINT_TYPE, this.pointType);

	setCustomData(tp);
    }

    @Override
    protected void setCustomData(HashMap<String, Object> tp)
	    throws UndefinedChallengeException {
	super.setCustomData(tp);

	customData.put(Constants.CH + this.chId + "_mode", this.mode);
	customData.put(Constants.CH + this.chId + "point_type", this.pointType);

	if (!tp.containsKey("percent"))
	    throw new UndefinedChallengeException("undefined challenge!");
	this.percent = ((Double) tp.get("percent")).doubleValue();
	if (!tp.containsKey("baseline"))
	    throw new UndefinedChallengeException("undefined challenge!");
	this.baseline = ((Double) tp.get("baseline")).doubleValue();
	customData.put("ch_target", this.baseline * (1.0 + this.percent));

	if (!tp.containsKey("bonus"))
	    throw new UndefinedChallengeException("undefined challenge!");
	this.prize = ((Integer) tp.get("bonus")).intValue();
	customData.put(Constants.CH + this.chId + "_bonus", this.prize);

	customData.put(Constants.CH + this.chId
		+ "_Km_traveled_during_challenge", new Double(0.0));
    }

    @Override
    public void compileChallenge(String playerId)
	    throws UndefinedChallengeException {
	if (mode == null || prize == null || percent <= 0 || baseline <= 0)
	    throw new UndefinedChallengeException("undefined challenge!");

	templateParams.put("ch_player", playerId);
	try {
	    generatedRules += generateRules(locateTemplate());
	} catch (IOException ioe) {
	    throw new UndefinedChallengeException(
		    "challenge cannot be compiled for user " + playerId);
	}
	return;
    }
}
