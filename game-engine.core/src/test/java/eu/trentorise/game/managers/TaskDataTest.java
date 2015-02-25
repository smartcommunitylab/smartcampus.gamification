package eu.trentorise.game.managers;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import eu.trentorise.game.config.AppConfig;
import eu.trentorise.game.config.MongoConfig;
import eu.trentorise.game.core.AppContextProvider;
import eu.trentorise.game.core.GameContext;
import eu.trentorise.game.core.GameTask;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, MongoConfig.class }, loader = AnnotationConfigContextLoader.class)
public class TaskDataTest {

	@Autowired
	private AppContextProvider provider;

	private static final String GAME = "gameTest";

	@Test
	public void taskDataSample() {
		GameTask task = new GameTask("taskSample", null) {

			public void execute(GameContext ctx) {
			}
		};

		GameContext ctx = (GameContext) provider.getApplicationContext()
				.getBean("gameCtx", GAME, task);

		SampleClass sc = new SampleClass();
		sc.setField1("myfield");
		sc.setField2(1111);
		String dataId = ctx.writeTaskData(sc);

		Assert.assertNotNull(dataId);

		Object d = ctx.readTaskData();

		Assert.assertEquals(1111, ((SampleClass) d).getField2());

		sc.setField1("new value");
		ctx.writeTaskData(sc);

		d = ctx.readTaskData();
		Assert.assertEquals("new value", ((SampleClass) d).getField1());

		ctx.writeTaskData(new SampleClass1());

		d = ctx.readTaskData();
		Assert.assertTrue(d instanceof SampleClass1);
		Assert.assertEquals(4, ((SampleClass1) d).getList().size());
	}

}

class SampleClass1 {
	private List<String> list = Arrays.asList("1", "2", "3", "4");

	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}

}

class SampleClass {
	private String field1;
	private int field2;

	public SampleClass() {
	}

	public String getField1() {
		return field1;
	}

	public void setField1(String field1) {
		this.field1 = field1;
	}

	public int getField2() {
		return field2;
	}

	public void setField2(int field2) {
		this.field2 = field2;
	}

}
