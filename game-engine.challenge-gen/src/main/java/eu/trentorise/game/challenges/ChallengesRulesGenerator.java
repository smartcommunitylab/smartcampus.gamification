package eu.trentorise.game.challenges;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.trentorise.game.challenges.api.ChallengeFactoryInterface;
import eu.trentorise.game.challenges.exception.UndefinedChallengeException;
import eu.trentorise.game.challenges.model.Challenge;
import eu.trentorise.game.challenges.model.ChallengeType;
import eu.trentorise.game.challenges.rest.Content;
import eu.trentorise.game.challenges.util.ChallengeRuleRow;

/**
 * Generate rules for challenges
 */
public class ChallengesRulesGenerator {

    private StringBuffer buffer;
    private ChallengeFactoryInterface factory;

    public ChallengesRulesGenerator(ChallengeFactoryInterface factory) {
	this.buffer = new StringBuffer();
	this.factory = factory;
    }

    /**
     * Generate rules starting from a challenge specification for a set of given
     * users
     * 
     * @param challengeSpec
     * @param users
     * @return
     * @throws UndefinedChallengeException
     */
    public String generateRules(ChallengeRuleRow challengeSpec,
	    List<Content> users) throws UndefinedChallengeException {
	Map<String, Object> params = new HashMap<String, Object>();
	buffer = new StringBuffer();
	buffer.append("/** " + challengeSpec.getType() + " "
		+ challengeSpec.getTarget().toString() + " **/\n");

	// get right challenge
	for (Content user : users) {
	    Challenge c = factory.createChallenge(ChallengeType
		    .valueOf(challengeSpec.getType()));
	    params = new HashMap<String, Object>();
	    if (challengeSpec.getTarget() instanceof Double) {
		params.put("percent", challengeSpec.getTarget());
	    }
	    params.put("mode", challengeSpec.getGoalType());
	    params.put("bonus", challengeSpec.getBonus());
	    params.put("point_type", challengeSpec.getPointType());
	    params.put("baseline", user.getCustomData()
		    .getAdditionalProperties().get("walk_km_past"));
	    c.setTemplateParams(params);
	    c.compileChallenge(user.getPlayerId());
	    buffer.append(c.getGeneratedRules());
	}
	// remove package declaration after first
	// TODO: we have to find a better way to fix this
	String temp = buffer.toString();
	buffer = new StringBuffer();
	boolean remove = false;
	try {
	    BufferedReader rdr = new BufferedReader(new StringReader(temp));
	    for (String line = rdr.readLine(); line != null; line = rdr
		    .readLine()) {
		if (line.startsWith("package") && !remove) {
		    remove = true;
		    buffer.append(line).append(
			    System.getProperty("line.separator"));

		} else if (line.startsWith("package") && remove) {
		    // do nothing
		    System.out.println();
		} else {
		    buffer.append(line).append(
			    System.getProperty("line.separator"));

		}
	    }
	    rdr.close();
	} catch (IOException e) {
	    // TODO log
	}
	// lines now contains all the strings between line breaks
	return buffer.toString();
    }
}
