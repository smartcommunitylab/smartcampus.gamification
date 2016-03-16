package eu.trentorise.game.challenges.util;

import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.trentorise.game.challenges.rest.Content;
import eu.trentorise.game.challenges.rest.Paginator;

public class Matcher {

    private static final Logger logger = LogManager.getLogger(Matcher.class);
    private static final String[] operators = { "&&" };
    private ChallengeRuleRow challenge;
    private ScriptEngineManager manager;
    private ScriptEngine engine;

    public Matcher(ChallengeRuleRow challenge) {
	this.challenge = challenge;
	this.manager = new ScriptEngineManager();
	this.engine = manager.getEngineByName("nashorn");
    }

    public List<Content> match(Paginator input) {
	List<Content> result = new ArrayList<Content>();
	for (Content user : input.getContent()) {
	    if (challengeMatch(user)) {
		result.add(user);
	    }
	}
	return result;
    }

    private boolean challengeMatch(Content user) {
	if (challenge.getType().equals("PERCENT")) {
	    String criteria = challenge.getSelectionCriteria();
	    logger.debug("criteria to evaluate: " + criteria);
	    if (isUserValid(user, criteria)) {
		engine.put("walk_trips_past", user.getCustomData()
			.getAdditionalProperties().get("walk_trips_past"));
		engine.put("walk_km_past", user.getCustomData()
			.getAdditionalProperties().get("walk_km_past"));

		try {
		    Object result = engine.eval(criteria);
		    if (result instanceof Boolean) {
			return (Boolean) result;
		    }
		    return false;
		} catch (ScriptException e) {
		    logger.error(e.getMessage(), e);
		}
	    }
	}
	return false;
    }

    /**
     * @return true if user exist and contains all custom data mentioned in
     *         criteria
     */
    private boolean isUserValid(Content user, String criteria) {
	if (user != null && criteria != null && !criteria.isEmpty()
		&& user.getCustomData() != null
		&& user.getCustomData().getAdditionalProperties() != null
		&& !user.getCustomData().getAdditionalProperties().isEmpty()) {
	    if (containsAnyOperator(criteria)) {
		for (String operator : operators) {
		    String[] expressions = criteria.split(operator);
		    for (int i = 0; i < expressions.length; i++) {
			// expression in the form: var operator value
			String var = expressions[i].split(" ")[0];
			if (!user.getCustomData().getAdditionalProperties()
				.containsKey(var)) {
			    logger.warn("Custom data not found " + var);
			    return false;
			}
			break;

		    }
		}
		return true;
	    }
	    logger.warn("no operator found in criteria: " + criteria
		    + " for operators " + operators.toString());
	}
	logger.warn("user null or not custom data available");
	return false;
    }

    private boolean containsAnyOperator(String value) {
	for (String operator : operators) {
	    if (value.contains(operator)) {
		return true;
	    }
	}
	return false;
    }
}
