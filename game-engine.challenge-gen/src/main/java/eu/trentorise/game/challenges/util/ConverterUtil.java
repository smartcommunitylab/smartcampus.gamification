package eu.trentorise.game.challenges.util;

import it.sayservice.platform.smartplanner.data.message.Itinerary;
import it.sayservice.platform.smartplanner.data.message.Leg;
import it.sayservice.platform.smartplanner.data.message.TType;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Maps;

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

}
