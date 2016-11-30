package eu.trentorise.smartcampus.gamification_web.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import eu.trentorise.smartcampus.gamification_web.service.ChallengesUtils;
import eu.trentorise.smartcampus.gamification_web.service.EmailService;
import eu.trentorise.smartcampus.gamification_web.service.EncryptDecrypt;
import eu.trentorise.smartcampus.gamification_web.service.StatusUtils;
import eu.trentorise.smartcampus.aac.AACException;
import eu.trentorise.smartcampus.gamification_web.models.BagesData;
import eu.trentorise.smartcampus.gamification_web.models.ConsoleUserData;
import eu.trentorise.smartcampus.gamification_web.models.MailImage;
import eu.trentorise.smartcampus.gamification_web.models.Notification;
import eu.trentorise.smartcampus.gamification_web.models.PlayerStatus;
import eu.trentorise.smartcampus.gamification_web.models.Summary;
import eu.trentorise.smartcampus.gamification_web.models.WeekConfData;
import eu.trentorise.smartcampus.gamification_web.models.WeekPrizeData;
import eu.trentorise.smartcampus.gamification_web.models.WeekWinnersData;
import eu.trentorise.smartcampus.gamification_web.models.status.ChallengeConcept;
import eu.trentorise.smartcampus.gamification_web.models.status.ChallengesData;
import eu.trentorise.smartcampus.gamification_web.models.status.PointConcept;
import eu.trentorise.smartcampus.gamification_web.repository.AuthPlayer;
import eu.trentorise.smartcampus.gamification_web.repository.AuthPlayerRepositoryDao;
import eu.trentorise.smartcampus.gamification_web.repository.ChallengeDescriptionDataSetup;
import eu.trentorise.smartcampus.gamification_web.repository.Player;
import eu.trentorise.smartcampus.gamification_web.repository.PlayerRepositoryDao;
import eu.trentorise.smartcampus.gamification_web.repository.SponsorBannerDataSetup;
import eu.trentorise.smartcampus.gamification_web.security.ConsoleUserDetailsService;
import eu.trentorise.smartcampus.profileservice.ProfileServiceException;
import eu.trentorise.smartcampus.profileservice.model.AccountProfile;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;

@Controller
@EnableScheduling
public class PortalController extends SCController{
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	private static final Logger logger = Logger.getLogger(PortalController.class);
	
	private int i = 0;
	
	@Autowired 
    private EmailService emailService;
	
    @Autowired
    private PlayerRepositoryDao playerRepositoryDao;
    
    @Autowired
    private AuthPlayerRepositoryDao authPlayerRepositoryDao;
    
    @Autowired
    private ChallengeDescriptionDataSetup challDescriptionSetup;
    
    @Autowired
    private SponsorBannerDataSetup sponsorBannerSetup;
    
    @Autowired
    private ConsoleUserDetailsService consoleUserDetailsService;
    
    //Config params
    @Autowired
	@Value("${smartcampus.gamification.url}")
	private String mainURL;
	@Autowired
	@Value("${smartcampus.urlws.gamification}")
	private String gamificationUrl;
	@Autowired
	@Value("${smartcampus.urlws.gameconsole}")
	private String gamificationConsoleUrl;
	@Autowired
	@Value("${smartcampus.gamification.gamename}")
	private String gameName;
	@Autowired
	@Value("${smartcampus.task.startupTime}")
	private String StartupTime;
	@Autowired
	@Value("${smartcampus.isTest}")
	private String isTest;
	@Autowired
	@Value("${smartcampus.gamification.pointtype}")
	private String p_types;
    @Autowired
	@Value("${gamification.useAuthorizationTable}")
    private String authorizationTable;
    @Autowired
    @Value("${smartcampus.gamification.gamename}")
    private String gameid;
    @Autowired
    @Value("${gamification.server.bauth.username}")
    private String basicAuthUsername;
    @Autowired
    @Value("${gamification.server.bauth.password}")
    private String basicAuthPassword;
    @Autowired
	@Value("${smartcampus.isShortClassification}")
	private String isShortClassification;
    @Autowired
	@Value("${smartcampus.shortClassificationSize}")
	private String shortClassificationSize;
    @Autowired
	@Value("${smartcampus.gamification.secure.crypto.key1}")
	private String SECRET_KEY_1;
	@Autowired
	@Value("${smartcampus.gamification.secure.crypto.key2}")
	private String SECRET_KEY_2;
	
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
    @Autowired
    @Value("${gamification.mailredirect.url}")
    private String mailRedirectUrl;
    @Autowired
    @Value("${gamification.survey.url}")
    private String mailSurveyUrl;
    @Autowired
    @Value("${gamification.mail.startgame}")
    private String mailStartGame;
    @Autowired
	@Value("${smartcampus.gamification.url}")
	private String gamificationWebUrl;
    
    /*private final String JSON_STATE = "state";
    private final String JSON_POINTCONCEPT = "PointConcept";
    private final String JSON_NAME = "name";
    private final String JSON_SCORE = "score";*/
    private final String JSON_GAMEID = "gameId";
    private final String JSON_PLAYERID = "playerId";
    private final String JSON_TIMESTAMP = "timestamp";
    private final String JSON_BADGE = "badge";
    private static final String ITA_LANG = "it";
    private static final String ENG_LANG = "en";
    
	/*
	 * OAUTH2
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/protected")
	public ModelAndView index_gameweb_protected(HttpServletRequest request) throws SecurityException, ProfileServiceException {
		Map<String, Object> model = new HashMap<String, Object>();
		BasicProfile user = null;
		try {
			model.put("token", getToken(request));
			user = profileService.getBasicProfile(getToken(request));
			model.put("user_id", user.getUserId());
			//model.put("user_name", user.getName());
			//model.put("user_surname", user.getSurname());
			model.put("gameid", gameid);
			model.put("bauth_user", basicAuthUsername);
			model.put("bauth_password", basicAuthPassword);
			model.put("point_types", p_types);	// point type
			model.put("challenge_desc_messages", challDescriptionSetup.getDescriptions());
			model.put("week_sponsor_data", sponsorBannerSetup.getSponsors());
			model.put("isTest", isTest);
			model.put("isShortClassification", isShortClassification);
			model.put("short_classification_size", shortClassificationSize);
			
			// Here i retrieve and pass the prize list
			URL resource = getClass().getResource("/");
			String path = resource.getPath();
			String conf_directory = "conf_file";
			if(isTest.compareTo("true") == 0){
				conf_directory = "conf_file_test";
			}
			List<WeekPrizeData> mailPrizeFileData = readWeekPrizesFile(path + "mail/" + conf_directory + "/game_week_prize.csv");
			List<WeekPrizeData> mailPrizeFileDataEng = readWeekPrizesFile(path + "mail/" + conf_directory + "/game_week_prize_en.csv");
			model.put("prizes", mailPrizeFileData);
			model.put("prizes_eng", mailPrizeFileDataEng);
			
			logger.debug(String
					.format("I am in get root. User id: " + user.getUserId()));
			AccountProfile account = profileService.getAccountProfile(getToken(request));
			Object[] objectArray = account.getAccountNames().toArray();
			Map <String, String> mappaAttributi = account.getAccountAttributes(objectArray[0].toString());
			String mailKey = "";
			String nick = "";
			boolean mailFind = false;
			for(String key : mappaAttributi.keySet()){
				if(mailFind)break;
				if(key.contains("email")){
					mailKey = key;
					mailFind = true;
				}
			}
			JSONObject attributes = new JSONObject(mappaAttributi);
			String attribute_mail = attributes.getString(mailKey);
			
			String type = (isTest.compareTo("true") == 0) ? "test" : "prod";
			//if(isTest.compareTo("true") == 0){
				
				// Check if the user belongs to the list of the testers (in test)
				Player player_check = playerRepositoryDao.findBySocialIdAndType(user.getUserId(),type);	// app user table
				if(player_check == null){
					//String attribute_mail = account.getAttribute(objectArray[0].toString(), "openid.ext1.value.email");
					logger.debug(String.format("Player to add: mail %s.", attribute_mail));
					if(attribute_mail != null){
						if(authorizationTable.compareTo("true") == 0){
							AuthPlayer auth_p = authPlayerRepositoryDao.findByMailAndType(attribute_mail, type);
							if(auth_p != null){
								logger.info(String.format("Add player: authorised %s.", auth_p.toJSONString()));
								Player new_p = new Player(user.getUserId(), user.getUserId(), user.getName(), user.getSurname(), auth_p.getNikName(), auth_p.getMail(), "it", true, null, null, true, type);
								//Player new_p = new Player(user.getUserId(), user.getUserId(), user.getName(), user.getSurname(), auth_p.getNikName(), auth_p.getMail(), "it", true, null, null, type);
								playerRepositoryDao.save(new_p);
								// here I call an api from gengine console
								createPlayerInGamification(user.getUserId());
								logger.info(String.format("Add player: created player %s.", new_p.toJSONString()));
							} else {
								return new ModelAndView("redirect:/logout");	// user not allowed - logout
							}
						} else {
							// case of no authentication table and user not in user table: I add the user
							//nick = generateNick(user.getName(), user.getSurname(), user.getUserId());
							Player new_p = new Player(user.getUserId(), user.getUserId(), user.getName(), user.getSurname(), nick, attribute_mail, "it", true, null, null, true, type);
							//Player new_p = new Player(user.getUserId(), user.getUserId(), user.getName(), user.getSurname(), nick, attribute_mail, "it", true, null, null, type);
							playerRepositoryDao.save(new_p);
							// here I call an api from gengine console
							createPlayerInGamification(user.getUserId());
							logger.info(String.format("Add new player: created player %s.", new_p.toJSONString()));
						}
					}
				}
			//} else {
				// Check if the user belongs to the list of the testers (in test)
				/*PlayerProd player_check = playerProdRepositoryDao.findBySocialId(user.getUserId());
				if(player_check == null){
					//String attribute_mail = account.getAttribute(objectArray[0].toString(), "openid.ext1.value.email");
					logger.info(String.format("Player to add: mail %s.", attribute_mail));
					if(attribute_mail != null){
						if(authorizationTable.compareTo("true") == 0){
							AuthPlayerProd auth_p = authPlayerProdRepositoryDao.findByMail(attribute_mail);
							if(auth_p != null){
								logger.info(String.format("Add player: authorised %s.", auth_p.toJSONString()));
								PlayerProd new_p = new PlayerProd(user.getUserId(), user.getUserId(), user.getName(), user.getSurname(), auth_p.getNikName(), auth_p.getMail(), null, null);
								playerProdRepositoryDao.save(new_p);
								// here I call an api from gengine console
								createPlayerInGamification(user.getUserId());
								logger.info(String.format("Add player: created player %s.", new_p.toJSONString()));
							} else {
								return new ModelAndView("redirect:/logout");	// user not allowed - logout
							}
						} else {
							// case of no authentication table and user not in user table: I add the user
							//nick = generateNick(user.getName(), user.getSurname(), user.getUserId());
							PlayerProd new_p = new PlayerProd(user.getUserId(), user.getUserId(), user.getName(), user.getSurname(), nick, attribute_mail, null, null);
							playerProdRepositoryDao.save(new_p);
							// here I call an api from gengine console
							createPlayerInGamification(user.getUserId());
							logger.info(String.format("Add new player: created player %s.", new_p.toJSONString()));
						}
					}
				}*/
			//}
		} catch (Exception ex){
			logger.error(String.format("Errore di conversione: %s", ex.getMessage()));
			return new ModelAndView("redirect:/logout");
		}
		return new ModelAndView("index", model);
	}
    
    @RequestMapping(method = RequestMethod.GET, value = "/")
	public ModelAndView index_gameweb(HttpServletRequest request) throws SecurityException, ProfileServiceException {
		Map<String, Object> model = new HashMap<String, Object>();
		// Here i retrieve and pass the prize list
		URL resource = getClass().getResource("/");
		String path = resource.getPath();
		String conf_directory = "conf_file";
		if(isTest.compareTo("true") == 0){
			conf_directory = "conf_file_test";
		}
		List<WeekPrizeData> mailPrizeFileData = readWeekPrizesFile(path + "mail/" + conf_directory + "/game_week_prize.csv");
		List<WeekPrizeData> mailPrizeFileDataEng = readWeekPrizesFile(path + "mail/" + conf_directory + "/game_week_prize_en.csv");
		model.put("prizes", mailPrizeFileData);
		model.put("prizes_eng", mailPrizeFileDataEng);
		
		return new ModelAndView("index", model);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/mobile")
	public ModelAndView secureMobile(HttpServletRequest request, @RequestParam String token, @RequestParam(required=false) String redirect_url)
			throws SecurityException, AACException {
		ModelAndView redirectMAV = null;
		List<GrantedAuthority> list = Collections.<GrantedAuthority> singletonList(new SimpleGrantedAuthority("ROLE_USER"));
		Authentication auth = new PreAuthenticatedAuthenticationToken(token, "", list);
		auth = authenticationManager.authenticate(auth);
		SecurityContextHolder.getContext().setAuthentication(auth);
		request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
				SecurityContextHolder.getContext());
		if(redirect_url != null && redirect_url.compareTo("") != 0){
			redirectMAV = new ModelAndView("redirect:/#/" + redirect_url);
		} else {
			redirectMAV = new ModelAndView("redirect:/");
		}
		return redirectMAV;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/check")
	public ModelAndView securePage(HttpServletRequest request, @RequestParam(required = false) String code, @RequestParam(required = false) String type)
			throws SecurityException, AACException {
		String redirectUri = mainURL + "/check";
		logger.debug(String.format("I am in get check. RedirectUri = %s", redirectUri));
		String userToken = aacService.exchngeCodeForToken(code, redirectUri).getAccess_token();
		//logger.info(String.format("User token = %s", userToken));
		List<GrantedAuthority> list = Collections.<GrantedAuthority> singletonList(new SimpleGrantedAuthority("ROLE_USER"));
		Authentication auth = new PreAuthenticatedAuthenticationToken(userToken, "", list);
		auth = authenticationManager.authenticate(auth);
		SecurityContextHolder.getContext().setAuthentication(auth);
		request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
				SecurityContextHolder.getContext());
		return new ModelAndView("redirect:/protected");
	}

	@RequestMapping(method = RequestMethod.GET, value = "/login")
	public ModelAndView secure(HttpServletRequest request) {
		String redirectUri = mainURL + "/check";
		String redirectAacService = aacService.generateAuthorizationURIForCodeFlow(redirectUri, "/google",
				"profile.basicprofile.me,profile.accountprofile.me", null);	//"smartcampus.profile.basicprofile.me,smartcampus.profile.accountprofile.me"
		return new ModelAndView(
				"redirect:"
						+ redirectAacService);
	}
	

	/*@RequestMapping(method = RequestMethod.GET, value = "/loginfb")
	public ModelAndView secureFb(HttpServletRequest request) {
		String redirectUri = mainURL + "/check";
		String redirectAacService = aacService.generateAuthorizationURIForCodeFlow(redirectUri, "/facebook",
				"profile.basicprofile.me,profile.accountprofile.me", null);	//"smartcampus.profile.basicprofile.me,smartcampus.profile.accountprofile.me"
		return new ModelAndView(
				"redirect:"
						+ redirectAacService);
	}*/
	
	/*@RequestMapping(method = RequestMethod.GET, value = "/prelogin")
	public ModelAndView preSecure(HttpServletRequest request) {
		logger.debug(String.format("I am in pre login"));
		ModelAndView model = new ModelAndView();
		//model.setViewName("landing");
		model.setViewName("login_console");
		return model;
	}*/
	
	@RequestMapping(method = RequestMethod.GET, value = "/console_check")
	public ModelAndView securePageConsole(HttpServletRequest request, @RequestParam(required = false) String code)
			throws SecurityException, AACException {
		String redirectUri = mainURL + "/console_check";
		logger.info(String.format("I am in get check console. RedirectUri = %s", redirectUri));
		String userToken = aacService.exchngeCodeForToken(code, redirectUri).getAccess_token();
		List<GrantedAuthority> list = Collections.<GrantedAuthority> singletonList(new SimpleGrantedAuthority("ROLE_USER"));
		Authentication auth = new PreAuthenticatedAuthenticationToken(userToken, "", list);
		auth = authenticationManager.authenticate(auth);
		SecurityContextHolder.getContext().setAuthentication(auth);
		request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
				SecurityContextHolder.getContext());
		// Only for tests and developing! Remove before distribution
		//practiceController.initPractices(request, "1");
		
		return new ModelAndView("redirect:/console/");
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/console/")
	public ModelAndView index_console(ModelMap model, Principal principal) {
		String name = "scoAdmin"; //principal.getName();
		ConsoleUserData user = consoleUserDetailsService.getUserDetails(name);
		
		model.addAttribute("user_name", user.getName());
		model.put("isShortClassification", isShortClassification);
		//model.addAttribute("mailMessage", "test messaggio successo");
		return new ModelAndView("console", model);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/console/home")
	public ModelAndView index_console_home(ModelMap model, Principal principal) {
		String name = "scoAdmin"; //principal.getName();
		ConsoleUserData user = consoleUserDetailsService.getUserDetails(name);
		
		model.addAttribute("user_name", user.getName());
		//model.addAttribute("mailMessage", "test messaggio successo");
		return new ModelAndView("console", model);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/console/console_login")
	public ModelAndView secureConsole(ModelMap model) {
		logger.error(String.format("I am in get login console"));
		//To use the basic autentication I think is necessary to
		// 1 - change the redirect Uri to a page with a login form
		// 2 - in the login form invoke a new metho that check the user credential
		// 3 - if success redirect to home_console else show the error
		return new ModelAndView("login_console");
	}
	
	/*@RequestMapping(method = RequestMethod.GET, value = "/console/logout")
	public ModelAndView secureLogout(ModelMap model) {
		logger.error(String.format("I am in logout console"));
		//To use the basic autentication I think is necessary to
		// 1 - change the redirect Uri to a page with a login form
		// 2 - in the login form invoke a new metho that check the user credential
		// 3 - if success redirect to home_console else show the error
		return new ModelAndView("login_console");
	}*/
	
	@RequestMapping(method = RequestMethod.GET, value = "/cookie_info")
	public ModelAndView preSecureCookie(HttpServletRequest request) {
		logger.debug(String.format("I am in cookie info page"));
		ModelAndView model = new ModelAndView();
		model.setViewName("cookie_info");
		return model;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/cookie_licence")
	public ModelAndView preSecureCookieLicence(HttpServletRequest request) {
		logger.debug(String.format("I am in cookie licence info page"));
		ModelAndView model = new ModelAndView();
		model.setViewName("cookie_licence");
		return model;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/view_prizes_ext")	// Changed url to ws to fail the direct external page url redirect
	public ModelAndView preSecurePrizesPage(HttpServletRequest request) {
		logger.debug(String.format("I am in prizes info page"));
		Map<String, Object> model = new HashMap<String, Object>();
		// Here i retrieve and pass the prize list
		URL resource = getClass().getResource("/");
		String path = resource.getPath();
		String conf_directory = "conf_file";
		if(isTest.compareTo("true") == 0){
			conf_directory = "conf_file_test";
		}
		List<WeekPrizeData> mailPrizeFileData = readWeekPrizesFile(path + "mail/" + conf_directory + "/game_week_prize.csv");
		List<WeekPrizeData> mailPrizeFileDataEng = readWeekPrizesFile(path + "mail/" + conf_directory + "/game_week_prize_en.csv");
		model.put("prizes", mailPrizeFileData);
		model.put("prizes_eng", mailPrizeFileDataEng);
		return new ModelAndView("g_prizes", model);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/view_rules")
	public ModelAndView preSecureRulesPage(HttpServletRequest request) {
		logger.debug(String.format("I am in rules info page"));
		ModelAndView model = new ModelAndView();
		model.setViewName("g_rules");
		return model;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/view_privacy")
	public ModelAndView preSecurePrivacyPage(HttpServletRequest request) {
		logger.debug(String.format("I am in privacy info page"));
		ModelAndView model = new ModelAndView();
		model.setViewName("g_privacy");
		return model;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/compile_survey/{pId}")
	public ModelAndView compileSurveyPage(HttpServletRequest request, @PathVariable String pId, @RequestParam(required=false) String language) {
		logger.info(String.format("Passed player id: " + pId));
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("user_id", pId);
		String used_lang = "it";
		if(language != null){
			if(language.compareTo("en") == 0){
				used_lang = "en";
			}
		}
		model.put("language", used_lang);
		return new ModelAndView("survey", model);
	}
	
	private void createPlayerInGamification(String playerId) throws Exception{
		RestTemplate restTemplate = new RestTemplate();
		Map<String, Object> data = new HashMap<String, Object>();
		//data.put("actionId", "app_sent_recommandation");
		//data.put("gameId", gameName);
		data.put("playerId", playerId);
		String partialUrl = "game/" + gameName + "/player";
		ResponseEntity<String> tmp_res = restTemplate.exchange(gamificationConsoleUrl + partialUrl, HttpMethod.POST, new HttpEntity<Object>(data,createHeaders()),String.class);
		logger.info("Sent player registration to gamification engine "+tmp_res.getStatusCode());
	}
	
	
//	@RequestMapping(method = RequestMethod.GET, value = "/logout")
//	public ModelAndView outSecure(HttpServletRequest request) {
//		logger.error(String.format("I am in logout"));
//		Map<String, Object> model_map = new HashMap<String, Object>();
//		
//		return new ModelAndView("landing", model_map);
//	}
	
	// Here I insert a task that invoke the WS notification
	//@Scheduled(fixedRate = 5*60*1000) // Repeat every 5 minutes
	//@Scheduled(cron="0 0 17 * * FRI") 		// Repeat every Friday at 17:00 PM
	public synchronized void checkNotification() throws IOException, NoSuchPaddingException, NoSuchAlgorithmException {
		EncryptDecrypt cryptUtils = new EncryptDecrypt(SECRET_KEY_1, SECRET_KEY_2);
		StatusUtils statusUtils = new StatusUtils();
		ArrayList<Summary> summaryMail = new ArrayList<Summary>();
		long millis = System.currentTimeMillis() - (7*24*60*60*1000);	// Delta in millis of one week //long millis = 1415660400000L; //(for test)
		String timestamp = "?timestamp=" + millis;
		
		ChallengesUtils challUtils = new ChallengesUtils();
		challUtils.setChallLongDescriptionList(challDescriptionSetup.getDescriptions());
		
		URL resource = getClass().getResource("/");
		String path = resource.getPath();
		logger.debug(String.format("class path : %s", path));
		
		ArrayList<MailImage> standardImages = new ArrayList<MailImage>();
		
		File greenScore = new File(path + "mail/img/green/greenLeavesbase.png");
		File healthScore = new File(path + "mail/img/health/healthLeavesBase.png");
		File prScore = new File(path + "mail/img/pr/prLeaves.png");
		File footer = new File(path + "mail/img/templateMail.png");
		File foglie03 = new File(path + "mail/img/foglie03.png");
		File foglie04 = new File(path + "mail/img/foglie04.png");
		standardImages.add(new MailImage(foglie03.getName(), FileUtils.readFileToByteArray(foglie03), "image/png"));
		standardImages.add(new MailImage(foglie04.getName(), FileUtils.readFileToByteArray(foglie04), "image/png"));
		standardImages.add(new MailImage(greenScore.getName(), FileUtils.readFileToByteArray(greenScore), "image/png"));
		standardImages.add(new MailImage(healthScore.getName(), FileUtils.readFileToByteArray(healthScore), "image/png"));
		standardImages.add(new MailImage(prScore.getName(), FileUtils.readFileToByteArray(prScore), "image/png"));
		standardImages.add(new MailImage(footer.getName(), FileUtils.readFileToByteArray(footer), "image/png"));
		
		logger.debug(String.format("Image data: path - %s length: %d", greenScore.getAbsolutePath(), greenScore.length()));
		
		//ArrayList<BagesData> allBadgeTest = getAllBadges(path);
		//try {
		//	this.emailService.sendMailGamification("NikName", "43", "32", "112", null, null, allBadgeTest, standardImages ,mailTo, Locale.ITALIAN);
		//} catch (MessagingException e1) {
		//	e1.printStackTrace();
		//}
			// New method
			logger.debug(String.format("Check Notification task. Cycle - %d", i++));
			// Here I have to read the mail conf file data
			String conf_directory = "conf_file";
			if(isTest.compareTo("true") == 0){
				conf_directory = "conf_file_test";
			}
			List<WeekConfData> mailConfigurationFileData = readWeekConfFile(path + "mail/" + conf_directory + "/game_week_configuration.csv");
			List<WeekPrizeData> mailPrizeFileData = readWeekPrizesFile(path + "mail/" + conf_directory + "/game_week_prize.csv");
			List<WeekPrizeData> mailPrizeFileDataEng = readWeekPrizesFile(path + "mail/" + conf_directory + "/game_week_prize_en.csv");
			List<WeekWinnersData> mailWinnersFileData = readWeekWinnersFile(path + "mail/" + conf_directory + "/game_week_winners.csv");
			List<WeekPrizeData> mailPrizeActualData = new ArrayList<WeekPrizeData>();
			// here I have to add the new mail parameters readed from csv files
			String actual_week = "";
			String actual_week_theme = "";
			String actual_week_theme_it = "";
			String actual_week_theme_eng = "";
			String last_week = "";
			Boolean are_chall = false;
			Boolean are_prizes = false;
			Boolean are_prizes_last_week = false;
			for(int i = 0; i < mailConfigurationFileData.size(); i++){
				WeekConfData tmpWConf = mailConfigurationFileData.get(i);
				if(tmpWConf.isActual()){
					actual_week = tmpWConf.getWeekNum();
					actual_week_theme_it = tmpWConf.getWeekTheme();
					actual_week_theme_eng = tmpWConf.getWeekThemeEng();
					last_week = Integer.toString(Integer.parseInt(actual_week) - 1);
					are_chall = tmpWConf.isChallenges();
					are_prizes = tmpWConf.isPrizes();
					are_prizes_last_week = tmpWConf.isPrizesLast();
					mailPrizeActualData = readWeekPrizesFileData(actual_week, mailPrizeFileData);
				}
			}
			String type = (isTest.compareTo("true") == 0) ? "test" : "prod";
			Iterable<Player> iter = playerRepositoryDao.findAllByType(type);
			
			for(Player p: iter){
				logger.debug(String.format("Profile finded  %s", p.getNikName()));
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e1) {
					logger.error(String.format("Errore in attesa thread: %s", e1.getMessage()));
				}
					
				if(p.isSendMail()){
					String surveyLanguage = "?language=" + ((p.getLanguage() != null) ? p.getLanguage() : "it");
					String compileSurveyUrl = mailSurveyUrl + "/" + p.getSocialId() + surveyLanguage;
					String encriptedId = "";
					try {
						encriptedId = cryptUtils.encrypt(p.getSocialId());
					} catch (InvalidKeyException e1) {
						logger.error("Errore in socialId encripting: " + e1.getMessage());
					} catch (InvalidAlgorithmParameterException e2) {
						logger.error("Errore in socialId encripting: " + e2.getMessage());
					} catch (BadPaddingException e3) {
						logger.error("Errore in socialId encripting: " + e3.getMessage());
					} catch (IllegalBlockSizeException e4) {
						logger.error("Errore in socialId encripting: " + e4.getMessage());
					}
					String unsubcribionLink = mainURL + "/out/rest/unsubscribeMail/?socialId=" + encriptedId;	//p.getSocialId();
					//ArrayList<State> states = null;
					List<PointConcept> states = null;
					ArrayList<Notification> notifications = null;
					ArrayList<BagesData> someBadge = null;
					List<ChallengesData> challenges = null;
					List<ChallengesData> lastWeekChallenges = null;
					Locale mailLoc = Locale.ITALIAN;
				
					try {
						// WS State Invocation
						String urlWSState = "state/" + gameName + "/" + p.getSocialId();
						//states = getState(urlWSState);
						// Challenges correction
						String completeState = getAllChallenges(urlWSState);
						String language = p.getLanguage();
						if(language == null || language.compareTo("") == 0){
							language = ITA_LANG;
						}
						if(language.compareTo(ENG_LANG) == 0){
							actual_week_theme = actual_week_theme_eng;
							mailLoc = Locale.ENGLISH;
							mailPrizeActualData = readWeekPrizesFileData(actual_week, mailPrizeFileDataEng);
						} else {
							actual_week_theme = actual_week_theme_it;
							mailLoc = Locale.ITALIAN;
							mailPrizeActualData = readWeekPrizesFileData(actual_week, mailPrizeFileData);
						}
						try {
							PlayerStatus completePlayerStatus = statusUtils.correctPlayerData(completeState, p.getSocialId(), gameName, p.getNikName(), challUtils, gamificationWebUrl, 0, language);
							states = completePlayerStatus.getPointConcept();
							ChallengeConcept challLists = completePlayerStatus.getChallengeConcept();
							//@SuppressWarnings("rawtypes")
							//List<List> challLists = challUtils.correctCustomData(completeState, 0);
							if(challLists != null){
								challenges = challLists.getActiveChallengeData();
								lastWeekChallenges = challLists.getOldChallengeData();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						
						// WS Notification Invocation
						String urlWSNot = "notification/" + gameName + "/" + p.getSocialId();	
						notifications = getNotifications(urlWSNot, timestamp);
					} catch (InterruptedException ie){
						logger.error(String.format("Ws invoke sleep exception  %s", ie.getMessage()));
					}
					
					if(notifications != null && notifications.size() > 0){
						ArrayList<BagesData> allBadge = getAllBadges(path);
						someBadge = checkCorrectBadges(allBadge, notifications);	
					}
					
					String mailto = null;
					mailto = p.getMail();
					String playerName = p.getNikName();
					if(mailto == null || mailto.compareTo("") == 0){
						mailto = mailTo;
					}
					//if(specialPlayers.contains(p.getSocialId())){
					if(mailSend.compareTo("true") == 0 && playerName != null && playerName.compareTo("") != 0){	//  && !noMailingPlayers.contains(p.getSocialId())
						try {
							if(notifications != null){
								if(states != null  && states.size() > 0){
									this.emailService.sendMailGamification(mailStartGame, playerName, states.get(0).getScore() + "", null, null, null, null, // health and pr point are null
											actual_week, actual_week_theme, last_week, are_chall, are_prizes, are_prizes_last_week, someBadge, 
											challenges, lastWeekChallenges, mailPrizeActualData, mailWinnersFileData, standardImages, mailto, mailRedirectUrl, compileSurveyUrl, unsubcribionLink, mailLoc);
								} else {
									this.emailService.sendMailGamification(mailStartGame, playerName, "0", "0", "0", null, null, 
											actual_week, actual_week_theme, last_week, are_chall, are_prizes, are_prizes_last_week, someBadge,
											challenges, lastWeekChallenges, mailPrizeActualData, mailWinnersFileData, standardImages, mailto, mailRedirectUrl, compileSurveyUrl, unsubcribionLink, mailLoc);
								}
							} else {
								if(states != null  && states.size() > 0){
									this.emailService.sendMailGamification(mailStartGame, playerName, states.get(0).getScore() + "", null, null, null, null, // health and pr point are null
											actual_week, actual_week_theme, last_week, are_chall, are_prizes, are_prizes_last_week, null, 
											challenges, lastWeekChallenges, mailPrizeActualData, mailWinnersFileData, standardImages, mailto, mailRedirectUrl, compileSurveyUrl, unsubcribionLink, mailLoc);
								} else {
									this.emailService.sendMailGamification(mailStartGame, playerName, "0", "0", "0", null, null, 
											actual_week, actual_week_theme, last_week, are_chall, are_prizes, are_prizes_last_week, null, 
											challenges, lastWeekChallenges, mailPrizeActualData, mailWinnersFileData, standardImages, mailto, mailRedirectUrl, compileSurveyUrl, unsubcribionLink, mailLoc);
								}
							}
						} catch (MessagingException e) {
							logger.error(String.format("Errore invio mail : %s", e.getMessage()));
						}
					} else {
						if(notifications != null){
							if(states != null && states.size() > 0){
								logger.debug(String.format("Invio mail a %s con notifica : %s e stato: %s", playerName ,notifications.toString(), states.toString()));
							} else {
								logger.debug(String.format("Invio mail a %s con notifica : %s", playerName ,notifications.toString()));
							}
						} else {
							if(states != null && states.size() > 0){
								logger.debug(String.format("Invio mail a %s con stato: %s", playerName , states.toString()));
							} else {
								logger.debug(String.format("Invio mail a %s", playerName));
							}
						}
						if(challenges != null && !challenges.isEmpty()){
							logger.debug(String.format("Invio mail a %s con challenges: %s", playerName , challenges.toString()));
						}
						if(lastWeekChallenges != null && !lastWeekChallenges.isEmpty()){
							logger.debug(String.format("Invio mail a %s con challenges scorsa settimana: %s", playerName , lastWeekChallenges.toString()));
						}
					}
					summaryMail.add(new Summary(p.getName() + " " + p.getSurname() + ": " + p.getNikName(), (states != null && states.get(0) != null) ? Integer.toString(states.get(0).getScore()) : "", (notifications != null) ? notifications.toString() : ""));
				} else {
					logger.info("Mail non inviata a " + p.getNikName()+ ". L'utente ha richiesto la disattivazione delle notifiche.");
				}
			}
			
			// Send summary mail
			if(mailSend.compareTo("true") == 0){
				// Here I send the summary mail (only if the sendMail parameter is true)
				try {
					this.emailService.sendMailSummary("Mattia", "0", "0", "0", summaryMail, standardImages, mailTo, Locale.ITALIAN);
				} catch (MessagingException e) {
					logger.error(String.format("Errore invio mail notifica : %s", e.getMessage()));
				}
			} else {
				logger.info("Ended mail sending process: no mail send (param in conf file set to false)");
			}
		//}
	}
	
	//@Scheduled(fixedRate = 5*60*1000) // Repeat every 5 minutes
	//@Scheduled(cron="0 30 14 * * WED") 		// Repeat every Wednesday at 14:30 AM
	public synchronized void checkWinnersNotification() throws IOException, NoSuchPaddingException, NoSuchAlgorithmException {
		EncryptDecrypt cryptUtils = new EncryptDecrypt(SECRET_KEY_1, SECRET_KEY_2);
		StatusUtils statusUtils = new StatusUtils();
		ArrayList<Summary> summaryMail = new ArrayList<Summary>();
		long millis = System.currentTimeMillis() - (7*24*60*60*1000);	// Delta in millis of N days: now 7 days
		String timestamp = "?timestamp=" + millis;
		//String timestamp = "";
		
		ChallengesUtils challUtils = new ChallengesUtils();
		challUtils.setChallLongDescriptionList(challDescriptionSetup.getDescriptions());
		
		URL resource = getClass().getResource("/");
		String path = resource.getPath();
		logger.debug(String.format("class path : %s", path));
		
		ArrayList<MailImage> standardImages = new ArrayList<MailImage>();
		
		File greenScore = new File(path + "mail/img/green/greenLeavesbase.png");
		File healthScore = new File(path + "mail/img/health/healthLeavesBase.png");
		File prScore = new File(path + "mail/img/pr/prLeaves.png");
		File footer = new File(path + "mail/img/templateMail.png");
		File foglie03 = new File(path + "mail/img/foglie03.png");
		File foglie04 = new File(path + "mail/img/foglie04.png");
		standardImages.add(new MailImage(foglie03.getName(), FileUtils.readFileToByteArray(foglie03), "image/png"));
		standardImages.add(new MailImage(foglie04.getName(), FileUtils.readFileToByteArray(foglie04), "image/png"));
		standardImages.add(new MailImage(greenScore.getName(), FileUtils.readFileToByteArray(greenScore), "image/png"));
		standardImages.add(new MailImage(healthScore.getName(), FileUtils.readFileToByteArray(healthScore), "image/png"));
		standardImages.add(new MailImage(prScore.getName(), FileUtils.readFileToByteArray(prScore), "image/png"));
		standardImages.add(new MailImage(footer.getName(), FileUtils.readFileToByteArray(footer), "image/png"));
		
		logger.debug(String.format("Image data: path - %s length: %d", greenScore.getAbsolutePath(), greenScore.length()));
		
		// New method
		logger.debug(String.format("Check Notification task. Cycle - %d", i++));
		// Here I have to read the mail conf file data
		List<WeekConfData> mailConfigurationFileData = readWeekConfFile(path + "mail/conf_file/game_week_configuration.csv");
		List<WeekPrizeData> mailPrizeFileData = readWeekPrizesFile(path + "mail/conf_file/game_week_prize.csv");
		List<WeekPrizeData> mailPrizeFileDataEng = readWeekPrizesFile(path + "mail/conf_file/game_week_prize_en.csv");
		List<WeekWinnersData> mailWinnersFileData = readWeekWinnersFile(path + "mail/conf_file/game_week_winners.csv");
		List<WeekPrizeData> mailPrizeActualData = new ArrayList<WeekPrizeData>();
		// here I have to add the new mail parameters readed from csv files
		String actual_week = "";
		String actual_week_theme = "";
		String actual_week_theme_it = "";
		String actual_week_theme_eng = "";
		String last_week = "";
		Boolean are_chall = false;
		Boolean are_prizes = false;
		Boolean are_prizes_last_week = false;
		for(int i = 0; i < mailConfigurationFileData.size(); i++){
			WeekConfData tmpWConf = mailConfigurationFileData.get(i);
			if(tmpWConf.isActual()){
				actual_week = tmpWConf.getWeekNum();
				actual_week_theme_it = tmpWConf.getWeekTheme();
				actual_week_theme_eng = tmpWConf.getWeekThemeEng();
				last_week = Integer.toString(Integer.parseInt(actual_week) - 1);
				are_chall = tmpWConf.isChallenges();
				are_prizes = tmpWConf.isPrizes();
				are_prizes_last_week = tmpWConf.isPrizesLast();
				mailPrizeActualData = readWeekPrizesFileData(actual_week, mailPrizeFileData);
			}
		}
		String type = (isTest.compareTo("true") == 0) ? "test" : "prod";
		Iterable<Player> iter = playerRepositoryDao.findAllByType(type);
			// Add user to exclude from the mailing list
			//List<String> noMailingPlayers = new ArrayList<String>();
			//noMailingPlayers.add("10730");	//"FILIPPO"	
			//noMailingPlayers.add("23755");	//"Fede"
			
		for(Player p: iter){
			logger.debug(String.format("Profile finded  %s", p.getNikName()));
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e1) {
				logger.error(String.format("Errore in attesa thread: %s", e1.getMessage()));
			}
			
			if(p.isSendMail()){
				String surveyLanguage = "?language=" + ((p.getLanguage() != null) ? p.getLanguage() : "it");
				String compileSurveyUrl = mailSurveyUrl + "/" + p.getSocialId() + surveyLanguage;
				String encriptedId = "";
				try {
					encriptedId = cryptUtils.encrypt(p.getSocialId());
				} catch (InvalidKeyException e1) {
					logger.error("Errore in socialId encripting: " + e1.getMessage());
				} catch (InvalidAlgorithmParameterException e2) {
					logger.error("Errore in socialId encripting: " + e2.getMessage());
				} catch (BadPaddingException e3) {
					logger.error("Errore in socialId encripting: " + e3.getMessage());
				} catch (IllegalBlockSizeException e4) {
					logger.error("Errore in socialId encripting: " + e4.getMessage());
				}
				String unsubcribionLink = mainURL + "/out/rest/unsubscribeMail/?socialId=" + encriptedId;	// + p.getSocialId();
				//ArrayList<State> states = null;
				List<PointConcept> states = null;
				ArrayList<Notification> notifications = null;
				ArrayList<BagesData> someBadge = null;
				List<ChallengesData> challenges = null;
				List<ChallengesData> lastWeekChallenges = null;
				Locale mailLoc = Locale.ITALIAN;
			
				try {
					// WS State Invocation
					String urlWSState = "state/" + gameName + "/" + p.getSocialId();
					//states = getState(urlWSState);
					// Challenges correction
					String completeState = getAllChallenges(urlWSState);
					String language = p.getLanguage();
					if(language == null || language.compareTo("") == 0){
						language = ITA_LANG;
					}
					if(language.compareTo(ENG_LANG) == 0){
						actual_week_theme = actual_week_theme_eng;
						mailLoc = Locale.ENGLISH;
						mailPrizeActualData = readWeekPrizesFileData(actual_week, mailPrizeFileDataEng);
					} else {
						actual_week_theme = actual_week_theme_it;
						mailLoc = Locale.ITALIAN;
						mailPrizeActualData = readWeekPrizesFileData(actual_week, mailPrizeFileData);
					}
					try {
						PlayerStatus completePlayerStatus = statusUtils.correctPlayerData(completeState, p.getSocialId(), gameName, p.getNikName(), challUtils, gamificationWebUrl, 0, language);
						states = completePlayerStatus.getPointConcept();
						ChallengeConcept challLists = completePlayerStatus.getChallengeConcept();
						//@SuppressWarnings("rawtypes")
						//List<List> challLists = challUtils.correctCustomData(completeState, 0);
						if(challLists != null){
							challenges = challLists.getActiveChallengeData();
							lastWeekChallenges = challLists.getOldChallengeData();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
					// WS Notification Invocation
					String urlWSNot = "notification/" + gameName + "/" + p.getSocialId();	
					notifications = getNotifications(urlWSNot, timestamp);
				} catch (InterruptedException ie){
					logger.error(String.format("Ws invoke sleep exception  %s", ie.getMessage()));
				}
				
				if(notifications != null && notifications.size() > 0){
					ArrayList<BagesData> allBadge = getAllBadges(path);
					someBadge = checkCorrectBadges(allBadge, notifications);	
				}
				
				String mailto = null;
				mailto = p.getMail();
				String playerName = p.getNikName();
				if(mailto == null || mailto.compareTo("") == 0){
					mailto = mailTo;
				}
				
				if(mailSend.compareTo("true") == 0 && playerName!= null && playerName.compareTo("")!=0){ //&& !noMailingPlayers.contains(p.getSocialId())
					try {
						if(notifications != null){
							if(states != null  && states.size() > 0){
								this.emailService.sendMailGamificationForWinners(playerName, states.get(0).getScore() + "", null, null, null, null, // health and pr point are null
										actual_week, actual_week_theme, last_week, are_chall, are_prizes, are_prizes_last_week, someBadge, 
										challenges, lastWeekChallenges, mailPrizeActualData, mailWinnersFileData, standardImages, mailto, mailRedirectUrl, compileSurveyUrl, unsubcribionLink, mailLoc);
							} else {
								this.emailService.sendMailGamificationForWinners(playerName, "0", "0", "0", null, null, 
										actual_week, actual_week_theme, last_week, are_chall, are_prizes, are_prizes_last_week, someBadge,
										challenges, lastWeekChallenges, mailPrizeActualData, mailWinnersFileData, standardImages, mailto, mailRedirectUrl, compileSurveyUrl, unsubcribionLink, mailLoc);
							}
						} else {
							if(states != null  && states.size() > 0){
								this.emailService.sendMailGamificationForWinners(playerName, states.get(0).getScore() + "", null, null, null, null, // health and pr point are null
										actual_week, actual_week_theme, last_week, are_chall, are_prizes, are_prizes_last_week, null, 
										challenges, lastWeekChallenges, mailPrizeActualData, mailWinnersFileData, standardImages, mailto, mailRedirectUrl, compileSurveyUrl, unsubcribionLink, mailLoc);
							} else {
								this.emailService.sendMailGamificationForWinners(playerName, "0", "0", "0", null, null, 
										actual_week, actual_week_theme, last_week, are_chall, are_prizes, are_prizes_last_week, null, 
										challenges, lastWeekChallenges, mailPrizeActualData, mailWinnersFileData, standardImages, mailto, mailRedirectUrl, compileSurveyUrl, unsubcribionLink, mailLoc);
							}
						}
					} catch (MessagingException e) {
						logger.error(String.format("Errore invio mail : %s", e.getMessage()));
					}
				} else {
					if(notifications != null){
						if(states != null && states.size() > 0){
							logger.debug(String.format("Invio mail a %s con notifica : %s e stato: %s", playerName ,notifications.toString(), states.toString()));
						} else {
							logger.debug(String.format("Invio mail a %s con notifica : %s", playerName ,notifications.toString()));
						}
					} else {
						if(states != null && states.size() > 0){
							logger.debug(String.format("Invio mail a %s con stato: %s", playerName , states.toString()));
						} else {
							logger.debug(String.format("Invio mail a %s", playerName));
						}
					}
					if(challenges != null && !challenges.isEmpty()){
						logger.debug(String.format("Invio mail a %s con challenges: %s", playerName , challenges.toString()));
					}
					if(lastWeekChallenges != null && !lastWeekChallenges.isEmpty()){
						logger.debug(String.format("Invio mail a %s con challenges scorsa settimana: %s", playerName , lastWeekChallenges.toString()));
					}
				}
				summaryMail.add(new Summary(p.getName() + " " + p.getSurname() + ": " + p.getNikName(), (states != null) ? states.toString() : "", (notifications != null) ? notifications.toString() : ""));
			} else {
				logger.info("Mail non inviata a " + p.getNikName()+ ". L'utente ha richiesto la disattivazione delle notifiche.");	
			}
		}
		// Send summary mail
		if(mailSend.compareTo("true") == 0){
			// Here I send the summary mail (only if the sendMail parameter is true)
			try {
				this.emailService.sendMailSummary("Mattia", "0", "0", "0", summaryMail, standardImages, mailTo, Locale.ITALIAN);
			} catch (MessagingException e) {
				logger.error(String.format("Errore invio mail notifica : %s", e.getMessage()));
			}
		}
	}
	
	//@Scheduled(fixedRate = 5*60*1000) // Repeat every 5 minutes
	//@Scheduled(cron="0 30 11 * * WED") 		// Repeat every Wednesday at 11:30 AM
	public synchronized void sendReportMail() throws IOException, NoSuchPaddingException, NoSuchAlgorithmException {
		EncryptDecrypt cryptUtils = new EncryptDecrypt(SECRET_KEY_1, SECRET_KEY_2);
		StatusUtils statusUtils = new StatusUtils();
		ArrayList<Summary> summaryMail = new ArrayList<Summary>();
		long millis = System.currentTimeMillis() - (7*24*60*60*1000);	// Delta in millis of N days: now 7 days
		String timestamp = "?timestamp=" + millis;
		
		ChallengesUtils challUtils = new ChallengesUtils();
		challUtils.setChallLongDescriptionList(challDescriptionSetup.getDescriptions());
		
		URL resource = getClass().getResource("/");
		String path = resource.getPath();
		logger.debug(String.format("class path : %s", path));
		
		ArrayList<MailImage> standardImages = new ArrayList<MailImage>();
		
		File greenScore = new File(path + "mail/img/green/greenLeavesbase.png");
		File healthScore = new File(path + "mail/img/health/healthLeavesBase.png");
		File prScore = new File(path + "mail/img/pr/prLeaves.png");
		File footer = new File(path + "mail/img/templateMail.png");
		File foglie03 = new File(path + "mail/img/foglie03.png");
		File foglie04 = new File(path + "mail/img/foglie04.png");
		standardImages.add(new MailImage(foglie03.getName(), FileUtils.readFileToByteArray(foglie03), "image/png"));
		standardImages.add(new MailImage(foglie04.getName(), FileUtils.readFileToByteArray(foglie04), "image/png"));
		standardImages.add(new MailImage(greenScore.getName(), FileUtils.readFileToByteArray(greenScore), "image/png"));
		standardImages.add(new MailImage(healthScore.getName(), FileUtils.readFileToByteArray(healthScore), "image/png"));
		standardImages.add(new MailImage(prScore.getName(), FileUtils.readFileToByteArray(prScore), "image/png"));
		standardImages.add(new MailImage(footer.getName(), FileUtils.readFileToByteArray(footer), "image/png"));
		
		logger.debug(String.format("Image data: path - %s length: %d", greenScore.getAbsolutePath(), greenScore.length()));
		
		// New method
		String type = (isTest.compareTo("true") == 0) ? "test" : "prod";
		Iterable<Player> iter = playerRepositoryDao.findAllByType(type);
			
		for(Player p: iter){
			logger.debug(String.format("Profile finded  %s", p.getNikName()));
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e1) {
				logger.error(String.format("Errore in attesa thread: %s", e1.getMessage()));
			}
			
			if(p.isSendMail()){
				String moduleName = "mail/pdf/finalModule_" + p.getSocialId() + ".pdf";
				try {
					File finalModule = new File(path + moduleName);
					String surveyLanguage = "?language=" + ((p.getLanguage() != null) ? p.getLanguage() : "it");
					String compileSurveyUrl = mailSurveyUrl + "/" + p.getSocialId() + surveyLanguage;
					String compileSurveyUrlShort = mailSurveyUrl + "/" + p.getSocialId();
					String encriptedId = "";
					try {
						encriptedId = cryptUtils.encrypt(p.getSocialId());
					} catch (InvalidKeyException e1) {
						logger.error("Errore in socialId encripting: " + e1.getMessage());
					} catch (InvalidAlgorithmParameterException e2) {
						logger.error("Errore in socialId encripting: " + e2.getMessage());
					} catch (BadPaddingException e3) {
						logger.error("Errore in socialId encripting: " + e3.getMessage());
					} catch (IllegalBlockSizeException e4) {
						logger.error("Errore in socialId encripting: " + e4.getMessage());
					}
					String unsubcribionLink = mainURL + "/out/rest/unsubscribeMail/?socialId=" + encriptedId;	// + p.getSocialId();
					//ArrayList<State> states = null;
					List<PointConcept> states = null;
					ArrayList<Notification> notifications = null;
					List<ChallengesData> challenges = null;
					List<ChallengesData> lastWeekChallenges = null;
					Locale mailLoc = Locale.ITALIAN;
				
					try {
						// WS State Invocation
						String urlWSState = "state/" + gameName + "/" + p.getSocialId();
						// Challenges correction
						String completeState = getAllChallenges(urlWSState);
						String language = p.getLanguage();
						if(language == null || language.compareTo("") == 0){
							language = ITA_LANG;
						}
						if(language.compareTo(ENG_LANG) == 0){
							mailLoc = Locale.ENGLISH;
						} else {
							mailLoc = Locale.ITALIAN;
						}
						try {
							PlayerStatus completePlayerStatus = statusUtils.correctPlayerData(completeState, p.getSocialId(), gameName, p.getNikName(), challUtils, gamificationWebUrl, 0, language);
							states = completePlayerStatus.getPointConcept();
							ChallengeConcept challLists = completePlayerStatus.getChallengeConcept();
							if(challLists != null){
								challenges = challLists.getActiveChallengeData();
								lastWeekChallenges = challLists.getOldChallengeData();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						
						// WS Notification Invocation
						String urlWSNot = "notification/" + gameName + "/" + p.getSocialId();	
						notifications = getNotifications(urlWSNot, timestamp);
					} catch (InterruptedException ie){
						logger.error(String.format("Ws invoke sleep exception  %s", ie.getMessage()));
					}
					
					String mailto = null;
					mailto = p.getMail();
					String playerName = p.getNikName();
					if(mailto == null || mailto.compareTo("") == 0){
						mailto = mailTo;
					}
					Boolean surveyCompiled = (p.getSurveyData() != null) ? true : false;
					
					if(mailSend.compareTo("true") == 0 && playerName!= null && playerName.compareTo("")!=0){ //&& !noMailingPlayers.contains(p.getSocialId())
						try {
							if(states != null  && states.size() > 0){
								this.emailService.sendMailGamificationWithReport(playerName, states.get(0).getScore() + "", null, null, null, null, // health and pr point are null
										null, null, null, null, null, null, surveyCompiled, finalModule, 
										challenges, lastWeekChallenges, null, null, standardImages, mailto, mailRedirectUrl, compileSurveyUrl, compileSurveyUrlShort, unsubcribionLink, mailLoc);
							} else {
								this.emailService.sendMailGamificationWithReport(playerName, "0", "0", "0", null, null, 
										null, null, null, null, null, null, surveyCompiled, finalModule,
										challenges, lastWeekChallenges, null, null, standardImages, mailto, mailRedirectUrl, compileSurveyUrl, compileSurveyUrlShort, unsubcribionLink, mailLoc);
							}
						} catch (MessagingException e) {
							logger.error(String.format("Errore invio mail : %s", e.getMessage()));
						}
					}
					summaryMail.add(new Summary(p.getName() + " " + p.getSurname() + ": " + p.getNikName(), (states != null) ? states.toString() : "", (notifications != null) ? notifications.toString() : ""));
				} catch (Exception ex) {
					logger.info("Mail non inviata a " + p.getNikName()+ ". Non esiste il pdf del modulo finale.");	
				}
			} else {
				logger.info("Mail non inviata a " + p.getNikName()+ ". L'utente ha richiesto la disattivazione delle notifiche.");	
			}
		}
		// Send summary mail
		if(mailSend.compareTo("true") == 0){
			// Here I send the summary mail (only if the sendMail parameter is true)
			try {
				this.emailService.sendMailSummary("Mattia", "0", "0", "0", summaryMail, standardImages, mailTo, Locale.ITALIAN);
			} catch (MessagingException e) {
				logger.error(String.format("Errore invio mail notifica : %s", e.getMessage()));
			}
		}
	}
	
	private ArrayList<BagesData> getAllBadges(String path) throws IOException {
		ArrayList<BagesData> allBadges = new ArrayList<BagesData>();
		// files for green badges
		File greenKing = new File(path + "mail/img/green/greenKingWeek.png");
		File green50 = new File(path + "mail/img/green/greenLeaves50.png");
		File green100 = new File(path + "mail/img/green/greenLeaves100.png");
		File green200 = new File(path + "mail/img/green/greenLeaves200.png");
		File green400 = new File(path + "mail/img/green/greenLeaves400.png");
		File green800 = new File(path + "mail/img/green/greenLeaves800.png");
		File green1500 = new File(path + "mail/img/green/greenLeaves1500.png");
		File green2500 = new File(path + "mail/img/green/greenLeaves2500.png");
		File green5000 = new File(path + "mail/img/green/greenLeaves5000.png");
		File green10000 = new File(path + "mail/img/green/greenLeaves10000.png");
		File green20000 = new File(path + "mail/img/green/greenLeaves20000.png");
		File greenBronze = new File(path + "mail/img/leaderboard/green/leaderboardGreen3.png");
		File greenSilver = new File(path + "mail/img/leaderboard/green/leaderboardGreen2.png");
		File greenGold = new File(path + "mail/img/leaderboard/green/leaderboardGreen1.png");
		
		allBadges.add(new BagesData(greenKing.getName(), FileUtils.readFileToByteArray(greenKing), "image/png", "king_week_green", "Re della Settimana - Green Leaves", "King of the Week - Green Leaves"));
		allBadges.add(new BagesData(green50.getName(), FileUtils.readFileToByteArray(green50), "image/png", "50_point_green", "50 Punti Green Leaves", "50 Green Leaves Points"));
		allBadges.add(new BagesData(green100.getName(), FileUtils.readFileToByteArray(green100), "image/png", "100_point_green", "100 Punti Green Leaves", "100 Green Leaves Points"));
		allBadges.add(new BagesData(green200.getName(), FileUtils.readFileToByteArray(green200), "image/png", "200_point_green", "200 Punti Green Leaves", "200 Green Leaves Points"));
		allBadges.add(new BagesData(green400.getName(), FileUtils.readFileToByteArray(green400), "image/png", "400_point_green", "400 Punti Green Leaves", "400 Green Leaves Points"));
		allBadges.add(new BagesData(green800.getName(), FileUtils.readFileToByteArray(green800), "image/png", "800_point_green", "800 Punti Green Leaves", "800 Green Leaves Points"));
		allBadges.add(new BagesData(green1500.getName(), FileUtils.readFileToByteArray(green1500), "image/png", "1500_point_green", "1500 Punti Green Leaves", "1500 Green Leaves Points"));
		allBadges.add(new BagesData(green2500.getName(), FileUtils.readFileToByteArray(green2500), "image/png", "2500_point_green", "2500 Punti Green Leaves", "2500 Green Leaves Points"));
		allBadges.add(new BagesData(green5000.getName(), FileUtils.readFileToByteArray(green5000), "image/png", "5000_point_green", "5000 Punti Green Leaves", "5000 Green Leaves Points"));
		allBadges.add(new BagesData(green10000.getName(), FileUtils.readFileToByteArray(green10000), "image/png", "10000_point_green", "10000 Punti Green Leaves", "10000 Green Leaves Points"));
		allBadges.add(new BagesData(green20000.getName(), FileUtils.readFileToByteArray(green20000), "image/png", "20000_point_green", "20000 Punti Green Leaves", "20000 Green Leaves Points"));
		allBadges.add(new BagesData(greenBronze.getName(), FileUtils.readFileToByteArray(greenBronze), "image/png", "bronze-medal-green", "Medaglia di Bronzo - Green Leaves", "Bronze Medal - Green Leaves"));
		allBadges.add(new BagesData(greenSilver.getName(), FileUtils.readFileToByteArray(greenSilver), "image/png", "silver-medal-green", "Medaglia d'Argento - Green Leaves", "Silver Medal - Green Leaves"));
		allBadges.add(new BagesData(greenGold.getName(), FileUtils.readFileToByteArray(greenGold), "image/png", "gold-medal-green", "Medaglia d'Oro - Green Leaves", "Gold Medal - Green Leaves"));
				
		// files for health badges
		File healthKing = new File(path + "mail/img/health/healthKingWeek.png");
		File health10 = new File(path + "mail/img/health/healthLeaves10.png");
		File health25 = new File(path + "mail/img/health/healthLeaves25.png");
		File health50 = new File(path + "mail/img/health/healthLeaves50.png");
		File health100 = new File(path + "mail/img/health/healthLeaves100.png");
		File health200 = new File(path + "mail/img/health/healthLeaves200.png");
		File healthBronze = new File(path + "mail/img/health/healthBronzeMedal.png");
		File healthSilver = new File(path + "mail/img/health/healthSilverMedal.png");
		File healthGold = new File(path + "mail/img/health/healthGoldMedal.png");
		
		allBadges.add(new BagesData(healthKing.getName(), FileUtils.readFileToByteArray(healthKing), "image/png", "king_week_health", "Re della Settimana - Salute", "King of the Week - Health"));
		allBadges.add(new BagesData(health10.getName(), FileUtils.readFileToByteArray(health10), "image/png", "10_point_health", "10 Punti Salute", "10 Health Points"));
		allBadges.add(new BagesData(health25.getName(), FileUtils.readFileToByteArray(health25), "image/png", "25_point_health", "25 Punti Salute", "25 Health Points"));
		allBadges.add(new BagesData(health50.getName(), FileUtils.readFileToByteArray(health50), "image/png", "50_point_health", "50 Punti Salute", "50 Health Points"));
		allBadges.add(new BagesData(health100.getName(), FileUtils.readFileToByteArray(health100), "image/png", "100_point_health", "100 Punti Salute", "100 Health Points"));
		allBadges.add(new BagesData(health200.getName(), FileUtils.readFileToByteArray(health200), "image/png", "200_point_health", "200 Punti Salute", "200 Health Points"));
		allBadges.add(new BagesData(healthBronze.getName(), FileUtils.readFileToByteArray(healthBronze), "image/png", "bronze_medal_health", "Medaglia di Bronzo - Salute", "Bronze Medal - Health"));
		allBadges.add(new BagesData(healthSilver.getName(), FileUtils.readFileToByteArray(healthSilver), "image/png", "silver_medal_health", "Medaglia d'Argento - Salute", "Silver Medal - Health"));
		allBadges.add(new BagesData(healthGold.getName(), FileUtils.readFileToByteArray(healthGold), "image/png", "gold_medal_health", "Medaglia d'Oro - Salute", "Gold Medal - Health"));
		
		// files for pr badges
		File prKing = new File(path + "mail/img/pr/prKingWeek.png");
		File pr10 = new File(path + "mail/img/pr/prLeaves10.png");
		File pr20 = new File(path + "mail/img/pr/prLeaves20.png");
		File pr50 = new File(path + "mail/img/pr/prLeaves50.png");
		File pr100 = new File(path + "mail/img/pr/prLeaves100.png");
		File pr200 = new File(path + "mail/img/pr/prLeaves200.png");
		File prBronze = new File(path + "mail/img/pr/prBronzeMedal.png");
		File prSilver = new File(path + "mail/img/pr/prSilverMedal.png");
		File prGold = new File(path + "mail/img/pr/prGoldMedal.png");
		File prManifattura = new File(path + "mail/img/pr/prPioneerManifattura.png");
		File prStadio = new File(path + "mail/img/pr/prPioneerStadio.png");
		File prRagazzi99 = new File(path + "mail/img/pr/prPioneerRagazzi99.png");
		File prLidorno = new File(path + "mail/img/pr/prPioneerLidorno.png");
		File prViaFersina = new File(path + "mail/img/pr/prPioneerViaFersina.png");
		File prAreaZuffo = new File(path + "mail/img/pr/prPioneerAreaZuffo.png");
		File prMonteBaldo = new File(path + "mail/img/pr/prPioneerMonteBaldo.png");
		File prVillazzanoFS = new File(path + "mail/img/pr/prPioneerVillazzanoStazioneFS.png");
		
		allBadges.add(new BagesData(prKing.getName(), FileUtils.readFileToByteArray(prKing), "image/png", "king_week_pr", "Re della Settimana - Park&Ride", "King of the Week - Park&Ride"));
		allBadges.add(new BagesData(pr10.getName(), FileUtils.readFileToByteArray(pr10), "image/png", "10_point_pr", "10 Punti Park&Ride", "10 Park&Ride Points"));
		allBadges.add(new BagesData(pr20.getName(), FileUtils.readFileToByteArray(pr20), "image/png", "20_point_pr", "20 Punti Park&Ride", "20 Park&Ride Points"));
		allBadges.add(new BagesData(pr50.getName(), FileUtils.readFileToByteArray(pr50), "image/png", "50_point_pr", "50 Punti Park&Ride", "50 Park&Ride Points"));
		allBadges.add(new BagesData(pr100.getName(), FileUtils.readFileToByteArray(pr100), "image/png", "100_point_pr", "100 Punti Park&Ride", "100 Park&Ride Points"));
		allBadges.add(new BagesData(pr200.getName(), FileUtils.readFileToByteArray(pr200), "image/png", "200_point_pr", "200 Punti Park&Ride", "200 Park&Ride Points"));
		allBadges.add(new BagesData(prBronze.getName(), FileUtils.readFileToByteArray(prBronze), "image/png", "bronze_medal_pr", "Medaglia di Bronzo - Park&Ride", "Bronze Medal - Park&Ride"));
		allBadges.add(new BagesData(prSilver.getName(), FileUtils.readFileToByteArray(prSilver), "image/png", "silver_medal_pr", "Medaglia d'Argento - Park&Ride", "Silver Medal - Park&Ride"));
		allBadges.add(new BagesData(prGold.getName(), FileUtils.readFileToByteArray(prGold), "image/png", "gold_medal_pr", "Medaglia d'Oro - Park&Ride", "Gold Medal - Park&Ride"));
		allBadges.add(new BagesData(prManifattura.getName(), FileUtils.readFileToByteArray(prManifattura), "image/png", "Manifattura_parking", "Parcheggio Manifattura - Park&Ride", "Manifattura Park - Park&Ride"));
		allBadges.add(new BagesData(prStadio.getName(), FileUtils.readFileToByteArray(prStadio), "image/png", "Stadio_parking", "Parcheggio Stadio - Park&Ride", "Stadio Park - Park&Ride"));
		allBadges.add(new BagesData(prRagazzi99.getName(), FileUtils.readFileToByteArray(prRagazzi99), "image/png", "Via Ragazzi del '99_parking", "Via ragazzi del 99 - Park&Ride", "Via ragazzi del 99 Park - Park&Ride"));
		allBadges.add(new BagesData(prLidorno.getName(), FileUtils.readFileToByteArray(prLidorno), "image/png", "Via Lidorno_parking", "Via Lidorno - Park&Ride", "Via Lidorno Park - Park&Ride"));
		allBadges.add(new BagesData(prViaFersina.getName(), FileUtils.readFileToByteArray(prViaFersina), "image/png", "Ghiaie via Fersina_parking", "Ghiaie via Fersina - Park&Ride", "Ghiaie Park - Park&Ride"));
		allBadges.add(new BagesData(prAreaZuffo.getName(), FileUtils.readFileToByteArray(prAreaZuffo), "image/png", "Ex-Zuffo_parking", "Ex Zuffo - Park&Ride", "Ex Zuffo Park - Park&Ride"));
		allBadges.add(new BagesData(prMonteBaldo.getName(), FileUtils.readFileToByteArray(prMonteBaldo), "image/png", "Monte Baldo_parking", "Monte Baldo - Park&Ride", "Monte Baldo Park - Park&Ride"));
		allBadges.add(new BagesData(prVillazzanoFS.getName(), FileUtils.readFileToByteArray(prVillazzanoFS), "image/png", "Via Asiago, Stazione FS Villazzano_parking", "Stazione FS Villazzano - Park&Ride", "Villazzano FS Station - Park&Ride"));
		
		// files for special badges
		File specialEmotion = new File(path + "mail/img/special/emotion.png");
		File specialZeroImpact = new File(path + "mail/img/special/impatto_zero.png");
		File specialStadioPark = new File(path + "mail/img/special/special_p_quercia.png");
		File specialManifattura = new File(path + "mail/img/special/special_special_p_manifattura.png");
		File specialCentroStorico = new File(path + "mail/img/special/special_p_centro.storico.png");
		File specialParcheggioCentro = new File(path + "mail/img/special/special_p_centro.png");
		File specialPleALeoni = new File(path + "mail/img/special/special_p_p.le.a.leoni.png");
		
		allBadges.add(new BagesData(specialEmotion.getName(), FileUtils.readFileToByteArray(specialEmotion), "image/png", "e-motion", "E-Motion", "E-Motion"));
		allBadges.add(new BagesData(specialZeroImpact.getName(), FileUtils.readFileToByteArray(specialZeroImpact), "image/png", "zero-impact", "Impatto Zero", "Zero Impact"));
		allBadges.add(new BagesData(specialStadioPark.getName(), FileUtils.readFileToByteArray(specialStadioPark), "image/png", "Stadio-park", "Parcheggio Stadio Quercia", "Stadio Quercia Park"));
		allBadges.add(new BagesData(specialManifattura.getName(), FileUtils.readFileToByteArray(specialManifattura), "image/png", "Ex Manifattura-park", "Parcheggio Ex Manifattura", "Ex Manifattura Park"));
		allBadges.add(new BagesData(specialCentroStorico.getName(), FileUtils.readFileToByteArray(specialCentroStorico), "image/png", "Centro Storico-park", "Parcheggio Centro Storico", "Centro Storico Park"));
		allBadges.add(new BagesData(specialParcheggioCentro.getName(), FileUtils.readFileToByteArray(specialParcheggioCentro), "image/png", "Parcheggio Centro-park", "Parcheggio Centro", "Centro Park"));
		allBadges.add(new BagesData(specialPleALeoni.getName(), FileUtils.readFileToByteArray(specialPleALeoni), "image/png", "P.le A.Leoni-park", "Parcheggio Piazzale Leoni", "Piazzale Leoni Park"));
		
		// files for bike
		File bike1 = new File(path + "mail/img/bike/bikeAficionado1.png");
		File bike5 = new File(path + "mail/img/bike/bikeAficionado5.png");
		File bike10 = new File(path + "mail/img/bike/bikeAficionado10.png");
		File bike25 = new File(path + "mail/img/bike/bikeAficionado25.png");
		File bike50 = new File(path + "mail/img/bike/bikeAficionado50.png");
		File bike100 = new File(path + "mail/img/bike/bikeAficionado100.png");
		File bike200 = new File(path + "mail/img/bike/bikeAficionado200.png");
		File bike500 = new File(path + "mail/img/bike/bikeAficionado500.png");
		
		allBadges.add(new BagesData(bike1.getName(), FileUtils.readFileToByteArray(bike1), "image/png", "1_bike_trip", "1 Viaggio in Bici", "1 Bike Trip"));
		allBadges.add(new BagesData(bike5.getName(), FileUtils.readFileToByteArray(bike5), "image/png", "5_bike_trip", "5 Viaggi in Bici", "5 Bike Trips"));
		allBadges.add(new BagesData(bike10.getName(), FileUtils.readFileToByteArray(bike10), "image/png", "10_bike_trip", "10 Viaggi in Bici", "10 Bike Trips"));
		allBadges.add(new BagesData(bike25.getName(), FileUtils.readFileToByteArray(bike25), "image/png", "25_bike_trip", "25 Viaggi in Bici", "25 Bike Trips"));
		allBadges.add(new BagesData(bike50.getName(), FileUtils.readFileToByteArray(bike50), "image/png", "50_bike_trip", "50 Viaggi in Bici", "50 Bike Trips"));
		allBadges.add(new BagesData(bike100.getName(), FileUtils.readFileToByteArray(bike100), "image/png", "100_bike_trip", "100 Viaggi in Bici", "100 Bike Trips"));
		allBadges.add(new BagesData(bike200.getName(), FileUtils.readFileToByteArray(bike200), "image/png", "200_bike_trip", "200 Viaggi in Bici", "200 Bike Trips"));
		allBadges.add(new BagesData(bike500.getName(), FileUtils.readFileToByteArray(bike500), "image/png", "500_bike_trip", "500 Viaggi in Bici", "500 Bike Trips"));
		
		// files for bike sharing
		File bikeShareBrione = new File(path + "mail/img/bike_sharing/bikeSharingPioneerBrione.png");
		File bikeShareLizzana = new File(path + "mail/img/bike_sharing/bikeSharingPioneerLizzana.png");
		File bikeShareMarco = new File(path + "mail/img/bike_sharing/bikeSharingPioneerMarco.png");
		File bikeShareMunicipio = new File(path + "mail/img/bike_sharing/bikeSharingPioneerMunicipio.png");
		File bikeShareNoriglio = new File(path + "mail/img/bike_sharing/bikeSharingPioneerNoriglio.png");
		File bikeShareOrsi = new File(path + "mail/img/bike_sharing/bikeSharingPioneerOrsi.png");
		File bikeShareOspedale = new File(path + "mail/img/bike_sharing/bikeSharingPioneerOspedale.png");
		File bikeSharePaoli = new File(path + "mail/img/bike_sharing/bikeSharingPioneerPaoli.png");
		File bikeSharePROsmini = new File(path + "mail/img/bike_sharing/bikeSharingPioneerPRosmini.png");
		File bikeShareQuercia = new File(path + "mail/img/bike_sharing/bikeSharingPioneerQuercia.png");
		File bikeShareSacco = new File(path + "mail/img/bike_sharing/bikeSharingPioneerSacco.png");
		File bikeShareStazione = new File(path + "mail/img/bike_sharing/bikeSharingPioneerStazione.png");
		File bikeShareZonaIndustriale = new File(path + "mail/img/bike_sharing/bikeSharingPioneerZonaIndustriale.png");
		File bikeShareMart = new File(path + "mail/img/bike_sharing/bikeSharingPioneerMART.png");
		// Real TN bike station url
		File bikeShareFFSSOspedale = new File(path + "mail/img/bike_sharing/bikeSharingPioneerFFSSOspedale.png");
		File bikeSharePiazzaVenezia = new File(path + "mail/img/bike_sharing/bikeSharingPioneerPiazzaVenezia.png");
		File bikeSharePiscina = new File(path + "mail/img/bike_sharing/bikeSharingPioneerPiscina.png");
		File bikeSharePiazzaMostra = new File(path + "mail/img/bike_sharing/bikeSharingPioneerPiazzaMostra.png");
		File bikeShareCentroSantaChiara = new File(path + "mail/img/bike_sharing/bikeSharingPioneerCentroSantaChiara.png");
		File bikeSharePiazzaCenta = new File(path + "mail/img/bike_sharing/bikeSharingPioneerPiazzaCenta.png");
		File bikeShareBiblioteca = new File(path + "mail/img/bike_sharing/bikeSharingPioneerBiblioteca.png");
		File bikeShareStazioneAutocorriere = new File(path + "mail/img/bike_sharing/bikeSharingPioneerStazioneAutocorriere.png");
		File bikeShareUniversita = new File(path + "mail/img/bike_sharing/bikeSharingPioneerUniversita.png");
		File bikeShareBezzi = new File(path + "mail/img/bike_sharing/bikeSharingPioneerBezzi.png");
		File bikeShareMuse = new File(path + "mail/img/bike_sharing/bikeSharingPioneerMuse.png");
		File bikeShareAziendaSanitaria = new File(path + "mail/img/bike_sharing/bikeSharingPioneerAziendaSanitaria.png");
		File bikeShareTopCenter = new File(path + "mail/img/bike_sharing/bikeSharingPioneerTopCenter.png");
		File bikeShareBrenCenter = new File(path + "mail/img/bike_sharing/bikeSharingPioneerBrenCenter.png");
		File bikeShareLidorno = new File(path + "mail/img/bike_sharing/bikeSharingPioneerLidorno.png");
		File bikeShareGardolo = new File(path + "mail/img/bike_sharing/bikeSharingPioneerGardolo.png");
		File bikeShareAeroporto = new File(path + "mail/img/bike_sharing/bikeSharingPioneerAeroporto.png");
				
		allBadges.add(new BagesData(bikeShareBrione.getName(), FileUtils.readFileToByteArray(bikeShareBrione), "image/png", "Brione - Rovereto_BSstation", "Parcheggio Bike Sharing Brione", "Brione Bike Sharing Park"));
		allBadges.add(new BagesData(bikeShareLizzana.getName(), FileUtils.readFileToByteArray(bikeShareLizzana), "image/png", "Lizzana - Rovereto_BSstation", "Parcheggio Bike Sharing Lizzana", "Lizzana Bike Sharing Park"));
		allBadges.add(new BagesData(bikeShareMarco.getName(), FileUtils.readFileToByteArray(bikeShareMarco), "image/png", "Marco - Rovereto_BSstation", "Parcheggio Bike Sharing Marco", "Marco Bike Sharing Park"));
		allBadges.add(new BagesData(bikeShareMunicipio.getName(), FileUtils.readFileToByteArray(bikeShareMunicipio), "image/png", "Municipio - Rovereto_BSstation", "Parcheggio Bike Sharing Municipio", "Municipio Bike Sharing Park"));
		allBadges.add(new BagesData(bikeShareNoriglio.getName(), FileUtils.readFileToByteArray(bikeShareNoriglio), "image/png", "Noriglio - Rovereto_BSstation", "Parcheggio Bike Sharing Noriglio", "Noriglio Bike Sharing Park"));
		allBadges.add(new BagesData(bikeShareOrsi.getName(), FileUtils.readFileToByteArray(bikeShareOrsi), "image/png", "Orsi - Rovereto_BSstation", "Parcheggio Bike Sharing Piazzale Orsi", "Piazzale Orsi Bike Sharing Park"));
		allBadges.add(new BagesData(bikeShareOspedale.getName(), FileUtils.readFileToByteArray(bikeShareOspedale), "image/png", "Ospedale - Rovereto_BSstation", "Parcheggio Bike Sharing Ospedale", "Ospedale Bike Sharing Park"));
		allBadges.add(new BagesData(bikeSharePaoli.getName(), FileUtils.readFileToByteArray(bikeSharePaoli), "image/png", "Via Paoli - Rovereto_BSstation", "Parcheggio Bike Sharing Via Paoli", "Via Paoli Bike Sharing Park"));
		allBadges.add(new BagesData(bikeSharePROsmini.getName(), FileUtils.readFileToByteArray(bikeSharePROsmini), "image/png", "P. Rosmini - Rovereto_BSstation", "Parcheggio Bike Sharing P. Rosmini", "P. Rosmini Bike Sharing Park"));
		allBadges.add(new BagesData(bikeShareQuercia.getName(), FileUtils.readFileToByteArray(bikeShareQuercia), "image/png", "Quercia - Rovereto_BSstation", "Parcheggio Bike Sharing Quercia", "Quercia Bike Sharing Park"));
		allBadges.add(new BagesData(bikeShareSacco.getName(), FileUtils.readFileToByteArray(bikeShareSacco), "image/png", "Sacco - Rovereto_BSstation", "Parcheggio Bike Sharing Sacco", "Sacco Bike Sharing Park"));
		allBadges.add(new BagesData(bikeShareStazione.getName(), FileUtils.readFileToByteArray(bikeShareStazione), "image/png", "Stazione FF.SS. - Rovereto_BSstation", "Parcheggio Bike Sharing Stazione FF.SS.", "Stazione FF.SS. Bike Sharing Park"));
		allBadges.add(new BagesData(bikeShareZonaIndustriale.getName(), FileUtils.readFileToByteArray(bikeShareZonaIndustriale), "image/png", "Zona Industriale - Rovereto_BSstation", "Parcheggio Bike Sharing Zona Industriale", "Zona Industriale Bike Sharing Park"));
		allBadges.add(new BagesData(bikeShareMart.getName(), FileUtils.readFileToByteArray(bikeShareMart), "image/png", "Mart - Rovereto_BSstation", "Parcheggio Bike Sharing MART", "MART Bike Sharing Park"));
		//TN bikeSharing stations
		allBadges.add(new BagesData(bikeShareFFSSOspedale.getName(), FileUtils.readFileToByteArray(bikeShareFFSSOspedale), "image/png", "Stazione FFSS - Ospedale - Trento_BSstation", "Parcheggio Bike Sharing Stazione FF.SS. Ospedale", "Stazine FF.SS Ospedale Bike Sharing Park"));
		allBadges.add(new BagesData(bikeSharePiazzaVenezia.getName(), FileUtils.readFileToByteArray(bikeSharePiazzaVenezia), "image/png", "Piazza Venezia - Trento_BSstation", "Parcheggio Bike Sharing Piazza Venezia", "Piazza Venezia Bike Sharing Park"));
		allBadges.add(new BagesData(bikeSharePiscina.getName(), FileUtils.readFileToByteArray(bikeSharePiscina), "image/png", "Piscina - Trento_BSstation", "Parcheggio Bike Sharing Piscina", "Piscina Bike Sharing Park"));
		allBadges.add(new BagesData(bikeSharePiazzaMostra.getName(), FileUtils.readFileToByteArray(bikeSharePiazzaMostra), "image/png", "Piazza della Mostra - Trento_BSstation", "Parcheggio Bike Sharing Piazza Mostra", "Piazza Mostra Bike Sharing Park"));
		allBadges.add(new BagesData(bikeShareCentroSantaChiara.getName(), FileUtils.readFileToByteArray(bikeShareCentroSantaChiara), "image/png", "Centro Santa Chiara - Trento_BSstation", "Parcheggio Bike Sharing Centro S.Chiara", "Centro S.Chiara Bike Sharing Park"));
		allBadges.add(new BagesData(bikeSharePiazzaCenta.getName(), FileUtils.readFileToByteArray(bikeSharePiazzaCenta), "image/png", "Piazza di Centa - Trento_BSstation", "Parcheggio Bike Sharing Piazza di Centa", "Piazza Centa Bike Sharing Park"));
		allBadges.add(new BagesData(bikeShareBiblioteca.getName(), FileUtils.readFileToByteArray(bikeShareBiblioteca), "image/png", "Biblioteca - Trento_BSstation", "Parcheggio Bike Sharing Biblioteca", "Biblioteca Bike Sharing Park"));
		allBadges.add(new BagesData(bikeShareStazioneAutocorriere.getName(), FileUtils.readFileToByteArray(bikeShareStazioneAutocorriere), "image/png", "Stazione Autocorriere - Trento_BSstation", "Parcheggio Bike Sharing Stazione Autocorriere", "Stazione Autocorriere Bike Sharing Park"));
		allBadges.add(new BagesData(bikeShareUniversita.getName(), FileUtils.readFileToByteArray(bikeShareUniversita), "image/png", "Università - Trento_BSstation", "Parcheggio Bike Sharing Universita'", "Universita Bike Sharing Park"));
		allBadges.add(new BagesData(bikeShareBezzi.getName(), FileUtils.readFileToByteArray(bikeShareBezzi), "image/png", "Bezzi - Trento_BSstation", "Parcheggio Bike Sharing Bezzi", "Bezzi Bike Sharing Park"));
		allBadges.add(new BagesData(bikeShareMuse.getName(), FileUtils.readFileToByteArray(bikeShareMuse), "image/png", "Muse - Trento_BSstation", "Parcheggio Bike Sharing Muse", "Muse Bike Sharing Park"));
		allBadges.add(new BagesData(bikeShareAziendaSanitaria.getName(), FileUtils.readFileToByteArray(bikeShareAziendaSanitaria), "image/png", "Azienda Sanitaria - Trento_BSstation", "Parcheggio Bike Sharing Azienda Sanitaria", "Azienda Sanitaria Bike Sharing Park"));
		allBadges.add(new BagesData(bikeShareTopCenter.getName(), FileUtils.readFileToByteArray(bikeShareTopCenter), "image/png", "Top Center - Trento_BSstation", "Parcheggio Bike Sharing Top Center", "Top Center Bike Sharing Park"));
		allBadges.add(new BagesData(bikeShareBrenCenter.getName(), FileUtils.readFileToByteArray(bikeShareBrenCenter), "image/png", "Bren Center - Trento_BSstation", "Parcheggio Bike Sharing Bren Center", "Bren Center Bike Sharing Park"));
		allBadges.add(new BagesData(bikeShareLidorno.getName(), FileUtils.readFileToByteArray(bikeShareLidorno), "image/png", "Lidorno - Trento_BSstation", "Parcheggio Bike Sharing Lidorno", "Lidorno Bike Sharing Park"));
		allBadges.add(new BagesData(bikeShareGardolo.getName(), FileUtils.readFileToByteArray(bikeShareGardolo), "image/png", "Gardolo - Trento_BSstation", "Parcheggio Bike Sharing Gardolo", "Gardolo Bike Sharing Park"));
		allBadges.add(new BagesData(bikeShareAeroporto.getName(), FileUtils.readFileToByteArray(bikeShareAeroporto), "image/png", "Aeroporto - Trento_BSstation", "Parcheggio Bike Sharing Aeroporto", "Aeroporto Bike Sharing Park"));
		
		// files for recommendation
		File recommendations3 = new File(path + "mail/img/recommendation/inviteFriends3.png");
		File recommendations5 = new File(path + "mail/img/recommendation/inviteFriends5.png");
		File recommendations10 = new File(path + "mail/img/recommendation/inviteFriends10.png");
		File recommendations25 = new File(path + "mail/img/recommendation/inviteFriends25.png");
				
		allBadges.add(new BagesData(recommendations3.getName(), FileUtils.readFileToByteArray(recommendations3), "image/png", "3_recommendations", "3 Amici Invitati", "3 Friends recommendation"));
		allBadges.add(new BagesData(recommendations5.getName(), FileUtils.readFileToByteArray(recommendations5), "image/png", "5_recommendations", "5 Amici Invitati", "5 Friends recommendation"));
		allBadges.add(new BagesData(recommendations10.getName(), FileUtils.readFileToByteArray(recommendations10), "image/png", "10_recommendations", "10 Amici Invitati", "10 Friends recommendation"));
		allBadges.add(new BagesData(recommendations25.getName(), FileUtils.readFileToByteArray(recommendations25), "image/png", "25_recommendations", "25 Amici Invitati", "25 Friends recommendation"));
				
		// files for public transport
		File publicTrans5 = new File(path + "mail/img/public_transport/publicTransportAficionado5.png");
		File publicTrans10 = new File(path + "mail/img/public_transport/publicTransportAficionado10.png");
		File publicTrans25 = new File(path + "mail/img/public_transport/publicTransportAficionado25.png");
		File publicTrans50 = new File(path + "mail/img/public_transport/publicTransportAficionado50.png");
		File publicTrans100 = new File(path + "mail/img/public_transport/publicTransportAficionado100.png");
		File publicTrans200 = new File(path + "mail/img/public_transport/publicTransportAficionado200.png");
		File publicTrans500 = new File(path + "mail/img/public_transport/publicTransportAficionado500.png");
		
		allBadges.add(new BagesData(publicTrans5.getName(), FileUtils.readFileToByteArray(publicTrans5), "image/png", "5_pt_trip", "5 Viaggi Mezzi Pubblici", "5 Public Trasport Trips"));
		allBadges.add(new BagesData(publicTrans10.getName(), FileUtils.readFileToByteArray(publicTrans10), "image/png", "10_pt_trip", "10 Viaggi Mezzi Pubblici", "10 Public Trasport Trips"));
		allBadges.add(new BagesData(publicTrans25.getName(), FileUtils.readFileToByteArray(publicTrans25), "image/png", "25_pt_trip", "25 Viaggi Mezzi Pubblici", "25 Public Trasport Trips"));
		allBadges.add(new BagesData(publicTrans50.getName(), FileUtils.readFileToByteArray(publicTrans50), "image/png", "50_pt_trip", "50 Viaggi Mezzi Pubblici", "50 Public Trasport Trips"));
		allBadges.add(new BagesData(publicTrans100.getName(), FileUtils.readFileToByteArray(publicTrans100), "image/png", "100_pt_trip", "100 Viaggi Mezzi Pubblici", "100 Public Trasport Trips"));
		allBadges.add(new BagesData(publicTrans200.getName(), FileUtils.readFileToByteArray(publicTrans200), "image/png", "200_pt_trip", "200 Viaggi Mezzi Pubblici", "200 Public Trasport Trips"));
		allBadges.add(new BagesData(publicTrans500.getName(), FileUtils.readFileToByteArray(publicTrans500), "image/png", "500_pt_trip", "500 Viaggi Mezzi Pubblici", "500 Public Trasport Trips"));
		
		// files for zero impact
		File zeroImpact1 = new File(path + "mail/img/zero_impact/zeroImpact1.png");
		File zeroImpact5 = new File(path + "mail/img/zero_impact/zeroImpact5.png");
		File zeroImpact10 = new File(path + "mail/img/zero_impact/zeroImpact10.png");
		File zeroImpact25 = new File(path + "mail/img/zero_impact/zeroImpact25.png");
		File zeroImpact50 = new File(path + "mail/img/zero_impact/zeroImpact50.png");
		File zeroImpact100 = new File(path + "mail/img/zero_impact/zeroImpact100.png");
		File zeroImpact200 = new File(path + "mail/img/zero_impact/zeroImpact200.png");
		File zeroImpact500 = new File(path + "mail/img/zero_impact/zeroImpact500.png");
				
		allBadges.add(new BagesData(zeroImpact1.getName(), FileUtils.readFileToByteArray(zeroImpact1), "image/png", "1_zero_impact_trip", "1 Viaggio Impatto Zero", "1 Zero Impact Trip"));
		allBadges.add(new BagesData(zeroImpact5.getName(), FileUtils.readFileToByteArray(zeroImpact5), "image/png", "5_zero_impact_trip", "5 Viaggi Impatto Zero", "5 Zero Impact Trips"));
		allBadges.add(new BagesData(zeroImpact10.getName(), FileUtils.readFileToByteArray(zeroImpact10), "image/png", "10_zero_impact_trip", "10 Viaggi Impatto Zero", "10 Zero Impact Trips"));
		allBadges.add(new BagesData(zeroImpact25.getName(), FileUtils.readFileToByteArray(zeroImpact25), "image/png", "25_zero_impact_trip", "25 Viaggi Impatto Zero", "25 Zero Impact Trips"));
		allBadges.add(new BagesData(zeroImpact50.getName(), FileUtils.readFileToByteArray(zeroImpact50), "image/png", "50_zero_impact_trip", "50 Viaggi Impatto Zero", "50 Zero Impact Trips"));	
		allBadges.add(new BagesData(zeroImpact100.getName(), FileUtils.readFileToByteArray(zeroImpact100), "image/png", "100_zero_impact_trip", "100 Viaggi Impatto Zero", "100 Zero Impact Trips"));
		allBadges.add(new BagesData(zeroImpact200.getName(), FileUtils.readFileToByteArray(zeroImpact200), "image/png", "200_zero_impact_trip", "200 Viaggi Impatto Zero", "200 Zero Impact Trips"));
		allBadges.add(new BagesData(zeroImpact500.getName(), FileUtils.readFileToByteArray(zeroImpact500), "image/png", "500_zero_impact_trip", "500 Viaggi Impatto Zero", "500 Zero Impact Trips"));
		
		// files for leaderboard top 3
		File firstOfWeek = new File(path + "mail/img/leaderboard/leaderboard1.png");
		File secondOfWeek = new File(path + "mail/img/leaderboard/leaderboard2.png");
		File thirdOfWeek = new File(path + "mail/img/leaderboard/leaderboard3.png");
						
		allBadges.add(new BagesData(firstOfWeek.getName(), FileUtils.readFileToByteArray(firstOfWeek), "image/png", "1st_of_the_week", "Primo della settimana", "First of the Week"));
		allBadges.add(new BagesData(secondOfWeek.getName(), FileUtils.readFileToByteArray(secondOfWeek), "image/png", "2nd_of_the_week", "Secondo della settimana", "Second of the Week"));
		allBadges.add(new BagesData(thirdOfWeek.getName(), FileUtils.readFileToByteArray(thirdOfWeek), "image/png", "3rd_of_the_week", "Terzo della settimana", "Third of the Week"));
		
		return allBadges;
	}
	
	private ArrayList<BagesData> checkCorrectBadges(ArrayList<BagesData> allB, ArrayList<Notification> notifics) throws IOException{
		ArrayList<BagesData> correctBadges = new ArrayList<BagesData>();
		
		for(int i = 0; i < allB.size(); i++){
			for(int j = 0; j < notifics.size(); j++){
				if(notifics.get(j).getBadge().compareTo(allB.get(i).getTextId()) == 0){
					logger.debug(String.format("Notification check notifics: %s, badge :%s", notifics.get(j).getBadge(), allB.get(i).getTextId()));
					correctBadges.add(allB.get(i));
				}
			}
		}
		return correctBadges;
	}
	
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
	
	
/*	*//**
	 * Method used to retrieve the state of a specific user and to send the find data via mail
	 * @param urlWS: url of the ws
	 * @return state ArrayList
	 * @throws InterruptedException 
	 *//*
	private ArrayList<State> getState(String urlWS) throws InterruptedException{
		RestTemplate restTemplate = new RestTemplate();
		logger.debug("State WS GET " + urlWS);
		String result = "";
		ResponseEntity<String> tmp_res = null;
		try {
			//result = restTemplate.getForObject(gamificationUrl + urlWS, String.class); //I pass the timestamp of the scheduled start time
			tmp_res = restTemplate.exchange(gamificationUrl + urlWS, HttpMethod.GET, new HttpEntity<Object>(createHeaders()),String.class);
		} catch (Exception ex){
			logger.error(String.format("Exception in proxyController get ws. Method: %s. Details: %s", urlWS, ex.getMessage()));
		}
		
		ArrayList<State> states = null;
		result = tmp_res.getBody();
		
		if(result != null && result.compareTo("") != 0){
			logger.debug(String.format("State Result Ok: %s", result));
			states = chekState(result);	
		} else {
			logger.error(String.format("State Result Fail: %s", result));
		}
		
		return states;
	}*/
	
	/**
	 * Method used to retrieve the state of a specific user and to send the find data via mail
	 * @param urlWS: url of the ws
	 * @return string complete state
	 * @throws InterruptedException 
	 */
	private String getAllChallenges(String urlWS) throws InterruptedException {
		
		RestTemplate restTemplate = new RestTemplate();
		logger.debug("Challenges WS GET " + urlWS);
		String result = "";
		ResponseEntity<String> tmp_res = null;
		try {
			//result = restTemplate.getForObject(gamificationUrl + urlWS, String.class); //I pass the timestamp of the scheduled start time
			tmp_res = restTemplate.exchange(gamificationUrl + urlWS, HttpMethod.GET, new HttpEntity<Object>(createHeaders()),String.class);
		} catch (Exception ex){
			logger.error(String.format("Exception in proxyController get ws. Method: %s. Details: %s", urlWS, ex.getMessage()));
		}
		result = tmp_res.getBody();
		return result;
	}	
	
	/**
	 * Method used to retrieve the notification of a specific user and to send the find data via mail
	 * @param urlWS: url of the ws
	 * @param timestamp: timestamp for the new notifications
	 * @return notification ArrayList
	 * @throws InterruptedException 
	 */
	private ArrayList<Notification> getNotifications(String urlWS, String timestamp) throws InterruptedException{
		
		RestTemplate restTemplate = new RestTemplate();
		logger.debug("Notification WS GET " + urlWS);
		String result = "";
		ResponseEntity<String> tmp_res = null;
		try {
			//result = restTemplate.getForObject(gamificationUrl + urlWS + timestamp, String.class); //I pass the timestamp of the scheduled start time
			tmp_res = restTemplate.exchange(gamificationUrl + urlWS + timestamp, HttpMethod.GET, new HttpEntity<Object>(createHeaders()),String.class);
		} catch (Exception ex){
			logger.error(String.format("Exception in proxyController get ws. Method: %s. Details: %s", urlWS, ex.getMessage()));
		}
		
		ArrayList<Notification> notifications = null;
		result = tmp_res.getBody();
		if(result != null){
			logger.debug(String.format("Notification Result Ok: %s", result));
			notifications = chekNotification(result);	
			
		} else {
			logger.error(String.format("Notification Result Fail: %s", result));
		}
		
		return notifications;
	}
	
	/**
	 * Method checlNotification: convert the result JSON string in an array of objects
	 * @param result: input string with the json of the ws
	 * @return Notification ArrayList
	 */
	private ArrayList<Notification> chekNotification(String result){
		ArrayList<Notification> notificationList = new ArrayList<Notification>();
		logger.debug(String.format("Result from WS: %s", result));
		
		// Here I have to convert result string in a list of notifications
		try {
			JSONArray JNotifics = new JSONArray(result);
			for(int i = 0; i < JNotifics.length(); i++){
				JSONObject fields = JNotifics.getJSONObject(i);
				String gameId = fields.getString(JSON_GAMEID);
				String playerId = fields.getString(JSON_PLAYERID);
				Long timestamp = fields.getLong(JSON_TIMESTAMP);
				String badge = fields.getString(JSON_BADGE);
				Notification notification = new Notification(gameId, playerId, timestamp, badge);
				notificationList.add(notification);
			}
			
		} catch (JSONException e) {
			logger.error(String.format("Exception in parsing notification: %s", e.getMessage()));
		}
		return notificationList;
	}
	
	/**
	 * Method checkNotification: convert the result JSON string in an array of objects
	 * @param result: input string with the json of the ws
	 * @return Notification ArrayList
	 */
	/*private ArrayList<State> chekState(String result){
		ArrayList<State> stateList = new ArrayList<State>();
		logger.debug(String.format("Result from WS: %s", result));
		
		try {
			JSONObject JOStates = new JSONObject(result);
			JSONObject JOMyState = JOStates.getJSONObject(JSON_STATE);
			JSONArray JScores = (!JOMyState.isNull(JSON_POINTCONCEPT)) ? JOMyState.getJSONArray(JSON_POINTCONCEPT) : null;
			if(JScores != null){
				for(int i = 0; i < JScores.length(); i++){
					String id = ""+i;
					JSONObject JOScore = JScores.getJSONObject(i);
					String name = JOScore.getString(JSON_NAME);
					String score = cleanStringFieldScore(JOScore.getString(JSON_SCORE));
					State state = new State(id, name, score);
					stateList.add(state);
				}
			}		
		} catch (JSONException e) {
			logger.error(String.format("Exception in parsing player state: %s", e.getMessage()));
		}
		
		return orderState(stateList);
	}*/
	
	/*private String cleanStringFieldScore(String fieldString){
		String field = fieldString.trim();
		Float score_num_f = Float.valueOf(field);
		int score_num_i = score_num_f.intValue();
		String cleanedScore = Integer.toString(score_num_i);
		return cleanedScore;
	}*/
	
	/**
	 * Method orderState: used to order the state array
	 * @param toOrder
	 * @return
	 */
	/*private ArrayList<State> orderState(ArrayList<State> toOrder){
		ArrayList<State> orderedList = new ArrayList<State>();
		// I order the list with green score at the first, health score at the second and pr at the third
		for(int i = 0; i < toOrder.size(); i++){
			if(toOrder.get(i).getName().compareTo("green leaves") == 0){
				orderedList.add(toOrder.get(i));
				break;
			}
		}
		for(int i = 0; i < toOrder.size(); i++){
			if(toOrder.get(i).getName().compareTo("health") == 0){
				orderedList.add(toOrder.get(i));
				break;
			}
		}
		for(int i = 0; i < toOrder.size(); i++){
			if(toOrder.get(i).getName().compareTo("p+r") == 0){
				orderedList.add(toOrder.get(i));
				break;
			}
		}
		return orderedList;
	}*/
	
	// Method used to read a week conf data file and store all values in a list of WeekConfData object
	public List<WeekConfData> readWeekConfFile(String src) {
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		List<WeekConfData> confWeekFileData = new ArrayList<WeekConfData>();

		try {
			br = new BufferedReader(new FileReader(src));
			line = br.readLine();	// read table header line
			while ((line = br.readLine()) != null) {
			    // use comma as separator
				String[] weekConfValues = line.split(cvsSplitBy);
				String weekNum = weekConfValues[0];
				String weekTheme = weekConfValues[1];
				String weekThemeEng = weekConfValues[2];
				String areChallenges = weekConfValues[3];
				String arePrizes = weekConfValues[4];
				String arePrizesLast= weekConfValues[5];
				String actualWeek = weekConfValues[6];
				logger.debug(String.format("Week conf file: week num %s, theme %s, challenges %s, prizes %s, prizes last %s, actual week %s", weekNum, weekTheme, areChallenges, arePrizes, arePrizesLast, actualWeek));
				// value conversion from string to boolean
				Boolean areChall = (areChallenges.compareTo("Y") == 0) ? true : false;
				Boolean arePriz = (arePrizes.compareTo("Y") == 0) ? true : false;
				Boolean arePrizLast = (arePrizesLast.compareTo("Y") == 0) ? true : false;
				Boolean isActual = (actualWeek.compareTo("Y") == 0) ? true : false;
				WeekConfData wconf = new WeekConfData(weekNum, weekTheme, weekThemeEng, areChall, arePriz, arePrizLast, isActual);
				confWeekFileData.add(wconf);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return confWeekFileData;
	}
	
	// Method used to read a week prizes file and store all data in a list of WeekPrizeData object
	public List<WeekPrizeData> readWeekPrizesFile(String src) {
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		List<WeekPrizeData> prizeWeekFileData = new ArrayList<WeekPrizeData>();

		try {
			br = new BufferedReader(new FileReader(src));
			line = br.readLine();	// read table header line
			while ((line = br.readLine()) != null) {
			    // use comma as separator
				String[] weekPrizeValues = line.split(cvsSplitBy);
				String weekNum = weekPrizeValues[0];
				String weekPrize = weekPrizeValues[1];
				String target = weekPrizeValues[2];
				String sponsor = weekPrizeValues[3];
				logger.debug(String.format("Week prize file: week num %s, prize %s, target %s, sponsor %s", weekNum, weekPrize, target, sponsor));
				WeekPrizeData wPrize = new WeekPrizeData(weekNum, weekPrize, target, sponsor);
				prizeWeekFileData.add(wPrize);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return prizeWeekFileData;
	}
	
	// Method used to read the week prizes data from conf file. More prizes for one week are allowed
	public List<WeekPrizeData> readWeekPrizesFileData(String weeknum, List<WeekPrizeData> allPrizes) {
		List<WeekPrizeData> prizeWeekData = new ArrayList<WeekPrizeData>();
		for(int i = 0; i < allPrizes.size(); i++){
			if(allPrizes.get(i).getWeekNum().compareTo(weeknum) == 0){
				prizeWeekData.add(allPrizes.get(i));
			}
		}
		return prizeWeekData;
	}
	
	public List<WeekWinnersData> readWeekWinnersFile(String src) {
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		List<WeekWinnersData> winnerWeekFileData = new ArrayList<WeekWinnersData>();

		try {
			br = new BufferedReader(new FileReader(src));
			line = br.readLine();	// read table header line
			while ((line = br.readLine()) != null) {
			    // use comma as separator
				String[] weekWinnerValues = line.split(cvsSplitBy);
				String weekNum = weekWinnerValues[0];
				String player = weekWinnerValues[1];
				String prize = weekWinnerValues[2];
				String target = weekWinnerValues[3];
				logger.debug(String.format("Week winner file: week num %s, player %s, prize %s, target %s", weekNum, player, prize, target));
				WeekWinnersData wWinners = new WeekWinnersData(weekNum, player, prize, target);
				winnerWeekFileData.add(wWinners);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return winnerWeekFileData;
	}

}
