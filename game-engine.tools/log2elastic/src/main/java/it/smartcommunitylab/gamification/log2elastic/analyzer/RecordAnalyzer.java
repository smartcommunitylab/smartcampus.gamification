package it.smartcommunitylab.gamification.log2elastic.analyzer;

import java.util.Map;

public interface RecordAnalyzer {

	public Map<String, String> extractData();

	public String[] getNomiCampi();

}
