package eu.trentorise.game.repo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import eu.trentorise.game.model.GroupChallenge;

public class GroupChallengeRepoImpl implements ExtendedGroupChallengeRepo {

    @Autowired
    private MongoTemplate mongo;

    @Override
    public List<GroupChallenge> playerGroupChallenges(String gameId, String playerId) {
        Criteria crit = new Criteria("gameId").is(gameId).and("attendees.playerId").is(playerId);
        return mongo.find(new Query(crit), GroupChallenge.class);

    }

}
