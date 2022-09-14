package eu.trentorise.game.api.rest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.annotation.PostConstruct;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.trentorise.game.config.AppConfig;
import eu.trentorise.game.config.MongoConfig;
import eu.trentorise.game.config.NoSecurityConfig;
import eu.trentorise.game.managers.NotificationManager;
import eu.trentorise.game.notification.BadgeNotification;
import eu.trentorise.game.notification.ChallengeAssignedNotification;
import eu.trentorise.game.notification.ChallengeCompletedNotication;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("no-sec")
@ContextConfiguration(classes = {AppConfig.class, MongoConfig.class, NoSecurityConfig.class,
        TestMVCConfiguration.class},
        loader = AnnotationConfigWebContextLoader.class)
@TestPropertySource(properties = {"game.createDemo=false"})
@WebAppConfiguration
public class NotificationControllerTest {

    private static final String GAME = "gameTest";
    private static final String PLAYER_1 = "player1";
    private static final String PLAYER_2 = "player2";

    private MockMvc mocker;

    private ObjectMapper mapper;

    @Autowired
    private MongoTemplate mongo;

    @Autowired
    private NotificationManager notificationManager;

    @Before
    public void cleanDB() {
        mongo.getDb().drop();
    }

    @Autowired
    private WebApplicationContext wac;

    @PostConstruct
    public void init() {
        mocker = MockMvcBuilders.webAppContextSetup(wac).build();
        mapper = new ObjectMapper();
    }

    @Test
    public void emptyNotificationsInGame() {

        RequestBuilder builder =
                MockMvcRequestBuilders.get("/notification/game/{gameId}", GAME);

        try {
            mocker.perform(builder).andDo(print()).andExpect(status().is(200))
                    .andExpect(content().string("[]"));
        } catch (Exception e) {
            Assert.fail("exception: " + e.getMessage());
        }
    }

    @Test
    public void retrieveAllNotificationsInGame() {
        setupTest_RetrieveAllNotificationsInGame();
        RequestBuilder builder =
                MockMvcRequestBuilders.get("/notification/game/{gameId}", GAME);

        try {
            mocker.perform(builder).andDo(print()).andExpect(status().is(200))
                    .andExpect(jsonPath("$", Matchers.hasSize(2)));
        } catch (Exception e) {
            Assert.fail("exception: " + e.getMessage());
        }

    }

    @Test
    public void retrieveAllNotificationsPlayer1() {
        setupTest_RetrieveAllNotificationsPlayer1();
        RequestBuilder builder = MockMvcRequestBuilders.get(
                "/notification/game/{gameId}/player/{playerId}", GAME, PLAYER_1);

        try {
            mocker.perform(builder).andDo(print()).andExpect(status().is(200))
                    .andExpect(jsonPath("$", Matchers.hasSize(1)));
        } catch (Exception e) {
            Assert.fail("exception: " + e.getMessage());
        }

    }

    @Test
    public void paginateNotificationToSize3() {
        setupPaginationTest();
        RequestBuilder builder = MockMvcRequestBuilders
                .get("/notification/game/{gameId}", GAME).param("size", "3");

        try {
            mocker.perform(builder).andDo(print()).andExpect(status().is(200))
                    .andExpect(jsonPath("$", Matchers.hasSize(3)));
        } catch (Exception e) {
            Assert.fail("exception: " + e.getMessage());
        }

    }

    @Test
    public void paginateNotificationPage2OfSize2() {
        setupPaginationTest();
        RequestBuilder builder =
                MockMvcRequestBuilders.get("/notification/game/{gameId}", GAME)
                        .param("size", "2").param("page", "2");

        try {
            mocker.perform(builder).andDo(print()).andExpect(status().is(200))
                    .andExpect(jsonPath("$", Matchers.hasSize(2)))
                    .andExpect(jsonPath("$[1].badge", Matchers.equalTo("4-gold-coin")));
        } catch (Exception e) {
            Assert.fail("exception: " + e.getMessage());
        }

    }

    @Test
    public void paginateNotificationPage2() {
        setupPaginationTest();
        RequestBuilder builder = MockMvcRequestBuilders
                .get("/notification/game/{gameId}", GAME).param("page", "2");

        try {
            mocker.perform(builder).andDo(print()).andExpect(status().is(200))
                    .andExpect(jsonPath("$", Matchers.hasSize(0)));
        } catch (Exception e) {
            Assert.fail("exception: " + e.getMessage());
        }

    }

    @Test
    public void paginateNotificationPage2OfSize2Player1() {
        setupPaginationTest();
        RequestBuilder builder =
                MockMvcRequestBuilders
                        .get("/notification/game/{gameId}/player/{playerId}", GAME, PLAYER_1)
                        .param("size", "2").param("page", "2");

        try {
            mocker.perform(builder).andDo(print()).andExpect(status().is(200))
                    .andExpect(jsonPath("$", Matchers.hasSize(2)))
                    .andExpect(jsonPath("$[1].badge", Matchers.equalTo("5-gold-coin")));
        } catch (Exception e) {
            Assert.fail("exception: " + e.getMessage());
        }

    }

    @Test
    public void excludeNotificationTypeChallengeAssignedNotification() {
        setup_ExcludeNotificationType();
        RequestBuilder builder = MockMvcRequestBuilders
                .get("/notification/game/{gameId}/player/{playerId}", GAME,
                        PLAYER_1)
                .param("excludeTypes", "ChallengeAssignedNotification");

        try {
            mocker.perform(builder).andDo(print()).andExpect(status().is(200))
                    .andExpect(jsonPath("$", Matchers.hasSize(2)));
        } catch (Exception e) {
            Assert.fail("exception: " + e.getMessage());
        }
    }

    @Test
    public void exclude2TypesOfNotifications() {
        setup_ExcludeNotificationType();
        RequestBuilder builder = MockMvcRequestBuilders
                .get("/notification/game/{gameId}/player/{playerId}", GAME,
                        PLAYER_1)
                .param("excludeTypes", "ChallengeAssignedNotification", "BadgeNotification");

        try {
            mocker.perform(builder).andDo(print()).andExpect(status().is(200))
                    .andExpect(jsonPath("$", Matchers.hasSize(1)));
        } catch (Exception e) {
            Assert.fail("exception: " + e.getMessage());
        }
    }

    @Test
    public void includeChallengeAssignedNotification() {
        setup_ExcludeNotificationType();
        RequestBuilder builder = MockMvcRequestBuilders
                .get("/notification/game/{gameId}/player/{playerId}", GAME,
                        PLAYER_1)
                .param("includeTypes", "ChallengeAssignedNotification");

        try {
            mocker.perform(builder).andDo(print()).andExpect(status().is(200))
                    .andExpect(jsonPath("$", Matchers.hasSize(2)));
        } catch (Exception e) {
            Assert.fail("exception: " + e.getMessage());
        }
    }

    private void setup_ExcludeNotificationType() {
        BadgeNotification notification =
                new BadgeNotification(GAME, PLAYER_1, "chest", "1-gold-coin");
        notificationManager.notificate(notification);

        ChallengeAssignedNotification assignNotification = new ChallengeAssignedNotification();
        assignNotification.setGameId(GAME);
        assignNotification.setPlayerId(PLAYER_1);
        assignNotification.setChallengeName("challenge_1");
        notificationManager.notificate(assignNotification);

        assignNotification = new ChallengeAssignedNotification();
        assignNotification.setGameId(GAME);
        assignNotification.setPlayerId(PLAYER_1);
        assignNotification.setChallengeName("challenge_2");
        notificationManager.notificate(assignNotification);

        ChallengeCompletedNotication completedNotification = new ChallengeCompletedNotication();
        completedNotification.setPlayerId(PLAYER_1);
        completedNotification.setGameId(GAME);
        completedNotification.setChallengeName("challenge_1");
        completedNotification.setModel("model1");
        completedNotification.setPointConcept("pc");
        notificationManager.notificate(completedNotification);

    }

    private void setupPaginationTest() {
        BadgeNotification notification =
                new BadgeNotification(GAME, PLAYER_1, "chest", "1-gold-coin");
        notificationManager.notificate(notification);
        notification = new BadgeNotification(GAME, PLAYER_1, "chest", "2-gold-coin");
        notificationManager.notificate(notification);
        notification = new BadgeNotification(GAME, PLAYER_1, "chest", "3-gold-coin");
        notificationManager.notificate(notification);
        notification = new BadgeNotification(GAME, PLAYER_2, "chest", "4-gold-coin");
        notificationManager.notificate(notification);
        notification = new BadgeNotification(GAME, PLAYER_1, "chest", "5-gold-coin");
        notificationManager.notificate(notification);

    }

    private void setupTest_RetrieveAllNotificationsInGame() {
        BadgeNotification notification =
                new BadgeNotification(GAME, PLAYER_1, "chest", "1-gold-coin");
        notificationManager.notificate(notification);

        notification = new BadgeNotification(GAME, PLAYER_1, "chest", "2-gold-coin");
        notificationManager.notificate(notification);
    }

    private void setupTest_RetrieveAllNotificationsPlayer1() {
        BadgeNotification notification =
                new BadgeNotification(GAME, PLAYER_1, "chest", "1-gold-coin");
        notificationManager.notificate(notification);

        notification = new BadgeNotification(GAME, PLAYER_2, "chest", "1-gold-coin");
        notificationManager.notificate(notification);
    }

}
