package eu.trentorise.smartcampus.gamification_web.controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Iterables;

import eu.trentorise.smartcampus.gamification_web.models.Event;
import eu.trentorise.smartcampus.gamification_web.models.PersonalData;
import eu.trentorise.smartcampus.gamification_web.models.PlayerClassification;
import eu.trentorise.smartcampus.gamification_web.models.PlayerStatus;
import eu.trentorise.smartcampus.gamification_web.models.SurveyData;
import eu.trentorise.smartcampus.gamification_web.models.UserCheck;
import eu.trentorise.smartcampus.gamification_web.models.classification.ClassificationData;
import eu.trentorise.smartcampus.gamification_web.repository.ChallengeDescriptionDataSetup;
import eu.trentorise.smartcampus.gamification_web.repository.Player;
import eu.trentorise.smartcampus.gamification_web.repository.PlayerRepositoryDao;
import eu.trentorise.smartcampus.gamification_web.service.ChallengesUtils;
import eu.trentorise.smartcampus.gamification_web.service.EncryptDecrypt;
import eu.trentorise.smartcampus.gamification_web.service.StatusUtils;
import eu.trentorise.smartcampus.profileservice.BasicProfileService;
import eu.trentorise.smartcampus.profileservice.model.AccountProfile;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;

@Controller
public class WsProxyController {
	
	private static transient final Logger logger = Logger.getLogger(WsProxyController.class);
	private static final String GREEN_CLASSIFICATION = "week classification";
	private static long CACHETIME = 15000;						// 10 seconds
	private static long LASTWEEKDELTA = 1000 * 60 * 60 * 24;	// one day of delta
	private Long oldWeekTimestamp;
	private Long actualTimeStamp = null;
	private String lastWeekClassification = "";
	private static final String CHECK_IN = "checkin";
	private static final String CHECK_IN_NU = "checkin_new_user_Trento_Fiera";

	@Autowired
	@Value("${smartcampus.gamification.secure.crypto.key1}")
	private String SECRET_KEY_1;
	
	@Autowired
	@Value("${smartcampus.gamification.secure.crypto.key2}")
	private String SECRET_KEY_2;
	
	@Autowired
	@Value("${smartcampus.urlws.gamification}")
	private String gamificationUrl;
	
	@Autowired
	@Value("${smartcampus.urlws.gameclass}")
	private String gamificationUrlClassification;
	
	@Autowired
	@Value("${smartcampus.gamification.url}")
	private String gamificationWebUrl;
	
	@Autowired
	@Value("${smartcampus.urlws.gameconsole}")
	private String gamificationConsoleUrl;
	
	@Autowired
	@Value("${smartcampus.urlws.post.gamification}")
	private String gamificationUrlPost;
	
	@Autowired
    private PlayerRepositoryDao playerRepositoryDao;
	
	@Autowired
    private ChallengeDescriptionDataSetup challDescriptionSetup;
	
	@Autowired
	@Value("${smartcampus.isTest}")
	private String isTest;
	
	@Autowired
    @Value("${gamification.server.bauth.username}")
    private String basicAuthUsername;
    @Autowired
    @Value("${gamification.server.bauth.password}")
    private String basicAuthPassword;
    
	@Autowired
	@Value("${aacURL}")
	private String aacURL;
	protected BasicProfileService profileService;

	@Autowired
	@Value("${smartcampus.gamification.gamename}")
	private String gameName;
	
	@Autowired
	@Value("${smartcampus.gamification.recommendation.points.min}")
	private String RECOMMENDATION_POINTS;

	@PostConstruct
	public void init() {
		profileService = new BasicProfileService(aacURL);
	}
//	
//	@Autowired
//	@Value("${smartcampus.cf.test}")
//	private String codFiscale;
	
	private String correctNameForQuery(String nikName){
		return "^"+nikName+"$";
	};
	
	HttpHeaders createHeaders( ){
		return new HttpHeaders(){
			{
				String auth = basicAuthUsername + ":" + basicAuthPassword;
				byte[] encodedAuth = Base64.encode( 
						auth.getBytes(Charset.forName("UTF-8")) );
				String authHeader = "Basic " + new String( encodedAuth );
				set( "Authorization", authHeader );
			}
		};
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "out/rest/ping")
	public String testRecommendation(HttpServletRequest request, @RequestParam String nik) throws Exception{
		String type = (isTest.compareTo("true") == 0) ? "test" : "prod";
		String status = "KO";
		Player recommender = playerRepositoryDao.findByNickIgnoreCaseAndType(correctNameForQuery(nik), type);
		if (recommender != null) {
			logger.info("Finded user with id " + recommender.getPid());
			status = "OK";
		}
		return status;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/rest/allGet")
	public @ResponseBody
	String getAll(HttpServletRequest request, @RequestParam String urlWS){
		RestTemplate restTemplate = new RestTemplate();
		logger.debug("WS-GET. Method " + urlWS);	//Added for log ws calls info in preliminary phase of portal
		String result = "";
		ResponseEntity<String> tmp_res = null;
		try {
			//result = restTemplate.getForObject(gamificationUrl + urlWS, String.class);
			tmp_res = restTemplate.exchange(gamificationUrl + urlWS, HttpMethod.GET, new HttpEntity<Object>(createHeaders()),String.class);
		} catch (Exception ex){
			logger.error(String.format("Exception in proxyController get ws. Method: %s. Details: %s", urlWS, ex.getMessage()));
		}
		if(tmp_res != null){
			result = tmp_res.getBody();
		}
		return result;	
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/rest/allGetClassification")
	public @ResponseBody
	String getAllClassification(HttpServletRequest request, @RequestParam String urlWS){
		RestTemplate restTemplate = new RestTemplate();
		logger.debug("WS-GET. Method " + urlWS);	//Added for log ws calls info in preliminary phase of portal
		String result = "";
		ResponseEntity<String> tmp_res = null;
		try {
			//result = restTemplate.getForObject(gamificationUrl + urlWS, String.class);
			tmp_res = restTemplate.exchange(gamificationUrlClassification + urlWS, HttpMethod.GET, new HttpEntity<Object>(createHeaders()),String.class);
		} catch (Exception ex){
			logger.error(String.format("Exception in proxyController get ws. Method: %s. Details: %s", urlWS, ex.getMessage()));
		}
		if(tmp_res != null){
			result = tmp_res.getBody();
		}
		return result;	
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/rest/allNiks")
	public @ResponseBody
	List<Player> getAllNiks(HttpServletRequest request, @RequestParam String urlWS) throws Exception{
		logger.debug("WS - get All profiles."); //Added for log ws calls info in preliminary phase of portal
		List<Player> list = new ArrayList<Player>();
		String type = (isTest.compareTo("true") == 0) ? "test" : "prod";
		Iterable<Player> iter = playerRepositoryDao.findAllByType(type);
		for(Player p: iter){
			logger.debug(String.format("Profile result %s", p.getNikName()));
			list.add(p);
		}
		//Map<String,Object> map = Collections.<String,Object>singletonMap("players", list);
		//return new ObjectMapper().writeValueAsString(map);
		return list;
	}
	
	List<Player> getAllNiksFromDB() throws Exception {
		logger.debug("DB - get All profiles."); //Added for log ws calls info in preliminary phase of portal
		List<Player> list = new ArrayList<Player>();
		String type = (isTest.compareTo("true") == 0) ? "test" : "prod";
		Iterable<Player> iter = playerRepositoryDao.findAllByType(type);
		for(Player p: iter){
			logger.debug(String.format("Profile result %s", p.getNikName()));
			list.add(p);
		}
		return list;
	}
	
	private void sendRecommendationToGamification(String recommenderId){
		RestTemplate restTemplate = new RestTemplate();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("actionId", "app_sent_recommandation");
		data.put("gameId", gameName);
		data.put("playerId", recommenderId);
		data.put("data", new HashMap<String, Object>());
		ResponseEntity<String> tmp_res = restTemplate.exchange(gamificationUrl + "execute", HttpMethod.POST, new HttpEntity<Object>(data,createHeaders()),String.class);
		logger.info("Sent app recommendation to gamification engine for user " + recommenderId + ": " +tmp_res.getStatusCode());
	}
	
	// Method for mobile player registration (in mobile app)
	@RequestMapping(method = RequestMethod.POST, value = "/out/rest/register")
	public @ResponseBody
	Player registerExternal(@RequestBody PersonalData data, @RequestParam String token, @RequestParam(required=false) String email, @RequestParam(required=false, defaultValue="it") String language, @RequestParam String nickname, HttpServletResponse res) {
		logger.debug("External registration. ");
		
		BasicProfile user = null;
		AccountProfile account = null;
		try {
			user = profileService.getBasicProfile(token);
			if (user == null) {
				res.setStatus(HttpStatus.UNAUTHORIZED.value());
				return null;
			}
			if(email == null){
				account = profileService.getAccountProfile(token);
				for (String aName : account.getAccountNames()) {
					for (String key : account.getAccountAttributes(aName).keySet()) {
						if (key.toLowerCase().contains("email")) {
							email = account.getAccountAttributes(aName).get(key);
							if (email != null) break;
						}
					}
					if (email != null) break;
				}
			}
		} catch (Exception e) {
			res.setStatus(HttpStatus.UNAUTHORIZED.value());
			return null;
		}
		String id = user.getUserId();
		logger.debug("External registration: found user profile with id "+id);
		String type = (isTest.compareTo("true") == 0) ? "test" : "prod";
		//Player withNick = playerRepositoryDao.findByNickAndType(nickname, type);
		Player withNick = playerRepositoryDao.findByNickIgnoreCaseAndType(correctNameForQuery(nickname), type);
		if (withNick != null && !withNick.getSocialId().equals(id)) {
			logger.debug("External registration: nickname conflict with user "+withNick.getPid());
			res.setStatus(HttpStatus.CONFLICT.value());
			return null;
		}
		Player p = playerRepositoryDao.findBySocialIdAndType(id, type);
		if (p != null) {
			logger.debug("External registration: user exists, updating data and nick");
			p.setNikName(nickname);
			p.setPersonalData(data);
		} else {
			logger.debug("External registration: new user");
			data.setTimestamp(System.currentTimeMillis());
			p = new Player(
					user.getUserId(), 
					user.getUserId(), 
					user.getName(), 
					user.getSurname(), 
					nickname, 
					email, 
					language,
					true,	// default sendMail attribute value is true
					data,
					null,
					true,
					type);
			if (StringUtils.hasText(data.getNick_recommandation())) {
				if(data.getNick_recommandation().compareTo("") != 0){
				//Player recommender = playerRepositoryDao.findByNickAndType(data.getNick_recommandation(), type);
					Player recommender = playerRepositoryDao.findByNickIgnoreCaseAndType(correctNameForQuery(data.getNick_recommandation()), type);
					if (recommender != null) {
						p.setCheckedRecommendation(false);
						//sendRecommendationToGamification(recommender.getPid());
					} else {
						p.setCheckedRecommendation(true);
					}
				}
			}
			try {
				createPlayerInGamification(user.getUserId());
				if(email != null)logger.info("Added user (mobile registration) " + email);
			} catch (Exception e) {
				logger.error("Exception in user registration to gamification " + e.getMessage());
			}
		}
		playerRepositoryDao.save(p);
		return p;
	}
	
	// Scheduled method used to check user that has registered with a recommendation nick. If they have points a recommendation is send to gamification
	@Scheduled(fixedRate = 30*60*1000)		// Repeat every 30 minutes
	public synchronized void checkRecommendation() {
		logger.debug("Starting recommendation check...");
		String allData = "";
		try {
			allData = chacheClass.get("complete");	// all classification data
		} catch (ExecutionException e) {
			logger.error("Exception in global classification reading: " + e.getMessage());
		}	
		StatusUtils statusUtils = new StatusUtils();
		Map<String, Integer> completeClassification = new HashMap<String, Integer>();
		try {
			completeClassification = statusUtils.correctGlobalClassification(allData);
		} catch (JSONException e) {
			logger.error("Exception in global classification calculating: " + e.getMessage());
		}
		String type = (isTest.compareTo("true") == 0) ? "test" : "prod";
		Iterable<Player> iter = playerRepositoryDao.findAllByTypeAndCheckedRecommendation(type, false);
		if(iter != null && Iterables.size(iter) > 0){
			for(Player p: iter){
				PersonalData pData = p.getPersonalData();
				String recommender = pData.getNick_recommandation();
				String userId = p.getSocialId();
				Integer points = completeClassification.get(userId);
				if(points != null){
					int score = points.intValue();
					logger.debug("Green leaves point user " + userId + ": " + score);
					int minRecPoints = 0;
					try	{
						minRecPoints = Integer.parseInt(RECOMMENDATION_POINTS);
					} catch (Exception ex){
						minRecPoints = 1;
					}
					if(score >= minRecPoints){
						Player recPlayer = playerRepositoryDao.findByNickIgnoreCaseAndType(correctNameForQuery(recommender), type);
						if (recommender != null) {
							sendRecommendationToGamification(recPlayer.getPid());
							p.setCheckedRecommendation(true);
							playerRepositoryDao.save(p);	//update player data in db
						}
					}
				} else {
					logger.debug("Green leaves point user " + userId + ": none");
				}
			}
		} else {
			logger.debug("No player with recommandation to check!");
		}
		logger.debug("Ending recommendation check...");
	}
	
	// Method to force the player creation in gamification engine
	private void createPlayerInGamification(String playerId) throws Exception{
		RestTemplate restTemplate = new RestTemplate();
		Map<String, Object> data = new HashMap<String, Object>();
		//data.put("actionId", "app_sent_recommandation");
		//data.put("gameId", gameName);
		data.put("playerId", playerId);
		String partialUrl = "game/" + gameName + "/player";
		ResponseEntity<String> tmp_res = restTemplate.exchange(gamificationConsoleUrl + partialUrl, HttpMethod.POST, new HttpEntity<Object>(data,createHeaders()),String.class);
		logger.info("Sent player registration to gamification engine(mobile-access) " + tmp_res.getStatusCode());
	}
	
	// Method to update the nickname data and personal data (initial survey)
	@RequestMapping(method = RequestMethod.POST, value = "/rest/updateNick")
	public @ResponseBody
	Player updateNick(HttpServletRequest request, @RequestParam String urlWS, @RequestBody Map<String, Object> data){
		logger.debug("WS-POST. Method " + urlWS + ". Passed data : " + data);
		String name = "";
		String mail = "";
		String id = data.get("id").toString();
		long millis = System.currentTimeMillis();
		PersonalData pdata = null;
		// part for personalData
		if(data.get("personalData") != null){
			String nick_recommandation = null;
			pdata = new PersonalData();
			try {
				JSONObject personalData = new JSONObject(data.get("personalData").toString());
				mail = personalData.getString("mail");
				name = personalData.getString("nickname");
				String age = personalData.getString("age");
				boolean transport = personalData.getBoolean("transport");
				JSONArray vehicles = personalData.getJSONArray("vehicle");
				List<String> vehicle_list = new ArrayList<String>();
				for(int i = 0; i < vehicles.length(); i++){
					vehicle_list.add(vehicles.get(i).toString());
				}
				int averagekm = personalData.getInt("averagekm");
				nick_recommandation = (!personalData.isNull("invitation")) ? personalData.getString("invitation") : null;
				pdata.setAge_range(age);
				pdata.setUse_transport(transport);
				pdata.setVehicles(vehicle_list);
				pdata.setAveragekm(averagekm);
				pdata.setNick_recommandation(nick_recommandation);
				pdata.setTimestamp(millis);
			} catch (JSONException e) {
				logger.error("JSON exception " + e.getMessage());
			}
		}
		String type = (isTest.compareTo("true") == 0) ? "test" : "prod";
		Player p = playerRepositoryDao.findBySocialIdAndType(id, type);
		p.setNikName(name);
		p.setMail(mail);
		p.setPersonalData(pdata);
		playerRepositoryDao.save(p);
		if (pdata.getNick_recommandation() != null) {
			//Player recommender = playerRepositoryDao.findByNickAndType(pdata.getNick_recommandation(), type);
			Player recommender = playerRepositoryDao.findByNickIgnoreCaseAndType(correctNameForQuery(pdata.getNick_recommandation()), type);
			if (recommender != null) {
				sendRecommendationToGamification(recommender.getPid());
			}
		}
		return p;	
	}
	
	//Method used to send the survey call to gamification engine (if user complete the survey the engine need to be updated with this call)
	private void sendSurveyToGamification(String playerId){
		RestTemplate restTemplate = new RestTemplate();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("actionId", "survey_complete");
		data.put("gameId", gameName);
		data.put("playerId", playerId);
		data.put("data", new HashMap<String, Object>());
		ResponseEntity<String> tmp_res = restTemplate.exchange(gamificationUrl + "execute", HttpMethod.POST, new HttpEntity<Object>(data,createHeaders()),String.class);
		logger.info("Sent app survey data to gamification engine "+tmp_res.getStatusCode());
	}
	
	//Method used to update the player ending-survey data
	@RequestMapping(method = RequestMethod.POST, value = "/rest/updateSurvey")
	public @ResponseBody
	Player updateSurvey(HttpServletRequest request, @RequestParam String urlWS,  @RequestBody SurveyData data) throws Exception{
		logger.debug("WS-POST. Method " + urlWS + ". Passed data : " + data.toString());
		Player p = null;
		if(urlWS.contains("=")){
			String playerId = urlWS.split("=")[1];
			String type = (isTest.compareTo("true") == 0) ? "test" : "prod";
			p = playerRepositoryDao.findBySocialIdAndType(playerId, type);
			p.setSurveyData(data);
			playerRepositoryDao.save(p);
			if (data != null) {
				sendSurveyToGamification(playerId);
				logger.debug("Call survey method for user " + playerId);
			}
		} else {
			 throw new Exception("No playerId passed in request");
		}
		return p;
	}
	
	// Method used to update the user mail (if user log to the system with facebook it could be without the mail)
	@RequestMapping(method = RequestMethod.POST, value = "/rest/updateMail")
	public @ResponseBody
	Player updateMail(HttpServletRequest request, @RequestParam String urlWS){
		String[] allData = urlWS.split("\\?");
		String[] allParams = allData[1].split("\\&");
		String playerid = getFieldValue(allParams[0]);
		String mail = getFieldValue(allParams[1]);
		logger.debug("WS-POST. Method " + urlWS + ". Passed data : " + mail);
		Player p = null;
		if(mail != null && mail.compareTo("") != 0){
			String type = (isTest.compareTo("true") == 0) ? "test" : "prod";
			p = playerRepositoryDao.findBySocialIdAndType(playerid, type);
			p.setMail(mail);
			playerRepositoryDao.save(p);
		}
		return p;
	}
	
	// Method used to unsubscribe user to mailing list
	@RequestMapping(method = RequestMethod.GET, value = "/out/rest/unsubscribeMail")	///{socialId}
	public 
	ModelAndView unsubscribeMail(HttpServletRequest request, @RequestParam String socialId) throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException {
		EncryptDecrypt cryptUtils = new EncryptDecrypt(SECRET_KEY_1, SECRET_KEY_2);
		Map<String, Object> model = new HashMap<String, Object>();
		String user_language = "it";
		Player p = null;
		if(socialId != null && socialId.compareTo("") != 0 && socialId.length() >= 16){
			logger.debug("WS-GET. Method unsubscribeMail. Passed data : " + socialId);
			String sId = "";
			try {
				sId = cryptUtils.decrypt(socialId);
			} catch (InvalidKeyException e1) {
				logger.error("Error in decrypting socialId: " + e1.getMessage());
			} catch (InvalidAlgorithmParameterException e2) {
				logger.error("Error in decrypting socialId: " + e2.getMessage());
			} catch (BadPaddingException e3) {
				logger.error("Error in decrypting socialId: " + e3.getMessage());
			} catch (IllegalBlockSizeException e4) {
				logger.error("Error in decrypting socialId: " + e4.getMessage());
			}
			if(sId != null && sId.compareTo("") != 0){	// case of incorrect encrypted string
				logger.info("WS-GET. Method unsubscribeMail. Finded player : " + sId);
				try {
					String type = (isTest.compareTo("true") == 0) ? "test" : "prod";
					p = playerRepositoryDao.findBySocialIdAndType(sId, type);
					p.setSendMail(false);
					playerRepositoryDao.save(p);
					user_language = (p.getLanguage() != null && p.getLanguage().compareTo("") != 0) ? p.getLanguage() : "it";
				} catch (Exception ex){
					logger.error("Error in mailing unsubscribtion " + ex.getMessage());
				}
			}
		}
		boolean res = (p != null) ? true : false;
		model.put("wsresult", res);
		model.put("language", user_language);
		return new ModelAndView("unsubscribe", model);
	}
	
	// Method used to unsubscribe user to mailing list (old version to maintain ws for old mails)
	@RequestMapping(method = RequestMethod.GET, value = "/out/rest/unsubscribeMail/{socialId}")	
	public 
	ModelAndView unsubscribeMailOld(HttpServletRequest request, @PathVariable String socialId) throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException {
		EncryptDecrypt cryptUtils = new EncryptDecrypt(SECRET_KEY_1, SECRET_KEY_2);
		Map<String, Object> model = new HashMap<String, Object>();
		String user_language = "it";
		Player p = null;
		if(socialId != null && socialId.compareTo("") != 0 && socialId.length() >= 16){
			logger.debug("WS-GET. Method unsubscribeMail. Passed data : " + socialId);
			String sId = "";
			try {
				sId = cryptUtils.decrypt(socialId);
			} catch (InvalidKeyException e1) {
				logger.error("Error in decrypting socialId: " + e1.getMessage());
			} catch (InvalidAlgorithmParameterException e2) {
				logger.error("Error in decrypting socialId: " + e2.getMessage());
			} catch (BadPaddingException e3) {
				logger.error("Error in decrypting socialId: " + e3.getMessage());
			} catch (IllegalBlockSizeException e4) {
				logger.error("Error in decrypting socialId: " + e4.getMessage());
			}
			if(sId != null && sId.compareTo("") != 0){	// case of incorrect encrypted string
				logger.info("WS-GET. Method unsubscribeMail. Finded player : " + sId);
				try {
					String type = (isTest.compareTo("true") == 0) ? "test" : "prod";
					p = playerRepositoryDao.findBySocialIdAndType(sId, type);
					p.setSendMail(false);
					playerRepositoryDao.save(p);
					user_language = (p.getLanguage() != null && p.getLanguage().compareTo("") != 0) ? p.getLanguage() : "it";
				} catch (Exception ex){
					logger.error("Error in mailing unsubscribtion " + ex.getMessage());
				}
			}
		}
		boolean res = (p != null) ? true : false;
		model.put("wsresult", res);
		model.put("language", user_language);
		return new ModelAndView("unsubscribe", model);
	}
	
	//Method used to send the survey call to gamification engine (if user complete the survey the engine need to be updated with this call)
	private void sendCheckinToGamification(String playerId, String event){
		RestTemplate restTemplate = new RestTemplate();
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, String> event_data = new HashMap<String, String>();
		if(event != null && event.compareTo("") != 0){
			event_data.put("checkinName", event);
		}
		data.put("actionId", CHECK_IN);
		data.put("gameId", gameName);
		data.put("playerId", playerId);
		data.put("data", event_data);
		ResponseEntity<String> tmp_res = restTemplate.exchange(gamificationUrl + "execute", HttpMethod.POST, new HttpEntity<Object>(data,createHeaders()),String.class);
		logger.info("Sent checkIn data to gamification engine " + tmp_res.getStatusCode() + " for player " + playerId);
	}
	
	//Method used to send the survey call to gamification engine (if user complete the survey the engine need to be updated with this call)
	private void sendNewUserCheckinToGamification(String playerId){
		RestTemplate restTemplate = new RestTemplate();
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, String> event_data = new HashMap<String, String>();
		data.put("actionId", CHECK_IN_NU);
		data.put("gameId", gameName);
		data.put("playerId", playerId);
		data.put("data", event_data);
		ResponseEntity<String> tmp_res = restTemplate.exchange(gamificationUrl + "execute", HttpMethod.POST, new HttpEntity<Object>(data,createHeaders()),String.class);
		logger.info("Sent new user checkIn data to gamification engine " + tmp_res.getStatusCode() + " for player " + playerId);
	}
		
	//Method used to update the player ending-survey data
	@RequestMapping(method = RequestMethod.POST, value = "/rest/console/sendUserCheckIn/{type}")
	public @ResponseBody
	String sendCheckin(HttpServletRequest request, @PathVariable String type, @RequestParam String playerId, @RequestParam(required=false) String event) throws Exception{
		
		Player p = null;
		if(playerId != null && playerId.compareTo("") != 0){
			if(type != null && type.compareTo(CHECK_IN) == 0){
				if(event != null && event.compareTo("") != 0){
					sendCheckinToGamification(playerId, event);
					String p_type = (isTest.compareTo("true") == 0) ? "test" : "prod";
					p = playerRepositoryDao.findBySocialIdAndType(playerId, p_type);
					Event ev = new Event(event, CHECK_IN, System.currentTimeMillis());
					if(p.getEventsCheckIn() != null){
						p.getEventsCheckIn().add(ev);
					} else {
						List<Event> events = new ArrayList<Event>();
						events.add(ev);
						p.setEventsCheckIn(events);
					}
					playerRepositoryDao.save(p);
				} else {
					throw new Exception("No event passed in request");
				}
			} else if(type != null && type.compareTo(CHECK_IN_NU) == 0) {
				sendNewUserCheckinToGamification(playerId);
				if(event != null && event.compareTo("") != 0){
					String p_type = (isTest.compareTo("true") == 0) ? "test" : "prod";
					p = playerRepositoryDao.findBySocialIdAndType(playerId, p_type);
					Event ev = new Event(event, CHECK_IN_NU, System.currentTimeMillis());
					if(p.getEventsCheckIn() != null){
						p.getEventsCheckIn().add(ev);
					} else {
						List<Event> events = new ArrayList<Event>();
						events.add(ev);
						p.setEventsCheckIn(events);
					}
					playerRepositoryDao.save(p);
				}
			}
		} else {
			 throw new Exception("No playerId passed in request");
		}
		return "OK";
	}
	
	// Method used to check if a user is registered or not to the system (by mobile app)
	@RequestMapping(method = RequestMethod.GET, value = "/out/rest/checkuser/{socialId}")
	public @ResponseBody
	UserCheck getUserData(HttpServletRequest request, @PathVariable String socialId){
		logger.debug("WS-get checkuser " + socialId);
		boolean result = false;
		String type = (isTest.compareTo("true") == 0) ? "test" : "prod";
		Player p = playerRepositoryDao.findBySocialIdAndType(socialId, type);
		if(p != null && p.getNikName() != null && p.getNikName().compareTo("") != 0){
			logger.debug(String.format("Profile find result %s", p.toJSONString()));
			result = true;
		}		
		UserCheck uc = new UserCheck(result);
		logger.debug(String.format("WS-get check if user %s already access app: %s", socialId, result));
		return uc;
	}
	
	private String getFieldValue(String completeParam){
		String val = "";
		String[] nameAndVal = completeParam.split("=");
		if(nameAndVal.length > 1){
			val = nameAndVal[1];
		}
		return val;
	}
	
	// Method used to get the user status data (by mobyle app)
	@RequestMapping(method = RequestMethod.GET, value = "/out/rest/status")
	public @ResponseBody
	PlayerStatus getPlayerStatus(HttpServletRequest request, @RequestParam String token, HttpServletResponse res) throws JSONException{
		logger.debug("WS-get status user token " + token);
		BasicProfile user = null;
		try {
			user = profileService.getBasicProfile(token);
			if (user == null) {
				res.setStatus(HttpStatus.UNAUTHORIZED.value());
				return null;
			}
		} catch (Exception e) {
			res.setStatus(HttpStatus.UNAUTHORIZED.value());
			return null;
		}
		String userId = user.getUserId();
		Player p = null;
		String nickName = "";
		String language = "it";
		String type = (isTest.compareTo("true") == 0) ? "test" : "prod";
		p = playerRepositoryDao.findBySocialIdAndType(userId, type);
		if(p != null){
			nickName = p.getNikName();
			language = ((p.getLanguage() != null) && (p.getLanguage().compareTo("") != 0)) ? p.getLanguage() : "it";
		}
		String statusUrl = "state/" + gameName + "/" + userId;
		String allData = this.getAll(request, statusUrl);
		
		ChallengesUtils challUtils = new ChallengesUtils();
		if(challUtils.getChallLongDescriptionList() == null || challUtils.getChallLongDescriptionList().isEmpty()){
			challUtils.setChallLongDescriptionList(challDescriptionSetup.getDescriptions());
		}
		
		StatusUtils statusUtils = new StatusUtils();
		return statusUtils.correctPlayerData(allData, userId, gameName, nickName, challUtils, gamificationWebUrl, 1, language);
	}
	
	// Cache for actual week classification
	LoadingCache<String, String> chacheClass = CacheBuilder.newBuilder()
		.maximumSize(100)
		.expireAfterWrite(15, TimeUnit.SECONDS)
		.build(
			new CacheLoader<String, String>() {
				public String load(String actualWeekTs) throws Exception {
					return callWSFromEngine(actualWeekTs);
				}
		});
	
	// Cache for get all nicknames method
	@SuppressWarnings("rawtypes")
	LoadingCache<String, List> chacheNiks = CacheBuilder.newBuilder()
		.maximumSize(1000)
		.expireAfterWrite(15, TimeUnit.SECONDS)
		.build(
			new CacheLoader<String, List>() {
				public List<Player> load(String actualWeekTs) throws Exception {
					return getNiks();
				}
		});
	
	// Scheduled method to cache the old week classification.
	//@Scheduled(cron="55 59 23 * * FRI") 		// Repeat every Friday at 23:59:55 PM
	@Scheduled(fixedRate = 31*60*1000) 		// Repeat every hour
	public synchronized void refreshOldWeekClassification() throws IOException {
		//oldWeekTimestamp = System.currentTimeMillis() - (LASTWEEKDELTA * 3);
		oldWeekTimestamp = System.currentTimeMillis() - (LASTWEEKDELTA * 7);
		logger.debug("Refreshing old week classification: new timestamp - " + oldWeekTimestamp);
		lastWeekClassification = callWSFromEngine(oldWeekTimestamp + "");
	}
	
	// Method used to get the user classification data (by mobyle app)
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = "/out/rest/classification")
	public @ResponseBody
	PlayerClassification getPlayerClassification(final HttpServletRequest request, @RequestParam String token, @RequestParam(required=false) Long timestamp, @RequestParam(required=false) Integer start, @RequestParam(required=false) Integer end, HttpServletResponse res) throws JSONException{
		boolean actualWeek = true;
		long currTime = System.currentTimeMillis();
		logger.debug("WS-get classification user token " + token);
		PlayerClassification playerClassificationData = new PlayerClassification();
		BasicProfile user = null;
		try {
			user = profileService.getBasicProfile(token);
			if (user == null) {
				res.setStatus(HttpStatus.UNAUTHORIZED.value());
				return null;
			}
		} catch (Exception e) {
			res.setStatus(HttpStatus.UNAUTHORIZED.value());
			return null;
		}
		String userId = user.getUserId();
		Player p = null;
		String nickName = "";
		String type = (isTest.compareTo("true") == 0) ? "test" : "prod";
		p = playerRepositoryDao.findBySocialIdAndType(userId, type);
		nickName = (p != null) ? p.getNikName() : null;
		
		String allData = "";
		// MB: part for new incremental classification: uncomment when server support this call
		/*if(timestamp != null){
			String incClassUrl = "game/" + gameName + "/incclassification/"  + GREEN_CLASSIFICATION + "?timestamp=" + timestamp;
			allData = this.getAllClassification(request, incClassUrl);
		} else {
			String classUrl = "state/" + gameName + "?page=1&size=" + maxClassificationSize;
			allData = this.getAll(request, classUrl);		// call to get all user status (classification)
		}*/
		if(timestamp != null){
			if((currTime - timestamp) > LASTWEEKDELTA){
				// last week timestamp
				actualWeek = false;
			} else {
				// current week timestamp
				actualWeek = true;
				if(actualTimeStamp == null){
					actualTimeStamp = timestamp;
				} else {
					long diff = timestamp - actualTimeStamp;
					if(diff <= CACHETIME){
						timestamp = actualTimeStamp;
					} else {
						actualTimeStamp = timestamp;
					}
				}
			}
		}
		try {
			if(actualWeek){
				allData = (timestamp != null) ? chacheClass.get("" + timestamp) : chacheClass.get("complete");
			} else {
				if(oldWeekTimestamp == null){	// the first time I need to initialize the oldWeekTimestamp Value
					oldWeekTimestamp = System.currentTimeMillis() - (LASTWEEKDELTA * 7);
				}
				if(lastWeekClassification.compareTo("") == 0){
					lastWeekClassification = callWSFromEngine(oldWeekTimestamp + "");
				}
				allData = lastWeekClassification;
			}
		} catch (ExecutionException e) {
			logger.error(e.getMessage());
		}
					
		String statusUrl = "state/" + gameName + "/" + userId;
		String statusData = this.getAll(request, statusUrl);	// call to get actual user status (user scores)
		
		List<Player> allNicks = null;
		/*try {
			allNicks = this.getAllNiks(request, "");
		} catch (Exception ex){
			logger.error("Exception in all nick names reading " + ex.getMessage());
		}*/
		try {
			allNicks = (timestamp != null) ? chacheNiks.get("" + timestamp) : chacheNiks.get("complete");
		} catch (ExecutionException e) {
			logger.error(e.getMessage());
		}
		
		StatusUtils statusUtils = new StatusUtils();
		ClassificationData actualPlayerClass = statusUtils.correctPlayerClassificationData(statusData, userId, nickName, timestamp, type);
		List<ClassificationData> playersClass = new ArrayList<ClassificationData>();
		// MB: part for new incremental classification: uncomment when server support this call
		if(timestamp != null){
			playersClass = statusUtils.correctClassificationIncData(allData, allNicks, timestamp, type);
		} else {
			playersClass = statusUtils.correctClassificationData(allData, allNicks, timestamp, type);
		}	
		
		// Sorting
		Collections.sort(playersClass, Collections.reverseOrder());
		playerClassificationData = statusUtils.completeClassificationPosition(playersClass, actualPlayerClass, start, end);
		
		return playerClassificationData;
	}
	
	// Method used to retrieve the classification data from web service call
	private String callWSFromEngine(String sTimestamp){
		final int maxClassificationSize = 1000;
		logger.debug("Retrieve all classification from DB");
		Long timestamp = null;
		if(sTimestamp != null && sTimestamp.compareTo("complete") != 0){
			timestamp = Long.parseLong(sTimestamp);
		}
		String classData = "";
		if(timestamp != null){
			String incClassUrl = "game/" + gameName + "/incclassification/"  + GREEN_CLASSIFICATION + "?timestamp=" + timestamp;
			classData = this.getAllClassification(null, incClassUrl);
		} else {
			String classUrl = "state/" + gameName + "?page=1&size=" + maxClassificationSize;
			classData = this.getAll(null, classUrl);		// call to get all user status (classification)
		}
		
		return classData;
	}
	
	/*private String callWSState(String userId){
		String statusUrl = "state/" + gameName + "/" + userId;
		String statusData = this.getAll(null, statusUrl);	// call to get actual user status (user scores)
		return statusData;
	}*/
	
	// Method used to retrieve all nick names from the db
	private List<Player> getNiks(){
		logger.debug("Retrieve all nicks from DB");
		List<Player> allNicks = null;
		try {
			allNicks = this.getAllNiksFromDB();
		} catch (Exception ex){
			logger.error("Exception in all nick names reading " + ex.getMessage());
		}
		return allNicks;
	}
	
	
	
	
}
