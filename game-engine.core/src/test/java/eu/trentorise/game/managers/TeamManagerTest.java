package eu.trentorise.game.managers;

import java.util.Arrays;

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

import eu.trentorise.game.config.AppConfig;
import eu.trentorise.game.config.MongoConfig;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.TeamState;
import eu.trentorise.game.repo.GamePersistence;
import eu.trentorise.game.repo.NotificationPersistence;
import eu.trentorise.game.repo.StatePersistence;
import eu.trentorise.game.services.PlayerService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, MongoConfig.class, BraveAutoConfiguration.class }, loader = AnnotationConfigContextLoader.class)
public class TeamManagerTest {

	@Autowired
	private PlayerService playerSrv;

	@Autowired
	private MongoTemplate mongo;

	@Before
	public void cleanDB() {
		// clean mongo
		mongo.dropCollection(StatePersistence.class);
		mongo.dropCollection(GamePersistence.class);
		mongo.dropCollection(NotificationPersistence.class);
	}

	@Test
	public void createTeam() {
		TeamState t = new TeamState("game1", "team1");
		t.setName("muppet");
		t.setMembers(Arrays.asList("12", "dsadfaf0", "388fjjs"));

		t = playerSrv.saveTeam(t);
		Assert.assertEquals("muppet", t.getName());
		Assert.assertEquals(3, t.getMembers().size());
	}

	@Test
	public void readAndDeleteTeams() {
		TeamState t = new TeamState("game1", "team1");
		t.setName("muppet");
		t.setMembers(Arrays.asList("12", "dsadfaf0", "388fjjs"));
		playerSrv.saveTeam(t);

		playerSrv.saveState(new PlayerState("game1", "23"));
		playerSrv.saveState(new PlayerState("game1", "24"));
		playerSrv.saveState(new PlayerState("game1", "25"));

		Assert.assertEquals(1, playerSrv.readTeams("game1").size());
		Assert.assertEquals(4, playerSrv.readPlayers("game1").size());

		t = new TeamState("game2", "team2");
		t.setName("muppet1");
		t.setMembers(Arrays.asList("12", "dsadfaf0", "388fjjs"));
		playerSrv.saveTeam(t);

		Assert.assertEquals(1, playerSrv.readTeams("game1").size());
		Assert.assertEquals(4, playerSrv.readPlayers("game1").size());

		t = new TeamState("game1", "team2");
		t.setName("muppet1");
		t.setMembers(Arrays.asList("12", "dsadfaf0", "388fjjs"));
		t = playerSrv.saveTeam(t);

		Assert.assertEquals(2, playerSrv.readTeams("game1").size());
		Assert.assertEquals(5, playerSrv.readPlayers("game1").size());

		playerSrv.deleteState(t.getGameId(), t.getPlayerId());

		Assert.assertEquals(1, playerSrv.readTeams("game1").size());
		Assert.assertEquals(4, playerSrv.readPlayers("game1").size());
	}

	@Test
	public void findMyTeams() {
		PlayerState me = new PlayerState("game1", "p1");
		playerSrv.saveState(me);

		TeamState t = new TeamState("game1", "t1");
		t.getMembers().add("p1");
		t.setName("team1");
		playerSrv.saveTeam(t);

		t = new TeamState("game1", "t2");
		t.getMembers().add("p1");
		t.getMembers().add("p13");
		t.getMembers().add("p21");
		t.setName("team2");
		playerSrv.saveTeam(t);

		t = new TeamState("game1", "t3");
		t.getMembers().add("p211");
		t.setName("team3");
		playerSrv.saveTeam(t);

		Assert.assertEquals(2, playerSrv.readTeams("game1", "p1").size());
	}

	@Test
	public void membership() {
		TeamState t = new TeamState("game1", "t1");
		t.getMembers().add("p1");
		t.setName("team1");
		playerSrv.saveTeam(t);

		playerSrv.addToTeam("game1", "t1", "p12");

		t = playerSrv.readTeam("game1", "t1");

		Assert.assertArrayEquals(new String[] { "p1", "p12" }, t.getMembers()
				.toArray(new String[0]));

		playerSrv.removeFromTeam("game1", "t1", "p1");

		t = playerSrv.readTeam("game1", "t1");

		Assert.assertArrayEquals(new String[] { "p12" }, t.getMembers()
				.toArray(new String[0]));

	}
}