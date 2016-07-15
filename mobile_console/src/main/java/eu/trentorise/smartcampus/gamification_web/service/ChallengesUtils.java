package eu.trentorise.smartcampus.gamification_web.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import eu.trentorise.smartcampus.gamification_web.models.ChallengeDescriptionData;
import eu.trentorise.smartcampus.gamification_web.models.status.ChallengesData;

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
	private final String CHAL_DESC_1 = "Fai almeno altri TARGET km MODE e avrai un bonus di BONUS punti POINT_TYPE";
	private final String CHAL_DESC_2 = "Fai almeno TARGET viaggio senza usare MODE e avrai un bonus di BONUS punti POINT_TYPE";
	private final String CHAL_DESC_3 = "Fai almeno TARGET viaggio MODE e avrai un bonus di BONUS punti POINT_TYPE";
	private final String CHAL_DESC_5 = "Ottieni almeno TARGET punti POINT_TYPE e avrai ulteriori BONUS punti POINT_TYPE in bonus";
	private final String CHAL_DESC_6 = "Ottieni almeno TARGET badge nella Badge Collection BADGE_COLL_NAME e vinci un bonus di BONUS punti POINT_TYPE";
	private final String CHAL_DESC_7 = "Completa una Badge Collection e vinci un bonus di BONUS punti POINT_TYPE";
	private final String CHAL_DESC_8 = "Compila il questionario di fine gioco e guadagni BONUS punti POINT_TYPE";
	private final String CHAL_DESC_9 = "Raccomanda la App ad almeno TARGET utenti e guadagni BONUS punti POINT_TYPE";
	private final String CHAL_TYPE_1 = "PERCENT";
	private final String CHAL_TYPE_1A = "BSPERCENT";
	private final String CHAL_TYPE_2 = "NEGATEDMODE";
	private final String CHAL_TYPE_3 = "TRIPNUMBER";
	private final String CHAL_TYPE_3A = "BSTRIPNUMBER";
	private final String CHAL_TYPE_4 = "ZEROIMPACT";
	private final String CHAL_TYPE_5 = "POINTSEARNED";
	private final String CHAL_TYPE_6 = "NEXTBADGE";
	private final String CHAL_TYPE_7 = "BADGECOLLECTION";
	private final String CHAL_TYPE_8 = "SURVEYDATA";
	private final String CHAL_TYPE_9 = "RECOMMENDATION";
	/*private final String CHAL_TYPE_1 = "ch1";
	private final String CHAL_TYPE_3 = "ch3";
	private final String CHAL_TYPE_5 = "ch5";
	private final String CHAL_TYPE_7 = "ch7";
	private final String CHAL_TYPE_9 = "ch9";*/
	private final String CHAL_ALLOWED_MODE_W = "walk";
	private final String CHAL_ALLOWED_MODE_BK = "bike";
	private final String CHAL_ALLOWED_MODE_BKS = "bikesharing";
	private final String CHAL_ALLOWED_MODE_T = "train";
	private final String CHAL_ALLOWED_MODE_B = "bus";
	private final String CHAL_ALLOWED_MODE_C = "car";
	private final String CHAL_ALLOWED_MODE_Z = "zeroimpact";
	private final String CHAL_ALLOWED_MODE_P = "promoted";
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
	
	private String getLongDescriptionByChall(String type, String mobMode, String target, String pointType){
		String correctDesc = "";
		List<ChallengeDescriptionData> challDescList = getChallLongDescriptionList();
		if(type.compareTo(CHAL_TYPE_1) == 0 || type.compareTo(CHAL_TYPE_1A) == 0){
			if(mobMode.compareTo(CHAL_ALLOWED_MODE_BK) == 0 || mobMode.compareTo(CHAL_ALLOWED_MODE_BK + "Distance") == 0){
				correctDesc = challDescList.get(0).getDescription();
			} else if(mobMode.compareTo(CHAL_ALLOWED_MODE_BKS) == 0 || mobMode.compareTo(CHAL_ALLOWED_MODE_BKS + "Distance") == 0){
				correctDesc = challDescList.get(1).getDescription();
			} else if(mobMode.compareTo(CHAL_ALLOWED_MODE_W) == 0 || mobMode.compareTo(CHAL_ALLOWED_MODE_W + "Distance") == 0){
				correctDesc = challDescList.get(2).getDescription();
			}
		} else if(type.compareTo(CHAL_TYPE_2) == 0){
			if(mobMode.compareTo(CHAL_ALLOWED_MODE_B) == 0 || mobMode.compareTo(CHAL_ALLOWED_MODE_B + "Distance") == 0){
				correctDesc = challDescList.get(21).getDescription();
			} else if(mobMode.compareTo(CHAL_ALLOWED_MODE_BK) == 0 || mobMode.compareTo(CHAL_ALLOWED_MODE_BK + "Distance") == 0){
				correctDesc = challDescList.get(23).getDescription();
			} else if(mobMode.compareTo(CHAL_ALLOWED_MODE_BKS) == 0 || mobMode.compareTo(CHAL_ALLOWED_MODE_BKS + "Distance") == 0){
				correctDesc = challDescList.get(20).getDescription();
			} else if(mobMode.compareTo(CHAL_ALLOWED_MODE_T) == 0 || mobMode.compareTo(CHAL_ALLOWED_MODE_T + "Distance") == 0){
				correctDesc = challDescList.get(22).getDescription();
			} else if(mobMode.compareTo(CHAL_ALLOWED_MODE_C) == 0 || mobMode.compareTo(CHAL_ALLOWED_MODE_C + "Distance") == 0){
				correctDesc = challDescList.get(24).getDescription();
			} else if(mobMode.compareTo(CHAL_ALLOWED_MODE_W) == 0 || mobMode.compareTo(CHAL_ALLOWED_MODE_W + "Distance") == 0){
				correctDesc = challDescList.get(25).getDescription();
			} 
		} else if(type.compareTo(CHAL_TYPE_3) == 0 || type.compareTo(CHAL_TYPE_3A) == 0){
			if(mobMode.compareTo(CHAL_ALLOWED_MODE_B) == 0 || mobMode.compareTo(CHAL_ALLOWED_MODE_B + "Distance") == 0){
				correctDesc = challDescList.get(4).getDescription();
			} else if(mobMode.compareTo(CHAL_ALLOWED_MODE_BK) == 0 || mobMode.compareTo(CHAL_ALLOWED_MODE_BK + "Distance") == 0){
				correctDesc = challDescList.get(8).getDescription().replace("[bici, bike sharing, bus, treno]","la bici");
			} else if(mobMode.compareTo(CHAL_ALLOWED_MODE_BKS) == 0 || mobMode.compareTo(CHAL_ALLOWED_MODE_BKS + "Distance") == 0){
				correctDesc = challDescList.get(3).getDescription();
			} else if(mobMode.compareTo(CHAL_ALLOWED_MODE_T) == 0 || mobMode.compareTo(CHAL_ALLOWED_MODE_T + "Distance") == 0){
				correctDesc = challDescList.get(5).getDescription();
			} else if(mobMode.compareTo(CHAL_ALLOWED_MODE_P) == 0 || mobMode.compareTo(CHAL_ALLOWED_MODE_P + "Distance") == 0){
				correctDesc = challDescList.get(7).getDescription();
			} else if(mobMode.compareTo(CHAL_ALLOWED_MODE_W) == 0 || mobMode.compareTo(CHAL_ALLOWED_MODE_W + "Distance") == 0){
				correctDesc = challDescList.get(26).getDescription();
			}
		} else if(type.compareTo(CHAL_TYPE_4) == 0){
			if(mobMode.compareTo(CHAL_ALLOWED_MODE_Z) == 0 || mobMode.compareTo(CHAL_ALLOWED_MODE_Z + "Distance") == 0){
				correctDesc = challDescList.get(6).getDescription();
			}
		} else if(type.compareTo(CHAL_TYPE_5) == 0){
			correctDesc = challDescList.get(10).getDescription().replace("X", target).replace("punti [green leaves, bici, salute, impatto 0]", pointType);
		} else if(type.compareTo(CHAL_TYPE_6) == 0){
			if(mobMode.compareTo("green leaves") == 0){
				correctDesc = challDescList.get(14).getDescription();
			} else if(mobMode.compareTo("bike sharing pioneer") == 0){
				correctDesc = challDescList.get(13).getDescription();
			} else if(mobMode.compareTo("sustainable life") == 0){
				correctDesc = challDescList.get(15).getDescription();
			} else if(mobMode.compareTo("park and ride pioneer") == 0){
				correctDesc = challDescList.get(12).getDescription();	// park and ride
			}	
		} else if(type.compareTo(CHAL_TYPE_7) == 0){
			correctDesc = challDescList.get(19).getDescription();
		} else if(type.compareTo(CHAL_TYPE_8) == 0){
			correctDesc = challDescList.get(27).getDescription();
		} else if(type.compareTo(CHAL_TYPE_9) == 0){
			int racc_num = Integer.parseInt(target);
			if(racc_num == 1){
				correctDesc = challDescList.get(9).getDescription().replace("X", target).replace("tuoi amici si devono", "tuo amico si deve");
			} else {
				correctDesc = challDescList.get(9).getDescription().replace("X", target);
			}
		}
		return correctDesc;
	}
	
	@SuppressWarnings("rawtypes")
	public List<List> correctCustomData(String profile, int type) throws JSONException{
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
	    			if(ch_type.compareTo(CHAL_TYPE_1) == 0 || ch_type.compareTo(CHAL_TYPE_1A) == 0){
	    				int walked_km = (!customData.isNull(CHAL_K + ch_id + CHAL_K_WALKED_KM)) ? customData.getInt(CHAL_K + ch_id + CHAL_K_WALKED_KM) : 0;
	    				row_status = round(walked_km, 2);
	    				String mobility_mode = (!customData.isNull(CHAL_K + ch_id + CHAL_K_MODE)) ? customData.getString(CHAL_K + ch_id + CHAL_K_MODE) : CHAL_ALLOWED_MODE_B;
	    				status = (walked_km * 100) / target;
	    				if(status > 100)status = 100;
	    				desc = correctDesc(CHAL_DESC_1, target, bonus, point_type, mobility_mode, null);
	    				tmp_chall.setChallCompleteDesc(getLongDescriptionByChall(ch_type, mobility_mode, target + "", point_type));
	    			}
	    			if(ch_type.compareTo(CHAL_TYPE_2) == 0){
	    				int count = (!customData.isNull(CHAL_K + ch_id + CHAL_K_COUNTER)) ? customData.getInt(CHAL_K + ch_id + CHAL_K_COUNTER) : 0;
	    				row_status = round(count, 2);
	    				String mobility_mode = (!customData.isNull(CHAL_K + ch_id + CHAL_K_MODE)) ? customData.getString(CHAL_K + ch_id + CHAL_K_MODE) : CHAL_ALLOWED_MODE_B;
	    				status = count * 100 / target;
	    				desc = correctDesc(CHAL_DESC_2, target, bonus, point_type, mobility_mode, null);
	    				tmp_chall.setChallCompleteDesc(getLongDescriptionByChall(ch_type, mobility_mode, target + "", point_type));
	    			}
	    			if(ch_type.compareTo(CHAL_TYPE_3) == 0 || ch_type.compareTo(CHAL_TYPE_3A) == 0){
	    				int count = (!customData.isNull(CHAL_K + ch_id + CHAL_K_COUNTER)) ? customData.getInt(CHAL_K + ch_id + CHAL_K_COUNTER) : 0;
	    				row_status = round(count, 2);
	    				String mobility_mode = (!customData.isNull(CHAL_K + ch_id + CHAL_K_MODE)) ? customData.getString(CHAL_K + ch_id + CHAL_K_MODE) : CHAL_ALLOWED_MODE_B;
	    				status = count * 100 / target;
	    				desc = correctDesc(CHAL_DESC_3, target, bonus, point_type, mobility_mode, null);
	    				tmp_chall.setChallCompleteDesc(getLongDescriptionByChall(ch_type, mobility_mode, target + "", point_type));
	    			}
	    			if(ch_type.compareTo(CHAL_TYPE_4) == 0){
	    				int count = (!customData.isNull(CHAL_K + ch_id + CHAL_K_COUNTER)) ? customData.getInt(CHAL_K + ch_id + CHAL_K_COUNTER) : 0;
	    				row_status = round(count, 2);
	    				String mobility_mode = CHAL_ALLOWED_MODE_Z;
	    				status = count * 100 / target;
	    				desc = correctDesc(CHAL_DESC_3, target, bonus, point_type, mobility_mode, null);
	    				tmp_chall.setChallCompleteDesc(getLongDescriptionByChall(ch_type, mobility_mode, target + "", point_type));
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
	    				desc = correctDesc(CHAL_DESC_5, target, bonus, point_type, "", null);
	    				tmp_chall.setChallCompleteDesc(getLongDescriptionByChall(ch_type, "", target + "", point_type));
	    			}
	    			if(ch_type.compareTo(CHAL_TYPE_6) == 0){
	    				int count = (!customData.isNull(CHAL_K + ch_id + CHAL_K_COUNTER)) ? customData.getInt(CHAL_K + ch_id + CHAL_K_COUNTER) : 0;
	    				row_status = round(count, 2);
	    				String badge_coll_name = (!customData.isNull(CHAL_K + ch_id + CHAL_K_BADGE_COLL_NAME)) ? customData.getString(CHAL_K + ch_id + CHAL_K_BADGE_COLL_NAME) : CHAL_ALLOWED_PT_GREEN;
	    				status = count * 100 / target;
	    				desc = correctDesc(CHAL_DESC_6, target, bonus, point_type, "", badge_coll_name);
	    				tmp_chall.setChallCompleteDesc(getLongDescriptionByChall(ch_type, badge_coll_name, target + "", point_type));
	    			}
	    			if(ch_type.compareTo(CHAL_TYPE_7) == 0){
	    				success = (!customData.isNull(CHAL_K + ch_id + CHAL_K_SUCCESS)) ? customData.getBoolean(CHAL_K + ch_id + CHAL_K_SUCCESS) : false;
	    				if(success){
							status = 100;
							row_status = 1.00;
						}
	    				desc = correctDesc(CHAL_DESC_7, target, bonus, point_type, "", null);
	    				tmp_chall.setChallCompleteDesc(getLongDescriptionByChall(ch_type, "", target + "", point_type));
	    			}
	    			if(ch_type.compareTo(CHAL_TYPE_8) == 0){
	    				int survey = (!customData.isNull(CHAL_K + ch_id + CHAL_K_SURVEY)) ? customData.getInt(CHAL_K + ch_id + CHAL_K_SURVEY) : 0;
	    				row_status = round(survey, 2);
	    				if(success){
    						survey = 1;
    					}
	    				status = survey * 100 / target;
	    				if(status > 100)status = 100;
	    				desc = correctDesc(CHAL_DESC_8, target, bonus, point_type, "", null);
	    				tmp_chall.setChallCompleteDesc(getLongDescriptionByChall(ch_type, "", target + "", point_type));
	    			}
	    			if(ch_type.compareTo(CHAL_TYPE_9) == 0){
	    				int recommandation = (!customData.isNull(CHAL_K + ch_id + CHAL_K_RECOM)) ? customData.getInt(CHAL_K + ch_id + CHAL_K_RECOM) : 0;
	    				row_status = round(recommandation, 2);
	    				status = recommandation * 100 / target;
	    				if(status > 100)status = 100;
	    				desc = correctDesc(CHAL_DESC_9, target, bonus, point_type, "", null);
	    				tmp_chall.setChallCompleteDesc(getLongDescriptionByChall(ch_type, "", target + "", point_type));
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
	
	private int calculateRemainingDays(long endTime, long now){
    	int remainingDays = 0;
    	if(now < endTime){
    		long tmpMillis = endTime - now;
    		remainingDays = (int) Math.ceil(tmpMillis / MILLIS_IN_DAY);
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
	
    private String correctDesc(String desc, int target, int bonus, String p_type, String mode, String coll_name){
    	if(desc.contains("TARGET")){
    		desc = desc.replace("TARGET", Integer.toString(target));
    	}
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
    	if(desc.contains("BONUS")){
    		desc = desc.replace("BONUS", Integer.toString(bonus));
    	}
    	if(desc.contains("POINT_TYPE")){
    		desc = desc.replaceAll("POINT_TYPE", getCorrectPointType(p_type));
    	}
    	if(mode != null && mode != ""){
    		if(desc.contains("MODE")){
    			if(desc.contains("senza")){
    				desc = desc.replace("MODE", getCorrectMode(mode, 1));
    			} else {
    				desc = desc.replace("MODE", getCorrectMode(mode, 0));
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
    
    private String getCorrectMode(String mode, int type){
    	String corr_mode = "";
    	if(mode.compareTo(CHAL_ALLOWED_MODE_W) == 0){
    		corr_mode = (type == 0) ? "a piedi" : "spostamenti a piedi";
    	}
    	if(mode.compareTo(CHAL_ALLOWED_MODE_BK) == 0){
    		corr_mode = (type == 0) ? "in bici" : "la bici";
    	}
    	if(mode.compareTo(CHAL_ALLOWED_MODE_BKS) == 0){
    		corr_mode = (type == 0) ? "con bici condivisa" : "la bici condivisa";
    	}
    	if(mode.compareTo(CHAL_ALLOWED_MODE_T) == 0){
    		corr_mode = (type == 0) ? "in treno" : "il treno";
    	}
    	if(mode.compareTo(CHAL_ALLOWED_MODE_B) == 0){
    		corr_mode = (type == 0) ? "in autobus" : "l'autobus";
    	}
    	if(mode.compareTo(CHAL_ALLOWED_MODE_C) == 0){
    		corr_mode = (type == 0) ? "in auto" : "l'automobile";
    	}
    	if(mode.compareTo(CHAL_ALLOWED_MODE_Z) == 0){
    		corr_mode = (type == 0) ? "a impatto zero" : "spostamenti a impatto zero";
    	}
    	if(mode.compareTo(CHAL_ALLOWED_MODE_P) == 0){
    		corr_mode = (type == 0) ? "promoted" : "spostamenti promoted";
    	}
    	if(mode.compareTo(CHAL_ALLOWED_MODE_W_DIS) == 0){
    		corr_mode = (type == 0) ? "a piedi" : "spostamenti a piedi";
    	}
    	if(mode.compareTo(CHAL_ALLOWED_MODE_BK_DIS) == 0){
    		corr_mode = (type == 0) ? "in bici" : "la bici";
    	}
    	if(mode.compareTo(CHAL_ALLOWED_MODE_BKS_DIS) == 0){
    		corr_mode = (type == 0) ? "con bici condivisa" : "la bici condivisa";
    	}
    	if(mode.compareTo(CHAL_ALLOWED_MODE_T_DIS) == 0){
    		corr_mode = (type == 0) ? "in treno" : "il treno";
    	}
    	if(mode.compareTo(CHAL_ALLOWED_MODE_B_DIS) == 0){
    		corr_mode = (type == 0) ? "in autobus" : "l'autobus";
    	}
    	if(mode.compareTo(CHAL_ALLOWED_MODE_C_DIS) == 0){
    		corr_mode = (type == 0) ? "in auto" : "l'automobile";
    	}
    	if(mode.compareTo(CHAL_ALLOWED_MODE_Z_DIS) == 0){
    		corr_mode = (type == 0) ? "a impatto zero" : "spostamenti a impatto zero";
    	}
    	if(mode.compareTo(CHAL_ALLOWED_MODE_P_DIS) == 0){
    		corr_mode = (type == 0) ? "promoted" : "spostamenti promoted";
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
