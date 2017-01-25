package eu.trentorise.game.utils;

import java.util.HashSet;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.util.Assert;

import eu.trentorise.game.bean.IncrementalClassificationDTO;
import eu.trentorise.game.config.AppConfig;
import eu.trentorise.game.config.MongoConfig;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.model.core.GameTask;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.task.IncrementalClassificationTask;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, MongoConfig.class }, loader = AnnotationConfigContextLoader.class)
public class ConverterTest {

	@Autowired
	private Converter converter;

	@Autowired
	private GameService gameSrv;

	private final String POINT_CONCEPT = "green";
	private final String PERIOD_NAME = "p1";
	private final String GAME_ID = "mytest";

	@Test
	public void convertIncrementalClassification() {

		gameSrv.saveGameDefinition(defineGame());

		IncrementalClassificationDTO incrClassDTO = new IncrementalClassificationDTO();
		incrClassDTO.setClassificationName("test");
		incrClassDTO.setItemsToNotificate(2);
		incrClassDTO.setItemType(POINT_CONCEPT);
		incrClassDTO.setPeriodName(PERIOD_NAME);
		incrClassDTO.setGameId(GAME_ID);
		IncrementalClassificationTask converted = converter
				.convertClassificationTask(incrClassDTO);
		Assert.notNull(converted.getSchedule());
		Assert.notNull(converted.getSchedule().getStart());
		Assert.notNull(converted.getSchedule().getPeriod());
	}

	@Test(expected = IllegalArgumentException.class)
	public void incrementalNoGameId() {

		gameSrv.saveGameDefinition(defineGame());

		IncrementalClassificationDTO incrClassDTO = new IncrementalClassificationDTO();
		incrClassDTO.setClassificationName("test");
		incrClassDTO.setItemsToNotificate(2);
		incrClassDTO.setItemType(POINT_CONCEPT);
		incrClassDTO.setPeriodName(PERIOD_NAME);

		IncrementalClassificationTask converted = converter
				.convertClassificationTask(incrClassDTO);
		Assert.notNull(converted.getSchedule());
		Assert.notNull(converted.getSchedule().getStart());
		Assert.notNull(converted.getSchedule().getPeriod());
	}

	private Game defineGame() {
		PointConcept p = new PointConcept(POINT_CONCEPT);
		p.addPeriod(PERIOD_NAME, new LocalDate().toDate(), 60000);

		Game g = new Game();
		g.setId(GAME_ID);
		g.setName(GAME_ID);
		g.setConcepts(new HashSet<GameConcept>());
		g.getConcepts().add(p);
		g.setTasks(new HashSet<GameTask>());
		return g;
	}
}
