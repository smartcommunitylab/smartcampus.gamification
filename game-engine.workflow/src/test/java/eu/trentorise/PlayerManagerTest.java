package eu.trentorise;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import eu.trentorise.game.Application;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.services.PlayerService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class, loader = AnnotationConfigContextLoader.class)
public class PlayerManagerTest {

	@Autowired
	PlayerService playerSrv;

	@Test
	public void crud() {
		PlayerState state = new PlayerState();
		Map<String, Object> myState = new HashMap<String, Object>();
		myState.put("concept1", 21);
		myState.put("concept2", 21);
		state.setState(myState);
		playerSrv.saveState("331", "12", state);
		Assert.assertTrue(true);

	}
}
