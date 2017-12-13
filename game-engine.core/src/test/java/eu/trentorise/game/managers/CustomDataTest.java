/**
 * Copyright 2015 Fondazione Bruno Kessler - Trento RISE
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package eu.trentorise.game.managers;

import java.util.HashSet;
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
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.core.ClasspathRule;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.model.core.GameTask;
import eu.trentorise.game.repo.GamePersistence;
import eu.trentorise.game.repo.NotificationPersistence;
import eu.trentorise.game.repo.StatePersistence;
import eu.trentorise.game.services.PlayerService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, MongoConfig.class},
        loader = AnnotationConfigContextLoader.class)
public class CustomDataTest {

    private static final String GAME = "customData";
    private static final String PLAYER = "player1";
    private static final int TIMEOUT = 2000;
    private static final String DOMAIN = "my-domain";


    @Autowired
    private GameManager gameManager;

    @Autowired
    private PlayerService playerSrv;

    @Autowired
    private GameWorkflow workflow;

    @Autowired
    private MongoTemplate mongo;

    private Game defineGame() {
        Game game = new Game();

        game.setId(GAME);
        game.setName(GAME);
        game.setDomain(DOMAIN);

        game.setActions(new HashSet<String>());
        game.getActions().add("init-data");
        game.getActions().add("edit-data");
        game.getActions().add("add-area");

        game.setConcepts(new HashSet<GameConcept>());
        game.setTasks(new HashSet<GameTask>());
        return game;

    }

    @Before
    public void cleanDB() {
        // clean mongo
        mongo.dropCollection(StatePersistence.class);
        mongo.dropCollection(GamePersistence.class);
        mongo.dropCollection(NotificationPersistence.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void workflow() throws InterruptedException {
        Game game = gameManager.saveGameDefinition(defineGame());
        ClasspathRule rule = new ClasspathRule(game.getId(), "rules/" + GAME + "/rule1.drl");
        gameManager.addRule(rule);

        PlayerState state = playerSrv.loadState(game.getId(), PLAYER, true);
        Assert.assertEquals(0, state.getCustomData().size());
        workflow.apply(game.getId(), "init-data", PLAYER, null, null);

        Thread.sleep(TIMEOUT);
        state = playerSrv.loadState(game.getId(), PLAYER, false);
        Assert.assertEquals(2, state.getCustomData().size());
        Assert.assertEquals(1000, state.getCustomData().get("counter"));
        Assert.assertEquals(0, ((List<String>) state.getCustomData().get("areas")).size());

        workflow.apply(game.getId(), "edit-data", PLAYER, null, null);
        Thread.sleep(TIMEOUT);
        state = playerSrv.loadState(game.getId(), PLAYER, false);
        Assert.assertEquals(2, state.getCustomData().size());
        Assert.assertEquals(1010, state.getCustomData().get("counter"));

        workflow.apply(game.getId(), "edit-data", PLAYER, null, null);
        Thread.sleep(TIMEOUT);
        state = playerSrv.loadState(game.getId(), PLAYER, false);
        Assert.assertEquals(2, state.getCustomData().size());
        Assert.assertEquals(1020, state.getCustomData().get("counter"));

        workflow.apply(game.getId(), "add-area", PLAYER, null, null);
        Thread.sleep(TIMEOUT);
        state = playerSrv.loadState(game.getId(), PLAYER, false);
        Assert.assertEquals(2, state.getCustomData().size());
        Assert.assertEquals(1, ((List<String>) state.getCustomData().get("areas")).size());

        workflow.apply(game.getId(), "add-area", PLAYER, null, null);
        Thread.sleep(TIMEOUT);
        state = playerSrv.loadState(game.getId(), PLAYER, false);
        Assert.assertEquals(1, ((List<String>) state.getCustomData().get("areas")).size());
    }
}
