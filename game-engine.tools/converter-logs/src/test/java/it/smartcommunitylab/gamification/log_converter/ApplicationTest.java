package it.smartcommunitylab.gamification.log_converter;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class ApplicationTest {

	@Test
	public void elaborazione() throws IOException {
		String result = Application.elaborazione("lol.txt");
		Assert.assertEquals(
				"\"INFO - \"58a55bece4b0d3fae8c96ff7\" \"18b1a799-c971-41f7-9f9a-8f588f1fc388\" 6e2f6e8c-33f8-4419-b0dc-2b74865064c7 1496729100247 1496729100267 type=Action action=\"PedibusKidTrip\" "
						+ "\"INFO - \"58a55bece4b0d3fae8c96ff7\" \"18b1a799-c971-41f7-9f9a-8f588f1fc388\" 6e2f6e8c-33f8-4419-b0dc-2b74865064c7 1496729100247 1496729100267 type=Action action=\"PedibusKidTrip\" "
						+ "\"INFO - \"58a55bece4b0d3fae8c96ff7\" \"18b1a799-c971-41f7-9f9a-8f588f1fc388\" 6e2f6e8c-33f8-4419-b0dc-2b74865064c7 1496729100247 1496729100267 type=Action action=\"PedibusKidTrip\" ",
				result);
	}
}
