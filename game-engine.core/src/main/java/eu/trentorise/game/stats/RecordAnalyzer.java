package eu.trentorise.game.stats;

import java.util.Map;

public interface RecordAnalyzer {

	public Map<String, String> extractData();

	public String[] getNomiCampi();

}
