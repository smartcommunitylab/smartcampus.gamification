package it.smartcommunitylab.gamification.log2elastic.analyzer;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import it.smartcommunitylab.gamification.log2elastic.Record;

public class RecordAnalyzerTest {

	@Test
	public void testAction() throws IOException, URISyntaxException {
		String recordInput = Files.asCharSource(
				new File(Thread.currentThread().getContextClassLoader().getResource("action-record-input").toURI()),
				Charsets.UTF_8).read();

		Map<String, String> dataExpected = new HashMap<>();

		dataExpected.put("logLevel", "INFO");
		dataExpected.put("gameId", "57ac710fd4c6ac7872b0e7a1");
		dataExpected.put("playerId", "24587");
		dataExpected.put("executionId", "0a2f204f-324f-4350-b02e-3765100ec936");
		dataExpected.put("executionTime", "1477087206137");
		dataExpected.put("timestamp", "1486292961614");
		dataExpected.put("eventType", "Action");
		dataExpected.put("actionName", "save_itinerary");

		Record record = new Record(recordInput);
		RecordAnalyzer analyzer = new ActionRecordAnalyzer(record);
		Map<String, String> data = analyzer.extractData();
		Assert.assertEquals(dataExpected, data);
	}

	@Test
	public void testPointConcept() throws IOException, URISyntaxException {
		String recordInput = Files.asCharSource(new File(
				Thread.currentThread().getContextClassLoader().getResource("pointConcept-record-input").toURI()),
				Charsets.UTF_8).read();

		Map<String, String> dataExpected = new HashMap<>();

		dataExpected.put("logLevel", "INFO");
		dataExpected.put("gameId", "57ac710fd4c6ac7872b0e7a1");
		dataExpected.put("playerId", "24642");
		dataExpected.put("executionId", "7e287737-ca7a-4d02-ad81-5295cc6a614c");
		dataExpected.put("executionTime", "1477087464452");
		dataExpected.put("timestamp", "1477087465426");
		dataExpected.put("eventType", "PointConcept");
		dataExpected.put("ruleName", "update walk counters");
		dataExpected.put("name", "Walk_Km");
		dataExpected.put("deltaScore", "1.953271623734048");
		dataExpected.put("score", "5.0684636993923196");

		Record record = new Record(recordInput);
		RecordAnalyzer analyzer = new PointConceptRecordAnalyzer(record);
		Map<String, String> data = analyzer.extractData();
		MatcherAssert.assertThat(dataExpected, Matchers.equalTo(data));
	}

	@Test
	public void testBadgeCollectionConcept() throws IOException, URISyntaxException {
		String recordInput = Files.asCharSource(new File(
				Thread.currentThread().getContextClassLoader().getResource("badgeCollection-record-input").toURI()),
				Charsets.UTF_8).read();

		Map<String, String> dataExpected = new HashMap<>();
		dataExpected.put("logLevel", "INFO");
		dataExpected.put("gameId", "57ac710fd4c6ac7872b0e7a1");
		dataExpected.put("playerId", "24233");
		dataExpected.put("executionId", "d1109b4b-2c4f-47f2-ac2b-c78571f01968");
		dataExpected.put("executionTime", "1477087206145");
		dataExpected.put("timestamp", "1477087216697");
		dataExpected.put("eventType", "BadgeCollectionConcept");
		dataExpected.put("ruleName", "3rd of the week");
		dataExpected.put("name", "leaderboard top 3");
		dataExpected.put("new_badge", "3rd_of_the_week");

		Record record = new Record(recordInput);
		RecordAnalyzer analyzer = new BadgeCollectionConceptRecordAnalyzer(record);
		Map<String, String> data = analyzer.extractData();
		MatcherAssert.assertThat(dataExpected, Matchers.equalTo(data));
	}

	@Test
	public void testClassification() throws IOException, URISyntaxException {
		String recordInput = Files.asCharSource(new File(
				Thread.currentThread().getContextClassLoader().getResource("classification-record-input").toURI()),
				Charsets.UTF_8).read();

		Map<String, String> dataExpected = new HashMap<>();
		dataExpected.put("logLevel", "INFO");
		dataExpected.put("gameId", "57ac710fd4c6ac7872b0e7a1");
		dataExpected.put("playerId", "24233");
		dataExpected.put("executionId", "d1109b4b-2c4f-47f2-ac2b-c78571f01968");
		dataExpected.put("executionTime", "1477087206145");
		dataExpected.put("timestamp", "1477087213870");
		dataExpected.put("eventType", "Classification");
		dataExpected.put("classificationName", "week classification green");
		dataExpected.put("classificationPosition", "3");

		Record record = new Record(recordInput);
		RecordAnalyzer analyzer = new ClassificationRecordAnalyzer(record);
		Map<String, String> data = analyzer.extractData();
		MatcherAssert.assertThat(dataExpected, Matchers.equalTo(data));
	}

	@Test
	public void testUserCreation() throws IOException, URISyntaxException {
		String recordInput = Files.asCharSource(new File(
				Thread.currentThread().getContextClassLoader().getResource("userCreation-record-input").toURI()),
				Charsets.UTF_8).read();
		Map<String, String> dataExpected = new HashMap<>();
		dataExpected.put("logLevel", "INFO");
		dataExpected.put("gameId", "57ac710fd4c6ac7872b0e7a1");
		dataExpected.put("playerId", "24643");
		dataExpected.put("executionId", "6bf3fa26-d34c-4e83-b372-e2155db1f1a1");
		dataExpected.put("executionTime", "1477140586036");
		dataExpected.put("timestamp", "1477140586036");
		dataExpected.put("eventType", "UserCreation");

		Record record = new Record(recordInput);
		RecordAnalyzer analyzer = new UserCreationRecordAnalyzer(record);
		Map<String, String> data = analyzer.extractData();
		MatcherAssert.assertThat(dataExpected, Matchers.equalTo(data));
	}

	@Test
	public void testChallengeCompleted() throws IOException, URISyntaxException {
		String recordInput = Files.asCharSource(new File(
				Thread.currentThread().getContextClassLoader().getResource("challengeCompleted-record-input").toURI()),
				Charsets.UTF_8).read();
		Map<String, String> dataExpected = new HashMap<>();
		dataExpected.put("logLevel", "INFO");
		dataExpected.put("gameId", "57ac710fd4c6ac7872b0e7a1");
		dataExpected.put("playerId", "24340");
		dataExpected.put("executionId", "4ec61032-b3eb-48d0-bd92-3bd51c71250c");
		dataExpected.put("executionTime", "1476269822849");
		dataExpected.put("timestamp", "1476269822849");
		dataExpected.put("eventType", "ChallengeCompleted");
		dataExpected.put("name", "w2_walk_percent_30_47411045-1cb4-4174-8552-60a6a31ebac4");

		Record record = new Record(recordInput);
		RecordAnalyzer analyzer = new ChallengeCompletedRecordAnalyzer(record);
		Map<String, String> data = analyzer.extractData();
		MatcherAssert.assertThat(dataExpected, Matchers.equalTo(data));
	}

	@Test
	public void testChallengeAssigned() throws IOException, URISyntaxException {
		String recordInput = Files.asCharSource(new File(
				Thread.currentThread().getContextClassLoader().getResource("challengeAssigned-record-input").toURI()),
				Charsets.UTF_8).read();
		Map<String, String> dataExpected = new HashMap<>();

		dataExpected.put("logLevel", "INFO");
		dataExpected.put("gameId", "57ac710fd4c6ac7872b0e7a1");
		dataExpected.put("playerId", "24340");
		dataExpected.put("executionId", "4ec61032-b3eb-48d0-bd92-3bd51c71250c");
		dataExpected.put("executionTime", "1475877600000");
		dataExpected.put("timestamp", "1475877600000");
		dataExpected.put("eventType", "ChallengeAssigned");
		dataExpected.put("name", "w2_walk_percent_30_47411045-1cb4-4174-8552-60a6a31ebac4");
		dataExpected.put("startDate", "1475877600000");
		dataExpected.put("endDate", "1476482400000");

		Record record = new Record(recordInput);
		RecordAnalyzer analyzer = new ChallengeAssignedRecordAnalyzer(record);
		Map<String, String> data = analyzer.extractData();
		MatcherAssert.assertThat(dataExpected, Matchers.equalTo(data));
	}

}
