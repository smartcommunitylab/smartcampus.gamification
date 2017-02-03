package eu.trentorise.game.repo;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import eu.trentorise.game.config.AppConfig;
import eu.trentorise.game.config.MongoConfig;
import eu.trentorise.game.model.CustomData;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, MongoConfig.class }, loader = AnnotationConfigContextLoader.class)
public class PlayerRepoTest {

	@Autowired
	private PlayerRepo playerRepo;

	@Autowired
	private MongoTemplate mongo;

	@Before
	public void cleanDB() {
		// clean mongo
		mongo.getDb().dropDatabase();
	}

	private static final String GAME = "repo-game";
	private static final String PLAYER = "1000";

	@Test
	public void searchProjection() {
		StatePersistence state = new StatePersistence(GAME, PLAYER);
		state.setCustomData(new CustomData());
		state.getCustomData().put("field", "value");
		state.getCustomData().put("indicator", "indicator-value");
		playerRepo.save(state);

		List<StatePersistence> results = playerRepo.search(Arrays.asList(
				"playerId", "customData.field"));
		Assert.assertEquals(1, results.size());
		Assert.assertNull(results.get(0).getGameId());
		Assert.assertNotNull(results.get(0).getPlayerId());
		Assert.assertNotNull(results.get(0).getId());
		Assert.assertNotNull(results.get(0).getCustomData().get("field"));
		Assert.assertNull(results.get(0).getCustomData().get("indicator"));

	}
}
