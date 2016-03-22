package eu.trentorise.game.challenges.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Load challenges rule from and csv file
 *
 * {@link IOUtils}
 */
public final class ChallengeRulesLoader {

    private static final Logger logger = LogManager
	    .getLogger(ChallengeRulesLoader.class);

    private ChallengeRulesLoader() {
    }

    public static ChallengeRules load(String ref) throws IOException,
	    NullPointerException, IllegalArgumentException {
	if (ref == null) {
	    logger.error("Input file must be not null");
	    throw new NullPointerException("Input file must be not null");
	}
	if (!ref.endsWith(".csv")) {
	    logger.error("challenges rules file must be a csv file");
	    throw new IllegalArgumentException(
		    "challenges rules file must be a csv file");
	}
	try {
	    // open csv file
	    BufferedReader rdr = new BufferedReader(new StringReader(
		    IOUtils.toString(Thread.currentThread()
			    .getContextClassLoader().getResourceAsStream(ref))));
	    ChallengeRules response = new ChallengeRules();
	    boolean first = true;
	    for (String line = rdr.readLine(); line != null; line = rdr
		    .readLine()) {
		if (first) {
		    first = false;
		    continue;
		}
		String[] elements = line.split(";");
		ChallengeRuleRow crr = new ChallengeRuleRow();
		crr.setName(elements[0]);
		crr.setType(elements[1]);
		crr.setGoalType(elements[2]);
		crr.setTarget(Double.valueOf(elements[3]));
		crr.setBonus(Integer.valueOf(elements[4]));
		crr.setPointType(elements[5]);
		crr.setSelectionCriteria(elements[7]);
		response.getChallenges().add(crr);
	    }

	    logger.debug("Rows in file " + response.getChallenges().size());
	    return response;
	} catch (IOException e) {
	    logger.error(e.getMessage(), e);
	    return null;
	} catch (NumberFormatException e) {
	    logger.error(e.getMessage(), e);
	    return null;
	}
    }
}
