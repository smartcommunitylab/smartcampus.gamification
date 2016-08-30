package eu.trentorise.smartcampus.gamification_web.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import eu.trentorise.smartcampus.gamification_web.models.ChallengeDescriptionData;
import eu.trentorise.smartcampus.gamification_web.models.status.BadgeCollectionConcept;
import eu.trentorise.smartcampus.gamification_web.models.status.ChallengesData;
import eu.trentorise.smartcampus.gamification_web.models.status.PointConcept;
import eu.trentorise.smartcampus.gamification_web.models.status.PointConceptPeriod;
import eu.trentorise.smartcampus.gamification_web.models.status.ServerChallengesData;

public class ChallengesUtils {

	// Class used to read the player personalData and create the correct challenges
	// challeng_keys
	private final String CHAL_K = "ch_";
	private final String CHAL_K_TYPE = "_type";
	private final String CHAL_K_STS = "_startChTs";
	private final String CHAL_K_ETS = "_endChTs";
	private final String CHAL_K_WALKED_KM = "_Km_traveled_during_challenge";
	private final String CHAL_K_EARNED_POINT = "_points_earned_during_challenges";
	private final String CHAL_K_EARNED_POINT_NEW = "gp_current";
	private final String CHAL_K_TARGET = "_target";
	private final String CHAL_K_BONUS = "_bonus";
	private final String CHAL_K_RECOM = "_recommendations_sent_during_challenges";
	private final String CHAL_K_SURVEY = "_survey_sent_during_challenges";
	private final String CHAL_K_SUCCESS = "_success";
	private final String CHAL_K_COUNTER = "_counter";
	private final String CHAL_K_POINT_TYPE = "_point_type";
	private final String CHAL_K_MODE = "_mode";	// possibility: walk, bike, bikesharing, train, bus, car
	private final String CHAL_K_BADGE_COLL_NAME = "_badge_collection";
	// ita chall descriptions
	private final String CHAL_DESC_1 = "Fai almeno altri TARGET km MODE e avrai un bonus di BONUS punti POINT_TYPE";
	private final String CHAL_DESC_2 = "Fai almeno TARGET viaggio senza usare MODE e avrai un bonus di BONUS punti POINT_TYPE";
	private final String CHAL_DESC_3 = "Fai almeno TARGET viaggio MODE e avrai un bonus di BONUS punti POINT_TYPE";
	private final String CHAL_DESC_5 = "Ottieni almeno TARGET punti POINT_TYPE e avrai ulteriori BONUS punti POINT_TYPE in bonus";
	private final String CHAL_DESC_6 = "Ottieni almeno TARGET badge nella Badge Collection BADGE_COLL_NAME e vinci un bonus di BONUS punti POINT_TYPE";
	private final String CHAL_DESC_7 = "Completa una Badge Collection e vinci un bonus di BONUS punti POINT_TYPE";
	private final String CHAL_DESC_8 = "Compila il questionario di fine gioco e guadagni BONUS punti POINT_TYPE";
	private final String CHAL_DESC_9 = "Raccomanda la App ad almeno TARGET utenti e guadagni BONUS punti POINT_TYPE";
	// eng chall descriptions
	private final String CHAL_DESC_1_ENG = "Do at least TARGET more km MODE and you will get a bonus of BONUS POINT_TYPE points";
	private final String CHAL_DESC_2_ENG = "Do at least TARGET trip without MODE and you will get a bonus of BONUS POINT_TYPE points";
	private final String CHAL_DESC_3_ENG = "Do at least TARGET trip MODE and you will get a bonus of BONUS POINT_TYPE points";
	private final String CHAL_DESC_5_ENG = "Get at least TARGET POINT_TYPE points and you will have BONUS additinal POINT_TYPE bonus points";
	private final String CHAL_DESC_6_ENG = "Get at least TARGET badge in BADGE_COLL_NAME Badge Collection and you will get a bonus of BONUS POINT_TYPE points";
	private final String CHAL_DESC_7_ENG = "Complete a Bange Collection and you will get a bonus of BONUS POINT_TYPE points";
	private final String CHAL_DESC_8_ENG = "Fill out the end-game survay and you will gain a bonus of BONUS POINT_TYPE points";
	private final String CHAL_DESC_9_ENG = "Recommend the app at least TARGET users and you will gain a bonus of BONUS POINT_TYPE points";
	
	private final String CHAL_TYPE_1 = "PERCENT";
	private final String CHAL_TYPE_2 = "NEGATEDMODE";
	private final String CHAL_TYPE_3 = "TRIPNUMBER";
	private final String CHAL_TYPE_4 = "ZEROIMPACT";
	private final String CHAL_TYPE_5 = "POINTSEARNED";
	private final String CHAL_TYPE_6 = "NEXTBADGE";
	private final String CHAL_TYPE_7 = "BADGECOLLECTION";
	private final String CHAL_TYPE_8 = "SURVEYDATA";
	private final String CHAL_TYPE_9 = "RECOMMENDATION";

	private final String SERVER_CHAL_ALLOWED_MODE_W = "Walk_";
	private final String SERVER_CHAL_ALLOWED_MODE_BK = "Bike_";
	private final String SERVER_CHAL_ALLOWED_MODE_BKS = "BikeSharing_";
	private final String SERVER_CHAL_ALLOWED_MODE_T = "Train_";
	private final String SERVER_CHAL_ALLOWED_MODE_B = "Bus_";
	private final String SERVER_CHAL_ALLOWED_MODE_C_NEG = "NoCar";
	private final String SERVER_CHAL_ALLOWED_MODE_C = "Car";
	private final String SERVER_CHAL_ALLOWED_MODE_Z = "ZeroImpact";
	private final String SERVER_CHAL_ALLOWED_MODE_P = "Promoted";
	private final String SERVER_CHAL_ALLOWED_MODE_R = "Recommendations";
	private final String SERVER_CHAL_ALLOWED_MODE_PE = "green leaves";
	private final String SERVER_CHAL_ALLOWED_MODE_CAB = "Cable";
	private final String CHAL_ALLOWED_MODE_W = "walk";
	private final String CHAL_ALLOWED_MODE_BK = "bike";
	private final String CHAL_ALLOWED_MODE_BKS = "bikesharing";
	private final String CHAL_ALLOWED_MODE_T = "train";
	private final String CHAL_ALLOWED_MODE_B = "bus";
	private final String CHAL_ALLOWED_MODE_C = "car";
	private final String CHAL_ALLOWED_MODE_C_NEG = "no car";
	private final String CHAL_ALLOWED_MODE_Z = "zeroimpact";
	private final String CHAL_ALLOWED_MODE_P = "promoted";
	private final String CHAL_ALLOWED_MODE_CAB = "cable";
	private final String CHAL_ALLOWED_MODE_W_DIS = "walkDistance";
	private final String CHAL_ALLOWED_MODE_BK_DIS = "bikeDistance";
	private final String CHAL_ALLOWED_MODE_BKS_DIS = "bikesharingDistance";
	private final String CHAL_ALLOWED_MODE_T_DIS = "trainDistance";
	private final String CHAL_ALLOWED_MODE_B_DIS = "busDistance";
	private final String CHAL_ALLOWED_MODE_C_DIS = "carDistance";
	private final String CHAL_ALLOWED_MODE_Z_DIS = "zeroimpactDistance";
	private final String CHAL_ALLOWED_MODE_P_DIS = "promotedDistance";
	private final String CHAL_ALLOWED_PT_GREEN = "green leaves";
	private final String CHAL_ALLOWED_PT_HEALTH = "health";
	private final String CHAL_ALLOWED_PT_PR = "pr";
	private final int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
	//private final long CHAL_TS_OFFSET = 1000 * 60 * 60 * 24 * 7;	// millis in one week
	
	private static final String CHALLENGE_CONCEPT = "ChallengeConcept";
	private static final String CHAL_NAME = "name";
	private static final String CHAL_MODEL_NAME = "modelName";
	private static final String CHAL_DATA_FIELDS = "fields";
	private static final String CHAL_START = "start";
	private static final String CHAL_END = "end";
	private static final String CHAL_COMPLETED = "completed";
	private static final String CHAL_COMPLETED_DATE = "dateCompleted";
	// challange fields
	private static final String CHAL_FIELDS_PERIOD_NAME = "periodName";
	private static final String CHAL_FIELDS_BONUS_POINT_TYPE = "bonusPointType";
	private static final String CHAL_FIELDS_COUNTER_NAME = "counterName";
	private static final String CHAL_FIELDS_BADGE_COLLECTION_NAME = "badgeCollectionName";
	private static final String CHAL_FIELDS_BONUS_SCORE = "bonusScore";
	private static final String CHAL_FIELDS_BASELINE = "baseline";
	private static final String CHAL_FIELDS_TARGET = "target";
	private static final String CHAL_FIELDS_INITIAL_BADGE_NUM = "initialBadgeNum";
	// new challenge types
	private static final String CHAL_MODEL_PERCENTAGE_INC = "percentageIncrement";
	private static final String CHAL_MODEL_ABSOLUTE_INC = "absoluteIncrement";
	private static final String CHAL_MODEL_NEXT_BADGE = "nextBadge";
	private static final String CHAL_MODEL_COMPLETE_BADGE_COLL = "completeBadgeCollection";
	private static final String CHAL_MODEL_SURVEY = "survey";
	private static final String STATE = "state";
	private static final String ITA_LANG = "it";
	private static final String ENG_LANG = "en";
	// week delta in milliseconds
	private static final Long W_DELTA = 2000L;
	
	private static final Logger logger = Logger.getLogger(ChallengesUtils.class);
	
	private List<ChallengeDescriptionData> challLongDescriptionList;
	
	public List<ChallengeDescriptionData> getChallLongDescriptionList() {
		return challLongDescriptionList;
	}

	public void setChallLongDescriptionList(List<ChallengeDescriptionData> challLongDescriptionList) {
		this.challLongDescriptionList = challLongDescriptionList;
	}

	public ChallengesUtils() {
	}
	
	private String getLongDescriptionByChall(String type, String mobMode, String target, String pointType, String language){
		String correctDesc = "";
		List<ChallengeDescriptionData> challDescList = getChallLongDescriptionList();
		if(type.compareTo(CHAL_TYPE_1) == 0){
			if(mobMode.compareTo(CHAL_ALLOWED_MODE_BK) == 0 || mobMode.compareTo(CHAL_ALLOWED_MODE_BK + "Distance") == 0){
				correctDesc = (language.compareTo(ITA_LANG) == 0) ? challDescList.get(0).getDescription() : challDescList.get(0).getDescription_eng();
			} else if(mobMode.compareTo(CHAL_ALLOWED_MODE_BKS) == 0 || mobMode.compareTo(CHAL_ALLOWED_MODE_BKS + "Distance") == 0){
				correctDesc = (language.compareTo(ITA_LANG) == 0) ? challDescList.get(1).getDescription() : challDescList.get(1).getDescription_eng();
			} else if(mobMode.compareTo(CHAL_ALLOWED_MODE_W) == 0 || mobMode.compareTo(CHAL_ALLOWED_MODE_W + "Distance") == 0){
				correctDesc = (language.compareTo(ITA_LANG) == 0) ? challDescList.get(2).getDescription() : challDescList.get(2).getDescription_eng();
			}
		} else if(type.compareTo(CHAL_TYPE_2) == 0){
			if(mobMode.compareTo(CHAL_ALLOWED_MODE_B) == 0 || mobMode.compareTo(CHAL_ALLOWED_MODE_B + "Distance") == 0){
				correctDesc = (language.compareTo(ITA_LANG) == 0) ? challDescList.get(21).getDescription() : challDescList.get(21).getDescription_eng();
			} else if(mobMode.compareTo(CHAL_ALLOWED_MODE_BK) == 0 || mobMode.compareTo(CHAL_ALLOWED_MODE_BK + "Distance") == 0){
				correctDesc = (language.compareTo(ITA_LANG) == 0) ? challDescList.get(23).getDescription() : challDescList.get(23).getDescription_eng();
			} else if(mobMode.compareTo(CHAL_ALLOWED_MODE_BKS) == 0 || mobMode.compareTo(CHAL_ALLOWED_MODE_BKS + "Distance") == 0){
				correctDesc = (language.compareTo(ITA_LANG) == 0) ? challDescList.get(20).getDescription() : challDescList.get(20).getDescription_eng();
			} else if(mobMode.compareTo(CHAL_ALLOWED_MODE_T) == 0 || mobMode.compareTo(CHAL_ALLOWED_MODE_T + "Distance") == 0){
				correctDesc = (language.compareTo(ITA_LANG) == 0) ? challDescList.get(22).getDescription() : challDescList.get(22).getDescription_eng();
			} else if(mobMode.compareTo(CHAL_ALLOWED_MODE_C) == 0 || mobMode.compareTo(CHAL_ALLOWED_MODE_C + "Distance") == 0){
				correctDesc = (language.compareTo(ITA_LANG) == 0) ? challDescList.get(24).getDescription() : challDescList.get(24).getDescription_eng();
			} else if(mobMode.compareTo(CHAL_ALLOWED_MODE_W) == 0 || mobMode.compareTo(CHAL_ALLOWED_MODE_W + "Distance") == 0){
				correctDesc = (language.compareTo(ITA_LANG) == 0) ? challDescList.get(25).getDescription() : challDescList.get(25).getDescription_eng();
			} 
		} else if(type.compareTo(CHAL_TYPE_3) == 0){
			if(mobMode.compareTo(CHAL_ALLOWED_MODE_B) == 0 || mobMode.compareTo(CHAL_ALLOWED_MODE_B + "Distance") == 0){
				correctDesc = (language.compareTo(ITA_LANG) == 0) ? challDescList.get(4).getDescription() : challDescList.get(4).getDescription();
			} else if(mobMode.compareTo(CHAL_ALLOWED_MODE_BK) == 0 || mobMode.compareTo(CHAL_ALLOWED_MODE_BK + "Distance") == 0){
				correctDesc = (language.compareTo(ITA_LANG) == 0) ? challDescList.get(8).getDescription().replace("[bici, bike sharing, bus, treno]","la bici") : challDescList.get(8).getDescription_eng().replace("[bike, bike sharing, bus, train]","the bike");
			} else if(mobMode.compareTo(CHAL_ALLOWED_MODE_BKS) == 0 || mobMode.compareTo(CHAL_ALLOWED_MODE_BKS + "Distance") == 0){
				correctDesc = (language.compareTo(ITA_LANG) == 0) ? challDescList.get(3).getDescription() : challDescList.get(3).getDescription_eng();
			} else if(mobMode.compareTo(CHAL_ALLOWED_MODE_T) == 0 || mobMode.compareTo(CHAL_ALLOWED_MODE_T + "Distance") == 0){
				correctDesc = (language.compareTo(ITA_LANG) == 0) ? challDescList.get(5).getDescription() : challDescList.get(5).getDescription_eng();
			} else if(mobMode.compareTo(CHAL_ALLOWED_MODE_P) == 0 || mobMode.compareTo(CHAL_ALLOWED_MODE_P + "Distance") == 0){
				correctDesc = (language.compareTo(ITA_LANG) == 0) ? challDescList.get(7).getDescription() : challDescList.get(7).getDescription_eng();
			} else if(mobMode.compareTo(CHAL_ALLOWED_MODE_W) == 0 || mobMode.compareTo(CHAL_ALLOWED_MODE_W + "Distance") == 0){
				correctDesc = (language.compareTo(ITA_LANG) == 0) ? challDescList.get(26).getDescription() : challDescList.get(26).getDescription_eng();
			} else if(mobMode.compareTo(CHAL_ALLOWED_MODE_CAB) == 0 || mobMode.compareTo(CHAL_ALLOWED_MODE_CAB + "Distance") == 0){
				correctDesc = (language.compareTo(ITA_LANG) == 0) ? challDescList.get(28).getDescription() : challDescList.get(28).getDescription_eng();
			}
		} else if(type.compareTo(CHAL_TYPE_4) == 0){
			if(mobMode.compareTo(CHAL_ALLOWED_MODE_Z) == 0 || mobMode.compareTo(CHAL_ALLOWED_MODE_Z + "Distance") == 0){
				correctDesc = (language.compareTo(ITA_LANG) == 0) ? challDescList.get(6).getDescription() : challDescList.get(6).getDescription_eng();
			}
		} else if(type.compareTo(CHAL_TYPE_5) == 0){
			correctDesc = (language.compareTo(ITA_LANG) == 0) ? challDescList.get(10).getDescription().replace("X", target).replace("punti [green leaves, bici, salute, impatto 0]", pointType) : challDescList.get(10).getDescription_eng().replace("X", target).replace("[green leaves, bike, ealth, 0 impact] points", pointType);
		} else if(type.compareTo(CHAL_TYPE_6) == 0){
			if(mobMode.compareTo("green leaves") == 0){
				correctDesc = (language.compareTo(ITA_LANG) == 0) ? challDescList.get(14).getDescription() : challDescList.get(14).getDescription_eng();
			} else if(mobMode.compareTo("bike sharing pioneer") == 0){
				correctDesc = (language.compareTo(ITA_LANG) == 0) ? challDescList.get(13).getDescription() : challDescList.get(13).getDescription_eng();
			} else if(mobMode.compareTo("sustainable life") == 0){
				correctDesc = (language.compareTo(ITA_LANG) == 0) ? challDescList.get(15).getDescription() : challDescList.get(15).getDescription_eng();
			} else if(mobMode.compareTo("park and ride pioneer") == 0){
				correctDesc = (language.compareTo(ITA_LANG) == 0) ? challDescList.get(12).getDescription() : challDescList.get(12).getDescription_eng();	// park and ride
			}	
		} else if(type.compareTo(CHAL_TYPE_7) == 0){
			correctDesc = (language.compareTo(ITA_LANG) == 0) ? challDescList.get(19).getDescription() : challDescList.get(19).getDescription_eng();
		} else if(type.compareTo(CHAL_TYPE_8) == 0){
			correctDesc = (language.compareTo(ITA_LANG) == 0) ? challDescList.get(27).getDescription() : challDescList.get(27).getDescription_eng();
		} else if(type.compareTo(CHAL_TYPE_9) == 0){
			int racc_num = Integer.parseInt(target);
			if(racc_num == 1){
				correctDesc = (language.compareTo(ITA_LANG) == 0) ? challDescList.get(9).getDescription().replace("X", target).replace("tuoi amici si devono", "tuo amico si deve") : challDescList.get(9).getDescription_eng().replace("X", target).replace("friends of yours have to", "friend of yours has to");
			} else {
				correctDesc = (language.compareTo(ITA_LANG) == 0) ? challDescList.get(9).getDescription().replace("X", target) : challDescList.get(9).getDescription_eng().replace("X", target);
			}
		}
		return correctDesc;
	}
	
	@SuppressWarnings("rawtypes")
	public List<List> correctCustomData(String profile, int type, String language) throws JSONException{
    	List<String> challIndxArray = new ArrayList<String>();
    	List<ChallengesData> challenges = new ArrayList<ChallengesData>();
    	List<ChallengesData> oldChallenges = new ArrayList<ChallengesData>();
    	List<List> challengesList = new ArrayList<List>();
    	if(profile != null && profile.compareTo("") != 0){
    		
    		JSONObject profileData = new JSONObject(profile);
    		JSONObject customData = profileData.getJSONObject("customData");
    		Iterator keys = customData.keys();
    		while( keys.hasNext() ) {
    		    String key = (String)keys.next();
    		    String value = customData.getString(key);
    		    if(key.contains(CHAL_K)){
    		    	// case key is a challeng key
    		    	String chalString = key.substring(3, key.length());
    		    	String[] partialIndex = chalString.split("_");
    		    	String chal_Indx = partialIndex[0];
    		    	if(challIndxArray.size() == 0){
    		    		challIndxArray.add(chal_Indx);
    		    	} else {
    		    		if(!challIndxArray.contains(chal_Indx)){
    		    			challIndxArray.add(chal_Indx);
    		    		}
    		    	}
    		    	logger.debug("key: " + key + ", value: " + value );
    		    }
    		}
    		
    		for(int i = 0; i < challIndxArray.size(); i++){
    			String ch_id = challIndxArray.get(i);
    			if(ch_id.length()<=39){
    				String id = challIndxArray.get(i);
	    			String ch_type = (!customData.isNull(CHAL_K + ch_id + CHAL_K_TYPE)) ? customData.getString(CHAL_K + ch_id + CHAL_K_TYPE) : CHAL_TYPE_1;
	    			String targetVal = (!customData.isNull(CHAL_K + ch_id + CHAL_K_TARGET)) ? customData.getString(CHAL_K + ch_id + CHAL_K_TARGET) : "0";
	    			int target = 0;
	    			if(targetVal.contains(".")){
	    				try {
	    				Float f_target = Float.parseFloat(targetVal);
	    				target = f_target.intValue();
	    				} catch (Exception ex){
	    					logger.error("String target value error from float");
	    				}
	    			} else {
		    			try {
		    				target = Integer.parseInt(targetVal);
		    			} catch (Exception ex){
		    				logger.error("String target value error from int"); 
		    			}
	    			}
					int bonus = (!customData.isNull(CHAL_K + ch_id + CHAL_K_BONUS)) ? customData.getInt(CHAL_K + ch_id + CHAL_K_BONUS) : 0;
					String endChTs = (!customData.isNull(CHAL_K + ch_id + CHAL_K_ETS)) ? customData.getString(CHAL_K + ch_id + CHAL_K_ETS) : "0";
					String point_type = (!customData.isNull(CHAL_K + ch_id + CHAL_K_POINT_TYPE)) ? customData.getString(CHAL_K + ch_id + CHAL_K_POINT_TYPE) : CHAL_ALLOWED_PT_GREEN;
					long now = System.currentTimeMillis();
					//int daysToEnd = calculateRemainingDays(endChTs, now);
					Boolean success = (!customData.isNull(CHAL_K + ch_id + CHAL_K_SUCCESS)) ? customData.getBoolean(CHAL_K + ch_id + CHAL_K_SUCCESS) : false;
					long endTime = 0L;
					try {
						endTime = Long.parseLong(endChTs);
					} catch (Exception ex){
						if(endChTs.contains("E")){
							String[] completeEnd = endChTs.split("E");
							String newEndChTs = completeEnd[0];
							String powedVal = completeEnd[1];
							double powedNum = Double.parseDouble(powedVal);
							double doubleValue = Double.parseDouble(newEndChTs);
							double powVal = Math.pow(10d, powedNum);
							endTime = (long)(doubleValue * powVal);
							logger.debug("Converted E value to new long " + endTime);
						} else {
							endTime = 0L; 
							logger.error(ex.getMessage());
						}
					}
					long startTime = 0L;
					startTime = (!customData.isNull(CHAL_K + ch_id + CHAL_K_STS)) ? customData.getLong(CHAL_K + ch_id + CHAL_K_STS) : 0L;
					int daysToEnd = calculateRemainingDays(endTime, now);
					Boolean active = (now < endTime);
					int status = 0;
					double row_status = 0D;
					String desc = "";
	    			ChallengesData tmp_chall = new ChallengesData();
	    			if(target == 0)target = 1; // to solve division by zero problem
	    			if(ch_type.compareTo(CHAL_TYPE_1) == 0){
	    				int walked_km = (!customData.isNull(CHAL_K + ch_id + CHAL_K_WALKED_KM)) ? customData.getInt(CHAL_K + ch_id + CHAL_K_WALKED_KM) : 0;
	    				row_status = round(walked_km, 2);
	    				String mobility_mode = (!customData.isNull(CHAL_K + ch_id + CHAL_K_MODE)) ? customData.getString(CHAL_K + ch_id + CHAL_K_MODE) : CHAL_ALLOWED_MODE_B;
	    				status = (walked_km * 100) / target;
	    				if(status > 100)status = 100;
	    				desc = (language == ITA_LANG) ? correctDesc(CHAL_DESC_1, target, bonus, point_type, mobility_mode, null, language) : correctDesc(CHAL_DESC_1_ENG, target, bonus, point_type, mobility_mode, null, language);
	    				tmp_chall.setChallCompleteDesc(getLongDescriptionByChall(ch_type, mobility_mode, target + "", point_type, language));
	    			}
	    			if(ch_type.compareTo(CHAL_TYPE_2) == 0){
	    				int count = (!customData.isNull(CHAL_K + ch_id + CHAL_K_COUNTER)) ? customData.getInt(CHAL_K + ch_id + CHAL_K_COUNTER) : 0;
	    				row_status = round(count, 2);
	    				String mobility_mode = (!customData.isNull(CHAL_K + ch_id + CHAL_K_MODE)) ? customData.getString(CHAL_K + ch_id + CHAL_K_MODE) : CHAL_ALLOWED_MODE_B;
	    				status = count * 100 / target;
	    				desc = (language == ITA_LANG) ? correctDesc(CHAL_DESC_2, target, bonus, point_type, mobility_mode, null, language) : correctDesc(CHAL_DESC_2_ENG, target, bonus, point_type, mobility_mode, null, language);
	    				tmp_chall.setChallCompleteDesc(getLongDescriptionByChall(ch_type, mobility_mode, target + "", point_type, language));
	    			}
	    			if(ch_type.compareTo(CHAL_TYPE_3) == 0){
	    				int count = (!customData.isNull(CHAL_K + ch_id + CHAL_K_COUNTER)) ? customData.getInt(CHAL_K + ch_id + CHAL_K_COUNTER) : 0;
	    				row_status = round(count, 2);
	    				String mobility_mode = (!customData.isNull(CHAL_K + ch_id + CHAL_K_MODE)) ? customData.getString(CHAL_K + ch_id + CHAL_K_MODE) : CHAL_ALLOWED_MODE_B;
	    				status = count * 100 / target;
	    				desc = (language == ITA_LANG) ? correctDesc(CHAL_DESC_3, target, bonus, point_type, mobility_mode, null, language) : correctDesc(CHAL_DESC_3_ENG, target, bonus, point_type, mobility_mode, null, language);
	    				tmp_chall.setChallCompleteDesc(getLongDescriptionByChall(ch_type, mobility_mode, target + "", point_type, language));
	    			}
	    			if(ch_type.compareTo(CHAL_TYPE_4) == 0){
	    				int count = (!customData.isNull(CHAL_K + ch_id + CHAL_K_COUNTER)) ? customData.getInt(CHAL_K + ch_id + CHAL_K_COUNTER) : 0;
	    				row_status = round(count, 2);
	    				String mobility_mode = CHAL_ALLOWED_MODE_Z;
	    				status = count * 100 / target;
	    				desc = (language == ITA_LANG) ? correctDesc(CHAL_DESC_3, target, bonus, point_type, mobility_mode, null, language) : correctDesc(CHAL_DESC_3_ENG, target, bonus, point_type, mobility_mode, null, language);
	    				tmp_chall.setChallCompleteDesc(getLongDescriptionByChall(ch_type, mobility_mode, target + "", point_type, language));
	    			}
	    			if(ch_type.compareTo(CHAL_TYPE_5) == 0){
	    				success = (!customData.isNull(CHAL_K + ch_id + CHAL_K_SUCCESS)) ? customData.getBoolean(CHAL_K + ch_id + CHAL_K_SUCCESS) : false;
	    				int earned_points = 0;
	    				if(!customData.isNull(CHAL_K + ch_id + CHAL_K_EARNED_POINT)){
	    					earned_points = customData.getInt(CHAL_K + ch_id + CHAL_K_EARNED_POINT);
	    				} else if(!customData.isNull(CHAL_K_EARNED_POINT_NEW)){
	    					earned_points = customData.getInt(CHAL_K_EARNED_POINT_NEW);
	    				}
	    				row_status = round(earned_points, 2);
	    				status = earned_points * 100 / target;
	    				if(status > 100)status = 100;
	    				desc = (language == ITA_LANG) ? correctDesc(CHAL_DESC_5, target, bonus, point_type, "", null, language) : correctDesc(CHAL_DESC_5_ENG, target, bonus, point_type, "", null, language);
	    				tmp_chall.setChallCompleteDesc(getLongDescriptionByChall(ch_type, "", target + "", point_type, language));
	    			}
	    			if(ch_type.compareTo(CHAL_TYPE_6) == 0){
	    				int count = (!customData.isNull(CHAL_K + ch_id + CHAL_K_COUNTER)) ? customData.getInt(CHAL_K + ch_id + CHAL_K_COUNTER) : 0;
	    				row_status = round(count, 2);
	    				String badge_coll_name = (!customData.isNull(CHAL_K + ch_id + CHAL_K_BADGE_COLL_NAME)) ? customData.getString(CHAL_K + ch_id + CHAL_K_BADGE_COLL_NAME) : CHAL_ALLOWED_PT_GREEN;
	    				status = count * 100 / target;
	    				desc = (language == ITA_LANG) ? correctDesc(CHAL_DESC_6, target, bonus, point_type, "", badge_coll_name, language) : correctDesc(CHAL_DESC_6_ENG, target, bonus, point_type, "", badge_coll_name, language);
	    				tmp_chall.setChallCompleteDesc(getLongDescriptionByChall(ch_type, badge_coll_name, target + "", point_type, language));
	    			}
	    			if(ch_type.compareTo(CHAL_TYPE_7) == 0){
	    				success = (!customData.isNull(CHAL_K + ch_id + CHAL_K_SUCCESS)) ? customData.getBoolean(CHAL_K + ch_id + CHAL_K_SUCCESS) : false;
	    				if(success){
							status = 100;
							row_status = 1.00;
						}
	    				desc = (language == ITA_LANG) ? correctDesc(CHAL_DESC_7, target, bonus, point_type, "", null, language) : correctDesc(CHAL_DESC_7_ENG, target, bonus, point_type, "", null, language);
	    				tmp_chall.setChallCompleteDesc(getLongDescriptionByChall(ch_type, "", target + "", point_type, language));
	    			}
	    			if(ch_type.compareTo(CHAL_TYPE_8) == 0){
	    				int survey = (!customData.isNull(CHAL_K + ch_id + CHAL_K_SURVEY)) ? customData.getInt(CHAL_K + ch_id + CHAL_K_SURVEY) : 0;
	    				row_status = round(survey, 2);
	    				if(success){
    						survey = 1;
    					}
	    				status = survey * 100 / target;
	    				if(status > 100)status = 100;
	    				desc = (language == ITA_LANG) ? correctDesc(CHAL_DESC_8, target, bonus, point_type, "", null, language) : correctDesc(CHAL_DESC_8_ENG, target, bonus, point_type, "", null, language);
	    				tmp_chall.setChallCompleteDesc(getLongDescriptionByChall(ch_type, "", target + "", point_type, language));
	    			}
	    			if(ch_type.compareTo(CHAL_TYPE_9) == 0){
	    				int recommandation = (!customData.isNull(CHAL_K + ch_id + CHAL_K_RECOM)) ? customData.getInt(CHAL_K + ch_id + CHAL_K_RECOM) : 0;
	    				row_status = round(recommandation, 2);
	    				status = recommandation * 100 / target;
	    				if(status > 100)status = 100;
	    				desc = (language == ITA_LANG) ? correctDesc(CHAL_DESC_9, target, bonus, point_type, "", null, language) : correctDesc(CHAL_DESC_9_ENG, target, bonus, point_type, "", null, language);
	    				tmp_chall.setChallCompleteDesc(getLongDescriptionByChall(ch_type, "", target + "", point_type, language));
	    			}
	    			tmp_chall.setChallId(id);
    				tmp_chall.setChallDesc(desc);
    				tmp_chall.setChallTarget(target);
    				tmp_chall.setType(ch_type);
    				tmp_chall.setStatus(status);
    				tmp_chall.setRow_status(row_status);
    				tmp_chall.setActive(active);
    				tmp_chall.setSuccess(success);
    				tmp_chall.setStartDate(startTime);
    				tmp_chall.setEndDate(endTime);
    				tmp_chall.setDaysToEnd(daysToEnd);
	    			
	    			if(type == 0){
	    				if(now >= startTime - MILLIS_IN_DAY){	// if challenge is started (with one day of offset for mail)
			    			if(now < endTime - MILLIS_IN_DAY){	// if challenge is not ended
			    				challenges.add(tmp_chall);
			    			} else if(now < endTime + MILLIS_IN_DAY){	//CHAL_TS_OFFSET
			    				oldChallenges.add(tmp_chall);	// last week challenges
			    			}
		    			}
	    			} else {
			    		if(now < endTime){	// if challenge is not ended
			    			challenges.add(tmp_chall);
			    		} else if(now >= endTime){	//CHAL_TS_OFFSET
			    			oldChallenges.add(tmp_chall);	// last week challenges
			    		}
	    			}
    			}
    		}
    		// Sorting
        	/*Collections.sort(challenges, new Comparator<ChallengesData>() {
        	    public int compare(ChallengesData chalData2, ChallengesData chalData1){
        	        return  chalData2.getChallId().compareTo(chalData1.getChallId());
        	    }
        	});
        	Collections.sort(oldChallenges, new Comparator<ChallengesData>() {
        	    public int compare(ChallengesData chalData2, ChallengesData chalData1){
        	        return  chalData1.getChallId().compareTo(chalData2.getChallId());
        	    }
        	});*/
    		challengesList.add(challenges);
			challengesList.add(oldChallenges);
    	}
    	return challengesList;
    }
	
	// Method correctChallengeData: used to retrieve the challenge data objects from the user profile data
	@SuppressWarnings("rawtypes")
	public List<List> correctChallengeData(String profile, int type, String language, List<PointConcept> pointConcept, List<BadgeCollectionConcept> bcc_list) throws JSONException{
		List<ChallengesData> challenges = new ArrayList<ChallengesData>();
    	List<ChallengesData> oldChallenges = new ArrayList<ChallengesData>();
    	List<List> challengesList = new ArrayList<List>();
    	if(profile != null && profile.compareTo("") != 0){
    		
    		JSONArray challengeConceptData = null;
    		JSONObject profileData = new JSONObject(profile);
    		//JSONObject customData = profileData.getJSONObject("customData");
    		JSONObject stateData = (!profileData.isNull(STATE)) ? profileData.getJSONObject(STATE) : null;
    		if(stateData != null){
	    		challengeConceptData = (!stateData.isNull(CHALLENGE_CONCEPT)) ? stateData.getJSONArray(CHALLENGE_CONCEPT) : null;
				if(challengeConceptData != null){
					for(int i = 0; i < challengeConceptData.length(); i++){
						JSONObject challenge = challengeConceptData.getJSONObject(i);
						String name = (!challenge.isNull(CHAL_NAME)) ? challenge.getString(CHAL_NAME) : "";
						String modelName = (!challenge.isNull(CHAL_MODEL_NAME)) ? challenge.getString(CHAL_MODEL_NAME) : "";
						String startTime = (!challenge.isNull(CHAL_START)) ? challenge.getString(CHAL_START) : "0";
						String endTime = (!challenge.isNull(CHAL_END)) ? challenge.getString(CHAL_END) : "0";
						long start = Long.parseLong(startTime);
						long end = Long.parseLong(endTime);
						Boolean completed = (!challenge.isNull(CHAL_COMPLETED)) ? challenge.getBoolean(CHAL_COMPLETED) : false;
						String dateCompletedTime = (!challenge.isNull(CHAL_COMPLETED_DATE)) ? challenge.getString(CHAL_COMPLETED_DATE) : "0";
						long dateCompleted = Long.parseLong(dateCompletedTime);
						int bonusScore = 0;
						String periodName = "";
						String bonusPointType = "green leaves";
						String counterName = "";
						String targetRow = "0";
						int target = 0;
						String badgeCollectionName = "";
						int baseline = 0;
						int initialBadgeNum = 0;
						JSONObject chalFields = (!challenge.isNull(CHAL_DATA_FIELDS)) ? challenge.getJSONObject(CHAL_DATA_FIELDS) : null;
						if(chalFields != null){
							bonusScore = (!chalFields.isNull(CHAL_FIELDS_BONUS_SCORE)) ? chalFields.getInt(CHAL_FIELDS_BONUS_SCORE) : 0;
							periodName = (!chalFields.isNull(CHAL_FIELDS_PERIOD_NAME)) ? chalFields.getString(CHAL_FIELDS_PERIOD_NAME) : "";
							bonusPointType = (!chalFields.isNull(CHAL_FIELDS_BONUS_POINT_TYPE)) ? chalFields.getString(CHAL_FIELDS_BONUS_POINT_TYPE) : "";
							counterName = (!chalFields.isNull(CHAL_FIELDS_COUNTER_NAME)) ? chalFields.getString(CHAL_FIELDS_COUNTER_NAME) : "";
							targetRow = (!chalFields.isNull(CHAL_FIELDS_TARGET)) ? chalFields.getString(CHAL_FIELDS_TARGET) : "0";
			    			if(targetRow.contains(".")){
			    				try {
			    				Float f_target = Float.parseFloat(targetRow);
			    				target = f_target.intValue();
			    				} catch (Exception ex){
			    					logger.error("String target value error from float");
			    				}
			    			} else {
				    			try {
				    				target = Integer.parseInt(targetRow);
				    			} catch (Exception ex){
				    				logger.error("String target value error from int"); 
				    			}
			    			}
							badgeCollectionName = (!chalFields.isNull(CHAL_FIELDS_BADGE_COLLECTION_NAME)) ? chalFields.getString(CHAL_FIELDS_BADGE_COLLECTION_NAME) : "";
							baseline = (!chalFields.isNull(CHAL_FIELDS_BASELINE)) ? chalFields.getInt(CHAL_FIELDS_BASELINE) : 0;
							initialBadgeNum = (!chalFields.isNull(CHAL_FIELDS_INITIAL_BADGE_NUM)) ? chalFields.getInt(CHAL_FIELDS_INITIAL_BADGE_NUM) : 0;
						}
						ServerChallengesData challData = new ServerChallengesData();
						challData.setName(name);
						challData.setModelName(modelName);
						challData.setStart(start);
						challData.setEnd(end);
						challData.setCompleted(completed);
						challData.setDateCompleted(dateCompleted);
						challData.setBonusScore(bonusScore);
						challData.setPeriodName(periodName);
						challData.setBonusPointType(bonusPointType);
						challData.setCounterName(counterName);
						challData.setTarget(target);
						challData.setInitialBadgeNum(initialBadgeNum);
						challData.setBadgeCollectionName(badgeCollectionName);
						challData.setBaseline(baseline);
						
						// Convert data to old challenges models
						String ch_id = challData.getName();
						String ch_type = challData.getModelName();
						String old_ch_type = "";
						int ch_target = target;
						int ch_bonus = challData.getBonusScore();
						long ch_startTime = challData.getStart();
						long ch_endTime = challData.getEnd();
						String ch_point_type = challData.getBonusPointType();
						Boolean ch_success = challData.getCompleted();
						long now = System.currentTimeMillis();
						int daysToEnd = calculateRemainingDays(ch_endTime, now);
						Boolean active = (now < ch_endTime);
						int status = 0;
						double row_status = 0D;
						String ch_desc = "";
		    			ChallengesData tmp_chall = new ChallengesData();
		    			
		    			if(target == 0)target = 1; // to solve division by zero problem
		    			if(ch_type.compareTo(CHAL_MODEL_PERCENTAGE_INC) == 0){
		    				old_ch_type = CHAL_TYPE_1;
		    				String mobility_mode = retrieveMobilityModeFromCounterName(counterName);
		    				int walked_km = retrieveCorrectStatusFromCounterName(counterName, pointConcept, ch_startTime, ch_endTime, null);
		    				row_status = round(walked_km, 2);
		    				status = (walked_km * 100) / target;
		    				if(status > 100)status = 100;
		    				ch_desc = (language == ITA_LANG) ? correctDesc(CHAL_DESC_1, target, ch_bonus, ch_point_type, mobility_mode, null, language) : correctDesc(CHAL_DESC_1_ENG, target, ch_bonus, ch_point_type, mobility_mode, null, language);
		    				tmp_chall.setChallCompleteDesc(getLongDescriptionByChall(old_ch_type, mobility_mode, target + "", ch_point_type, language));
		    			}
		    			if(ch_type.compareTo(CHAL_MODEL_ABSOLUTE_INC) == 0){
		    				// check how to merge old challenge type in this type
		    				if(counterName.contains(SERVER_CHAL_ALLOWED_MODE_C_NEG)){
		    					// negatedmode
		    					old_ch_type = CHAL_TYPE_2;
		    					String mobility_mode = retrieveMobilityModeFromCounterName(counterName);
		    					int count = retrieveCorrectStatusFromCounterName(counterName, pointConcept, ch_startTime, ch_endTime, null);
			    				row_status = round(count, 2);
			    				status = count * 100 / target;
			    				ch_desc = (language == ITA_LANG) ? correctDesc(CHAL_DESC_2, target, ch_bonus, ch_point_type, mobility_mode, null, language) : correctDesc(CHAL_DESC_2_ENG, target, ch_bonus, ch_point_type, mobility_mode, null, language);
			    				tmp_chall.setChallCompleteDesc(getLongDescriptionByChall(old_ch_type, mobility_mode, target + "", ch_point_type, language));
		    				} else if(counterName.contains(SERVER_CHAL_ALLOWED_MODE_R)){
		    					// recommendation
		    					old_ch_type = CHAL_TYPE_9;
		    					int recommandation = retrieveCorrectStatusFromCounterName(counterName, pointConcept, ch_startTime, ch_endTime, null);
			    				row_status = round(recommandation, 2);
			    				status = recommandation * 100 / target;
			    				if(status > 100)status = 100;
			    				ch_desc = (language == ITA_LANG) ? correctDesc(CHAL_DESC_9, target, ch_bonus, ch_point_type, "", null, language) : correctDesc(CHAL_DESC_9_ENG, target, ch_bonus, ch_point_type, "", null, language);
			    				tmp_chall.setChallCompleteDesc(getLongDescriptionByChall(old_ch_type, "", target + "", ch_point_type, language));
		    				} else if(counterName.contains(SERVER_CHAL_ALLOWED_MODE_PE)){
		    					// point earned
		    					old_ch_type = CHAL_TYPE_5;
		    					int earned_points = retrieveCorrectStatusFromCounterName(counterName, pointConcept, ch_startTime, ch_endTime, null);
			    				row_status = round(earned_points, 2);
			    				status = earned_points * 100 / target;
			    				if(status > 100)status = 100;
			    				ch_desc = (language == ITA_LANG) ? correctDesc(CHAL_DESC_5, target, ch_bonus, ch_point_type, "", null, language) : correctDesc(CHAL_DESC_5_ENG, target, ch_bonus, ch_point_type, "", null, language);
			    				tmp_chall.setChallCompleteDesc(getLongDescriptionByChall(old_ch_type, "", target + "", ch_point_type, language));
		    				} else if(counterName.contains(SERVER_CHAL_ALLOWED_MODE_Z)){
		    					// zero impact
		    					old_ch_type = CHAL_TYPE_4;
		    					String mobility_mode = CHAL_ALLOWED_MODE_Z;
		    					int count = retrieveCorrectStatusFromCounterName(counterName, pointConcept, ch_startTime, ch_endTime, null);
			    				row_status = round(count, 2);
			    				status = count * 100 / target;
			    				ch_desc = (language == ITA_LANG) ? correctDesc(CHAL_DESC_3, target, ch_bonus, ch_point_type, mobility_mode, null, language) : correctDesc(CHAL_DESC_3_ENG, target, ch_bonus, ch_point_type, mobility_mode, null, language);
			    				tmp_chall.setChallCompleteDesc(getLongDescriptionByChall(old_ch_type, mobility_mode, target + "", ch_point_type, language));
		    				} else { 
		    					// tripnum
		    					old_ch_type = CHAL_TYPE_3;
		    					String mobility_mode = retrieveMobilityModeFromCounterName(counterName);
		    					int count = retrieveCorrectStatusFromCounterName(counterName, pointConcept, ch_startTime, ch_endTime, null);
			    				row_status = round(count, 2);
			    				status = count * 100 / target;
			    				ch_desc = (language == ITA_LANG) ? correctDesc(CHAL_DESC_3, target, ch_bonus, ch_point_type, mobility_mode, null, language) :  correctDesc(CHAL_DESC_3_ENG, target, ch_bonus, ch_point_type, mobility_mode, null, language);
			    				tmp_chall.setChallCompleteDesc(getLongDescriptionByChall(old_ch_type, mobility_mode, target + "", ch_point_type, language));
		    				}
		    			}
		    			if(ch_type.compareTo(CHAL_MODEL_NEXT_BADGE) == 0){
		    				old_ch_type = CHAL_TYPE_6;
		    				int initialBadges = challData.getInitialBadgeNum();
		    				String badge_coll_name = challData.getBadgeCollectionName();
		    				int count = getEarnedBadgesFromList(bcc_list, badge_coll_name, initialBadges);
		    				if(!active){	// NB: fix to avoid situation with challenge not win and count > target
		    					if(ch_success){
		    						count = target;
		    					} else {
		    						count = target - 1;
		    					}
		    				}
		    				row_status = round(count, 2);
		    				status = count * 100 / target;
		    				ch_desc = (language == ITA_LANG) ? correctDesc(CHAL_DESC_6, target, ch_bonus, ch_point_type, "", badge_coll_name, language) : correctDesc(CHAL_DESC_6_ENG, target, ch_bonus, ch_point_type, "", badge_coll_name, language);
		    				tmp_chall.setChallCompleteDesc(getLongDescriptionByChall(old_ch_type, badge_coll_name, target + "", ch_point_type, language));
		    			}
		    			if(ch_type.compareTo(CHAL_MODEL_COMPLETE_BADGE_COLL) == 0){
		    				old_ch_type = CHAL_TYPE_7;
		    				if(ch_success){
								status = 100;
								row_status = 1.00;
							}
		    				ch_desc = (language == ITA_LANG) ? correctDesc(CHAL_DESC_7, target, ch_bonus, ch_point_type, "", null, language) : correctDesc(CHAL_DESC_7_ENG, target, ch_bonus, ch_point_type, "", null, language);
		    				tmp_chall.setChallCompleteDesc(getLongDescriptionByChall(old_ch_type, "", target + "", ch_point_type, language));
		    			}
		    			if(ch_type.compareTo(CHAL_MODEL_SURVEY) == 0){
		    				old_ch_type = CHAL_TYPE_8;
		    				int survey = 0;
		    				row_status = round(survey, 2);
		    				if(ch_success){
	    						survey = 1;
	    					}
		    				status = survey * 100 / target;
		    				if(status > 100)status = 100;
		    				ch_desc = (language == ITA_LANG) ? correctDesc(CHAL_DESC_8, target, ch_bonus, ch_point_type, "", null, language) : correctDesc(CHAL_DESC_8_ENG, target, ch_bonus, ch_point_type, "", null, language);
		    				tmp_chall.setChallCompleteDesc(getLongDescriptionByChall(old_ch_type, "", target + "", ch_point_type, language));
		    			}
		    			tmp_chall.setChallId(ch_id);
	    				tmp_chall.setChallDesc(ch_desc);
	    				tmp_chall.setChallTarget(ch_target);
	    				tmp_chall.setType(ch_type);
	    				tmp_chall.setStatus(status);
	    				tmp_chall.setRow_status(row_status);
	    				tmp_chall.setActive(active);
	    				tmp_chall.setSuccess(ch_success);
	    				tmp_chall.setStartDate(ch_startTime);
	    				tmp_chall.setEndDate(ch_endTime);
	    				tmp_chall.setDaysToEnd(daysToEnd);
		    			
		    			if(type == 0){
		    				if(now >= ch_startTime - MILLIS_IN_DAY){	// if challenge is started (with one day of offset for mail)
				    			if(now < ch_endTime - MILLIS_IN_DAY){	// if challenge is not ended
				    				challenges.add(tmp_chall);
				    			} else if(now < ch_endTime + MILLIS_IN_DAY){	//CHAL_TS_OFFSET
				    				oldChallenges.add(tmp_chall);	// last week challenges
				    			}
			    			}
		    			} else {
				    		if(now < ch_endTime){	// if challenge is not ended
				    			if(now >= ch_startTime){
				    				challenges.add(tmp_chall);
				    			}
				    		} else if(now >= ch_endTime){	//CHAL_TS_OFFSET
				    			oldChallenges.add(tmp_chall);	// last week challenges
				    		}
		    			}
					}
					challengesList.add(challenges);
	    			challengesList.add(oldChallenges);
				}
    		}
    		// Sorting
        	/*Collections.sort(challenges, new Comparator<ChallengesData>() {
        	    public int compare(ChallengesData chalData2, ChallengesData chalData1){
        	        return  chalData2.getChallId().compareTo(chalData1.getChallId());
        	    }
        	});
        	Collections.sort(oldChallenges, new Comparator<ChallengesData>() {
        	    public int compare(ChallengesData chalData2, ChallengesData chalData1){
        	        return  chalData1.getChallId().compareTo(chalData2.getChallId());
        	    }
        	});*/
		}
    	return challengesList;
    }
	
	// Method retrieveMobilityModeFromCounterName: used to retrieve the mobility mode value from counter name
	private String retrieveMobilityModeFromCounterName(String cName){
		String corrMobility = CHAL_ALLOWED_MODE_W;
		if(cName.contains(SERVER_CHAL_ALLOWED_MODE_W)){
			corrMobility = CHAL_ALLOWED_MODE_W;
		}
		if(cName.contains(SERVER_CHAL_ALLOWED_MODE_BK)){
			corrMobility = CHAL_ALLOWED_MODE_BK;
		} else if(cName.contains(SERVER_CHAL_ALLOWED_MODE_BKS)){
			corrMobility = CHAL_ALLOWED_MODE_BKS;
		}
		if(cName.contains(SERVER_CHAL_ALLOWED_MODE_T)){
			corrMobility = CHAL_ALLOWED_MODE_T;
		}
		if(cName.contains(SERVER_CHAL_ALLOWED_MODE_B)){
			corrMobility = CHAL_ALLOWED_MODE_B;
		}
		if(cName.contains(SERVER_CHAL_ALLOWED_MODE_C)){
			corrMobility = CHAL_ALLOWED_MODE_C;
		} else if(cName.contains(SERVER_CHAL_ALLOWED_MODE_C_NEG)){
			corrMobility = CHAL_ALLOWED_MODE_C_NEG;
		}
		if(cName.contains(SERVER_CHAL_ALLOWED_MODE_Z)){
			corrMobility = CHAL_ALLOWED_MODE_Z;
		}
		if(cName.contains(SERVER_CHAL_ALLOWED_MODE_P)){
			corrMobility = CHAL_ALLOWED_MODE_P;
		}
		// NB: new part for cable
		if(cName.contains(SERVER_CHAL_ALLOWED_MODE_CAB)){
			corrMobility = CHAL_ALLOWED_MODE_CAB;
		}
		return corrMobility;
	}
	
	// Method retrieveCorrectStatusFromCounterName: used to get the correct player status starting from counter name field
	private int retrieveCorrectStatusFromCounterName(String cName, List<PointConcept> pointConcept, Long chalStart, Long chalEnd, Long now){
		int actualStatus = 0; // km or trips
		if(cName != null && cName.compareTo("") != 0){
			for(PointConcept pt : pointConcept){
				if(pt.getName().compareTo(cName) == 0){
					List<PointConceptPeriod> allPeriods = pt.getInstances();
					for(PointConceptPeriod pcp : allPeriods){
						if(chalStart != null && chalEnd != null){
							if((pcp.getStart() - W_DELTA) <= chalStart && (pcp.getEnd() + W_DELTA) >= chalEnd){	// the week duration instance is major or equals the challenge duration 
								actualStatus = pcp.getScore();
								break;
							}
						} else {
							if(now != null){
								if(pcp.getStart() <= now && pcp.getEnd() >= now){	// the actual time is contained in the week duration instance
									actualStatus = pcp.getScore();
									break;
								}
							}
						}
					}
					break;
				}
			}
		}
		return actualStatus;
	}
	
	// Method getEarnedBadgesFromList: used to get the earned badge number during challenge
	private int getEarnedBadgesFromList(List<BadgeCollectionConcept> bcc_list, String badgeCollName, int initial){
		int earnedBadges = 0;
		for(BadgeCollectionConcept bcc : bcc_list){
			if(bcc.getName().compareTo(badgeCollName) == 0){
				earnedBadges = bcc.getBadgeEarned().size() - initial;
				break;
			}
		}
		return earnedBadges;
	}
	
	private int calculateRemainingDays(long endTime, long now){
    	int remainingDays = 0;
    	if(now < endTime){
    		long tmpMillis = endTime - now;
    		remainingDays = (int) Math.ceil((float)tmpMillis / MILLIS_IN_DAY);
    	}
    	return remainingDays;
    }
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
	
    private String correctDesc(String desc, int target, int bonus, String p_type, String mode, String coll_name, String language){
    	if(desc.contains("TARGET")){
    		desc = desc.replace("TARGET", Integer.toString(target));
    	}
    	if(language.compareTo(ITA_LANG) == 0){
	    	if(target > 1 && desc.contains("viaggio")){
	    		desc = desc.replace("viaggio", "viaggi");
	    	}
	    	if(target == 1 && desc.contains("utenti")){
	    		desc = desc.replace("utenti", "utente");
	    	}
	    	if(target == 1 && desc.contains("altri")){
	    		desc = desc.replace("altri", "");
	    		desc = desc.replace("km", "altro km");
	    	}
    	} else {
    		if(target > 1 && desc.contains("trip")){
	    		desc = desc.replace("trip", "trips");
	    	}
	    	if(target == 1 && desc.contains("users")){
	    		desc = desc.replace("users", "user");
	    	}
	    	if(target > 1 && desc.contains("km")){
	    		desc = desc.replace("km", "kms");
	    	}
    	}
    	if(desc.contains("BONUS")){
    		desc = desc.replace("BONUS", Integer.toString(bonus));
    	}
    	if(desc.contains("POINT_TYPE")){
    		desc = desc.replaceAll("POINT_TYPE", getCorrectPointType(p_type));
    	}
    	if(mode != null && mode != ""){
    		if(desc.contains("MODE")){
    			if(language.compareTo(ITA_LANG) == 0){
	    			if(desc.contains("senza")){
	    				desc = desc.replace("MODE", getCorrectMode(mode, 1, language));
	    			} else {
	    				desc = desc.replace("MODE", getCorrectMode(mode, 0, language));
	    			}
    			} else {
    				if(desc.contains("without")){
	    				desc = desc.replace("MODE", getCorrectMode(mode, 1, language));
	    			} else {
	    				desc = desc.replace("MODE", getCorrectMode(mode, 0, language));
	    			}
    			}
    		}
    	}
    	if(coll_name != null && coll_name != ""){
    		if(desc.contains("BADGE_COLL_NAME")){
    			desc = desc.replace("BADGE_COLL_NAME", coll_name);
    		}
    	}
    	return desc;
    }
    
    private String getCorrectMode(String mode, int type, String language){
    	String corr_mode = "";
    	if(mode.compareTo(CHAL_ALLOWED_MODE_W) == 0){
    		if(language == ITA_LANG){
    			corr_mode = (type == 0) ? "a piedi" : "spostamenti a piedi";
    		} else {
    			corr_mode = (type == 0) ? "walking" : "travel on foot";
    		}
    	}
    	if(mode.compareTo(CHAL_ALLOWED_MODE_BK) == 0){
    		if(language == ITA_LANG){
    			corr_mode = (type == 0) ? "in bici" : "la bici";
    		} else {
    			corr_mode = (type == 0) ? "by bike" : "the bike";
    		}
    	}
    	if(mode.compareTo(CHAL_ALLOWED_MODE_BKS) == 0){
    		if(language == ITA_LANG){
    			corr_mode = (type == 0) ? "con bici condivisa" : "la bici condivisa";
    		} else {
    			corr_mode = (type == 0) ? "with shared bike" : "bike sharing";
    		}
    	}
    	if(mode.compareTo(CHAL_ALLOWED_MODE_T) == 0){
    		if(language == ITA_LANG){
    			corr_mode = (type == 0) ? "in treno" : "il treno";
    		} else {
    			corr_mode = (type == 0) ? "by train" : "the train";
    		}
    	}
    	if(mode.compareTo(CHAL_ALLOWED_MODE_B) == 0){
    		if(language == ITA_LANG){
    			corr_mode = (type == 0) ? "in autobus" : "l'autobus";
    		} else {
    			corr_mode = (type == 0) ? "by bus" : "the bus";
    		}
    	}
    	if(mode.compareTo(CHAL_ALLOWED_MODE_C) == 0){
    		if(language == ITA_LANG){
    			corr_mode = (type == 0) ? "in auto" : "l'automobile";
    		} else {
    			corr_mode = (type == 0) ? "by car" : "the car";
    		}
    	}
    	if(mode.compareTo(CHAL_ALLOWED_MODE_Z) == 0){
    		if(language == ITA_LANG){
    			corr_mode = (type == 0) ? "a impatto zero" : "spostamenti a impatto zero";
    		} else {
    			corr_mode = (type == 0) ? "zero impact" : "zero impact trips";
    		}
    	}
    	if(mode.compareTo(CHAL_ALLOWED_MODE_P) == 0){
    		if(language == ITA_LANG){
    			corr_mode = (type == 0) ? "promoted" : "spostamenti promoted";
    		} else {
    			corr_mode = (type == 0) ? "promoted" : "promoted trips";
    		}
    	}
    	if(mode.compareTo(CHAL_ALLOWED_MODE_CAB) == 0){
    		if(language == ITA_LANG){
    			corr_mode = (type == 0) ? "in funivia" : "la funivia";	// New part for cable
    		} else {
    			corr_mode = (type == 0) ? "by cable" : "the cable";	// New part for cable
    		}
    	}
    	if(mode.compareTo(CHAL_ALLOWED_MODE_W_DIS) == 0){
    		if(language == ITA_LANG){
    			corr_mode = (type == 0) ? "a piedi" : "spostamenti a piedi";
    		} else {
    			corr_mode = (type == 0) ? "walking" : "travel on foot";
    		}
    	}
    	if(mode.compareTo(CHAL_ALLOWED_MODE_BK_DIS) == 0){
    		if(language == ITA_LANG){
    			corr_mode = (type == 0) ? "in bici" : "la bici";
    		} else {
    			corr_mode = (type == 0) ? "by bike" : "the bike";
    		}
    	}
    	if(mode.compareTo(CHAL_ALLOWED_MODE_BKS_DIS) == 0){
    		if(language == ITA_LANG){
    			corr_mode = (type == 0) ? "con bici condivisa" : "la bici condivisa";
    		} else {
    			corr_mode = (type == 0) ? "with shared bike" : "bike sharing";
    		}
    	}
    	if(mode.compareTo(CHAL_ALLOWED_MODE_T_DIS) == 0){
    		if(language == ITA_LANG){
    			corr_mode = (type == 0) ? "in treno" : "il treno";
    		} else {
    			corr_mode = (type == 0) ? "by train" : "the train";
    		}
    	}
    	if(mode.compareTo(CHAL_ALLOWED_MODE_B_DIS) == 0){
    		if(language == ITA_LANG){
    			corr_mode = (type == 0) ? "in autobus" : "l'autobus";
    		} else {
    			corr_mode = (type == 0) ? "by bus" : "the bus";
    		}
    	}
    	if(mode.compareTo(CHAL_ALLOWED_MODE_C_DIS) == 0){
    		if(language == ITA_LANG){
    			corr_mode = (type == 0) ? "in auto" : "l'automobile";
    		} else {
    			corr_mode = (type == 0) ? "by car" : "the car";
    		}
    	}
    	if(mode.compareTo(CHAL_ALLOWED_MODE_Z_DIS) == 0){
    		if(language == ITA_LANG){
    			corr_mode = (type == 0) ? "a impatto zero" : "spostamenti a impatto zero";
    		} else {
    			corr_mode = (type == 0) ? "zero impact" : "zero impact trips";
    		}
    	}
    	if(mode.compareTo(CHAL_ALLOWED_MODE_P_DIS) == 0){
    		if(language == ITA_LANG){
    			corr_mode = (type == 0) ? "promoted" : "spostamenti promoted";
    		} else {
    			corr_mode = (type == 0) ? "promoted" : "promoted trips";
    		}
    	}
    	return corr_mode;
    }
    
    private String getCorrectPointType(String ptype){
    	String result = "";
    	if(ptype.compareTo(CHAL_ALLOWED_PT_GREEN) == 0){
    		result = "Green Leaves";
    	} else if(ptype.compareTo(CHAL_ALLOWED_PT_HEALTH) == 0){
    		result = "Salute";
    	} else if(ptype.compareTo(CHAL_ALLOWED_PT_PR) == 0){
    		result = "Park & Ride";
    	}
    	return result;
    };
	

}
