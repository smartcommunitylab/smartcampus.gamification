package eu.trentorise.challenge.util;

import static org.junit.Assert.assertTrue;
import it.sayservice.platform.smartplanner.data.message.Itinerary;
import it.sayservice.platform.smartplanner.data.message.Leg;
import it.sayservice.platform.smartplanner.data.message.Position;
import it.sayservice.platform.smartplanner.data.message.StopId;
import it.sayservice.platform.smartplanner.data.message.TType;
import it.sayservice.platform.smartplanner.data.message.Transport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

import eu.trentorise.game.challenges.util.ChallengeRules;
import eu.trentorise.game.challenges.util.ChallengeRulesLoader;
import eu.trentorise.game.challenges.util.ConverterUtil;

public class UtilTest {

    @Test(expected = NullPointerException.class)
    public void loadNullTest() throws NullPointerException,
	    IllegalArgumentException, IOException {
	ChallengeRulesLoader.load(null);
    }

    @Test
    public void loadChallengesRulesTest() throws IOException {
	ChallengeRules result = ChallengeRulesLoader
		.load("challengesRules.xls");

	assertTrue(!result.getChallenges().isEmpty());
    }

    @Test
    public void readUserData() throws IOException {
	String ref = "savedtrips1.json";
	// read all lines from file
	List<String> lines = IOUtils.readLines(Thread.currentThread()
		.getContextClassLoader().getResourceAsStream(ref));

	assertTrue(lines != null);

	for (String line : lines) {

	    Object document = Configuration.defaultConfiguration()
		    .jsonProvider().parse(line);
	    List<LinkedHashMap> temp = JsonPath.parse(document).read(
		    "$.data.data.leg");
	    List<Leg> legs = new ArrayList<Leg>();
	    for (LinkedHashMap map : temp) {
		Leg leg = new Leg();
		Transport transport = buildTransport((Map) map.get("transport"));
		leg.setTransport(transport);
		Position to = buildPosition((Map) map.get("to"));
		leg.setTo(to);
		leg.setLength((Double) map.get("length"));
		legs.add(leg);
	    }
	    Integer duration = JsonPath.read(document, "$.data.data.duration");
	    Long endtime = JsonPath.read(document, "$.data.data.endtime");
	    String latlon = JsonPath.read(document, "$.data.data.from.lat")
		    + "," + JsonPath.read(document, "$.data.data.from.lon");
	    Position from = new Position(latlon);
	    latlon = JsonPath.read(document, "$.data.data.to.lat") + ","
		    + JsonPath.read(document, "$.data.data.to.lon");
	    Integer walkingDuration = JsonPath.read(document,
		    "$.data.data.walkingDuration");
	    Position to = new Position(latlon);
	    // set data
	    Itinerary it = new Itinerary();
	    it.setLeg(legs);
	    it.setDuration(duration);
	    it.setEndtime(endtime);
	    it.setFrom(from);
	    it.setTo(to);
	    it.setWalkingDuration(walkingDuration);

	    Map<String, Object> output = ConverterUtil
		    .convertFromSmartPlannerToPlayerGameState(it);
	}
    }

    private Position buildPosition(Map map) {
	if (map == null) {
	    return null;
	}
	StopId stopId = buildStopId((Map) map.get("stopId"));
	String stopCode = (String) map.get("stopCode");
	String lon = (String) map.get("lon");
	String lat = (String) map.get("lat");
	Position p = new Position((String) map.get("name"), stopId, stopCode,
		lon, lat);
	return p;
    }

    private StopId buildStopId(Map map) {
	if (map == null) {
	    return null;
	}
	StopId si = new StopId((String) map.get("agencyId"),
		(String) map.get("_id"));
	return si;
    }

    private Transport buildTransport(Map map) {
	Transport t = new Transport(TType.getMode((String) map.get("type")),
		(String) map.get("agencyId"), (String) map.get("routeId"),
		(String) map.get("tripId"));
	return t;
    }
}
