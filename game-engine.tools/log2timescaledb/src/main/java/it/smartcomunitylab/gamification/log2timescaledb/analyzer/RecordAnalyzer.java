package it.smartcomunitylab.gamification.log2timescaledb.analyzer;

import java.util.Map;

public interface RecordAnalyzer {

	public Map<String, String> extractData();

	public String[] getNomiCampi();

}
