package eu.trentorise.smartcampus.gamification_web.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
public class WsProxyController {
	
	private static transient final Logger logger = Logger.getLogger(WsProxyController.class);

	@Autowired
	//@Value("${smartcampus.urlws.epu}")
	@Value("${smartcampus.urlws.gamification}")
	private String gamificationUrl;
	
//	@Autowired
//	@Value("${smartcampus.mode.test}")
//	private String isTest;
//	
//	@Autowired
//	@Value("${smartcampus.cf.test}")
//	private String codFiscale;
	
	@RequestMapping(method = RequestMethod.GET, value = "/rest/allGet")
	public @ResponseBody
	String getAll(HttpServletRequest request, @RequestParam String urlWS){
		RestTemplate restTemplate = new RestTemplate();
		logger.error("WS-GET. Method " + urlWS);	//Added for log ws calls info in preliminary phase of portal
		
		String result = "";
		try {
			result = restTemplate.getForObject(gamificationUrl + urlWS, String.class);
		} catch (Exception ex){
			logger.error(String.format("Exception in proxyController get ws. Method: %s. Details: %s", urlWS, ex.getMessage()));
			//restTemplate.getErrorHandler();
		}
		
		return result;	
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/rest/allPost")
	public @ResponseBody
	String postAll(HttpServletRequest request, @RequestParam String urlWS, @RequestBody Map<String, Object> data){
		RestTemplate restTemplate = new RestTemplate();
		logger.error("WS-POST. Method " + urlWS + ". Passed data : " + data); //Added for log ws calls info in preliminary phase of portal
		
		String result = "";
		try {
			result = restTemplate.postForObject(gamificationUrl + urlWS, data, String.class);
			if(urlWS.compareTo("GetPDF") == 0 && (result.contains("error") || result.contains("exception"))){
				logger.error("WS-POST. Method " + urlWS + ". Error : " + result);
			}
		} catch (Exception ex){
			logger.error(String.format("Exception in proxyController post ws. Method: %s. Details: %s", urlWS, ex.getMessage()));
			//restTemplate.getErrorHandler();
		}
		
		return result;	
	}
	
	
	
}
