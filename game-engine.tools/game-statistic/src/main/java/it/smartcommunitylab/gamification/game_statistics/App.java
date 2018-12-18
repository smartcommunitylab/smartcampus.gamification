package it.smartcommunitylab.gamification.game_statistics;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joda.time.Interval;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoFactoryBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.google.common.math.Quantiles;
import com.mongodb.Mongo;

import eu.trentorise.game.managers.ClassificationUtils;
import eu.trentorise.game.model.GameStatistics;
import eu.trentorise.game.repo.StatePersistence;

public class App {

    private static final String GAME_ID = "5b7a885149c95d50c5f9d442";
    private static final String PERIOD_NAME = "weekly";

    private static final long START_DATE = 1539986400000L;
    private static final long PERIOD = 604800000;


    private static final String MONGO_HOST = "localhost";
    private static final int MONGO_PORT = 27017;
    private static final String DB_NAME = "gamification";

    private static final Set<String> scoreNames = new HashSet<>();

    static {
        scoreNames.add("green leaves");
        scoreNames.add("Walk_Km");
        scoreNames.add("Bike_Km");
        scoreNames.add("Bus_Trips");
        scoreNames.add("Train_Trips");
    }


    public static void main(String[] args) {
        MongoTemplate mongoTemplate = getInstance();
        long now = System.currentTimeMillis();
        for (String pointConceptName : scoreNames) {
            LocalDateTime cursorDate = new LocalDateTime(START_DATE);
            final Period period = new Period(PERIOD);
            Interval interval = null;
            do {
                interval = new Interval(cursorDate.toDateTime(),
                        cursorDate.withPeriodAdded(period, 1).toDateTime());
                cursorDate = interval.getEnd().toLocalDateTime();

                String key = interval.getStart().toString("yyyy-MM-dd'T'HH:mm:ss");

                Query query = new Query();
                Criteria criteria = new Criteria("gameId").is(GAME_ID);
                query.addCriteria(criteria);
                query.fields().include("concepts.PointConcept." + pointConceptName + ".obj.periods."
                        + PERIOD_NAME + ".instances." + key + ".score");

                List<StatePersistence> pStates = mongoTemplate.find(query, StatePersistence.class);

                double[] data = new double[pStates.size()];
                for (int i = 0; i < pStates.size(); i++) {
                    data[i] =
                            pStates.get(i).getIncrementalScore(pointConceptName, PERIOD_NAME, key);
                }

                // average.
                double average = Arrays.stream(data).average().getAsDouble();
                // variance.
                double variance = ClassificationUtils.calculateVariance(data);
                // 10 quantiles.
                Map<Integer, Double> q =
                        Quantiles.scale(10).indexes(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).compute(data);

                Query qGameStats = new Query();
                Criteria cGameStats = new Criteria("gameId").is(GAME_ID).and("pointConceptName")
                        .is(pointConceptName).and("periodName").is(PERIOD_NAME).and("periodIndex")
                        .is(key);
                qGameStats.addCriteria(cGameStats);

                Update update = new Update();
                update.set("gameId", GAME_ID);
                update.set("pointConceptName", pointConceptName);
                update.set("periodName", PERIOD_NAME);
                update.set("periodIndex", key);
                update.set("startDate", interval.getStart().toDate().getTime());
                update.set("endDate", interval.getEnd().toDate().getTime());
                update.set("average", average);
                update.set("variance", variance);
                update.set("quantiles", q);
                update.set("lastUpdated", System.currentTimeMillis());

                FindAndModifyOptions options = new FindAndModifyOptions();
                options.upsert(true);
                options.returnNew(true);
                mongoTemplate.findAndModify(qGameStats, update, options, GameStatistics.class);
                System.out.println(String.format("Calculated statistic on key %s score %s", key,
                        pointConceptName));
            } while (!interval.contains(now));
        }
        System.out.println("end statistics calculation");
    }

    private static MongoTemplate getInstance() {
        MongoFactoryBean mongo = new MongoFactoryBean();
        mongo.setHost(MONGO_HOST);
        mongo.setPort(MONGO_PORT);
        try {
            mongo.afterPropertiesSet();
            Mongo mongoObj = mongo.getObject();
            MongoTemplate mongoTemplate = new MongoTemplate(
                    new SimpleMongoDbFactory(mongoObj, DB_NAME));
            return mongoTemplate;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
