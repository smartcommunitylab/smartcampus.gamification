package it.smartcommunitylab.gamification.log2elastic.analyzer;

import org.apache.log4j.Logger;

import it.smartcommunitylab.gamification.log2elastic.Record;

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
		case USERCREATION:
			analyzer = new UserCreationRecordAnalyzer(record);
			logger.debug("created UserCreation analyzer");
			break;
		case CHALLENGECOMPLETED:
			analyzer = new ChallengeCompletedRecordAnalyzer(record);
			logger.debug("created ChallengeCompleted analyzer");
			break;
		case CHALLENGEASSIGNED:
			analyzer = new ChallengeAssignedRecordAnalyzer(record);
			logger.debug("created ChallengeAssigned analyzer");
			break;
		default:
			String msg = String.format("Format type %s is not supported", record.getType());
			logger.error(msg);
			throw new IllegalArgumentException(msg);
		}
		return analyzer;
	}
}
