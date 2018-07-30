package eu.trentorise.game.model;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Date;

import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.trentorise.game.config.AppConfig;
import eu.trentorise.game.config.MongoConfig;
import eu.trentorise.game.core.Clock;
import eu.trentorise.game.model.ChallengeConcept.ChallengeState;
import eu.trentorise.game.services.PlayerService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, MongoConfig.class},
        loader = AnnotationConfigContextLoader.class)
public class ChallengeConceptTest {

    @Mock
    private Clock clock;

    @Autowired
    private PlayerService playerSrv;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void update_challenge_state() {
        ChallengeConcept challenge = new ChallengeConcept();
        challenge = challenge.updateState(ChallengeState.ACTIVE);
        Assert.assertEquals(ChallengeState.ACTIVE, challenge.getState());
    }


    @Test(expected = IllegalArgumentException.class)
    public void update_to_null_state() {
        ChallengeConcept challenge = new ChallengeConcept();
        challenge = challenge.updateState(null);
    }

    @Test
    public void default_state_of_a_challenge() {
        Date dateOfAssignment = date("2018-07-20T00:12:11");
        BDDMockito.given(clock.now()).willReturn(dateOfAssignment);
        ChallengeConcept challenge = new ChallengeConcept(clock);
        assertThat(challenge.getState(), is(ChallengeState.ASSIGNED));
        assertThat(challenge.getDate(ChallengeState.ASSIGNED), is(dateOfAssignment));
        assertThat(challenge.getDate(ChallengeState.PROPOSED), is(nullValue()));
    }

    @Test
    public void look_the_date_challenge_moved_to_failed() {
        Date dateOfFailure = date("2018-07-20T15:12:11");
        BDDMockito.given(clock.now()).willReturn(dateOfFailure);
        ChallengeConcept challenge = new ChallengeConcept(clock);
        challenge = challenge.updateState(ChallengeState.FAILED);
        assertThat(challenge.getDate(ChallengeState.FAILED), is(dateOfFailure));
    }

    @Test
    public void look_the_date_of_state_never_reached() {
        ChallengeConcept challenge = new ChallengeConcept(clock);
        challenge = challenge.updateState(ChallengeState.ASSIGNED);
        assertThat(challenge.getDate(ChallengeState.FAILED), is(nullValue()));
    }

    @Test
    public void complete_a_challenge() throws InterruptedException {
        Date completionDate = date("2018-07-25T14:22");
        BDDMockito.given(clock.now()).willReturn(completionDate);
        ChallengeConcept challenge = new ChallengeConcept(clock);
        challenge = challenge.updateState(ChallengeState.COMPLETED);
        Assert.assertEquals(true, challenge.isCompleted());
        Assert.assertEquals(completionDate, challenge.getDateCompleted());
    }


    @Test
    public void complete_a_challenge_using_completed_method() {
        Date completionDate = date("2018-07-25T14:22");
        BDDMockito.given(clock.now()).willReturn(completionDate);
        ChallengeConcept challenge = new ChallengeConcept(clock);
        boolean isCompleted = challenge.completed();
        assertThat(isCompleted, is(true));
        assertThat(challenge.getDate(ChallengeState.COMPLETED), is(completionDate));
        assertThat(challenge.getState(), is(ChallengeState.COMPLETED));
    }


    @Test
    public void persist_and_load_challenge() {
        Date activateDate = date("2018-07-25T14:22");
        BDDMockito.given(clock.now()).willReturn(activateDate);
        ChallengeConcept challenge = new ChallengeConcept(clock);
        challenge.setName("ch1");
        challenge.updateState(ChallengeState.ACTIVE);
        
        PlayerState player = new PlayerState("my_game", "my_player");
        player.getState().add(challenge);

        PlayerState saved = playerSrv.saveState(player);
        assertThat(saved.getState(), hasSize(1));
        ChallengeConcept loaded = (ChallengeConcept) saved.getState().stream().findFirst().get();
        assertThat(loaded.getState(), is(ChallengeState.ACTIVE));
        assertThat(loaded.getDate(ChallengeState.ACTIVE), is(activateDate));

    }

    private Date date(String isoDate) {
        return LocalDateTime.parse(isoDate).toDate();
    }

    @Test
    public void load_completed_challenge_before_state_introduction()
            throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        ChallengeConcept challenge =
                mapper.readValue(Thread.currentThread().getContextClassLoader().getResourceAsStream(
                        "concepts/completed-challenge-v2.4.0.json"), ChallengeConcept.class);
        assertThat(challenge.getModelName(), is("absoluteIncrement"));
        assertThat(challenge.getState(), is(ChallengeState.COMPLETED));
        assertThat(challenge.getDate(ChallengeState.COMPLETED),
                is(date("2017-10-21T20:55:59.045")));
        assertThat(challenge.getFields().values(), hasSize(5));
    }


}
