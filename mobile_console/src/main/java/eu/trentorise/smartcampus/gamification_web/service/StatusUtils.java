package eu.trentorise.smartcampus.gamification_web.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import eu.trentorise.smartcampus.gamification_web.models.PlayerClassification;
import eu.trentorise.smartcampus.gamification_web.models.PlayerStatus;
import eu.trentorise.smartcampus.gamification_web.models.classification.ClassificationData;
import eu.trentorise.smartcampus.gamification_web.models.status.BadgeCollectionConcept;
import eu.trentorise.smartcampus.gamification_web.models.status.BadgeConcept;
import eu.trentorise.smartcampus.gamification_web.models.status.ChallengeConcept;
import eu.trentorise.smartcampus.gamification_web.models.status.ChallengesData;
import eu.trentorise.smartcampus.gamification_web.models.status.PlayerData;
import eu.trentorise.smartcampus.gamification_web.models.status.PointConcept;
import eu.trentorise.smartcampus.gamification_web.repository.Player;

public class StatusUtils {

	private static final String STATE = "state";
	private static final String PLAYER_ID = "playerId";
	private static final String BADGE_COLLECTION_CONCEPT = "BadgeCollectionConcept";
	private static final String BC_NAME = "name";
	private static final String BC_BADGE_EARNED = "badgeEarned";
	private static final String POINT_CONCEPT = "PointConcept";
	private static final String PC_GREEN_LEAVES = "green leaves";
	private static final String PC_NAME = "name";
	private static final String PC_SCORE = "score";
	private static final String PC_CLASSIFICATION_WEEK = "green leaves week ";
	private static final String PC_CLASSIFICATION_WEEK_TEST = "green leaves week test";
	
	public static final long GAME_STARTING_TIME = 1460757600000L;	// for RV 16 april
	public static final long GAME_STARTING_TIME_TEST = 1468101601000L; // for TN test 10 july
	public static final long MILLIS_IN_WEEK = 1000 * 60 * 60 * 24 * 7;
	
	public StatusUtils() {
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PlayerStatus correctPlayerData(String profile, String playerId, String gameId, String nickName, ChallengesUtils challUtils, String gamificationUrl) throws JSONException{
		List<ChallengesData> challenges = new ArrayList<ChallengesData>();
    	List<ChallengesData> oldChallenges = new ArrayList<ChallengesData>();
    	List<PointConcept> pointConcept = new ArrayList<PointConcept>();
    	PlayerStatus ps = new PlayerStatus();
    	if(profile != null && profile.compareTo("") != 0){
    		
    		PlayerData playerData = new PlayerData(playerId, gameId, nickName);
    		List<BadgeCollectionConcept> bcc_list = new ArrayList<BadgeCollectionConcept>();
    		ChallengeConcept cc = new ChallengeConcept();
    		JSONArray badgeCollectionData = null;
    		JSONArray pointConceptData = null;
    		JSONObject profileData = new JSONObject(profile);
    		JSONObject stateData = (!profileData.isNull(STATE)) ? profileData.getJSONObject(STATE) : null;
    		if(stateData != null){
    			badgeCollectionData = stateData.getJSONArray(BADGE_COLLECTION_CONCEPT);
    			for(int i = 0; i < badgeCollectionData.length(); i++){
    				JSONObject badgeColl = badgeCollectionData.getJSONObject(i);
    				String bc_name = (!badgeColl.isNull(BC_NAME)) ? badgeColl.getString(BC_NAME) : null;
    				List<BadgeConcept> bc_badges = new ArrayList<BadgeConcept>();
    				JSONArray bc_badgesEarned = (!badgeColl.isNull(BC_BADGE_EARNED)) ? badgeColl.getJSONArray(BC_BADGE_EARNED) : null;
    				for(int j = 0; j < bc_badgesEarned.length(); j++){
    					String b_name = bc_badgesEarned.getString(j);
    					String b_url = getUrlFromBadgeName(gamificationUrl, b_name);
    					BadgeConcept badge = new BadgeConcept(b_name, b_url);
    					bc_badges.add(badge);
    				}
    				BadgeCollectionConcept bcc = new BadgeCollectionConcept(bc_name, bc_badges);
    				bcc_list.add(bcc);
    			}
    			pointConceptData = stateData.getJSONArray(POINT_CONCEPT);
    			for(int i = 0; i < pointConceptData.length(); i++){
    				JSONObject point = pointConceptData.getJSONObject(i);
    				String pc_name = (!point.isNull(PC_NAME)) ? point.getString(PC_NAME) : null;
    				int pc_score = 0;
    				if(pc_name != null && pc_name.compareTo(PC_GREEN_LEAVES) == 0){
    					pc_score = (!point.isNull(PC_SCORE)) ? point.getInt(PC_SCORE) : null;
    					PointConcept pt = new PointConcept(pc_name, pc_score);
    					pointConcept.add(pt);
    				}
    			}
    			
    			try {
					List<List> challLists = challUtils.correctCustomData(profile, 1);
					if(challLists != null && challLists.size() == 2){
						challenges = challLists.get(0);
						oldChallenges = challLists.get(1);
						cc.setActiveChallengeData(challenges);
						cc.setOldChallengeData(oldChallenges);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
    		}
    		
    		ps.setPlayerData(playerData);
    		ps.setBadgeCollectionConcept(bcc_list);
    		ps.setPointConcept(pointConcept);
    		ps.setChallengeConcept(cc);
    	}
    	return ps;
    }
	
	public ClassificationData correctPlayerClassificationData(String profile, String playerId, String nickName, Long timestamp, String type) throws JSONException{
    	ClassificationData playerClass = new ClassificationData();
    	if(profile != null && profile.compareTo("") != 0){
    		
    		int score = 0;
    		long time = (timestamp == null || timestamp.longValue() == 0L) ? System.currentTimeMillis() : timestamp.longValue();
    		int weekNum = getActualWeek(time, type);
    		
    		JSONObject profileData = new JSONObject(profile);
    		JSONObject stateData = (!profileData.isNull(STATE)) ? profileData.getJSONObject(STATE) : null;
    		JSONArray pointConceptData = null;
    		if(stateData != null){
    			pointConceptData = stateData.getJSONArray(POINT_CONCEPT);
    			for(int i = 0; i < pointConceptData.length(); i++){
    				JSONObject point = pointConceptData.getJSONObject(i);
    				String pc_name = (!point.isNull(PC_NAME)) ? point.getString(PC_NAME) : null;
    				if(timestamp == null || timestamp.longValue() == 0L){	// global
    					if(pc_name != null && pc_name.compareTo(PC_GREEN_LEAVES) == 0){
        					score = (!point.isNull(PC_SCORE)) ? point.getInt(PC_SCORE) : null;
        				}
    				} else {												// specific week
    					if(type.compareTo("test") == 0){
	    					if(pc_name != null && pc_name.compareTo(PC_CLASSIFICATION_WEEK_TEST + weekNum) == 0){
		    					score = (!point.isNull(PC_SCORE)) ? point.getInt(PC_SCORE) : null;
		    				}
    					} else {
	    					if(pc_name != null && pc_name.compareTo(PC_CLASSIFICATION_WEEK + weekNum) == 0){
		    					score = (!point.isNull(PC_SCORE)) ? point.getInt(PC_SCORE) : null;
		    				}
    					}
    				}
    			}
    			playerClass.setNickName(nickName);
    			playerClass.setPlayerId(playerId);
    			playerClass.setScore(score);
    		}
    		
    	}
    	return playerClass;
    }
	
	public List<ClassificationData> correctClassificationData(String allStatus, List<Player> allNicks, Long timestamp, String type) throws JSONException{
		List<ClassificationData> playerClassList = new ArrayList<ClassificationData>();
    	if(allStatus != null && allStatus.compareTo("") != 0){
    		
    		int score = 0;
    		long time = (timestamp == null || timestamp.longValue() == 0L) ? System.currentTimeMillis() : timestamp;
    		int weekNum = getActualWeek(time, type);
    		
    		JSONObject allPlayersData = new JSONObject(allStatus);
    		JSONArray allPlayersDataList = (!allPlayersData.isNull("content")) ? allPlayersData.getJSONArray("content") : null;
    		if(allPlayersDataList != null){
    			for(int i = 0 ; i < allPlayersDataList.length(); i++){
		    		JSONObject profileData = allPlayersDataList.getJSONObject(i);
		    		String playerId = (!profileData.isNull(PLAYER_ID)) ? profileData.getString(PLAYER_ID) : "0";
		    		JSONObject stateData = (!profileData.isNull(STATE)) ? profileData.getJSONObject(STATE) : null;
		    		JSONArray pointConceptData = null;
		    		if(stateData != null){
		    			pointConceptData = stateData.getJSONArray(POINT_CONCEPT);
		    			for(int j = 0; j < pointConceptData.length(); j++){
		    				JSONObject point = pointConceptData.getJSONObject(j);
		    				String pc_name = (!point.isNull(PC_NAME)) ? point.getString(PC_NAME) : null;
		    				if(timestamp == null || timestamp.longValue() == 0L){	// global
		    					if(pc_name != null && pc_name.compareTo(PC_GREEN_LEAVES) == 0){
		        					score = (!point.isNull(PC_SCORE)) ? point.getInt(PC_SCORE) : null;
		        				}
		    				} else {												// specific week
		    					if(type.compareTo("test") == 0){
			    					if(pc_name != null && pc_name.compareTo(PC_CLASSIFICATION_WEEK_TEST + weekNum) == 0){
				    					score = (!point.isNull(PC_SCORE)) ? point.getInt(PC_SCORE) : null;
				    				}
		    					} else {
				    				if(pc_name != null && pc_name.compareTo(PC_CLASSIFICATION_WEEK + weekNum) == 0){
				    					score = (!point.isNull(PC_SCORE)) ? point.getInt(PC_SCORE) : null;
				    				}
		    					}
		    				}
		    			}
		    			
		    			String nickName = getPlayerNameById(allNicks, playerId);
		    			ClassificationData playerClass = new ClassificationData();
		    			playerClass.setNickName(nickName);
		    			playerClass.setPlayerId(playerId);
		    			playerClass.setScore(score);
		    			if(nickName != null && nickName.compareTo("") != 0){	// if nickName present (user registered and active)
		    				playerClassList.add(playerClass);
		    			}
		    		}
    			}
    		}
    		
    	}
    	return playerClassList;
    }
	
	public PlayerClassification completeClassificationPosition(List<ClassificationData> playersClass, ClassificationData actualPlayerClass, Integer from, Integer to){
		List<ClassificationData> playersClassCorr = new ArrayList<ClassificationData>();
		int from_index = 0;
		int to_index = playersClass.size();
		PlayerClassification pc = new PlayerClassification();
		ClassificationData prec_pt = null;
		for(int i = 0; i < playersClass.size(); i++){
			ClassificationData pt = playersClass.get(i);
			if(i > 0){
				if(pt.getScore() < prec_pt.getScore()){
	    			pt.setPosition(i + 1);
	    		} else {
	    			pt.setPosition(prec_pt.getPosition());
	    		}
			} else {
				pt.setPosition(i + 1);
			}
			prec_pt = pt;
			if(pt.getPlayerId().compareTo(actualPlayerClass.getPlayerId()) == 0){
				actualPlayerClass.setPosition(pt.getPosition());
			}
			playersClassCorr.add(pt);
		}
		if(from != null){
			from_index = from.intValue();
		}
		if(to != null){
			to_index = to.intValue();
		}
		if(from_index < 0)from_index = 0;
		if(from_index > playersClass.size())from_index = playersClass.size() - 1;
		if(to_index < 0)to_index = 0;
		if(to_index > playersClass.size())to_index = playersClass.size();
		if(from_index > to_index)from_index = to_index;
		pc.setClassificationList(playersClassCorr.subList(from_index, to_index));
		pc.setActualUser(actualPlayerClass);
		return pc;
	}
	
	private int getActualWeek(long timestamp, String type){
		int currWeek = 0;
		long millisFromGameStart = (type.compareTo("test") == 0) ? timestamp - GAME_STARTING_TIME_TEST : timestamp - GAME_STARTING_TIME;
		currWeek = (int)Math.ceil((float)millisFromGameStart / MILLIS_IN_WEEK);
		return currWeek;
	}
	
	private String getPlayerNameById(List<Player> allNicks, String id) throws JSONException{
    	boolean find = false;
    	String name = "";
    	if(allNicks != null && !allNicks.isEmpty()){
    		//JSONObject playersData = new JSONObject(allNickJson);
    		//JSONArray allNicksObjects = (!playersData.isNull("players")) ? playersData.getJSONArray("players") : null;
    		for(int i = 0; (i < allNicks.size()) && !find; i++){
    			Player player = allNicks.get(i);
    			if(player != null){
    				String socialId = player.getSocialId();
    				if(socialId.compareTo(id) == 0){
    					name = player.getNikName();
    					find = true;
    				}
    			}
    		}
    	}
    	return name;
    }
	
	private String getUrlFromBadgeName(String gamificationUrl, String b_name){
		// green leaves badges
		if(b_name.compareTo("king_week_green") == 0){
			return gamificationUrl + "/img/green/greenKingWeek.svg";
		}
		if(b_name.compareTo("50_point_green") == 0){
			return gamificationUrl + "/img/green/greenLeaves50.svg";
		}
		if(b_name.compareTo("100_point_green") == 0){
			return gamificationUrl + "/img/green/greenLeaves100.svg";
		}
		if(b_name.compareTo("200_point_green") == 0){
			return gamificationUrl + "/img/green/greenLeaves200.svg";
		}
		if(b_name.compareTo("400_point_green") == 0){
			return gamificationUrl + "/img/green/greenLeaves400.svg";
		}
		if(b_name.compareTo("800_point_green") == 0){
			return gamificationUrl + "/img/green/greenLeaves800.svg";
		}
		if(b_name.compareTo("1500_point_green") == 0){
			return gamificationUrl + "/img/green/greenLeaves1500.svg";
		}
		if(b_name.compareTo("2500_point_green") == 0){
			return gamificationUrl + "/img/green/greenLeaves2500.svg";
		}
		if(b_name.compareTo("5000_point_green") == 0){
			return gamificationUrl + "/img/green/greenLeaves5000.svg";
		}
		if(b_name.compareTo("10000_point_green") == 0){
			return gamificationUrl + "/img/green/greenLeaves10000.svg";
		}
		if(b_name.compareTo("20000_point_green") == 0){
			return gamificationUrl + "/img/green/greenLeaves20000.svg";
		}
		if(b_name.compareTo("bronze-medal-green") == 0){
			return gamificationUrl + "/img/green/leaderboardGreen3.svg";
		}
		if(b_name.compareTo("silver-medal-green") == 0){
			return gamificationUrl + "/img/green/leaderboardGreen2.svg";
		}
		if(b_name.compareTo("gold-medal-green") == 0){
			return gamificationUrl + "/img/green/leaderboardGreen1.svg";
		}
		// badges for health
		if(b_name.compareTo("king_week_health") == 0){
				return gamificationUrl + "/img/health/healthKingWeek.svg";
		}
		if(b_name.compareTo("10_point_health") == 0){
			return gamificationUrl + "/img/health/healthLeaves10.svg";
		}
		if(b_name.compareTo("25_point_health") == 0){
			return gamificationUrl + "/img/health/healthLeaves25.svg";
		}
		if(b_name.compareTo("50_point_health") == 0){
			return gamificationUrl + "/img/health/healthLeaves50.svg";
		}
		if(b_name.compareTo("100_point_health") == 0){
			return gamificationUrl + "/img/health/healthLeaves100.svg";
		}
		if(b_name.compareTo("200_point_health") == 0){
			return gamificationUrl + "/img/health/healthLeaves200.svg";
		}
		if(b_name.compareTo("bronze_medal_health") == 0){
			return gamificationUrl + "/img/health/healthBronzeMedal.svg";
		}
		if(b_name.compareTo("silver_medal_health") == 0){
			return gamificationUrl + "/img/health/healthSilverMedal.svg";
		}
		if(b_name.compareTo("gold_medal_health") == 0){
			return gamificationUrl + "/img/health/healthGoldMedal.svg";
		}
		// pr badges
		if(b_name.compareTo("king_week_pr") == 0){
			return gamificationUrl + "/img/pr/prKingWeek.svg";
		}
		if(b_name.compareTo("10_point_pr") == 0){
			return gamificationUrl + "/img/pr/prLeaves10.svg";
		}
		if(b_name.compareTo("20_point_pr") == 0){
			return gamificationUrl + "/img/pr/prLeaves20.svg";
		}
		if(b_name.compareTo("50_point_pr") == 0){
			return gamificationUrl + "/img/pr/prLeaves50.svg";
		}
		if(b_name.compareTo("100_point_pr") == 0){
			return gamificationUrl + "/img/pr/prLeaves100.svg";
		}
		if(b_name.compareTo("200_point_pr") == 0){
			return gamificationUrl + "/img/pr/prLeaves200.svg";
		}
		if(b_name.compareTo("bronze_medal_pr") == 0){
			return gamificationUrl + "/img/pr/prBronzeMedal.svg";
		}
		if(b_name.compareTo("silver_medal_pr") == 0){
			return gamificationUrl + "/img/pr/prSilverMedal.svg";
		}
		if(b_name.compareTo("gold_medal_pr") == 0){
			return gamificationUrl + "/img/pr/prGoldMedal.svg";
		}
		if(b_name.compareTo("Manifattura_parking") == 0){
			return gamificationUrl + "/img/pr/prPioneerManifattura.svg";
		}
		if(b_name.compareTo("Stadio_parking") == 0){
			return gamificationUrl + "/img/pr/prPioneerStadio.svg";
		}
		
		// badges for bike
		if(b_name.compareTo("1_bike_trip") == 0){
			return gamificationUrl + "/img/bike/bikeAficionado1.svg";
		}
		if(b_name.compareTo("5_bike_trip") == 0){
			return gamificationUrl + "/img/bike/bikeAficionado5.svg";
		}
		if(b_name.compareTo("10_bike_trip") == 0){
			return gamificationUrl + "/img/bike/bikeAficionado10.svg";
		}
		if(b_name.compareTo("25_bike_trip") == 0){
			return gamificationUrl + "/img/bike/bikeAficionado25.svg";
		}
		if(b_name.compareTo("50_bike_trip") == 0){
			return gamificationUrl + "/img/bike/bikeAficionado50.svg";
		}
		if(b_name.compareTo("100_bike_trip") == 0){
			return gamificationUrl + "/img/bike/bikeAficionado100.svg";
		}
		if(b_name.compareTo("200_bike_trip") == 0){
			return gamificationUrl + "/img/bike/bikeAficionado200.svg";
		}
		if(b_name.compareTo("500_bike_trip") == 0){
			return gamificationUrl + "/img/bike/bikeAficionado500.svg";
		}
		// badges for bike sharing
		if(b_name.compareTo("Brione - Rovereto_BSstation") == 0){
			return gamificationUrl + "/img/bike_sharing/bikeSharingPioneerBrione.svg";
		}
		if(b_name.compareTo("Lizzana - Rovereto_BSstation") == 0){
			return gamificationUrl + "/img/bike_sharing/bikeSharingPioneerLizzana.svg";
		}
		if(b_name.compareTo("Marco - Rovereto_BSstation") == 0){
			return gamificationUrl + "/img/bike_sharing/bikeSharingPioneerMarco.svg";
		}
		if(b_name.compareTo("Municipio - Rovereto_BSstation") == 0){
			return gamificationUrl + "/img/bike_sharing/bikeSharingPioneerMunicipio.svg";
		}
		if(b_name.compareTo("Noriglio - Rovereto_BSstation") == 0){
			return gamificationUrl + "/img/bike_sharing/bikeSharingPioneerNoriglio.svg";
		}
		if(b_name.compareTo("Orsi - Rovereto_BSstation") == 0){
			return gamificationUrl + "/img/bike_sharing/bikeSharingPioneerOrsi.svg";
		}
		if(b_name.compareTo("Ospedale - Rovereto_BSstation") == 0){
			return gamificationUrl + "/img/bike_sharing/bikeSharingPioneerOspedale.svg";
		}
		if(b_name.compareTo("Via Paoli - Rovereto_BSstation") == 0){
			return gamificationUrl + "/img/bike_sharing/bikeSharingPioneerPaoli.svg";
		}
		if(b_name.compareTo("P. Rosmini - Rovereto_BSstation") == 0){
			return gamificationUrl + "/img/bike_sharing/bikeSharingPioneerPRosmini.svg";
		}
		if(b_name.compareTo("Quercia - Rovereto_BSstation") == 0){
			return gamificationUrl + "/img/bike_sharing/bikeSharingPioneerQuercia.svg";
		}
		if(b_name.compareTo("Sacco - Rovereto_BSstation") == 0){
			return gamificationUrl + "/img/bike_sharing/bikeSharingPioneerSacco.svg";
		}
		if(b_name.compareTo("Stazione FF.SS. - Rovereto_BSstation") == 0){
			return gamificationUrl + "/img/bike_sharing/bikeSharingPioneerStazione.svg";
		}
		if(b_name.compareTo("Zona Industriale - Rovereto_BSstation") == 0){
			return gamificationUrl + "/img/bike_sharing/bikeSharingPioneerZonaIndustriale.svg";
		}
		if(b_name.compareTo("Mart - Rovereto_BSstation") == 0){
			return gamificationUrl + "/img/bike_sharing/bikeSharingPioneerMART.svg";
		}
		// badges for recommendation
		if(b_name.compareTo("3_recommendations") == 0){
			return gamificationUrl + "/img/recommendation/inviteFriends3.svg";
		}
		if(b_name.compareTo("5_recommendations") == 0){
			return gamificationUrl + "/img/recommendation/inviteFriends5.svg";
		}
		if(b_name.compareTo("10_recommendations") == 0){
			return gamificationUrl + "/img/recommendation/inviteFriends10.svg";
		}
		if(b_name.compareTo("25_recommendations") == 0){
			return gamificationUrl + "/img/recommendation/inviteFriends25.svg";
		}	
		// badges for public transport
		if(b_name.compareTo("5_pt_trip") == 0){
			return gamificationUrl + "/img/public_transport/publicTransportAficionado5.svg";
		}
		if(b_name.compareTo("10_pt_trip") == 0){
			return gamificationUrl + "/img/public_transport/publicTransportAficionado10.svg";
		}
		if(b_name.compareTo("25_pt_trip") == 0){
			return gamificationUrl + "/img/public_transport/publicTransportAficionado25.svg";
		}
		if(b_name.compareTo("50_pt_trip") == 0){
			return gamificationUrl + "/img/public_transport/publicTransportAficionado50.svg";
		}
		if(b_name.compareTo("100_pt_trip") == 0){
			return gamificationUrl + "/img/public_transport/publicTransportAficionado100.svg";
		}
		if(b_name.compareTo("200_pt_trip") == 0){
			return gamificationUrl + "/img/public_transport/publicTransportAficionado200.svg";
		}
		if(b_name.compareTo("500_pt_trip") == 0){
			return gamificationUrl + "/img/public_transport/publicTransportAficionado500.svg";
		}
		// badges for zero impact
		if(b_name.compareTo("1_zero_impact_trip") == 0){
			return gamificationUrl + "/img/zero_impact/zeroImpact1.svg";
		}
		if(b_name.compareTo("5_zero_impact_trip") == 0){
			return gamificationUrl + "/img/zero_impact/zeroImpact5.svg";
		}
		if(b_name.compareTo("10_zero_impact_trip") == 0){
			return gamificationUrl + "/img/zero_impact/zeroImpact10.svg";
		}
		if(b_name.compareTo("25_zero_impact_trip") == 0){
			return gamificationUrl + "/img/zero_impact/zeroImpact25.svg";
		}
		if(b_name.compareTo("50_zero_impact_trip") == 0){
			return gamificationUrl + "/img/zero_impact/zeroImpact50.svg";
		}
		if(b_name.compareTo("100_zero_impact_trip") == 0){
			return gamificationUrl + "/img/zero_impact/zeroImpact100.svg";
		}
		if(b_name.compareTo("200_zero_impact_trip") == 0){
			return gamificationUrl + "/img/zero_impact/zeroImpact200.svg";
		}
		if(b_name.compareTo("500_zero_impact_trip") == 0){
			return gamificationUrl + "/img/zero_impact/zeroImpact500.svg";
		}
		// badges for leaderboard top 3
		if(b_name.compareTo("1st_of_the_week") == 0){
			return gamificationUrl + "/img/leaderboard/leaderboard1.svg";
		}
		if(b_name.compareTo("2nd_of_the_week") == 0){
			return gamificationUrl + "/img/leaderboard/leaderboard2.svg";
		}
		if(b_name.compareTo("3rd_of_the_week") == 0){
			return gamificationUrl + "/img/leaderboard/leaderboard3.svg";
		}
		return "";
	}

}
