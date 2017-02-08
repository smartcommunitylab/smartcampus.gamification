package eu.trentorise.game.repo;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import eu.trentorise.game.config.AppConfig;
import eu.trentorise.game.config.MongoConfig;
import eu.trentorise.game.model.CustomData;
import eu.trentorise.game.model.core.SearchCriteria;
import eu.trentorise.game.model.core.SearchCriteria.Projection;
import eu.trentorise.game.model.core.SortItem;
import eu.trentorise.game.model.core.SortItem.Direction;

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

		Page<StatePersistence> resultPaged = playerRepo.search(
				new SearchCriteria(null, new Projection(Arrays.asList(
						"playerId", "customData.field"), null)), null);
		List<StatePersistence> results = resultPaged.getContent();
		Assert.assertEquals(1, results.size());
		Assert.assertNull(results.get(0).getGameId());
		Assert.assertNotNull(results.get(0).getPlayerId());
		Assert.assertNotNull(results.get(0).getId());
		Assert.assertNotNull(results.get(0).getCustomData().get("field"));
		Assert.assertNull(results.get(0).getCustomData().get("indicator"));
	}

	@Test
	public void searchProjectionNonExistentField() {
		StatePersistence state = new StatePersistence(GAME, PLAYER);
		state.setCustomData(new CustomData());
		state.getCustomData().put("field", "value");
		state.getCustomData().put("indicator", "indicator-value");
		playerRepo.save(state);

		Page<StatePersistence> resultPaged = playerRepo.search(
				new SearchCriteria(null, new Projection(Arrays.asList(
						"playerId", "customData.field",
						"notExistentField.field"), null)), null);
		List<StatePersistence> results = resultPaged.getContent();
		Assert.assertEquals(1, results.size());
		Assert.assertNull(results.get(0).getGameId());
		Assert.assertNotNull(results.get(0).getPlayerId());
		Assert.assertNotNull(results.get(0).getId());
		Assert.assertNotNull(results.get(0).getCustomData().get("field"));
		Assert.assertNull(results.get(0).getCustomData().get("indicator"));
	}

	@Test
	public void projectionExcludefield() {
		StatePersistence state = new StatePersistence(GAME, PLAYER);
		state.setCustomData(new CustomData());
		state.getCustomData().put("field", "value");
		state.getCustomData().put("indicator", "indicator-value");
		playerRepo.save(state);

		Page<StatePersistence> resultPaged = playerRepo.search(
				new SearchCriteria(null, new Projection(Arrays.asList(
						"playerId", "customData.field"), Arrays.asList("id"))),
				null);
		List<StatePersistence> results = resultPaged.getContent();
		Assert.assertEquals(1, results.size());
		Assert.assertNull(results.get(0).getGameId());
		Assert.assertNotNull(results.get(0).getPlayerId());
		Assert.assertNotNull(results.get(0).getCustomData().get("field"));
		Assert.assertNull(results.get(0).getCustomData().get("indicator"));
		Assert.assertNull(results.get(0).getId());
	}

	@Test
	public void pagination() {
		for (int i = 0; i < 5; i++) {
			StatePersistence state = new StatePersistence(GAME, "100" + i);
			state.setCustomData(new CustomData());
			state.getCustomData().put("field", "value-" + i);
			playerRepo.save(state);
		}

		Page<StatePersistence> results = playerRepo.search(null,
				new PageRequest(0, 1));

		Assert.assertEquals(1, results.getNumberOfElements());

		results = playerRepo
				.search(new SearchCriteria(), new PageRequest(0, 4));

		Assert.assertEquals(4, results.getNumberOfElements());

		results = playerRepo.search(new SearchCriteria(),
				new PageRequest(0, 20));

		Assert.assertEquals(5, results.getNumberOfElements());
	}

	@Test
	public void sort() {
		for (int i = 0; i < 5; i++) {
			StatePersistence state = new StatePersistence(GAME, "100" + i);
			state.setCustomData(new CustomData());
			state.getCustomData().put("field", "value-" + i);
			playerRepo.save(state);
		}

		SearchCriteria criteria = new SearchCriteria(
				Arrays.asList(new SortItem("customData.field", Direction.DESC)),
				null);
		Page<StatePersistence> results = playerRepo.search(criteria, null);
		List<StatePersistence> states = results.getContent();

		Assert.assertEquals("value-4",
				states.get(0).getCustomData().get("field"));
		Assert.assertEquals("value-3",
				states.get(1).getCustomData().get("field"));

	}

	@Test
	public void sortByTwoFields() {
		for (int i = 0; i < 5; i++) {
			StatePersistence state = new StatePersistence(GAME, "100" + i);
			state.setCustomData(new CustomData());
			state.getCustomData().put("field", "value-" + i % 2);
			state.getCustomData().put("points", 10 * i);
			playerRepo.save(state);
		}

		SearchCriteria criteria = new SearchCriteria(Arrays.asList(
				new SortItem("customData.field", Direction.DESC), new SortItem(
						"customData.points", Direction.ASC)), null);
		Page<StatePersistence> results = playerRepo.search(criteria, null);
		List<StatePersistence> states = results.getContent();

		Assert.assertEquals("value-1",
				states.get(0).getCustomData().get("field"));
		Assert.assertEquals(10, states.get(0).getCustomData().get("points"));
		Assert.assertEquals("value-1",
				states.get(1).getCustomData().get("field"));
		Assert.assertEquals(30, states.get(1).getCustomData().get("points"));

	}
}
