package eu.trentorise.game.challenges.model;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.UUID;

import org.drools.template.ObjectDataCompiler;

import eu.trentorise.game.challenges.Constants;
import eu.trentorise.game.challenges.exception.UndefinedChallengeException;

public abstract class Challenge {

    protected String drlName;

    protected int difficulty = 0;
    protected UUID chId;
    protected ChallengeType type;
    protected String templateName;
    protected Collection<Object> players = null;

    protected HashMap<String, Object> templateParams = null;
    protected HashMap<String, Object> customData = null;
    protected String generatedRules = "";

    public abstract void compileChallenge(String playerId)
	    throws UndefinedChallengeException;

    public abstract void setTemplateParams(HashMap<String, Object> tp)
	    throws UndefinedChallengeException;

    protected void setCustomData(HashMap<String, Object> tp)
	    throws UndefinedChallengeException {
	customData = new HashMap<String, Object>();
	customData.put(Constants.CH + this.chId + Constants.TYPE, this.type);
	// add beginning and end of challenge
	Calendar calendar = new GregorianCalendar();
	calendar.add(Calendar.DAY_OF_MONTH, 1); // tomorrow
	customData.put(Constants.CH + this.chId + Constants.START_CHTS,
		calendar.getTimeInMillis());
	calendar.add(Calendar.DAY_OF_MONTH, 7); // tomorrow + 1 week
	customData.put(Constants.CH + this.chId + Constants.END_CHTS,
		calendar.getTimeInMillis());
	customData.put(Constants.CH + this.chId + Constants.SUCCESS, false);
    }

    protected final void generateChId() {
	this.chId = UUID.randomUUID();
    }

    protected String generateRules(String template) throws IOException {
	ObjectDataCompiler compiler = new ObjectDataCompiler();
	return compiler.compile(Arrays.asList(templateParams),
		Thread.currentThread().getContextClassLoader()
			.getResourceAsStream(template));
    }

    public UUID getChId() {
	return chId;
    }

    public ChallengeType getType() {
	return type;
    }

    public String getGeneratedRules() {
	return generatedRules;
    }

    public HashMap<String, Object> getTemplateParams() {
	return templateParams;
    }

    /*
     * from the template file name in templateName compute the path of the
     * actual .drt template file to be used TODO: implement a general way to
     * discover template resource
     */
    protected String locateTemplate() {
	String ret = "rules/templates/" + templateName;
	System.out.println(ret);
	return ret;
    }
}