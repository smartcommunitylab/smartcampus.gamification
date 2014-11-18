package eu.trentorise.smartcampus.gamification_web.controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import eu.trentorise.smartcampus.gamification_web.service.EmailService;

import eu.trentorise.smartcampus.aac.AACException;
import eu.trentorise.smartcampus.gamification_web.models.Notification;
//import eu.trentorise.smartcampus.gamification_web.models.SubjectDn;
import eu.trentorise.smartcampus.gamification_web.models.UserCS;
import eu.trentorise.smartcampus.gamification_web.repository.Player;
import eu.trentorise.smartcampus.gamification_web.repository.PlayerRepositoryDao;
import eu.trentorise.smartcampus.profileservice.ProfileServiceException;
import eu.trentorise.smartcampus.profileservice.model.AccountProfile;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;

@Controller
@EnableScheduling
public class PortalController extends SCController{
	
	@Autowired
	@Value("${smartcampus.gamification.url}")
	private String mainURL;
	
	@Autowired
	@Value("${smartcampus.urlws.gamification}")
	private String gamificationUrl;
	
	@Autowired
	@Value("${smartcampus.gamification.gamename}")
	private String gameName;
	
	@Autowired
	@Value("${smartcampus.task.startupTime}")
	private String StartupTime;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	private static final Logger logger = Logger.getLogger(PortalController.class);
	
	private int i = 0;
	
	@Autowired 
    private EmailService emailService;
	
    @Autowired
    private PlayerRepositoryDao playerRepositoryDao;
	
	//Mail Params
	@Autowired
	@Value("${gamification.mail.host}")
    private String mailHost;
    @Autowired
    @Value("${gamification.mail.port}")
    private String mailPort;
    @Autowired
    @Value("${gamification.mail.username}")
    private String mailUser;
    @Autowired
    @Value("${gamification.mail.password}")
    private String mailPassword;
    @Autowired
    @Value("${gamification.mail.from}")
    private String mailFrom;
    @Autowired
    @Value("${gamification.mail.to}")
    private String mailTo;
    @Autowired
    @Value("${gamification.mail.subject}")
    private String mailSubject;
    @Autowired
    @Value("${gamification.mail.send}")
    private String mailSend;
    
	
	/*
	 * OAUTH2
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/")
	public ModelAndView index_myweb(HttpServletRequest request) throws SecurityException, ProfileServiceException {
		Map<String, Object> model = new HashMap<String, Object>();
		BasicProfile user = null;
		try {
			model.put("token", getToken(request));
			user = profileService.getBasicProfile(getToken(request));
			model.put("user_id", user.getUserId());
			model.put("user_name", user.getName());
			model.put("user_surname", user.getSurname());
			logger.info(String
					.format("I am in get root. User id: " + user.getUserId()));
			AccountProfile account = profileService.getAccountProfile(getToken(request));
			Object[] objectArray = account.getAccountNames().toArray();
		
			Map <String, String> mappaAttributi = account.getAccountAttributes(objectArray[0].toString());
			
			UserCS utente = createUserCartaServiziByMap(mappaAttributi);
			
			logger.error(String.format("Account attributes info: %s", mappaAttributi));
			//String mail = getAttributeFromId("openid.ext1.value.email", mappaAttributi);
			//model.put("e_mail", mail);
			model.put("nome", utente.getNome());
			model.put("cognome", utente.getCognome());
			model.put("sesso", utente.getSesso());
			model.put("dataNascita", utente.getDataNascita());
			model.put("provinciaNascita", utente.getProvinciaNascita());
			model.put("luogoNascita", utente.getLuogoNascita());
			model.put("codiceFiscale", utente.getCodiceFiscale());
			model.put("cellulare", utente.getCellulare());
			model.put("email", utente.getEmail());
			model.put("indirizzoRes", utente.getIndirizzoRes());
			model.put("capRes", utente.getCapRes());
			model.put("cittaRes", utente.getCittaRes());
			model.put("provinciaRes", utente.getProvinciaRes());
			model.put("issuerdn", utente.getIssuersdn());
			//model.put("subjectdn", utente.getSubjectdn());
			//String base_tmp = utente.getBase64();
			//model.put("base64", base_tmp.compareTo("") == 0 ? "noAdmin" : base_tmp);
			model.put("base64", utente.getBase64());
		} catch (Exception ex){
			logger.error(String.format("Errore di conversione: %s", ex.getMessage()));
			return new ModelAndView("redirect:/logout");
		}
		
		//SubjectDn subj = new SubjectDn(utente.getSubjectdn());
		//logger.error(String.format("Subjextdn : cn: %s; ou: %s: o: %s; c: %s", subj.getCn(), subj.getOu(),subj.getO(),subj.getC()));
		
		return new ModelAndView("index", model);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/mobile")
	public ModelAndView secureMobile(HttpServletRequest request, @RequestParam String token)
			throws SecurityException, AACException {
		List<GrantedAuthority> list = Collections.<GrantedAuthority> singletonList(new SimpleGrantedAuthority("ROLE_USER"));
		Authentication auth = new PreAuthenticatedAuthenticationToken(token, "", list);
		auth = authenticationManager.authenticate(auth);
		SecurityContextHolder.getContext().setAuthentication(auth);
		request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
				SecurityContextHolder.getContext());
		return new ModelAndView("redirect:/");
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/check")
	public ModelAndView securePage(HttpServletRequest request, @RequestParam(required = false) String code, @RequestParam(required = false) String type)
			throws SecurityException, AACException {
		String redirectUri = mainURL + "/check";
		logger.info(String.format("I am in get check. RedirectUri = %s", redirectUri));
		String userToken = aacService.exchngeCodeForToken(code, redirectUri).getAccess_token();
		//logger.info(String.format("User token = %s", userToken));
		List<GrantedAuthority> list = Collections.<GrantedAuthority> singletonList(new SimpleGrantedAuthority("ROLE_USER"));
		Authentication auth = new PreAuthenticatedAuthenticationToken(userToken, "", list);
		auth = authenticationManager.authenticate(auth);
		SecurityContextHolder.getContext().setAuthentication(auth);
		request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
				SecurityContextHolder.getContext());
		return new ModelAndView("redirect:/");
	}

	@RequestMapping(method = RequestMethod.GET, value = "/login")
	public ModelAndView secure(HttpServletRequest request) {
		String redirectUri = mainURL + "/check";
		String redirectAacService = aacService.generateAuthorizationURIForCodeFlow(redirectUri, "/google",
				"smartcampus.profile.basicprofile.me,smartcampus.profile.accountprofile.me", null);
		//logger.error(String.format("Redirect url : %s", redirectAacService));
		return new ModelAndView(
				"redirect:"
						+ redirectAacService);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/prelogin")
	public ModelAndView preSecure(HttpServletRequest request) {
		//String redirectUri = mainURL + "/check";
		logger.error(String.format("I am in pre login"));
		ModelAndView model = new ModelAndView();
		model.setViewName("landing");
		return model;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/cookie_info")
	public ModelAndView preSecureCookie(HttpServletRequest request) {
		//String redirectUri = mainURL + "/check";
		logger.error(String.format("I am in cookie info page"));
		ModelAndView model = new ModelAndView();
		model.setViewName("cookie_info");
		return model;
	}
	
//	@RequestMapping(method = RequestMethod.GET, value = "/logout")
//	public ModelAndView outSecure(HttpServletRequest request) {
//		logger.error(String.format("I am in logout"));
//		Map<String, Object> model_map = new HashMap<String, Object>();
//		
//		return new ModelAndView("landing", model_map);
//	}
	
	
	@SuppressWarnings("rawtypes")
	private String getAttributeFromId(String key, Map map){
		String value = "";
		try{
			value = map.get(key).toString();
		} catch (Exception ex){
			logger.error("No value found for key " + key);
		}
		return value;
	}
	
	@SuppressWarnings("rawtypes")
	private UserCS createUserCartaServiziByMap(Map map){
		String name = getAttributeFromId("eu.trentorise.smartcampus.givenname", map);
		String surname = getAttributeFromId("eu.trentorise.smartcampus.surname", map);
		String sesso = getAttributeFromId("pat_attribute_sesso", map);
		String dataNascita = getAttributeFromId("pat_attribute_datanascita", map);
		String provinciaNascita = getAttributeFromId("pat_attribute_provincianascita", map);
		String luogoNascita = getAttributeFromId("pat_attribute_luogonascita", map);
		String codiceFiscale = getAttributeFromId("pat_attribute_codicefiscale", map);
		String cellulare = getAttributeFromId("pat_attribute_cellulare", map);
		String email = getAttributeFromId("pat_attribute_email", map);
		String indirizzoRes = getAttributeFromId("pat_attribute_indirizzoresidenza", map);
		String capRes = getAttributeFromId("pat_attribute_capresidenza", map);
		String cittaRes = getAttributeFromId("pat_attribute_cittaresidenza", map);
		String provinciaRes = getAttributeFromId("pat_attribute_provinciaresidenza", map);
		String issuerdn = getAttributeFromId("pat_attribute_issuerdn", map);
		String subjectdn = getAttributeFromId("pat_attribute_subjectdn", map);
		String base64 = getAttributeFromId("pat_attribute_base64", map);
		
		return new UserCS(name, surname, sesso, dataNascita, provinciaNascita, luogoNascita, codiceFiscale, cellulare, email, indirizzoRes, capRes, cittaRes, provinciaRes, issuerdn, subjectdn, base64);	
	}
	
	// Here I insert a task that invoke the WS notification
	//@Scheduled(fixedRate = 5*60*1000) // Repeat once a minute
	@Scheduled(cron="0 0 8-23/2 * * *") // Repeat every two hours strarting from 8 to 23:59
	public synchronized void checkNotification(){
		
		//if(itsTime(new Date())){
			logger.error(String.format("Check Notification task. Cycle - %d", i++));
			
			String urlWS = "notification/" + gameName;
			
			RestTemplate restTemplate = new RestTemplate();
			logger.error("Notification WS GET " + urlWS);
			
			//long millis = System.currentTimeMillis() - (24*60*60*1000);	// Delta in millis of one day
			long millis = System.currentTimeMillis() - (2*60*60*1000);	// Delta in millis of 2 hours
			String timestamp = "?timestamp=" + millis;
			//String timestamp = "";
			String result = "";
			try {
				result = restTemplate.getForObject(gamificationUrl + urlWS + timestamp, String.class); //I pass the timestamp of the scheduled start time
			} catch (Exception ex){
				logger.error(String.format("Exception in proxyController get ws. Method: %s. Details: %s", urlWS, ex.getMessage()));
				//restTemplate.getErrorHandler();
			}
			
			if(result != null){
				logger.error(String.format("Notification Result Ok: %s", result));
				ArrayList<Notification> notifications = chekNotification(result);
				
				if(!notifications.isEmpty()){
					for(int i = 0; i < notifications.size(); i++){
						//Convert the playerid in a mail
						logger.error(String.format("Player %s", notifications.get(i).getPlayerId()));
						
						Player player = playerRepositoryDao.findBySocialId(notifications.get(i).getPlayerId());	
						//Player player = new Player("43", "Mattia", "Bortolamedi", "regolo85", "regolo85@gmail.com");
						
//						Player player = null;
//						boolean find = false;
//						Iterable<Player> players = playerRepositoryDao.findAll();
//						Iterator<Player> iter = players.iterator();
//						while(players.iterator().hasNext()){
//							Player tmp_player = players.iterator().next();
//							logger.error(String.format("Player in db : %s", tmp_player.getNikName()));
//							logger.error(String.format("PId : %s", tmp_player.getPId()));
//							logger.error(String.format("PlayerId notifications : %s", notifications.get(i).getPlayerId()));
//							if(tmp_player.getPId().compareTo(notifications.get(i).getPlayerId()) == 0){
//								//find = true;
//								player = tmp_player;
//							}
//						}
						String mailto = null;
						String playerName = notifications.get(i).getPlayerId();
						if(player != null){
							mailto = player.getMail();
							playerName = player.getName();
						}
						if(mailto == null || mailto.compareTo("") == 0){
							mailto = mailTo;
						}
						
						if(mailSend.compareTo("true") == 0){
							try {
								this.emailService.sendMailGamification(playerName, null, notifications.get(i).getBadge(), "np", mailto, Locale.ITALIAN);
							} catch (MessagingException e) {
								logger.error(String.format("Errore invio mail : %s", e.getMessage()));
							}
							logger.error(String.format("Invio mail a %s con notifica : %s", playerName ,notifications.get(i).toString()));
						}
					}
				}		
				
			} else {
				logger.error(String.format("Notification Result Fail: %s", result));
			}
		//}
	}
	
	/**
	 * Method checlNotification: convert the result JSON string in an array of objects
	 * @param result: input string with the json of the ws
	 * @return Notification ArrayList
	 */
	private ArrayList<Notification> chekNotification(String result){
		ArrayList<Notification> notificationList = new ArrayList<Notification>();
		logger.error(String.format("Result from WS: %s", result));
		
		// Here I have to convert string in a list of notifications
		String[] notificationStrings = result.split("}");
		for(int i = 0; i < notificationStrings.length-1; i++){
			if(notificationStrings[i].charAt(0) == ','){
				notificationStrings[i] = notificationStrings[i].substring(1);
			}
			String fieldStrings[] = notificationStrings[i].split(",");
			String gameId = cleanField(fieldStrings[0].split(":"));
			String playerId = cleanField(fieldStrings[1].split(":"));
			Long timestamp = Long.parseLong(cleanField(fieldStrings[2].split(":")));
			String badge = cleanField(fieldStrings[3].split(":"));
			Notification notification = new Notification(gameId, playerId, timestamp, badge);
			notificationList.add(notification);
		}
		
		return notificationList;
	}
	
	private String cleanField(String[] fieldStrings){
		String field = fieldStrings[1].replace('"', ' ').trim();
		return field;
	};
	
	/**
	 * Method used to check if it is time to run check notification
	 * @param date: input date now
	 * @return true if now is equal than StartupTime
	 */
	private boolean itsTime(Date date){
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy-HH:mm");
		String inputDate = dateFormat.format(date);
		String dateStrings[] = inputDate.split("-");
		String inputTime = dateStrings[1];
		
		if(inputTime.compareTo(StartupTime) == 0){
			return true;
		} else {
			return false;
		}
	}

}
