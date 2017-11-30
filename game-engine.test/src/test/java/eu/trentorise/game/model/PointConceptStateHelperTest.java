package eu.trentorise.game.model;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

import org.junit.Assert;
import org.junit.Test;

import eu.trentorise.game.model.PointConcept.PeriodInstance;

public class PointConceptStateHelperTest {

    @Test(expected = IllegalArgumentException.class)
    public void definition_not_exist() {
        Game game = new Game();
        PointConceptStateHelperFactory helper = new PointConceptStateHelperFactory();
        PointConcept p = helper.instanceOf(game, "green").build();
    }


    @Test
    public void found_definition() {
        Game game = new Game();
        PointConcept definitionGreen = new PointConcept("green");
        PointConcept definitionYellow = new PointConcept("yellow");
        game.setConcepts(new HashSet<>(Arrays.asList(definitionGreen, definitionYellow)));

        PointConceptStateHelperFactory helper = new PointConceptStateHelperFactory();
        PointConcept p = helper.instanceOf(game, "green").build();
        Assert.assertNotNull(p);
    }

    @Test
    public void set_score_in_time_when_there_are_not_periods_in_definition() {
        Game game = new Game();
        PointConcept definitionGreen = new PointConcept("green");
        game.setConcepts(new HashSet<>(Arrays.asList(definitionGreen)));

        PointConceptStateHelperFactory helper = new PointConceptStateHelperFactory();
        Date moment = Date
                .from(LocalDate.of(2017, 11, 28).atStartOfDay(ZoneId.systemDefault()).toInstant());
        PointConcept p = helper.instanceOf(game, "green").setScoreInTime(moment, 10d).build();
        Assert.assertNotNull(p);
    }

    @Test
    public void set_multiple_scores_in_time() {
        Game game = new Game();
        PointConcept definitionGreen = new PointConcept("green");
        Date start = Date
                .from(LocalDate.of(2017, 11, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        definitionGreen.addPeriod("daily", start, 60 * 60 * 24 * 1000);
        game.setConcepts(new HashSet<>(Arrays.asList(definitionGreen)));

        PointConceptStateHelperFactory helper = new PointConceptStateHelperFactory();
        ZonedDateTime zonedDate = LocalDate.of(2017, 11, 28).atStartOfDay(ZoneId.systemDefault());
        Date moment = Date.from(zonedDate.toInstant());

        PointConcept p = helper.instanceOf(game, "green")
                .setScoreInTime(Date.from(zonedDate.minusDays(2).toInstant()), 4)
                .setScoreInTime(moment, 10d).build();
        PeriodInstance current = p.getPeriodCurrentInstance("daily");
        Assert.assertEquals(Double.valueOf(4d), p.getPeriodScore("daily", current.getIndex() - 2));
    }

}
