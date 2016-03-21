package eu.trentorise.game.challenges.util;

import it.sayservice.platform.smartplanner.data.message.Itinerary;
import it.sayservice.platform.smartplanner.data.message.Leg;
import it.sayservice.platform.smartplanner.data.message.Position;
import it.sayservice.platform.smartplanner.data.message.StopId;
import it.sayservice.platform.smartplanner.data.message.TType;
import it.sayservice.platform.smartplanner.data.message.Transport;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Maps;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

public final class ConverterUtil {

    private static final Logger logger = LogManager.getLogger(Matcher.class);

    private ConverterUtil() {
    }

    public static Map<String, Object> convertFromSmartPlannerToPlayerGameState(
	    Itinerary itinerary) {
	return computeTripData(itinerary);
    }

    private static Map<String, Object> computeTripData(Itinerary itinerary) {
	Map<String, Object> data = Maps.newTreeMap();

	String parkName = null; // name of the parking
	boolean pnr = false; // (park-n-ride)
	boolean bikeSharing = false;
	double bikeDist = 0; // km
	double walkDist = 0; // km
	double trainDist = 0; // km
	double busDist = 0; // km
	double carDist = 0; // km

	logger.info("Analyzing itinerary for gamification.");
	if (itinerary != null) {
	    for (Leg leg : itinerary.getLeg()) {
		if (leg.getTransport().getType().equals(TType.CAR)) {
		    carDist += leg.getLength() / 1000;
		    if (leg.getTo().getStopId() != null) {
			pnr = true;
			parkName = leg.getTo().getStopId().getId();
		    }
		}
		if (leg.getTransport().getType().equals(TType.BICYCLE)) {
		    bikeDist += leg.getLength() / 1000;
		    if (leg.getTo().getStopId() != null) {
			bikeSharing = true;
		    }
		}
		if (leg.getTransport().getType().equals(TType.WALK)) {
		    walkDist += leg.getLength() / 1000;
		}
		if (leg.getTransport().getType().equals(TType.TRAIN)) {
		    trainDist += leg.getLength() / 1000;
		}
		if (leg.getTransport().getType().equals(TType.BUS)) {
		    busDist += leg.getLength() / 1000;
		}
	    }
	}
	logger.info("Analysis results:");
	logger.info("Distances [walk = " + walkDist + ", bike = " + bikeDist
		+ ", train = " + trainDist + ", bus = " + busDist + ", car = "
		+ carDist + "]");
	logger.info("Park and ride = " + pnr + " , Bikesharing = "
		+ bikeSharing);
	logger.info("Park = " + parkName);

	// old score
	// Long score = (long)((bikeDist + walkDist) * 5 + (busDist + trainDist)
	// + (itinerary.isPromoted() ? 5 : 0) + (pnr ? 10 : 0));

	Double score = 0.0;
	score += (walkDist < 0.1 ? 0 : Math.min(5, walkDist)) * 10;
	score += (bikeDist < 0.1 ? 0 : Math.min(10, bikeDist)) * 5;
	if (busDist > 0) {
	    score += ((busDist < 5) ? 15 : (busDist >= 5 && busDist < 10) ? 20
		    : 30);
	}
	if (trainDist > 0) {
	    score += ((trainDist > 0 && trainDist < 10) ? 10
		    : (trainDist >= 10 && trainDist < 20) ? 20 : 30);
	}
	// score += (itinerary.isPromoted() ? 5 : 0);

	if (bikeDist > 0)
	    data.put("bikeDistance", bikeDist);
	if (walkDist > 0)
	    data.put("walkDistance", walkDist);
	if (busDist > 0)
	    data.put("busDistance", busDist);
	if (trainDist > 0)
	    data.put("trainDistance", trainDist);
	if (carDist > 0)
	    data.put("carDistance", carDist);
	if (bikeSharing)
	    data.put("bikesharing", bikeSharing);
	if (parkName != null)
	    data.put("park", parkName);
	if (pnr)
	    data.put("p+r", pnr);
	// data.put("sustainable", itinerary.isPromoted());
	data.put("estimatedScore", score.longValue());

	return data;
    }

    public static JourneyData extractJourneyData(String line) {
	Object document = Configuration.defaultConfiguration().jsonProvider()
		.parse(line);
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
	String latlon = JsonPath.read(document, "$.data.data.from.lat") + ","
		+ JsonPath.read(document, "$.data.data.from.lon");
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

	Map<String, Object> data = ConverterUtil
		.convertFromSmartPlannerToPlayerGameState(it);

	JourneyData result = new JourneyData();
	result.setData(data);
	result.setUserId((String) JsonPath.read(document, "$.userId"));
	return result;
    }

    private static Position buildPosition(Map map) {
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

    private static StopId buildStopId(Map map) {
	if (map == null) {
	    return null;
	}
	StopId si = new StopId((String) map.get("agencyId"),
		(String) map.get("_id"));
	return si;
    }

    private static Transport buildTransport(Map map) {
	Transport t = new Transport(TType.getMode((String) map.get("type")),
		(String) map.get("agencyId"), (String) map.get("routeId"),
		(String) map.get("tripId"));
	return t;
    }
}
