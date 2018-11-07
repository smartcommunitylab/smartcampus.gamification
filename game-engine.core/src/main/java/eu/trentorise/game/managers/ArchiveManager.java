package eu.trentorise.game.managers;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import eu.trentorise.game.model.ChallengeConcept;
import eu.trentorise.game.model.GroupChallenge;
import eu.trentorise.game.model.core.ArchivedConcept;

@Service
public class ArchiveManager {

    private static final String CHALLENGE_ARCHIVE_COLLECTION = "challengeArchive";

    @Autowired
    private MongoTemplate mongoTemplate;

    public void moveToArchive(String gameId, String playerId, ChallengeConcept challenge) {
        ArchivedConcept archived = new ArchivedConcept();
        archived.setChallenge(challenge);
        archived.setGameId(gameId);
        archived.setPlayerId(playerId);
        mongoTemplate.save(archived, CHALLENGE_ARCHIVE_COLLECTION);
    }

    public void moveToArchive(String gameId, GroupChallenge challenge) {
        ArchivedConcept archived = new ArchivedConcept();
        archived.setGroupChallenge(challenge);
        archived.setGameId(gameId);
        mongoTemplate.save(archived, CHALLENGE_ARCHIVE_COLLECTION);
    }

    public List<ArchivedConcept> readArchives(String gameId, String playerId, String state,
            Date from, Date to) {
        List<ArchivedConcept> result;

        Query query = new Query();

        Criteria criteria = Criteria.where("gameId").is(gameId);

        if (playerId != null) {
            criteria = criteria.and("playerId").is(playerId);
        }

        if (state != null) {
            criteria = criteria.and("challenge.state").is(state);
        }

        if (from != null && to != null) {
            criteria = criteria.and("archivingDate").gte(from).lte(to);
        } else if (from != null) {
            criteria = criteria.and("archivingDate").gte(from);
        } else if (to != null) {
            criteria = criteria.and("archivingDate").lte(to);
        }

        query.addCriteria(criteria);

        result = mongoTemplate.find(query, ArchivedConcept.class);

        return result;

    }
}
