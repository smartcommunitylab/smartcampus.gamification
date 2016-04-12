package eu.trentorise.smartcampus.gamification_web.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import eu.trentorise.smartcampus.gamification_web.models.ChallengesData;

public class ChallengesUtils {

	// Class used to read the player personalData and create the correct challenges
	// challeng_keys
	private final String CHAL_K = "ch_";
	private final String CHAL_K_TYPE = "_type";
	private final String CHAL_K_STS = "_startChTs";
	private final String CHAL_K_ETS = "_endChTs";
	private final String CHAL_K_WALKED_KM = "_Km_traveled_during_challenge";
	private final String CHAL_K_EARNED_POINT = "_points_earned_during_challenges";
	private final String CHAL_K_TARGET = "_target";
	private final String CHAL_K_BONUS = "_bonus";
	private final String CHAL_K_RECOM = "_recommendations_sent_during_challenges";
	private final String CHAL_K_SUCCESS = "_success";
	private final String CHAL_K_COUNTER = "_counter";
	private final String CHAL_K_POINT_TYPE = "_point_type";
	private final String CHAL_K_MODE = "_mode";	// possibility: walk, bike, bikesharing, train, bus, car
	private final String CHAL_DESC_1 = "Fai almeno altri TARGET km MODE e avrai BONUS punti POINT_TYPE in bonus";
	private final String CHAL_DESC_3 = "Fai almeno TARGET viaggio MODE e avrai BONUS punti POINT_TYPE in bonus";
	private final String CHAL_DESC_5 = "Ottieni almeno TARGET punti POINT_TYPE e avrai ulteriori BONUS punti POINT_TYPE in bonus";
	private final String CHAL_DESC_7 = "Completa una Badge Collection e vinci un bonus di BONUS punti POINT_TYPE";
	private final String CHAL_DESC_9 = "Raccomanda la App ad almeno TARGET utenti e guadagni BONUS punti POINT_TYPE";
	private final String CHAL_TYPE_1 = "PERCENT";
	private final String CHAL_TYPE_3 = "TRIPNUMBER";
	private final String CHAL_TYPE_5 = "POINTSEARNED";
	private final String CHAL_TYPE_7 = "COMPLETECOLLECTION";
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
	private final String CHAL_PT_GREEN_STRING = "Punti Green";
	private final String CHAL_PT_HEALTH_STRING = "Punti Salute";
	private final String CHAL_PT_PR_STRING = "Punti Park&Ride";
	private final int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
	private final long CHAL_TS_OFFSET = 1000 * 60 * 60 * 24 * 7;	// millis in one week
	
	private static final Logger logger = Logger.getLogger(ChallengesUtils.class);
	
	public ChallengesUtils() {
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("rawtypes")
	public List<List> correctCustomData(String profile) throws JSONException{
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
    		    	logger.info("key: " + key + ", value: " + value );
    		    }
    		}
    		
    		for(int i = 0; i < challIndxArray.size(); i++){
    			String ch_id = challIndxArray.get(i);
    			String ch_type = customData.getString(CHAL_K + ch_id + CHAL_K_TYPE);
    			String targetVal = customData.getString(CHAL_K + ch_id + CHAL_K_TARGET);
    			int target = 0;
    			if(targetVal.contains(".")){
    				try {
    				Float f_target = Float.parseFloat(targetVal);
    				target = f_target.intValue();
    				} catch (Exception ex){
    					logger.info("String target value error from float");
    				}
    			} else {
	    			try {
	    				target = Integer.parseInt(targetVal);
	    			} catch (Exception ex){
	    				logger.info("String target value error from int"); 
	    			}
    			}
				int bonus = customData.getInt(CHAL_K + ch_id + CHAL_K_BONUS);
				String endChTs = customData.getString(CHAL_K + ch_id + CHAL_K_ETS);
				String point_type = customData.getString(CHAL_K + ch_id + CHAL_K_POINT_TYPE);
				long now = System.currentTimeMillis();
				int daysToEnd = calculateRemainingDays(endChTs, now);
				Boolean success = (!customData.isNull(CHAL_K + ch_id + CHAL_K_SUCCESS)) ? customData.getBoolean(CHAL_K + ch_id + CHAL_K_SUCCESS) : false;
				long endTime = Long.parseLong(endChTs);
				long startTime = 0L;
				Boolean active = (now < endTime);
				int status = 0;
				String row_status = "";
    			ChallengesData tmp_chall = new ChallengesData();
    			if(ch_type.compareTo(CHAL_TYPE_1) == 0){
    				int walked_km = customData.getInt(CHAL_K + ch_id + CHAL_K_WALKED_KM);
    				String mobility_mode = customData.getString(CHAL_K + ch_id + CHAL_K_MODE);
    				status = (walked_km * 100) / target;
    				row_status = walked_km + "/" + target;
    				if(status > 100)status = 100;
    				String id = challIndxArray.get(i);
    				String desc = correctDesc(CHAL_DESC_1, target, bonus, point_type, mobility_mode);
    				startTime = customData.getLong(CHAL_K + ch_id + CHAL_K_STS);
    				tmp_chall.setChallId(id);
    				tmp_chall.setChallDesc(desc);
    				tmp_chall.setChallTarget(target);
    				tmp_chall.setType(ch_type);
    				tmp_chall.setStatus(status);
    				tmp_chall.setActive(active);
    				tmp_chall.setSuccess(success);
    				tmp_chall.setStartDate(startTime);
    				tmp_chall.setEndDate(endTime);
    			}
    			if(ch_type.compareTo(CHAL_TYPE_3) == 0){
    				int count = customData.getInt(CHAL_K + ch_id + CHAL_K_COUNTER);
    				String mobility_mode = customData.getString(CHAL_K + ch_id + CHAL_K_MODE);
    				status = count * 100 / target;
    				row_status = count + "/" + target;
    				String id = challIndxArray.get(i);
    				String desc = correctDesc(CHAL_DESC_3, target, bonus, point_type, mobility_mode);
    				startTime = customData.getLong(CHAL_K + ch_id + CHAL_K_STS);
    				tmp_chall.setChallId(id);
    				tmp_chall.setChallDesc(desc);
    				tmp_chall.setChallTarget(target);
    				tmp_chall.setType(ch_type);
    				tmp_chall.setStatus(status);
    				tmp_chall.setActive(active);
    				tmp_chall.setSuccess(success);
    				tmp_chall.setStartDate(startTime);
    				tmp_chall.setEndDate(endTime);
    			}
    			if(ch_type.compareTo(CHAL_TYPE_5) == 0){
    				success = customData.getBoolean(CHAL_K + ch_id + CHAL_K_SUCCESS);
    				int earned_points = 0;
    				if(!customData.isNull(CHAL_K + ch_id + CHAL_K_EARNED_POINT)){
    					earned_points = customData.getInt(CHAL_K + ch_id + CHAL_K_EARNED_POINT);
    				}
    				status = earned_points * 100 / target;
    				row_status = earned_points + "/" + target;
    				if(status > 100)status = 100;
					String id = challIndxArray.get(i);
    				String desc = correctDesc(CHAL_DESC_5, target, bonus, point_type, "");
    				startTime = customData.getLong(CHAL_K + ch_id + CHAL_K_STS);
    				tmp_chall.setChallId(id);
    				tmp_chall.setChallDesc(desc);
    				tmp_chall.setChallTarget(target);
    				tmp_chall.setType(ch_type);
    				tmp_chall.setStatus(status);
    				tmp_chall.setActive(active);
    				tmp_chall.setSuccess(success);
    				tmp_chall.setStartDate(startTime);
    				tmp_chall.setEndDate(endTime);
    			}
    			if(ch_type.compareTo(CHAL_TYPE_7) == 0){
    				success = customData.getBoolean(CHAL_K + ch_id + CHAL_K_SUCCESS);
    				if(success){
						status = 100;
						row_status = "1/1";
					} else {
						row_status = "0/1";
					}
    				String id = challIndxArray.get(i);
    				String desc = correctDesc(CHAL_DESC_7, target, bonus, point_type, "");
    				startTime = customData.getLong(CHAL_K + ch_id + CHAL_K_STS);
    				tmp_chall.setChallId(id);
    				tmp_chall.setChallDesc(desc);
    				tmp_chall.setChallTarget(target);
    				tmp_chall.setType(ch_type);
    				tmp_chall.setStatus(status);
    				tmp_chall.setActive(active);
    				tmp_chall.setSuccess(success);
    				tmp_chall.setStartDate(startTime);
    				tmp_chall.setEndDate(endTime);
    			}
    			if(ch_type.compareTo(CHAL_TYPE_9) == 0){
    				int recommandation = customData.getInt(CHAL_K + ch_id + CHAL_K_RECOM);
    				status = recommandation * 100 / target;
    				row_status = recommandation + "/" + target;
    				if(status > 100)status = 100;
    				String id = challIndxArray.get(i);
    				String desc = correctDesc(CHAL_DESC_9, target, bonus, point_type, "");
    				startTime = customData.getLong(CHAL_K + ch_id + CHAL_K_STS);
    				tmp_chall.setChallId(id);
    				tmp_chall.setChallDesc(desc);
    				tmp_chall.setChallTarget(target);
    				tmp_chall.setType(ch_type);
    				tmp_chall.setStatus(status);
    				tmp_chall.setActive(active);
    				tmp_chall.setSuccess(success);
    				tmp_chall.setStartDate(startTime);
    				tmp_chall.setEndDate(endTime);
    			}
    			
    			//if(now >= startTime){	// if challenge is started
	    			if(now < endTime){	// if challenge is not ended
	    				challenges.add(tmp_chall);
	    			} else if(now < endTime + CHAL_TS_OFFSET){
	    				oldChallenges.add(tmp_chall);	// last week challenges
	    			}
    			//}
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
	
	private int calculateRemainingDays(String endTimeMillis, long now){
    	int remainingDays = 0;
    	long endTime = Long.parseLong(endTimeMillis);
    	if(now < endTime){
    		long tmpMillis = endTime - now;
    		remainingDays = (int) Math.ceil(tmpMillis / MILLIS_IN_DAY);
    	}
    	return remainingDays;
    };
	
    private String correctDesc(String desc, int target, int bonus, String p_type, String mode){
    	if(desc.contains("TARGET")){
    		desc = desc.replace("TARGET", Integer.toString(target));
    	}
    	if(target > 1 && desc.contains("viaggio")){
    		desc = desc.replace("viaggio", "viaggi");
    	}
    	if(desc.contains("BONUS")){
    		desc = desc.replace("BONUS", Integer.toString(bonus));
    	}
    	if(desc.contains("POINT_TYPE")){
    		desc = desc.replaceAll("POINT_TYPE", getCorrectPointType(p_type));
    	}
    	if(mode != null && mode != ""){
    		if(desc.contains("MODE")){
    			desc = desc.replace("MODE", getCorrectMode(mode));
    		}
    	}
    	return desc;
    }
    
    private String getCorrectMode(String mode){
    	String corr_mode = "";
    	if(mode.compareTo(CHAL_ALLOWED_MODE_W) == 0){
    		corr_mode = "a piedi";
    	}
    	if(mode.compareTo(CHAL_ALLOWED_MODE_BK) == 0){
    		corr_mode = "in bici";
    	}
    	if(mode.compareTo(CHAL_ALLOWED_MODE_BKS) == 0){
    		corr_mode = "con bici condivisa";
    	}
    	if(mode.compareTo(CHAL_ALLOWED_MODE_T) == 0){
    		corr_mode = "in treno";
    	}
    	if(mode.compareTo(CHAL_ALLOWED_MODE_B) == 0){
    		corr_mode = "in autobus";
    	}
    	if(mode.compareTo(CHAL_ALLOWED_MODE_C) == 0){
    		corr_mode = "in auto";
    	}
    	if(mode.compareTo(CHAL_ALLOWED_MODE_Z) == 0){
    		corr_mode = "a impatto zero";
    	}
    	if(mode.compareTo(CHAL_ALLOWED_MODE_P) == 0){
    		corr_mode = "promoted";
    	}
    	if(mode.compareTo(CHAL_ALLOWED_MODE_W_DIS) == 0){
    		corr_mode = "a piedi";
    	}
    	if(mode.compareTo(CHAL_ALLOWED_MODE_BK_DIS) == 0){
    		corr_mode = "in bici";
    	}
    	if(mode.compareTo(CHAL_ALLOWED_MODE_BKS_DIS) == 0){
    		corr_mode = "con bici condivisa";
    	}
    	if(mode.compareTo(CHAL_ALLOWED_MODE_T_DIS) == 0){
    		corr_mode = "in treno";
    	}
    	if(mode.compareTo(CHAL_ALLOWED_MODE_B_DIS) == 0){
    		corr_mode = "in autobus";
    	}
    	if(mode.compareTo(CHAL_ALLOWED_MODE_C_DIS) == 0){
    		corr_mode = "in auto";
    	}
    	if(mode.compareTo(CHAL_ALLOWED_MODE_Z_DIS) == 0){
    		corr_mode = "a impatto zero";
    	}
    	if(mode.compareTo(CHAL_ALLOWED_MODE_P_DIS) == 0){
    		corr_mode = "promoted";
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
    
    private String getCorrectTypeString(String stringtype){
    	String result = "";
    	if(stringtype.compareTo(CHAL_ALLOWED_PT_GREEN) == 0){
    		result = CHAL_PT_GREEN_STRING;
    	}
    	if(stringtype.compareTo(CHAL_ALLOWED_PT_HEALTH) == 0){
    		result = CHAL_PT_HEALTH_STRING;
    	}
    	if(stringtype.compareTo(CHAL_ALLOWED_PT_PR) == 0){
    		result = CHAL_PT_PR_STRING;
    	}
    	return result;
    };
	

}
