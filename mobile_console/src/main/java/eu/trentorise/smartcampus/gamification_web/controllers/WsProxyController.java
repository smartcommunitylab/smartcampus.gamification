package eu.trentorise.smartcampus.gamification_web.controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import eu.trentorise.smartcampus.gamification_web.models.PersonalData;
import eu.trentorise.smartcampus.gamification_web.repository.Player;
import eu.trentorise.smartcampus.gamification_web.repository.PlayerProd;
import eu.trentorise.smartcampus.gamification_web.repository.PlayerProdRepositoryDao;
import eu.trentorise.smartcampus.gamification_web.repository.PlayerRepositoryDao;

@Controller
public class WsProxyController {
	
	private static transient final Logger logger = Logger.getLogger(WsProxyController.class);

	@Autowired
	@Value("${smartcampus.urlws.gamification}")
	private String gamificationUrl;
	
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
//	
//	@Autowired
//	@Value("${smartcampus.cf.test}")
//	private String codFiscale;
	
	@RequestMapping(method = RequestMethod.GET, value = "/rest/allGet")
	public @ResponseBody
	String getAll(HttpServletRequest request, @RequestParam String urlWS){
		RestTemplate restTemplate = new RestTemplate();
		logger.info("WS-GET. Method " + urlWS);	//Added for log ws calls info in preliminary phase of portal
		String result = "";
		try {
			result = restTemplate.getForObject(gamificationUrl + urlWS, String.class);
		} catch (Exception ex){
			logger.info(String.format("Exception in proxyController get ws. Method: %s. Details: %s", urlWS, ex.getMessage()));
		}
		return result;	
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/rest/allPost")
	public @ResponseBody
	String postAll(HttpServletRequest request, @RequestParam String urlWS, @RequestBody Map<String, Object> data){
		RestTemplate restTemplate = new RestTemplate();
		logger.info("WS-POST. Method " + urlWS + ". Passed data : " + data); //Added for log ws calls info in preliminary phase of portal
		String result = "";
		try {
			//if(urlWS.contains("execute")){
			//	result = restTemplate.postForObject(gamificationUrlPost + urlWS, data, String.class);
			//} else {
				result = restTemplate.postForObject(gamificationUrl + urlWS, data, String.class);
			//}
			if(urlWS.compareTo("GetPDF") == 0 && (result.contains("error") || result.contains("exception"))){
				logger.info("WS-POST. Method " + urlWS + ". Error : " + result);
			}
		} catch (Exception ex){
			logger.info(String.format("Exception in proxyController post ws. Method: %s. Details: %s", urlWS, ex.getMessage()));
		}
		
		return result;	
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/rest/allNiks")
	public @ResponseBody
	String getAllNiks(HttpServletRequest request, @RequestParam String urlWS){
		logger.info("WS-get All profiles."); //Added for log ws calls info in preliminary phase of portal
		String result = "{ \"players\":[";
		if(isTest.compareTo("true") == 0){
			Iterable<Player> iter = playerRepositoryDao.findAll();
			for(Player p: iter){
				logger.info(String.format("Profile result %s", p.getNikName()));
				result += p.toJSONString() + ",";
			}
		} else {
			Iterable<PlayerProd> iter = playerProdRepositoryDao.findAll();
			for(PlayerProd p: iter){
				logger.info(String.format("Profile result %s", p.getNikName()));
				result += p.toJSONString() + ",";
			}
		}
		result = result.substring(0, result.length()-1);
		result += "]}";
		logger.info(String.format("WS-get all profiles result %s", result));
		return result;	
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/rest/updateNick")
	public @ResponseBody
	String updateNick(HttpServletRequest request, @RequestParam String urlWS, @RequestBody Map<String, Object> data){
		logger.info("WS-POST. Method " + urlWS + ". Passed data : " + data);
		String result = "";
		String name = "";//data.get("nickname").toString();
		String id = data.get("id").toString();
		PersonalData pdata = null;
		// part for personalData
		if(data.get("personalData") != null){
			String nick_recommandation = null;
			pdata = new PersonalData();
			//JSONObject allData = new JSONObject(data);
			try {
				JSONObject personalData = new JSONObject(data.get("personalData").toString());
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
			} catch (JSONException e) {
				logger.error("JSON exception " + e.getMessage());
			}
		}
		
		if(isTest.compareTo("true") == 0){
			Player p = playerRepositoryDao.findBySocialId(id);
			p.setNikName(name);
			p.setPersonalData(pdata);
			playerRepositoryDao.save(p);
			result = p.toJSONString();
		} else {
			PlayerProd p = playerProdRepositoryDao.findBySocialId(id);
			p.setNikName(name);
			p.setPersonalData(pdata);
			playerProdRepositoryDao.save(p);
			result = p.toJSONString();
		}
		
		return result;	
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/out/rest/checkuser/{socialId}")
	public @ResponseBody
	String getUserData(HttpServletRequest request, @PathVariable String socialId){
		logger.info("WS-get checkuser " + socialId);
		boolean result = false;
		String correctResult = "{";
		
		if(isTest.compareTo("true") == 0){
			Player p = playerRepositoryDao.findBySocialId(socialId);
			if(p != null && p.getNikName() != null && p.getNikName().compareTo("") != 0){
				logger.info(String.format("Profile find result %s", p.toJSONString()));
				result = true;
			}
		} else {
			PlayerProd pp = playerProdRepositoryDao.findBySocialId(socialId);
			if(pp != null && pp.getNikName() != null && pp.getNikName().compareTo("") != 0){
				logger.info(String.format("Profile find result %s", pp.toJSONString()));
				result = true;
			}
		}
		
		correctResult += "\"registered\":" + result + "}";
		logger.info(String.format("WS-get check if user %s already access app: %s", socialId, result));
		return correctResult;
	}
	
	
}
