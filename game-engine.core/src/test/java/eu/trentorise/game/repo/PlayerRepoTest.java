package eu.trentorise.game.repo;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.web.PageableDefault;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import eu.trentorise.game.config.AppConfig;
import eu.trentorise.game.config.MongoConfig;
import eu.trentorise.game.model.CustomData;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.core.ComplexSearchQuery;
import eu.trentorise.game.model.core.ComplexSearchQuery.StructuredElement;
import eu.trentorise.game.model.core.ComplexSearchQuery.StructuredProjection;
import eu.trentorise.game.model.core.ComplexSearchQuery.StructuredSortItem;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.model.core.RawSearchQuery;
import eu.trentorise.game.model.core.RawSearchQuery.Projection;
import eu.trentorise.game.model.core.RawSearchQuery.SortItem;
import eu.trentorise.game.model.core.RawSearchQuery.SortItem.Direction;
import eu.trentorise.game.model.core.StringSearchQuery;
import eu.trentorise.game.services.GameService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, MongoConfig.class},
        loader = AnnotationConfigContextLoader.class)
public class PlayerRepoTest {

    private static final String DOMAIN = "my-domain";

    @Autowired
    private PlayerRepo playerRepo;

    @Autowired
    private MongoTemplate mongo;

    @Autowired
    private GameService gameSrv;
    
    private PageRequest defaultPageable = PageRequest.of(0,20);
    
    

    @Before
    public void cleanDB() {
        // clean mongo
        mongo.getDb().drop();
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

        Projection proj = new Projection(Arrays.asList("playerId", "customData.field"), null);

        RawSearchQuery query = new RawSearchQuery(null, proj, null);
        Page<StatePersistence> resultPaged = playerRepo.search(GAME, query, defaultPageable);
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

        Projection proj = new Projection(
                Arrays.asList("playerId", "customData.field", "notExistentField.field"), null);

        RawSearchQuery query = new RawSearchQuery(null, proj, null);
        Page<StatePersistence> resultPaged = playerRepo.search(GAME, query, defaultPageable);
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

        Map<String, Object> o = new HashMap<>();
        o.put("gameId", GAME);
        RawSearchQuery query =
                new RawSearchQuery(o,
                        new eu.trentorise.game.model.core.RawSearchQuery.Projection(
                                Arrays.asList("playerId", "customData.field"), Arrays.asList("id")),
                        null);

        Page<StatePersistence> resultPaged = playerRepo.search(GAME, query, defaultPageable);
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

        RawSearchQuery emptyQuery = new RawSearchQuery(null, null, null);
        Page<StatePersistence> results = playerRepo.search(GAME, emptyQuery, PageRequest.of(0, 1));

        Assert.assertEquals(1, results.getNumberOfElements());

        results = playerRepo.search(null, emptyQuery, PageRequest.of(0, 4));

        Assert.assertEquals(4, results.getNumberOfElements());

        results = playerRepo.search(null, emptyQuery, PageRequest.of(0, 20));

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

        RawSearchQuery query = new RawSearchQuery(null, null,
                Arrays.asList(new SortItem("customData.field", Direction.DESC)));
        Page<StatePersistence> results = playerRepo.search(GAME, query, defaultPageable);
        List<StatePersistence> states = results.getContent();

        Assert.assertEquals("value-4", states.get(0).getCustomData().get("field"));
        Assert.assertEquals("value-3", states.get(1).getCustomData().get("field"));
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

        List<eu.trentorise.game.model.core.RawSearchQuery.SortItem> sortElements =
                Arrays.asList(new SortItem("customData.field", Direction.DESC),
                        new SortItem("customData.points", Direction.ASC));
        RawSearchQuery query = new RawSearchQuery(null, null, sortElements);
        Page<StatePersistence> results = playerRepo.search(GAME, query, defaultPageable);
        List<StatePersistence> states = results.getContent();

        Assert.assertEquals("value-1", states.get(0).getCustomData().get("field"));
        Assert.assertEquals(10, states.get(0).getCustomData().get("points"));
        Assert.assertEquals("value-1", states.get(1).getCustomData().get("field"));
        Assert.assertEquals(30, states.get(1).getCustomData().get("points"));
    }

    @Test
    public void complexQueryCustomDataOneClause() {
        for (int i = 0; i < 5; i++) {
            StatePersistence state = new StatePersistence(GAME, "100" + i);
            state.setCustomData(new CustomData());
            state.getCustomData().put("field", "value-" + i % 2);
            state.getCustomData().put("points", 10 * i);
            playerRepo.save(state);
        }
        Map<String, List<eu.trentorise.game.model.core.ComplexSearchQuery.QueryElement>> query =
                new HashMap<>();
        query.put("customData",
                Arrays.asList(new eu.trentorise.game.model.core.ComplexSearchQuery.QueryElement(
                        "points", "{$gt: 20}")));
        ComplexSearchQuery complexQuery = new ComplexSearchQuery(query, null, null);
        Page<StatePersistence> results = playerRepo.search(GAME, complexQuery, defaultPageable);
        List<StatePersistence> states = results.getContent();
        Assert.assertEquals(2, states.size());
    }

    @Test
    public void complexQueryCustomDataTwoClauses() {
        for (int i = 0; i < 5; i++) {
            StatePersistence state = new StatePersistence(GAME, "100" + i);
            state.setCustomData(new CustomData());
            state.getCustomData().put("field", "value-" + i % 2);
            state.getCustomData().put("points", 10 * i);
            playerRepo.save(state);
        }
        Map<String, List<eu.trentorise.game.model.core.ComplexSearchQuery.QueryElement>> query =
                new HashMap<>();
        query.put("customData",
                Arrays.asList(
                        new eu.trentorise.game.model.core.ComplexSearchQuery.QueryElement("points",
                                "{$gt: 20}"),
                        new eu.trentorise.game.model.core.ComplexSearchQuery.QueryElement("field",
                                "\"value-0\"")));
        ComplexSearchQuery complexQuery = new ComplexSearchQuery(query, null, null);
        Page<StatePersistence> results = playerRepo.search(GAME, complexQuery, defaultPageable);
        List<StatePersistence> states = results.getContent();
        Assert.assertEquals(1, states.size());
    }

    @Test
    public void rawQueryAllStatesWithBikeSharingGreaterThanZero() {
        setupEnv();

        Map<String, Object> obj = new HashMap<String, Object>();
        Map<String, Object> obj1 = new HashMap<String, Object>();
        obj1.put("$gt", 0);
        obj.put("concepts.PointConcept.BikeSharing_Km.obj.score", obj1);

        RawSearchQuery query = new RawSearchQuery(obj, null, null);
        Page<StatePersistence> results = playerRepo.search(GAME, query, defaultPageable);
        List<StatePersistence> states = results.getContent();
        Assert.assertEquals(2, states.size());
    }

    @Test
    public void rawQueryAllStatesWithPeriodicGreenLeavesLessThan1000() {
        setupEnv();
        Map<String, Object> obj = new HashMap<String, Object>();
        Map<String, Object> obj1 = new HashMap<String, Object>();
        obj1.put("$lt", 1000);
        obj.put("concepts.PointConcept.green leaves.obj.periods.weekly.instances.2016-09-10T00:00:00.score",
                obj1);
        RawSearchQuery query = new RawSearchQuery(obj, null, null);
        Page<StatePersistence> results = playerRepo.search(null, query, defaultPageable);
        List<StatePersistence> states = results.getContent();
        Assert.assertEquals(1, states.size());
    }

    @Test
    public void rawQueryStringCustomData() {
        setupEnv();
        String rawQuery = "{'custData.points' : {$gt: 20}}";
        StringSearchQuery query = new StringSearchQuery(rawQuery, null, null);
        Page<StatePersistence> results = playerRepo.search(GAME, query, defaultPageable);
        List<StatePersistence> states = results.getContent();
        Assert.assertEquals(0, states.size());
    }

    @Test
    public void rawQueryMapCustomData() {
        setupEnv();
        Map<String, Object> obj = new HashMap<String, Object>();
        Map<String, Object> obj1 = new HashMap<String, Object>();
        obj1.put("$gt", 20);
        obj.put("customData.points", obj1);
        RawSearchQuery rawQuery = new RawSearchQuery(obj, null, null);
        Page<StatePersistence> results = playerRepo.search(GAME, rawQuery, defaultPageable);
        List<StatePersistence> states = results.getContent();
        Assert.assertEquals(0, states.size());
    }

    @Test
    public void rawQueryObjAboutNotExistingField() {
        setupEnv();

        Map<String, Object> obj = new HashMap<String, Object>();
        Map<String, Object> obj1 = new HashMap<String, Object>();
        obj1.put("$gt", 20);
        obj.put("custData.points", obj1);
        RawSearchQuery rawQuery = new RawSearchQuery(obj, null, null);
        Page<StatePersistence> results = playerRepo.search(GAME, rawQuery, defaultPageable);
        List<StatePersistence> states = results.getContent();
        Assert.assertEquals(0, states.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void rawQueryObjWithInvalidOperator() {
        setupEnv();
        Map<String, Object> obj = new HashMap<String, Object>();
        Map<String, Object> obj1 = new HashMap<String, Object>();
        obj1.put("$gth", 20);
        obj.put("score", obj1);
        RawSearchQuery rawQuery = new RawSearchQuery(obj, null, null);
        playerRepo.search(GAME, rawQuery, defaultPageable);
    }

    @Test
    public void rawQueryObjEmpty() {
        setupEnv();
        Map<String, Object> obj = new HashMap<String, Object>();
        RawSearchQuery rawQuery = new RawSearchQuery(obj, null, null);
        Page<StatePersistence> results = playerRepo.search(GAME, rawQuery, defaultPageable);
        List<StatePersistence> states = results.getContent();
        Assert.assertEquals(3, states.size());
    }

    @Test
    public void complexQueryBikeSharingGreaterThan0() {
        setupEnv();

        Map<String, List<eu.trentorise.game.model.core.ComplexSearchQuery.QueryElement>> query =
                new HashMap<>();
        query.put("pointConcept",
                Arrays.asList(new eu.trentorise.game.model.core.ComplexSearchQuery.QueryElement(
                        "BikeSharing_Km", "{$gt: 0}")));

        ComplexSearchQuery complexQuery = new ComplexSearchQuery(query, null, null);
        Page<StatePersistence> results = playerRepo.search(GAME, complexQuery, defaultPageable);
        List<StatePersistence> states = results.getContent();
        Assert.assertEquals(2, states.size());
    }

    @Test
    public void complexQueryGreenLeavesBadgesContainsValue() {
        setupEnv();

        Map<String, List<eu.trentorise.game.model.core.ComplexSearchQuery.QueryElement>> query =
                new HashMap<>();
        query.put("badgeCollectionConcept",
                Arrays.asList(new eu.trentorise.game.model.core.ComplexSearchQuery.QueryElement(
                        "green leaves", "\"5000_point_green\"")));

        ComplexSearchQuery complexQuery = new ComplexSearchQuery(query, null, null);
        Page<StatePersistence> results = playerRepo.search(GAME, complexQuery, defaultPageable);
        List<StatePersistence> states = results.getContent();
        Assert.assertEquals(1, states.size());
    }

    @Test
    public void complexQueryGreenLeavesBadgesContains() {
        setupEnv();

        Map<String, List<eu.trentorise.game.model.core.ComplexSearchQuery.QueryElement>> query =
                new HashMap<>();
        query.put("badgeCollectionConcept",
                Arrays.asList(new eu.trentorise.game.model.core.ComplexSearchQuery.QueryElement(
                        "green leaves", "{$in : [\"50_point_green\"]}")));

        ComplexSearchQuery complexQuery = new ComplexSearchQuery(query, null, null);
        Page<StatePersistence> results = playerRepo.search(GAME, complexQuery, defaultPageable);
        List<StatePersistence> states = results.getContent();
        Assert.assertEquals(3, states.size());
    }

    @Test
    public void complexQueryAllStatesWithPeriodicGreenLeavesLessThan1000() {
        setupEnv();
        Date date = new LocalDate(2016, 9, 10).toDate();
        Map<String, List<eu.trentorise.game.model.core.ComplexSearchQuery.QueryElement>> query =
                new HashMap<>();
        query.put("periodicPointConcept",
                Arrays.asList(new eu.trentorise.game.model.core.ComplexSearchQuery.QueryElement(
                        "green leaves", "weekly", date, "{$lt: 1000}")));

        ComplexSearchQuery complexQuery = new ComplexSearchQuery(query, null, null);
        Page<StatePersistence> results = playerRepo.search(GAME, complexQuery, defaultPageable);
        List<StatePersistence> states = results.getContent();
        Assert.assertEquals(1, states.size());
    }

    @Test
    public void complexQueryAllStatesWithCompletedChallenge() {
        setupEnv();
        Map<String, List<eu.trentorise.game.model.core.ComplexSearchQuery.QueryElement>> query =
                new HashMap<>();
        query.put("challengeConcept",
                Arrays.asList(new eu.trentorise.game.model.core.ComplexSearchQuery.QueryElement(
                        "w9_green_leaves_1200_fb4c6e83-abcf-49b6-aad7-a3bd38e03056", "completed",
                        "true")));

        ComplexSearchQuery complexQuery = new ComplexSearchQuery(query, null, null);
        Page<StatePersistence> results = playerRepo.search(GAME, complexQuery, defaultPageable);
        List<StatePersistence> states = results.getContent();
        Assert.assertEquals(3, states.size());
    }

    @Test
    public void complexQueryAllStatesWithFailedChallenge() {
        setupEnv();
        Map<String, List<eu.trentorise.game.model.core.ComplexSearchQuery.QueryElement>> query =
                new HashMap<>();
        query.put("challengeConcept",
                Arrays.asList(new eu.trentorise.game.model.core.ComplexSearchQuery.QueryElement(
                        "w9_green_leaves_1200_fb4c6e83-abcf-49b6-aad7-a3bd38e03056", "completed",
                        "false")));
        ComplexSearchQuery complexQuery = new ComplexSearchQuery(query, null, null);
        Page<StatePersistence> results = playerRepo.search(GAME, complexQuery, defaultPageable);
        List<StatePersistence> states = results.getContent();
        Assert.assertEquals(0, states.size());
    }

    @Test
    public void complexQueryAllStatesWithPeriodFieldChallenge() {
        setupEnv();
        Map<String, List<eu.trentorise.game.model.core.ComplexSearchQuery.QueryElement>> query =
                new HashMap<>();
        query.put("challengeConcept",
                Arrays.asList(new eu.trentorise.game.model.core.ComplexSearchQuery.QueryElement(
                        "w9_green_leaves_1200_fb4c6e83-abcf-49b6-aad7-a3bd38e03056", "periodName",
                        "\"weekly\"")));

        ComplexSearchQuery complexQuery = new ComplexSearchQuery(query, null, null);
        Page<StatePersistence> results = playerRepo.search(GAME, complexQuery, defaultPageable);
        List<StatePersistence> states = results.getContent();
        Assert.assertEquals(3, states.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void complexQueryAllStatesNotExistingConcept() {
        setupEnv();
        Map<String, List<eu.trentorise.game.model.core.ComplexSearchQuery.QueryElement>> query =
                new HashMap<>();
        query.put("poinConcept",
                Arrays.asList(new eu.trentorise.game.model.core.ComplexSearchQuery.QueryElement(
                        "w9_green_leaves_1200_fb4c6e83-abcf-49b6-aad7-a3bd38e03056", "periodName",
                        "\"weekly\"")));

        ComplexSearchQuery complexQuery = new ComplexSearchQuery(query, null, null);

        Page<StatePersistence> results = playerRepo.search(GAME, complexQuery, defaultPageable);
        List<StatePersistence> states = results.getContent();
        Assert.assertEquals(3, states.size());
    }

    @Test
    public void structuredSort() {
        setupEnv();

        StructuredElement element =
                new StructuredElement("pointConcept", "green leaves", null, null, "score");
        StructuredSortItem sort = new StructuredSortItem(element,
                eu.trentorise.game.model.core.ComplexSearchQuery.StructuredSortItem.Direction.DESC);
        ComplexSearchQuery complexQuery = new ComplexSearchQuery(null, null, Arrays.asList(sort));
        Page<StatePersistence> results = playerRepo.search(GAME, complexQuery, defaultPageable);
        Assert.assertEquals("24131", results.getContent().get(0).getPlayerId());
        Assert.assertEquals("24153", results.getContent().get(1).getPlayerId());
        Assert.assertEquals("24100", results.getContent().get(2).getPlayerId());
    }

    @Test
    public void structuredProjection() {
        setupEnv();

        StructuredElement element = new StructuredElement("general", null, null, null, "playerId");
        StructuredElement element1 =
                new StructuredElement("pointConcept", "green leaves", null, null, "score");
        StructuredProjection proj =
                new StructuredProjection(Arrays.asList(element, element1), null);
        ComplexSearchQuery complexQuery = new ComplexSearchQuery(null, proj, null);
        Page<StatePersistence> results = playerRepo.search(GAME, complexQuery, defaultPageable);
        Assert.assertNotNull(results.getContent().get(0).getPlayerId());
        Assert.assertNull(results.getContent().get(0).getGameId());
    }

    private void setupEnv() {
        Game g = new Game(GAME);
        g.setDomain(DOMAIN);
        g.setConcepts(new HashSet<GameConcept>());
        PointConcept pc = new PointConcept("green leaves");
        pc.addPeriod("weekly", new LocalDate(2016, 9, 3).toDate(), 7 * 24 * 60 * 60 * 1000);
        g.getConcepts().add(pc);
        gameSrv.saveGameDefinition(g);
        String state1 = "{\n" + "    \"gameId\" : \"repo-game\",\n"
                + "    \"playerId\" : \"24153\",\n" + "    \"concepts\" : {\n"
                + "        \"BadgeCollectionConcept\" : {\n"
                + "            \"public transport aficionado\" : {\n"
                + "                \"_class\" : \"eu.trentorise.game.repo.GenericObjectPersistence\",\n"
                + "                \"obj\" : {\n" + "                    \"id\" : \"1\",\n"
                + "                    \"name\" : \"public transport aficionado\",\n"
                + "                    \"badgeEarned\" : [ \n"
                + "                        \"5_pt_trip\", \n"
                + "                        \"10_pt_trip\", \n"
                + "                        \"25_pt_trip\", \n"
                + "                        \"50_pt_trip\", \n"
                + "                        \"100_pt_trip\"\n" + "                    ]\n"
                + "                },\n"
                + "                \"type\" : \"eu.trentorise.game.model.BadgeCollectionConcept\"\n"
                + "            },\n" + "            \"leaderboard top 3\" : {\n"
                + "                \"_class\" : \"eu.trentorise.game.repo.GenericObjectPersistence\",\n"
                + "                \"obj\" : {\n" + "                    \"id\" : \"1\",\n"
                + "                    \"name\" : \"leaderboard top 3\",\n"
                + "                    \"badgeEarned\" : [ \n"
                + "                        \"1st_of_the_week\", \n"
                + "                        \"2nd_of_the_week\", \n"
                + "                        \"3rd_of_the_week\"\n" + "                    ]\n"
                + "                },\n"
                + "                \"type\" : \"eu.trentorise.game.model.BadgeCollectionConcept\"\n"
                + "            },\n" + "            \"green leaves\" : {\n"
                + "                \"_class\" : \"eu.trentorise.game.repo.GenericObjectPersistence\",\n"
                + "                \"obj\" : {\n" + "                    \"id\" : \"1\",\n"
                + "                    \"name\" : \"green leaves\",\n"
                + "                    \"badgeEarned\" : [ \n"
                + "                        \"50_point_green\", \n"
                + "                        \"100_point_green\", \n"
                + "                        \"200_point_green\", \n"
                + "                        \"400_point_green\", \n"
                + "                        \"800_point_green\", \n"
                + "                        \"1500_point_green\", \n"
                + "                        \"2500_point_green\", \n"
                + "                        \"5000_point_green\", \n"
                + "                        \"10000_point_green\", \n"
                + "                        \"20000_point_green\"\n" + "                    ]\n"
                + "                },\n"
                + "                \"type\" : \"eu.trentorise.game.model.BadgeCollectionConcept\"\n"
                + "            }\n" + "        },\n" + "        \"PointConcept\" : {\n"
                + "            \"BikeSharing_Km\" : {\n"
                + "                \"_class\" : \"eu.trentorise.game.repo.GenericObjectPersistence\",\n"
                + "                \"obj\" : {\n" + "                    \"id\" : \"1\",\n"
                + "                    \"name\" : \"BikeSharing_Km\",\n"
                + "                    \"score\" : 0.0,\n" + "                    \"periods\" : {\n"
                + "                        \"weekly\" : {\n"
                + "                            \"start\" : 1472853600000,\n"
                + "                            \"period\" : 604800000,\n"
                + "                            \"identifier\" : \"weekly\",\n"
                + "                            \"instances\" : {}\n" + "                        }\n"
                + "                    }\n" + "                },\n"
                + "                \"type\" : \"eu.trentorise.game.model.PointConcept\"\n"
                + "            },\n" + "            \"green leaves\" : {\n"
                + "                \"_class\" : \"eu.trentorise.game.repo.GenericObjectPersistence\",\n"
                + "                \"obj\" : {\n" + "                    \"id\" : \"1\",\n"
                + "                    \"name\" : \"green leaves\",\n"
                + "                    \"score\" : 21020.0,\n"
                + "                    \"periods\" : {\n"
                + "                        \"weekly\" : {\n"
                + "                            \"start\" : 1472853600000,\n"
                + "                            \"period\" : 604800000,\n"
                + "                            \"identifier\" : \"weekly\",\n"
                + "                            \"instances\" : {\n"
                + "                                \"2016-09-03T00:00:00\" : {\n"
                + "                                    \"score\" : 0.0,\n"
                + "                                    \"index\" : 0,\n"
                + "                                    \"end\" : 1473458400000,\n"
                + "                                    \"start\" : 1472853600000\n"
                + "                                },\n"
                + "                                \"2016-09-10T00:00:00\" : {\n"
                + "                                    \"score\" : 720.0,\n"
                + "                                    \"index\" : 1,\n"
                + "                                    \"end\" : 1474063200000,\n"
                + "                                    \"start\" : 1473458400000\n"
                + "                                },\n"
                + "                                \"2016-09-17T00:00:00\" : {\n"
                + "                                    \"score\" : 2962.0,\n"
                + "                                    \"index\" : 2,\n"
                + "                                    \"end\" : 1474668000000,\n"
                + "                                    \"start\" : 1474063200000\n"
                + "                                },\n"
                + "                                \"2016-09-24T00:00:00\" : {\n"
                + "                                    \"score\" : 2590.0,\n"
                + "                                    \"index\" : 3,\n"
                + "                                    \"end\" : 1475272800000,\n"
                + "                                    \"start\" : 1474668000000\n"
                + "                                }\n" + "                            }\n"
                + "                        }\n" + "                    }\n" + "                },\n"
                + "                \"type\" : \"eu.trentorise.game.model.PointConcept\"\n"
                + "            }\n" + "        },\n" + "        \"ChallengeConcept\" : {\n"
                + "            \"w9_green_leaves_1200_fb4c6e83-abcf-49b6-aad7-a3bd38e03056\" : {\n"
                + "                \"_class\" : \"eu.trentorise.game.repo.GenericObjectPersistence\",\n"
                + "                \"obj\" : {\n"
                + "                    \"name\" : \"w9_green_leaves_1200_fb4c6e83-abcf-49b6-aad7-a3bd38e03056\",\n"
                + "                    \"modelName\" : \"absoluteIncrement\",\n"
                + "                    \"fields\" : {\n"
                + "                        \"bonusScore\" : 150.0,\n"
                + "                        \"periodName\" : \"weekly\",\n"
                + "                        \"bonusPointType\" : \"green leaves\",\n"
                + "                        \"counterName\" : \"green leaves\",\n"
                + "                        \"target\" : 1200.0\n" + "                    },\n"
                + "                    \"start\" : 1478300400000,\n"
                + "                    \"end\" : 1478905200000,\n"
                + "                    \"completed\" : true,\n"
                + "                    \"dateCompleted\" : 1478888496028\n" + "                },\n"
                + "                \"type\" : \"eu.trentorise.game.model.ChallengeConcept\"\n"
                + "            },\n"
                + "            \"w2_zero_impact_8_eeec1d4d-74a8-47fd-b506-d0cc53008cd7\" : {\n"
                + "                \"_class\" : \"eu.trentorise.game.repo.GenericObjectPersistence\",\n"
                + "                \"obj\" : {\n"
                + "                    \"name\" : \"w2_zero_impact_8_eeec1d4d-74a8-47fd-b506-d0cc53008cd7\",\n"
                + "                    \"modelName\" : \"absoluteIncrement\",\n"
                + "                    \"fields\" : {\n"
                + "                        \"bonusScore\" : 100.0,\n"
                + "                        \"periodName\" : \"weekly\",\n"
                + "                        \"bonusPointType\" : \"green leaves\",\n"
                + "                        \"counterName\" : \"ZeroImpact_Trips\",\n"
                + "                        \"target\" : 8.0\n" + "                    },\n"
                + "                    \"start\" : 1474063200000,\n"
                + "                    \"end\" : 1474668000000,\n"
                + "                    \"completed\" : true,\n"
                + "                    \"dateCompleted\" : 1474129837683\n" + "                },\n"
                + "                \"type\" : \"eu.trentorise.game.model.ChallengeConcept\"\n"
                + "            }\n" + "        }\n" + "    },\n" + "    \"customData\" : {\n"
                + "        \"final_survey_complete\" : true\n" + "    },\n"
                + "    \"metadata\" : {}\n" + "}\n";
        String state2 = "{\n" + "    \"gameId\" : \"repo-game\",\n"
                + "    \"playerId\" : \"24100\",\n" + "    \"concepts\" : {\n"
                + "        \"BadgeCollectionConcept\" : {\n"
                + "            \"public transport aficionado\" : {\n"
                + "                \"_class\" : \"eu.trentorise.game.repo.GenericObjectPersistence\",\n"
                + "                \"obj\" : {\n" + "                    \"id\" : \"1\",\n"
                + "                    \"name\" : \"public transport aficionado\",\n"
                + "                    \"badgeEarned\" : [ \n"
                + "                        \"5_pt_trip\", \n"
                + "                        \"10_pt_trip\", \n"
                + "                        \"25_pt_trip\", \n"
                + "                        \"50_pt_trip\", \n"
                + "                        \"100_pt_trip\"\n" + "                    ]\n"
                + "                },\n"
                + "                \"type\" : \"eu.trentorise.game.model.BadgeCollectionConcept\"\n"
                + "            },\n" + "            \"leaderboard top 3\" : {\n"
                + "                \"_class\" : \"eu.trentorise.game.repo.GenericObjectPersistence\",\n"
                + "                \"obj\" : {\n" + "                    \"id\" : \"1\",\n"
                + "                    \"name\" : \"leaderboard top 3\",\n"
                + "                    \"badgeEarned\" : [ \n"
                + "                        \"1st_of_the_week\", \n"
                + "                        \"2nd_of_the_week\", \n"
                + "                        \"3rd_of_the_week\"\n" + "                    ]\n"
                + "                },\n"
                + "                \"type\" : \"eu.trentorise.game.model.BadgeCollectionConcept\"\n"
                + "            },\n" + "            \"green leaves\" : {\n"
                + "                \"_class\" : \"eu.trentorise.game.repo.GenericObjectPersistence\",\n"
                + "                \"obj\" : {\n" + "                    \"id\" : \"1\",\n"
                + "                    \"name\" : \"green leaves\",\n"
                + "                    \"badgeEarned\" : [ \n"
                + "                        \"50_point_green\", \n"
                + "                        \"100_point_green\"" + "                    ]\n"
                + "                },\n"
                + "                \"type\" : \"eu.trentorise.game.model.BadgeCollectionConcept\"\n"
                + "            }\n" + "        },\n" + "        \"PointConcept\" : {\n"
                + "            \"BikeSharing_Km\" : {\n"
                + "                \"_class\" : \"eu.trentorise.game.repo.GenericObjectPersistence\",\n"
                + "                \"obj\" : {\n" + "                    \"id\" : \"1\",\n"
                + "                    \"name\" : \"BikeSharing_Km\",\n"
                + "                    \"score\" : 20.0,\n"
                + "                    \"periods\" : {\n"
                + "                        \"weekly\" : {\n"
                + "                            \"start\" : 1472853600000,\n"
                + "                            \"period\" : 604800000,\n"
                + "                            \"identifier\" : \"weekly\",\n"
                + "                            \"instances\" : {}\n" + "                        }\n"
                + "                    }\n" + "                },\n"
                + "                \"type\" : \"eu.trentorise.game.model.PointConcept\"\n"
                + "            },\n" + "            \"green leaves\" : {\n"
                + "                \"_class\" : \"eu.trentorise.game.repo.GenericObjectPersistence\",\n"
                + "                \"obj\" : {\n" + "                    \"id\" : \"1\",\n"
                + "                    \"name\" : \"green leaves\",\n"
                + "                    \"score\" : 2102.0,\n"
                + "                    \"periods\" : {\n"
                + "                        \"weekly\" : {\n"
                + "                            \"start\" : 1472853600000,\n"
                + "                            \"period\" : 604800000,\n"
                + "                            \"identifier\" : \"weekly\",\n"
                + "                            \"instances\" : {\n"
                + "                                \"2016-09-03T00:00:00\" : {\n"
                + "                                    \"score\" : 0.0,\n"
                + "                                    \"index\" : 0,\n"
                + "                                    \"end\" : 1473458400000,\n"
                + "                                    \"start\" : 1472853600000\n"
                + "                                },\n"
                + "                                \"2016-09-10T00:00:00\" : {\n"
                + "                                    \"score\" : 2220.0,\n"
                + "                                    \"index\" : 1,\n"
                + "                                    \"end\" : 1474063200000,\n"
                + "                                    \"start\" : 1473458400000\n"
                + "                                },\n"
                + "                                \"2016-09-17T00:00:00\" : {\n"
                + "                                    \"score\" : 2962.0,\n"
                + "                                    \"index\" : 2,\n"
                + "                                    \"end\" : 1474668000000,\n"
                + "                                    \"start\" : 1474063200000\n"
                + "                                },\n"
                + "                                \"2016-09-24T00:00:00\" : {\n"
                + "                                    \"score\" : 2590.0,\n"
                + "                                    \"index\" : 3,\n"
                + "                                    \"end\" : 1475272800000,\n"
                + "                                    \"start\" : 1474668000000\n"
                + "                                }\n" + "                            }\n"
                + "                        }\n" + "                    }\n" + "                },\n"
                + "                \"type\" : \"eu.trentorise.game.model.PointConcept\"\n"
                + "            }\n" + "        },\n" + "        \"ChallengeConcept\" : {\n"
                + "            \"w9_green_leaves_1200_fb4c6e83-abcf-49b6-aad7-a3bd38e03056\" : {\n"
                + "                \"_class\" : \"eu.trentorise.game.repo.GenericObjectPersistence\",\n"
                + "                \"obj\" : {\n"
                + "                    \"name\" : \"w9_green_leaves_1200_fb4c6e83-abcf-49b6-aad7-a3bd38e03056\",\n"
                + "                    \"modelName\" : \"absoluteIncrement\",\n"
                + "                    \"fields\" : {\n"
                + "                        \"bonusScore\" : 150.0,\n"
                + "                        \"periodName\" : \"weekly\",\n"
                + "                        \"bonusPointType\" : \"green leaves\",\n"
                + "                        \"counterName\" : \"green leaves\",\n"
                + "                        \"target\" : 1200.0\n" + "                    },\n"
                + "                    \"start\" : 1478300400000,\n"
                + "                    \"end\" : 1478905200000,\n"
                + "                    \"completed\" : true,\n"
                + "                    \"dateCompleted\" : 1478888496028\n" + "                },\n"
                + "                \"type\" : \"eu.trentorise.game.model.ChallengeConcept\"\n"
                + "            },\n"
                + "            \"w2_zero_impact_8_eeec1d4d-74a8-47fd-b506-d0cc53008cd7\" : {\n"
                + "                \"_class\" : \"eu.trentorise.game.repo.GenericObjectPersistence\",\n"
                + "                \"obj\" : {\n"
                + "                    \"name\" : \"w2_zero_impact_8_eeec1d4d-74a8-47fd-b506-d0cc53008cd7\",\n"
                + "                    \"modelName\" : \"absoluteIncrement\",\n"
                + "                    \"fields\" : {\n"
                + "                        \"bonusScore\" : 100.0,\n"
                + "                        \"periodName\" : \"weekly\",\n"
                + "                        \"bonusPointType\" : \"green leaves\",\n"
                + "                        \"counterName\" : \"ZeroImpact_Trips\",\n"
                + "                        \"target\" : 8.0\n" + "                    },\n"
                + "                    \"start\" : 1474063200000,\n"
                + "                    \"end\" : 1474668000000,\n"
                + "                    \"completed\" : true,\n"
                + "                    \"dateCompleted\" : 1474129837683\n" + "                },\n"
                + "                \"type\" : \"eu.trentorise.game.model.ChallengeConcept\"\n"
                + "            }\n" + "        }\n" + "    },\n" + "    \"customData\" : {\n"
                + "        \"final_survey_complete\" : false\n" + "    },\n"
                + "    \"metadata\" : {}\n" + "}\n" + "";
        String state3 = "{\n" + "    \"gameId\" : \"repo-game\",\n"
                + "    \"playerId\" : \"24131\",\n" + "    \"concepts\" : {\n"
                + "        \"BadgeCollectionConcept\" : {\n"
                + "            \"public transport aficionado\" : {\n"
                + "                \"_class\" : \"eu.trentorise.game.repo.GenericObjectPersistence\",\n"
                + "                \"obj\" : {\n" + "                    \"id\" : \"1\",\n"
                + "                    \"name\" : \"public transport aficionado\",\n"
                + "                    \"badgeEarned\" : [ \n"
                + "                        \"5_pt_trip\", \n"
                + "                        \"10_pt_trip\", \n"
                + "                        \"25_pt_trip\", \n"
                + "                        \"50_pt_trip\", \n"
                + "                        \"100_pt_trip\"\n" + "                    ]\n"
                + "                },\n"
                + "                \"type\" : \"eu.trentorise.game.model.BadgeCollectionConcept\"\n"
                + "            },\n" + "            \"leaderboard top 3\" : {\n"
                + "                \"_class\" : \"eu.trentorise.game.repo.GenericObjectPersistence\",\n"
                + "                \"obj\" : {\n" + "                    \"id\" : \"1\",\n"
                + "                    \"name\" : \"leaderboard top 3\",\n"
                + "                    \"badgeEarned\" : [ \n"
                + "                        \"1st_of_the_week\", \n"
                + "                        \"2nd_of_the_week\", \n"
                + "                        \"3rd_of_the_week\"\n" + "                    ]\n"
                + "                },\n"
                + "                \"type\" : \"eu.trentorise.game.model.BadgeCollectionConcept\"\n"
                + "            },\n" + "            \"green leaves\" : {\n"
                + "                \"_class\" : \"eu.trentorise.game.repo.GenericObjectPersistence\",\n"
                + "                \"obj\" : {\n" + "                    \"id\" : \"1\",\n"
                + "                    \"name\" : \"green leaves\",\n"
                + "                    \"badgeEarned\" : [ \n"
                + "                        \"50_point_green\", \n"
                + "                        \"100_point_green\", \n"
                + "                        \"200_point_green\", \n"
                + "                        \"400_point_green\"" + "                    ]\n"
                + "                },\n"
                + "                \"type\" : \"eu.trentorise.game.model.BadgeCollectionConcept\"\n"
                + "            }\n" + "        },\n" + "        \"PointConcept\" : {\n"
                + "            \"BikeSharing_Km\" : {\n"
                + "                \"_class\" : \"eu.trentorise.game.repo.GenericObjectPersistence\",\n"
                + "                \"obj\" : {\n" + "                    \"id\" : \"1\",\n"
                + "                    \"name\" : \"BikeSharing_Km\",\n"
                + "                    \"score\" : 50.0,\n"
                + "                    \"periods\" : {\n"
                + "                        \"weekly\" : {\n"
                + "                            \"start\" : 1472853600000,\n"
                + "                            \"period\" : 604800000,\n"
                + "                            \"identifier\" : \"weekly\",\n"
                + "                            \"instances\" : {}\n" + "                        }\n"
                + "                    }\n" + "                },\n"
                + "                \"type\" : \"eu.trentorise.game.model.PointConcept\"\n"
                + "            },\n" + "            \"green leaves\" : {\n"
                + "                \"_class\" : \"eu.trentorise.game.repo.GenericObjectPersistence\",\n"
                + "                \"obj\" : {\n" + "                    \"id\" : \"1\",\n"
                + "                    \"name\" : \"green leaves\",\n"
                + "                    \"score\" : 22020.0,\n"
                + "                    \"periods\" : {\n"
                + "                        \"weekly\" : {\n"
                + "                            \"start\" : 1472853600000,\n"
                + "                            \"period\" : 604800000,\n"
                + "                            \"identifier\" : \"weekly\",\n"
                + "                            \"instances\" : {\n"
                + "                                \"2016-09-03T00:00:00\" : {\n"
                + "                                    \"score\" : 0.0,\n"
                + "                                    \"index\" : 0,\n"
                + "                                    \"end\" : 1473458400000,\n"
                + "                                    \"start\" : 1472853600000\n"
                + "                                },\n"
                + "                                \"2016-09-10T00:00:00\" : {\n"
                + "                                    \"score\" : 1720.0,\n"
                + "                                    \"index\" : 1,\n"
                + "                                    \"end\" : 1474063200000,\n"
                + "                                    \"start\" : 1473458400000\n"
                + "                                },\n"
                + "                                \"2016-09-17T00:00:00\" : {\n"
                + "                                    \"score\" : 2962.0,\n"
                + "                                    \"index\" : 2,\n"
                + "                                    \"end\" : 1474668000000,\n"
                + "                                    \"start\" : 1474063200000\n"
                + "                                },\n"
                + "                                \"2016-09-24T00:00:00\" : {\n"
                + "                                    \"score\" : 2590.0,\n"
                + "                                    \"index\" : 3,\n"
                + "                                    \"end\" : 1475272800000,\n"
                + "                                    \"start\" : 1474668000000\n"
                + "                                }\n" + "                            }\n"
                + "                        }\n" + "                    }\n" + "                },\n"
                + "                \"type\" : \"eu.trentorise.game.model.PointConcept\"\n"
                + "            }\n" + "        },\n" + "        \"ChallengeConcept\" : {\n"
                + "            \"w9_green_leaves_1200_fb4c6e83-abcf-49b6-aad7-a3bd38e03056\" : {\n"
                + "                \"_class\" : \"eu.trentorise.game.repo.GenericObjectPersistence\",\n"
                + "                \"obj\" : {\n"
                + "                    \"name\" : \"w9_green_leaves_1200_fb4c6e83-abcf-49b6-aad7-a3bd38e03056\",\n"
                + "                    \"modelName\" : \"absoluteIncrement\",\n"
                + "                    \"fields\" : {\n"
                + "                        \"bonusScore\" : 150.0,\n"
                + "                        \"periodName\" : \"weekly\",\n"
                + "                        \"bonusPointType\" : \"green leaves\",\n"
                + "                        \"counterName\" : \"green leaves\",\n"
                + "                        \"target\" : 1200.0\n" + "                    },\n"
                + "                    \"start\" : 1478300400000,\n"
                + "                    \"end\" : 1478905200000,\n"
                + "                    \"completed\" : true,\n"
                + "                    \"dateCompleted\" : 1478888496028\n" + "                },\n"
                + "                \"type\" : \"eu.trentorise.game.model.ChallengeConcept\"\n"
                + "            },\n"
                + "            \"w2_zero_impact_8_eeec1d4d-74a8-47fd-b506-d0cc53008cd7\" : {\n"
                + "                \"_class\" : \"eu.trentorise.game.repo.GenericObjectPersistence\",\n"
                + "                \"obj\" : {\n"
                + "                    \"name\" : \"w2_zero_impact_8_eeec1d4d-74a8-47fd-b506-d0cc53008cd7\",\n"
                + "                    \"modelName\" : \"absoluteIncrement\",\n"
                + "                    \"fields\" : {\n"
                + "                        \"bonusScore\" : 100.0,\n"
                + "                        \"periodName\" : \"weekly\",\n"
                + "                        \"bonusPointType\" : \"green leaves\",\n"
                + "                        \"counterName\" : \"ZeroImpact_Trips\",\n"
                + "                        \"target\" : 8.0\n" + "                    },\n"
                + "                    \"start\" : 1474063200000,\n"
                + "                    \"end\" : 1474668000000,\n"
                + "                    \"completed\" : false\n" + "                    \n"
                + "                },\n"
                + "                \"type\" : \"eu.trentorise.game.model.ChallengeConcept\"\n"
                + "            }\n" + "        }\n" + "    },\n" + "    \"customData\" : {\n"
                + "        \"final_survey_complete\" : true\n" + "    },\n"
                + "    \"metadata\" : {}\n" + "}\n" + "";

        mongo.getCollection("playerState").insertOne(Document.parse(state1));
        mongo.getCollection("playerState").insertOne(Document.parse(state2));
        mongo.getCollection("playerState").insertOne(Document.parse(state3));
    }

}
