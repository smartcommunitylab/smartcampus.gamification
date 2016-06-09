package eu.trentorise.smartcampus.gamification_web.controllers;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
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

import eu.trentorise.smartcampus.gamification_web.models.PersonalData;
import eu.trentorise.smartcampus.gamification_web.models.SurveyData;
import eu.trentorise.smartcampus.gamification_web.repository.Player;
import eu.trentorise.smartcampus.gamification_web.repository.PlayerProd;
import eu.trentorise.smartcampus.gamification_web.repository.PlayerProdRepositoryDao;
import eu.trentorise.smartcampus.gamification_web.repository.PlayerRepositoryDao;
import eu.trentorise.smartcampus.profileservice.BasicProfileService;
import eu.trentorise.smartcampus.profileservice.model.AccountProfile;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;

@Controller
public class WsProxyController {
	
	private static transient final Logger logger = Logger.getLogger(WsProxyController.class);

	@Autowired
	@Value("${smartcampus.urlws.gamification}")
	private String gamificationUrl;
	
	@Autowired
	@Value("${smartcampus.urlws.gameconsole}")
	private String gamificationConsoleUrl;
	
	@Autowired
	@Value("${smartcampus.urlws.post.gamification}")
	private String gamificationUrlPost;
	
	@Autowired
    private PlayerRepositoryDao playerRepositoryDao;
	
	@Autowired
    private PlayerProdRepositoryDao playerProdRepositoryDao;
	
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

	@PostConstruct
	public void init() {
		profileService = new BasicProfileService(aacURL);
	}
//	
//	@Autowired
//	@Value("${smartcampus.cf.test}")
//	private String codFiscale;
	
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
		result = tmp_res.getBody();
		return result;	
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/rest/allNiks")
	public @ResponseBody
	String getAllNiks(HttpServletRequest request, @RequestParam String urlWS){
		logger.debug("WS-get All profiles."); //Added for log ws calls info in preliminary phase of portal
		String result = "{ \"players\":[";
		if(isTest.compareTo("true") == 0){
			Iterable<Player> iter = playerRepositoryDao.findAll();
			for(Player p: iter){
				logger.debug(String.format("Profile result %s", p.getNikName()));
				result += p.toJSONString() + ",";
			}
		} else {
			Iterable<PlayerProd> iter = playerProdRepositoryDao.findAll();
			for(PlayerProd p: iter){
				logger.debug(String.format("Profile result %s", p.getNikName()));
				result += p.toJSONString() + ",";
			}
		}
		result = result.substring(0, result.length()-1);
		result += "]}";
		logger.debug(String.format("WS-get all profiles result %s", result));
		return result;	
	}

	
	private void sendRecommendationToGamification(String recommenderId){
		RestTemplate restTemplate = new RestTemplate();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("actionId", "app_sent_recommandation");
		data.put("gameId", gameName);
		data.put("playerId", recommenderId);
		data.put("data", new HashMap<String, Object>());
		ResponseEntity<String> tmp_res = restTemplate.exchange(gamificationUrl + "execute", HttpMethod.POST, new HttpEntity<Object>(data,createHeaders()),String.class);
		logger.info("Sent app recommendation to gamification engine "+tmp_res.getStatusCode());
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/out/rest/register")
	public @ResponseBody
	String registerExternal(@RequestBody PersonalData data, @RequestParam String token, @RequestParam String nickname, HttpServletResponse res) {
		logger.debug("External registration. ");
		
		BasicProfile user = null;
		AccountProfile account = null;
		String email = null;
		try {
			user = profileService.getBasicProfile(token);
			if (user == null) {
				res.setStatus(HttpStatus.UNAUTHORIZED.value());
				return null;
			}
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
		} catch (Exception e) {
			res.setStatus(HttpStatus.UNAUTHORIZED.value());
			return null;
		}
		String id = user.getUserId();
		logger.debug("External registration: found user profile with id "+id);
		
		if(isTest.compareTo("true") == 0){
			Player withNick = playerRepositoryDao.findByNick(nickname);
			//String corrNick = "^" + data.getNick_recommandation() + "$";
			//Player withNick = playerRepositoryDao.findByNickIgnoreCase(corrNick);
			if (withNick != null && !withNick.getSocialId().equals(id)) {
				logger.debug("External registration: nickname conflict with user "+withNick.getPid());
				res.setStatus(HttpStatus.CONFLICT.value());
				return null;
			}
					
			Player p = playerRepositoryDao.findBySocialId(id);
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
						data,
						null);
				if (StringUtils.hasText(data.getNick_recommandation())) {
					Player recommender = playerRepositoryDao.findByNick(data.getNick_recommandation());
					//Player recommender = playerRepositoryDao.findByNickIgnoreCase(corrNick);
					if (recommender != null) {
						sendRecommendationToGamification(recommender.getPid());
					}
				}
				try {
					createPlayerInGamification(user.getUserId());
					if(email != null)logger.info("Added user (mobile-registration) " + email);
				} catch (Exception e) {
					logger.error("Exception in user registration to gamification " + e.getMessage());
				}
			}
			playerRepositoryDao.save(p);
			
			return p.toJSONString();
		} else {
			PlayerProd withNick = playerProdRepositoryDao.findByNick(nickname);
			//String corrNick = "^" + data.getNick_recommandation() + "$";
			//PlayerProd withNick = playerProdRepositoryDao.findByNickIgnoreCase(corrNick);
			if (withNick != null && !withNick.getSocialId().equals(id)) {
				logger.debug("External registration: nickname conflict with user "+withNick.getPid());
				res.setStatus(HttpStatus.CONFLICT.value());
				return null;
			}
			PlayerProd p = playerProdRepositoryDao.findBySocialId(id);
			if (p != null) {
				logger.debug("External registration: user exists, updating data and nick");
				p.setNikName(nickname);
				p.setPersonalData(data);
			} else {
				logger.debug("External registration: new user");
				data.setTimestamp(System.currentTimeMillis());
				p = new PlayerProd(
						user.getUserId(), 
						user.getUserId(), 
						user.getName(), 
						user.getSurname(), 
						nickname, 
						email, 
						data,
						null);
				if (StringUtils.hasText(data.getNick_recommandation())) {
					PlayerProd recommender = playerProdRepositoryDao.findByNick(data.getNick_recommandation());
					//PlayerProd recommender = playerProdRepositoryDao.findByNickIgnoreCase(corrNick);
					if (recommender != null) {
						sendRecommendationToGamification(recommender.getPid());
					}
				}
				try {
					createPlayerInGamification(user.getUserId());
					if(email != null)logger.info("Added user (mobile registration) " + email);
				} catch (Exception e) {
					logger.error("Exception in user registration to gamification " + e.getMessage());
				}
			}
			playerProdRepositoryDao.save(p);
			return p.toJSONString();
		}

	}
	
	private void createPlayerInGamification(String playerId) throws Exception{
		RestTemplate restTemplate = new RestTemplate();
		Map<String, Object> data = new HashMap<String, Object>();
		//data.put("actionId", "app_sent_recommandation");
		//data.put("gameId", gameName);
		data.put("playerId", playerId);
		String partialUrl = "game/" + gameName + "/player";
		ResponseEntity<String> tmp_res = restTemplate.exchange(gamificationConsoleUrl + partialUrl, HttpMethod.POST, new HttpEntity<Object>(data,createHeaders()),String.class);
		logger.info("Sent player registration to gamification engine(mobile-access) "+tmp_res.getStatusCode());
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/rest/updateNick")
	public @ResponseBody
	String updateNick(HttpServletRequest request, @RequestParam String urlWS, @RequestBody Map<String, Object> data){
		logger.debug("WS-POST. Method " + urlWS + ". Passed data : " + data);
		String result = "";
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
		
		if(isTest.compareTo("true") == 0){
			Player p = playerRepositoryDao.findBySocialId(id);
			p.setNikName(name);
			p.setMail(mail);
			p.setPersonalData(pdata);
			if (pdata.getNick_recommandation() != null) {
				Player recommender = playerRepositoryDao.findByNick(pdata.getNick_recommandation());
				//String corrNick = "^" + pdata.getNick_recommandation() + "$";
				//Player recommender = playerRepositoryDao.findByNickIgnoreCase(corrNick);
				if (recommender != null) {
					sendRecommendationToGamification(recommender.getPid());
				}
			}
			playerRepositoryDao.save(p);
			result = p.toJSONString();
		} else {
			PlayerProd p = playerProdRepositoryDao.findBySocialId(id);
			p.setNikName(name);
			p.setMail(mail);
			p.setPersonalData(pdata);
			playerProdRepositoryDao.save(p);
			if (pdata.getNick_recommandation() != null) {
				PlayerProd recommender = playerProdRepositoryDao.findByNick(pdata.getNick_recommandation());
				//String corrNick = "^" + pdata.getNick_recommandation() + "$";
				//PlayerProd recommender = playerProdRepositoryDao.findByNickIgnoreCase(corrNick);
				if (recommender != null) {
					sendRecommendationToGamification(recommender.getPid());
				}
			}
			result = p.toJSONString();
		}
		return result;	
	}
	
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
	
	@RequestMapping(method = RequestMethod.POST, value = "/rest/updateSurvey")
	public @ResponseBody
	String updateSurvey(HttpServletRequest request, @RequestParam String urlWS,  @RequestBody SurveyData data){
		logger.debug("WS-POST. Method " + urlWS + ". Passed data : " + data.toString());
		String result = "";
		if(urlWS.contains("=")){
			String playerId = urlWS.split("=")[1];
			if(isTest.compareTo("true") == 0){
				Player p = playerRepositoryDao.findBySocialId(playerId);
				p.setSurveyData(data);
				if (data != null) {
					sendSurveyToGamification(playerId);
					logger.debug("Call survey method for user " + playerId);
				}
				playerRepositoryDao.save(p);
				result = p.toJSONString();
			} else {
				PlayerProd p = playerProdRepositoryDao.findBySocialId(playerId);
				p.setSurveyData(data);
				playerProdRepositoryDao.save(p);
				if (data != null) {
					sendSurveyToGamification(playerId);
					logger.debug("Call survey method for user " + playerId);
				}
				result = p.toJSONString();
			}
		} else {
			result = "No playerId passed in request";
		}
		return result;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/rest/updateMail")
	public @ResponseBody
	String updateMail(HttpServletRequest request, @RequestParam String urlWS){
		String[] allData = urlWS.split("\\?");
		String[] allParams = allData[1].split("\\&");
		String playerid = getFieldValue(allParams[0]);
		String mail = getFieldValue(allParams[1]);
		logger.debug("WS-POST. Method " + urlWS + ". Passed data : " + mail);
		String result = "";
		if(mail != null && mail.compareTo("") != 0){
			if(isTest.compareTo("true") == 0){
				Player p = playerRepositoryDao.findBySocialId(playerid);
				p.setMail(mail);
				playerRepositoryDao.save(p);
				result = p.toJSONString();
			} else {
				PlayerProd p = playerProdRepositoryDao.findBySocialId(playerid);
				p.setMail(mail);
				playerProdRepositoryDao.save(p);
				result = p.toJSONString();
			}
		}
		return result;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/out/rest/checkuser/{socialId}")
	public @ResponseBody
	String getUserData(HttpServletRequest request, @PathVariable String socialId){
		logger.debug("WS-get checkuser " + socialId);
		boolean result = false;
		String correctResult = "{";
		
		if(isTest.compareTo("true") == 0){
			Player p = playerRepositoryDao.findBySocialId(socialId);
			if(p != null && p.getNikName() != null && p.getNikName().compareTo("") != 0){
				logger.debug(String.format("Profile find result %s", p.toJSONString()));
				result = true;
			}
		} else {
			PlayerProd pp = playerProdRepositoryDao.findBySocialId(socialId);
			if(pp != null && pp.getNikName() != null && pp.getNikName().compareTo("") != 0){
				logger.debug(String.format("Profile find result %s", pp.toJSONString()));
				result = true;
			}
		}
		
		correctResult += "\"registered\":" + result + "}";
		logger.debug(String.format("WS-get check if user %s already access app: %s", socialId, result));
		return correctResult;
	}
	
	private String getFieldValue(String completeParam){
		String val = "";
		String[] nameAndVal = completeParam.split("=");
		if(nameAndVal.length > 1){
			val = nameAndVal[1];
		}
		return val;
	}
	
	
}
