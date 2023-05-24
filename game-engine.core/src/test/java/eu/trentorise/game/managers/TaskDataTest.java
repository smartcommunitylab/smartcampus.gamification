/**
 *    Copyright 2015 Fondazione Bruno Kessler - Trento RISE
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package eu.trentorise.game.managers;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.autoconfig.brave.BraveAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import eu.trentorise.game.config.AppConfig;
import eu.trentorise.game.config.MongoConfig;
import eu.trentorise.game.core.AppContextProvider;
import eu.trentorise.game.core.GameContext;
import eu.trentorise.game.model.core.GameTask;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, MongoConfig.class, BraveAutoConfiguration.class }, loader = AnnotationConfigContextLoader.class)
public class TaskDataTest {

	@Autowired
	private AppContextProvider provider;

	private static final String GAME = "gameTest";

	@Test
	public void taskDataSample() {
		GameTask task = new GameTask("taskSample", null) {

			public void execute(GameContext ctx) {
			}

			@Override
			public List<String> getExecutionActions() {
				return null;
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

	@Test
	public void saveDateField() {

		GameTask task = new GameTask("taskSample", null) {

			public void execute(GameContext ctx) {
			}

			@Override
			public List<String> getExecutionActions() {
				return null;
			}
		};

		GameContext ctx = (GameContext) provider.getApplicationContext()
				.getBean("gameCtx", GAME, task);

		Map<String, Object> data = new HashMap<String, Object>();
		Date now = new Date();
		data.put("now", now);
		ctx.writeTaskData(data);
		Map<String, Object> dataReaded = (Map<String, Object>) ctx
				.readTaskData();
		Assert.assertEquals(now, (Date) dataReaded.get("now"));

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
