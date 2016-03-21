package eu.trentorise.game.challenges;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import eu.trentorise.game.challenges.exception.UndefinedChallengeException;
import eu.trentorise.game.challenges.model.Challenge;
import eu.trentorise.game.challenges.model.ChallengeType;

public class BadgeCollectionCompletionChallenge extends Challenge {

    private String bCollection = null;
    private String finalBadge = null;
    private String pointType = null;
    private Integer prize = null;

    public BadgeCollectionCompletionChallenge() {
	generateChId();
	templateName = "BadgeCollectionCompletionTemplate.drt";
	type = ChallengeType.BADGECOLLECTION;
    }

    @Override
    public void setTemplateParams(Map<String, Object> tp)
	    throws UndefinedChallengeException {
	templateParams = new HashMap<String, Object>();
	templateParams.put("ch_ID", this.chId);

	if (!tp.containsKey("badge_collection"))
	    throw new UndefinedChallengeException("undefined challenge!");
	this.bCollection = (String) tp.get("badge_collection");
	templateParams.put("ch_badge_collection", this.bCollection);

	if (!tp.containsKey("point_type"))
	    throw new UndefinedChallengeException("undefined challenge!");
	this.pointType = (String) tp.get("point_type");
	templateParams.put("ch_point_type", this.pointType);

	setCustomData(tp);
    }

    @Override
    protected void setCustomData(Map<String, Object> tp)
	    throws UndefinedChallengeException {
	super.setCustomData(tp);

	customData.put("ch_" + this.chId + "point_type", this.pointType);

	if (!tp.containsKey("bonus"))
	    throw new UndefinedChallengeException("undefined challenge!");
	this.prize = ((Integer) tp.get("bonus")).intValue();
	customData.put("ch_" + this.chId + "_bonus", this.prize);

	if (!tp.containsKey("badge"))
	    throw new UndefinedChallengeException("undefined challenge!");
	this.finalBadge = (String) tp.get("badge");
	customData.put("ch_" + this.chId + "_target", this.finalBadge);

    }

    @Override
    public void compileChallenge(String playerId)
	    throws UndefinedChallengeException {
	if (bCollection == null || finalBadge == null || prize == null)
	    throw new UndefinedChallengeException("undefined challenge!");

	templateParams.put("ch_player", playerId);
	try {
	    generatedRules += generateRules(locateTemplate());
	} catch (IOException ioe) {
	    throw new UndefinedChallengeException(
		    "challenge cannot be compiled for user " + playerId);
	}
    }

}
