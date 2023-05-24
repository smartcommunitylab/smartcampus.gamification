package eu.trentorise.game.repo;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.autoconfig.brave.BraveAutoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.trentorise.game.config.AppConfig;
import eu.trentorise.game.config.MongoConfig;
import eu.trentorise.game.config.RabbitConf;
import eu.trentorise.game.model.ChallengeConcept;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, MongoConfig.class, RabbitConf.class, BraveAutoConfiguration.class }, loader = AnnotationConfigContextLoader.class)
public class ChallengeConceptRepoTest {

	private static final String GAME = "TESTREPO";
	private static final String PLAYER = "1000";
	@Autowired
	private ChallengeConceptRepo challengeConceptRepo;
	@Autowired
	private MongoTemplate mongo;

	@Before
	public void cleanDB() {
		mongo.getDb().drop();
	}

	private void setupEnv() {
		String challengeConcept1 = "{\r\n" + "	\"gameId\": \"TESTDOC\",\r\n" + "	\"playerId\": \"1000\",\r\n"
				+ "	\"obj\": {\r\n" + "		\"name\": \"w90_rs__Bike_Km_6868e0c1-6d16-4bfe-814c-c7fb7dc15745\",\r\n"
				+ "		\"modelName\": \"percentageIncrement\",\r\n" + "		\"fields\": {\r\n"
				+ "			\"difficulty\": 2,\r\n" + "			\"bonusScore\": 190,\r\n"
				+ "			\"wi\": 3.20781253697311,\r\n" + "			\"percentage\": 0.320781253697311,\r\n"
				+ "			\"bonusPointType\": \"green leaves\",\r\n" + "			\"periodName\": \"weekly\",\r\n"
				+ "			\"baseline\": 28.0137228601819,\r\n" + "			\"counterName\": \"Bike_Km\",\r\n"
				+ "			\"target\": 37.0\r\n" + "		},\r\n" + "		\"start\": 1595023200000,\r\n"
				+ "		\"end\": 1595628000000,\r\n" + "		\"completed\": false,\r\n"
				+ "		\"state\": \"FAILED\",\r\n" + "		\"stateDate\": {\r\n"
				+ "			\"PROPOSED\": 1594801699210,\r\n" + "			\"ASSIGNED\": 1594805619558,\r\n"
				+ "			\"ACTIVE\": 1595491607761,\r\n" + "			\"FAILED\": 1595628000000\r\n" + "		},\r\n"
				+ "		\"origin\": \"rs\",\r\n" + "		\"priority\": 2,\r\n" + "		\"forced\": false,\r\n"
				+ "		\"visibility\": {\r\n" + "			\"hidden\": false,\r\n"
				+ "			\"disclosureDate\": null\r\n" + "		}\r\n" + "	}\r\n" + "}\r\n" + "";

		String challengeConcept2 = "{\r\n" + "        \"gameId\": \"TESTDOC\",\r\n"
				+ "     	\"playerId\": \"1000\",\r\n" + "		\"obj\" : {\r\n"
				+ "            \"name\" : \"w91_rs__Walk_Km_489fefbb-01dd-486f-984c-26530d04f5f6\",\r\n"
				+ "            \"modelName\" : \"percentageIncrement\",\r\n" + "            \"fields\" : {\r\n"
				+ "                \"difficulty\" : 1,\r\n" + "                \"bonusScore\" : 170,\r\n"
				+ "                \"wi\" : 3.35201691089774,\r\n"
				+ "                \"percentage\" : 0.335201691089774,\r\n"
				+ "                \"bonusPointType\" : \"green leaves\",\r\n"
				+ "                \"periodName\" : \"weekly\",\r\n"
				+ "                \"baseline\" : 2.99580207746386,\r\n"
				+ "                \"counterName\" : \"Walk_Km\",\r\n" + "                \"target\" : 4.0\r\n"
				+ "            },\r\n" + "            \"start\" : 1595628000000,\r\n"
				+ "            \"end\" : 1596232800000,\r\n" + "            \"completed\" : false,\r\n"
				+ "            \"state\" : \"FAILED\",\r\n" + "            \"stateDate\" : {\r\n"
				+ "                \"PROPOSED\" : 1595346001291,\r\n"
				+ "                \"ASSIGNED\" : 1595584821195,\r\n"
				+ "                \"ACTIVE\" : 1596024311000,\r\n" + "                \"FAILED\" : 1596232800000\r\n"
				+ "            },\r\n" + "            \"origin\" : \"rs\",\r\n" + "            \"priority\" : 2,\r\n"
				+ "            \"forced\" : true,\r\n" + "            \"visibility\" : {\r\n"
				+ "                \"hidden\" : false,\r\n" + "                \"disclosureDate\" : null\r\n"
				+ "            }\r\n" + "        }\r\n" + "}";
		String challengeConcept3 = "{\r\n" + "	\"gameId\": \"TESTDOC\",\r\n" + "	\"playerId\": \"1000\",\r\n"
				+ "	\"obj\": {\r\n"
				+ "		\"name\": \"w90_rs__green leaves_fb95030f-1c8c-496a-b75a-d787b666ee7e\",\r\n"
				+ "		\"modelName\": \"repetitiveBehaviour\",\r\n" + "		\"fields\": {\r\n"
				+ "			\"bonusScore\": 100.0,\r\n" + "			\"bonusPointType\": \"green leaves\",\r\n"
				+ "			\"periodName\": \"daily\",\r\n" + "			\"counterName\": \"green leaves\",\r\n"
				+ "			\"periodTarget\": 2.0,\r\n" + "			\"target\": 30.0\r\n" + "		},\r\n"
				+ "		\"start\": 1595023200000,\r\n" + "		\"end\": 1595628000000,\r\n"
				+ "		\"completed\": false,\r\n" + "		\"state\": \"FAILED\",\r\n" + "		\"stateDate\": {\r\n"
				+ "			\"ASSIGNED\": 1594801698905,\r\n" + "			\"ACTIVE\": 1595491607761,\r\n"
				+ "			\"FAILED\": 1595628000000\r\n" + "		},\r\n" + "		\"origin\": \"rs\",\r\n"
				+ "		\"priority\": 0,\r\n" + "		\"forced\": false,\r\n" + "		\"visibility\": {\r\n"
				+ "			\"hidden\": false,\r\n" + "			\"disclosureDate\": null\r\n" + "		}\r\n"
				+ "	}\r\n" + "}";

		mongo.getCollection("ChallengeConcept").insertOne(Document.parse(challengeConcept1));
		mongo.getCollection("ChallengeConcept").insertOne(Document.parse(challengeConcept2));
		mongo.getCollection("ChallengeConcept").insertOne(Document.parse(challengeConcept3));
	}

	@Test
	public void searchChallengeConcept() {
		setupEnv();
		Assert.assertEquals(3, mongo.getCollection("ChallengeConcept").countDocuments());
	}

	@Test
	public void searchChallengeConceptByRepo() {
		try {
			setupRepo();
			List<ChallengeConceptPersistence> result = challengeConceptRepo.findByGameIdAndPlayerId(GAME, PLAYER);
			Assert.assertEquals(3, result.size());
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}

	private void setupRepo() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		String cc1ObjString = "{\r\n" + "	\"name\": \"w90_rs__Bike_Km_6868e0c1-6d16-4bfe-814c-c7fb7dc15745\",\r\n"
				+ "	\"modelName\": \"percentageIncrement\",\r\n" + "	\"fields\": {\r\n"
				+ "		\"difficulty\": 2,\r\n" + "		\"bonusScore\": 190,\r\n"
				+ "		\"wi\": 3.20781253697311,\r\n" + "		\"percentage\": 0.320781253697311,\r\n"
				+ "		\"bonusPointType\": \"green leaves\",\r\n" + "		\"periodName\": \"weekly\",\r\n"
				+ "		\"baseline\": 28.0137228601819,\r\n" + "		\"counterName\": \"Bike_Km\",\r\n"
				+ "		\"target\": 37.0\r\n" + "	},\r\n" + "	\"start\": 1595023200000,\r\n"
				+ "	\"end\": 1595628000000,\r\n" + "	\"completed\": false,\r\n" + "	\"state\": \"FAILED\",\r\n"
				+ "	\"stateDate\": {\r\n" + "		\"PROPOSED\": 1594801699210,\r\n"
				+ "		\"ASSIGNED\": 1594805619558,\r\n" + "		\"ACTIVE\": 1595491607761,\r\n"
				+ "		\"FAILED\": 1595628000000\r\n" + "	},\r\n" + "	\"origin\": \"rs\",\r\n"
				+ "	\"priority\": 2,\r\n" + "	\"forced\": false,\r\n" + "	\"visibility\": {\r\n"
				+ "		\"hidden\": false,\r\n" + "		\"disclosureDate\": null\r\n" + "	}\r\n" + "}";
		ChallengeConcept challengeConceptObj1 = mapper.readValue(cc1ObjString, ChallengeConcept.class);
		ChallengeConceptPersistence cc1 = new ChallengeConceptPersistence(challengeConceptObj1, GAME, PLAYER, challengeConceptObj1.getName());
		challengeConceptRepo.save(cc1);
		String cc2ObjString = "{\r\n" + "	\"name\": \"w90_rs__Bike_Km_6868e0c1-6d16-4bfe-814c-c7fb7dc15745\",\r\n"
				+ "	\"modelName\": \"percentageIncrement\",\r\n" + "	\"fields\": {\r\n"
				+ "		\"difficulty\": 2,\r\n" + "		\"bonusScore\": 190,\r\n"
				+ "		\"wi\": 3.20781253697311,\r\n" + "		\"percentage\": 0.320781253697311,\r\n"
				+ "		\"bonusPointType\": \"green leaves\",\r\n" + "		\"periodName\": \"weekly\",\r\n"
				+ "		\"baseline\": 28.0137228601819,\r\n" + "		\"counterName\": \"Bike_Km\",\r\n"
				+ "		\"target\": 37.0\r\n" + "	},\r\n" + "	\"start\": 1595023200000,\r\n"
				+ "	\"end\": 1595628000000,\r\n" + "	\"completed\": false,\r\n" + "	\"state\": \"FAILED\",\r\n"
				+ "	\"stateDate\": {\r\n" + "		\"PROPOSED\": 1594801699210,\r\n"
				+ "		\"ASSIGNED\": 1594805619558,\r\n" + "		\"ACTIVE\": 1595491607761,\r\n"
				+ "		\"FAILED\": 1595628000000\r\n" + "	},\r\n" + "	\"origin\": \"rs\",\r\n"
				+ "	\"priority\": 2,\r\n" + "	\"forced\": false,\r\n" + "	\"visibility\": {\r\n"
				+ "		\"hidden\": false,\r\n" + "		\"disclosureDate\": null\r\n" + "	}\r\n" + "}";
		ChallengeConcept challengeConceptObj2 = mapper.readValue(cc2ObjString, ChallengeConcept.class);
		ChallengeConceptPersistence cc2 = new ChallengeConceptPersistence(challengeConceptObj2, GAME, PLAYER, challengeConceptObj2.getName());
		challengeConceptRepo.save(cc2);
		String cc3ObjString = "{\r\n"
				+ "	\"name\": \"w90_rs__green leaves_fb95030f-1c8c-496a-b75a-d787b666ee7e\",\r\n"
				+ "	\"modelName\": \"repetitiveBehaviour\",\r\n" + "	\"fields\": {\r\n"
				+ "		\"bonusScore\": 100.0,\r\n" + "		\"bonusPointType\": \"green leaves\",\r\n"
				+ "		\"periodName\": \"daily\",\r\n" + "		\"counterName\": \"green leaves\",\r\n"
				+ "		\"periodTarget\": 2.0,\r\n" + "		\"target\": 30.0\r\n" + "	},\r\n"
				+ "	\"start\": 1595023200000,\r\n" + "	\"end\": 1595628000000,\r\n" + "	\"completed\": false,\r\n"
				+ "	\"state\": \"FAILED\",\r\n" + "	\"stateDate\": {\r\n" + "		\"ASSIGNED\": 1594801698905,\r\n"
				+ "		\"ACTIVE\": 1595491607761,\r\n" + "		\"FAILED\": 1595628000000\r\n" + "	},\r\n"
				+ "	\"origin\": \"rs\",\r\n" + "	\"priority\": 0,\r\n" + "	\"forced\": false,\r\n"
				+ "	\"visibility\": {\r\n" + "		\"hidden\": false,\r\n" + "		\"disclosureDate\": null\r\n"
				+ "	}\r\n" + "}";
		ChallengeConcept challengeConceptObj3 = mapper.readValue(cc3ObjString, ChallengeConcept.class);
		ChallengeConceptPersistence cc3 = new ChallengeConceptPersistence(challengeConceptObj3, GAME, PLAYER, challengeConceptObj3.getName());
		challengeConceptRepo.save(cc3);
	}

}
