package it.smartcomunitylab.gamification.log2timescaledb.analyzer;

import org.apache.log4j.Logger;

import it.smartcommunitylab.gamification.log2timescaledb.Record;

public class RecordAnalyzerFactory {

    private static final Logger logger = Logger.getLogger(RecordAnalyzerFactory.class);

    public static RecordAnalyzer getAnalyzer(Record record) {
        RecordAnalyzer analyzer = null;
        switch (record.getType()) {
            case ACTION:
                analyzer = new ActionRecordAnalyzer(record);
                logger.debug("created Action analyzer");
                break;
            case RULE_POINTCONCEPT:
                analyzer = new PointConceptRecordAnalyzer(record);
                logger.debug("created PointConcept analyzer");
                break;
            case CLASSIFICATION:
                analyzer = new ClassificationRecordAnalyzer(record);
                logger.debug("created Classification analyzer");
                break;
            case RULE_BADGECOLLECTIONCONCEPT:
                analyzer = new BadgeCollectionConceptRecordAnalyzer(record);
                logger.debug("created BadgeCollectionConcept analyzer");
                break;
            case USER_CREATION:
                analyzer = new UserCreationRecordAnalyzer(record);
                logger.debug("created UserCreation analyzer");
                break;
            case CHALLENGE_COMPLETED:
                analyzer = new ChallengeCompletedRecordAnalyzer(record);
                logger.debug("created ChallengeCompleted analyzer");
                break;
            case CHALLENGE_ASSIGNED:
                analyzer = new ChallengeAssignedRecordAnalyzer(record);
                logger.debug("created ChallengeAssigned analyzer");
                break;
            case END_GAME_ACTION:
                analyzer = new EndGameRecordAnalyzer(record);
                logger.debug("created EndGameAction analyzer");
                break;
            case LEVEL_GAINED:
                analyzer = new EndGameRecordAnalyzer(record);
                logger.debug("created LevelGained analyzer");
                break;
            case BLACKLIST:
                analyzer = new BlacklistRecordAnalyzer(record);
                logger.debug("created Blacklist analyzer");
                break;
            case UNBLACKLIST:
                analyzer = new UnblacklistRecordAnalyzer(record);
                logger.debug("created Unblacklist analyzer");
                break;
            case CHALLENGE_PROPOSED:
                analyzer = new ChallengeProposedRecordAnalyzer(record);
                logger.debug("created ChallengeProposed analyzer");
                break;
            case CHALLENGE_ACCEPTED:
                analyzer = new ChallengeAcceptedRecordAnalyzer(record);
                logger.debug("created ChallengeAccepted analyzer");
                break;
            case CHALLENGE_REFUSED:
                analyzer = new ChallengeRefusedRecordAnalyzer(record);
                logger.debug("created ChallengeRefused analyzer");
                break;
            case CHALLENGE_FAILED:
                analyzer = new ChallengeFailedRecordAnalyzer(record);
                logger.debug("created ChallengeFailed analyzer");
                break;
            case CHOICE_ACTIVATED:
                analyzer = new ChoiceActivatedRecordAnalyzer(record);
                logger.debug("created ChoiceActivated analyzer");
                break;
            case CHALLENGE_INVITATION:
                analyzer = new ChallengeInvitationRecordAnalyzer(record);
                logger.debug("created ChallengeInvitation analyzer");
                break;
            case CHALLENGE_INVITATION_ACCEPTED:
                analyzer = new ChallengeInvitationAcceptedRecordAnalyzer(record);
                logger.debug("created ChallengeInvitationAccepted analyzer");
                break;
            case CHALLENGE_INVITATION_REFUSED:
                analyzer = new ChallengeInvitationRefusedRecordAnalyzer(record);
                logger.debug("created ChallengeInvitationRefused analyzer");
                break;
            case CHALLENGE_INVITATION_CANCELED:
                analyzer = new ChallengeInvitationCanceledRecordAnalyzer(record);
                logger.debug("created ChallengeInvitationCanceled analyzer");
                break;
            case SURVEY_COMPLETED:
                analyzer = new SurveyCompletedAnalyzer(record);
                logger.debug("created SurveyCompleted analyzer");
                break;
            default:
                String msg = String.format("Format type %s is not supported", record.getType());
                logger.error(msg);
                throw new IllegalArgumentException(msg);
        }
        return analyzer;
    }
}
