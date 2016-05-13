package eu.trentorise.smartcampus.gamification_web.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import eu.trentorise.smartcampus.gamification_web.service.ChallengesUtils;
import eu.trentorise.smartcampus.gamification_web.service.EmailService;

import eu.trentorise.smartcampus.aac.AACException;
import eu.trentorise.smartcampus.gamification_web.models.BagesData;
import eu.trentorise.smartcampus.gamification_web.models.ChallengesData;
import eu.trentorise.smartcampus.gamification_web.models.MailImage;
import eu.trentorise.smartcampus.gamification_web.models.Notification;
import eu.trentorise.smartcampus.gamification_web.models.State;
import eu.trentorise.smartcampus.gamification_web.models.Summary;
import eu.trentorise.smartcampus.gamification_web.models.WeekConfData;
import eu.trentorise.smartcampus.gamification_web.models.WeekPrizeData;
import eu.trentorise.smartcampus.gamification_web.models.WeekWinnersData;
import eu.trentorise.smartcampus.gamification_web.repository.AuthPlayer;
import eu.trentorise.smartcampus.gamification_web.repository.AuthPlayerProd;
import eu.trentorise.smartcampus.gamification_web.repository.AuthPlayerProdRepositoryDao;
import eu.trentorise.smartcampus.gamification_web.repository.AuthPlayerRepositoryDao;
import eu.trentorise.smartcampus.gamification_web.repository.ChallengeDescriptionDataSetup;
import eu.trentorise.smartcampus.gamification_web.repository.Player;
import eu.trentorise.smartcampus.gamification_web.repository.PlayerProd;
import eu.trentorise.smartcampus.gamification_web.repository.PlayerProdRepositoryDao;
import eu.trentorise.smartcampus.gamification_web.repository.PlayerRepositoryDao;
import eu.trentorise.smartcampus.gamification_web.repository.SponsorBannerDataSetup;
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
    private PlayerProdRepositoryDao playerProdRepositoryDao;
    
    @Autowired
    private AuthPlayerRepositoryDao authPlayerRepositoryDao;
    
    @Autowired
    private AuthPlayerProdRepositoryDao authPlayerProdRepositoryDao;
    
    @Autowired
    private ChallengeDescriptionDataSetup challDescriptionSetup;
    
    @Autowired
    private SponsorBannerDataSetup sponsorBannerSetup;
    
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
    @Value("${gamification.mail.startgame}")
    private String mailStartGame;
    
    private final String JSON_STATE = "state";
    private final String JSON_POINTCONCEPT = "PointConcept";
    private final String JSON_NAME = "name";
    private final String JSON_SCORE = "score";
    private final String JSON_GAMEID = "gameId";
    private final String JSON_PLAYERID = "playerId";
    private final String JSON_TIMESTAMP = "timestamp";
    private final String JSON_BADGE = "badge";
    
	/*
	 * OAUTH2
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/")
	public ModelAndView index_gameweb(HttpServletRequest request) throws SecurityException, ProfileServiceException {
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
			
			if(isTest.compareTo("true") == 0){
				// Check if the user belongs to the list of the testers (in test)
				Player player_check = playerRepositoryDao.findBySocialId(user.getUserId());	// app user table
				if(player_check == null){
					//String attribute_mail = account.getAttribute(objectArray[0].toString(), "openid.ext1.value.email");
					logger.debug(String.format("Player to add: mail %s.", attribute_mail));
					if(attribute_mail != null){
						if(authorizationTable.compareTo("true") == 0){
							AuthPlayer auth_p = authPlayerRepositoryDao.findByMail(attribute_mail);
							if(auth_p != null){
								logger.info(String.format("Add player: authorised %s.", auth_p.toJSONString()));
								Player new_p = new Player(user.getUserId(), user.getUserId(), user.getName(), user.getSurname(), auth_p.getNikName(), auth_p.getMail(), null);
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
							Player new_p = new Player(user.getUserId(), user.getUserId(), user.getName(), user.getSurname(), nick, attribute_mail, null);
							playerRepositoryDao.save(new_p);
							// here I call an api from gengine console
							createPlayerInGamification(user.getUserId());
							logger.info(String.format("Add new player: created player %s.", new_p.toJSONString()));
						}
					}
				}
			} else {
				// Check if the user belongs to the list of the testers (in test)
				PlayerProd player_check = playerProdRepositoryDao.findBySocialId(user.getUserId());
				if(player_check == null){
					//String attribute_mail = account.getAttribute(objectArray[0].toString(), "openid.ext1.value.email");
					logger.info(String.format("Player to add: mail %s.", attribute_mail));
					if(attribute_mail != null){
						if(authorizationTable.compareTo("true") == 0){
							AuthPlayerProd auth_p = authPlayerProdRepositoryDao.findByMail(attribute_mail);
							if(auth_p != null){
								logger.info(String.format("Add player: authorised %s.", auth_p.toJSONString()));
								PlayerProd new_p = new PlayerProd(user.getUserId(), user.getUserId(), user.getName(), user.getSurname(), auth_p.getNikName(), auth_p.getMail(), null);
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
							PlayerProd new_p = new PlayerProd(user.getUserId(), user.getUserId(), user.getName(), user.getSurname(), nick, attribute_mail, null);
							playerProdRepositoryDao.save(new_p);
							// here I call an api from gengine console
							createPlayerInGamification(user.getUserId());
							logger.info(String.format("Add new player: created player %s.", new_p.toJSONString()));
						}
					}
				}
			}
		} catch (Exception ex){
			logger.error(String.format("Errore di conversione: %s", ex.getMessage()));
			return new ModelAndView("redirect:/logout");
		}
		
		//SubjectDn subj = new SubjectDn(utente.getSubjectdn());
		//logger.error(String.format("Subjextdn : cn: %s; ou: %s: o: %s; c: %s", subj.getCn(), subj.getOu(),subj.getO(),subj.getC()));
		
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
		return new ModelAndView("redirect:/");
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
	

	@RequestMapping(method = RequestMethod.GET, value = "/loginfb")
	public ModelAndView secureFb(HttpServletRequest request) {
		String redirectUri = mainURL + "/check";
		String redirectAacService = aacService.generateAuthorizationURIForCodeFlow(redirectUri, "/facebook",
				"profile.basicprofile.me,profile.accountprofile.me", null);	//"smartcampus.profile.basicprofile.me,smartcampus.profile.accountprofile.me"
		return new ModelAndView(
				"redirect:"
						+ redirectAacService);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/prelogin")
	public ModelAndView preSecure(HttpServletRequest request) {
		logger.debug(String.format("I am in pre login"));
		ModelAndView model = new ModelAndView();
		model.setViewName("landing");
		return model;
	}
	
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
	
	@RequestMapping(method = RequestMethod.GET, value = "/view_prizes")
	public ModelAndView preSecurePrizesPage(HttpServletRequest request) {
		logger.debug(String.format("I am in prizes info page"));
		ModelAndView model = new ModelAndView();
		model.setViewName("g_prizes");
		return model;
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
	@SuppressWarnings("unchecked")
	//@Scheduled(fixedRate = 5*60*1000) // Repeat every 5 minutes
	@Scheduled(cron="0 0 17 * * FRI") 		// Repeat every Friday at 5 PM
	public synchronized void checkNotification() throws IOException {
		ArrayList<Summary> summaryMail = new ArrayList<Summary>();
		long millis = System.currentTimeMillis() - (7*24*60*60*1000);	// Delta in millis of one week //long millis = 1415660400000L; //(for test)
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
		File footer = new File(path + "mail/img/footer.png");
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
			List<WeekWinnersData> mailWinnersFileData = readWeekWinnersFile(path + "mail/" + conf_directory + "/game_week_winners.csv");
			List<WeekPrizeData> mailPrizeActualData = new ArrayList<WeekPrizeData>();
			// here I have to add the new mail parameters readed from csv files
			String actual_week = "";
			String actual_week_theme = "";
			String last_week = "";
			Boolean are_chall = false;
			Boolean are_prizes = false;
			Boolean are_prizes_last_week = false;
			for(int i = 0; i < mailConfigurationFileData.size(); i++){
				WeekConfData tmpWConf = mailConfigurationFileData.get(i);
				if(tmpWConf.isActual()){
					actual_week = tmpWConf.getWeekNum();
					actual_week_theme = tmpWConf.getWeekTheme();
					last_week = Integer.toString(Integer.parseInt(actual_week) - 1);
					are_chall = tmpWConf.isChallenges();
					are_prizes = tmpWConf.isPrizes();
					are_prizes_last_week = tmpWConf.isPrizesLast();
					mailPrizeActualData = readWeekPrizesFileData(actual_week, mailPrizeFileData);
				}
			}
			if(isTest.compareTo("true") == 0){
				Iterable<Player> iter = playerRepositoryDao.findAll();
				for(Player p: iter){
					logger.debug(String.format("Profile finded  %s", p.getNikName()));
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e1) {
						logger.error(String.format("Errore in attesa thread: %s", e1.getMessage()));
					}
					
					ArrayList<State> states = null;
					ArrayList<Notification> notifications = null;
					ArrayList<BagesData> someBadge = null;
					List<ChallengesData> challenges = null;
					List<ChallengesData> lastWeekChallenges = null;
					
					try {
						// WS State Invocation
						String urlWSState = "state/" + gameName + "/" + p.getSocialId();
						states = getState(urlWSState);
						// Challenges correction
						String completeState = getAllChallenges(urlWSState);
						try {
							@SuppressWarnings("rawtypes")
							List<List> challLists = challUtils.correctCustomData(completeState);
							if(challLists != null && challLists.size() == 2){
								challenges = challLists.get(0);
								lastWeekChallenges = challLists.get(1);
							}
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
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
					
					if(mailSend.compareTo("true") == 0 && playerName!= null && playerName.compareTo("")!=0){
						try {
							if(notifications != null){
								if(states != null && states.size() > 0){
									this.emailService.sendMailGamification(mailStartGame, playerName, states.get(0).getScore(), null, null, null, null,		// health and pr point are null
											actual_week, actual_week_theme, last_week, are_chall, are_prizes, are_prizes_last_week, someBadge, 
											challenges, lastWeekChallenges, mailPrizeActualData, mailWinnersFileData, standardImages, mailto, mailRedirectUrl, Locale.ITALIAN);
								} else {
									this.emailService.sendMailGamification(mailStartGame, playerName, "0", "0", "0", null, null, 
											actual_week, actual_week_theme, last_week, are_chall, are_prizes, are_prizes_last_week, someBadge, 
											challenges, lastWeekChallenges, mailPrizeActualData, mailWinnersFileData, standardImages, mailto, mailRedirectUrl, Locale.ITALIAN);
								}
							} else {
								if(states != null  && states.size() > 0){
									this.emailService.sendMailGamification(mailStartGame, playerName, states.get(0).getScore(), null, null, null, null, // health and pr point are null
											actual_week, actual_week_theme, last_week, are_chall, are_prizes, are_prizes_last_week, null, 
											challenges, lastWeekChallenges, mailPrizeActualData, mailWinnersFileData, standardImages, mailto, mailRedirectUrl, Locale.ITALIAN);
								} else {
									this.emailService.sendMailGamification(mailStartGame, playerName, "0", "0", "0", null, null, 
											actual_week, actual_week_theme, last_week, are_chall, are_prizes, are_prizes_last_week, null, 
											challenges, lastWeekChallenges, mailPrizeActualData, mailWinnersFileData, standardImages, mailto, mailRedirectUrl, Locale.ITALIAN);
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
							if(states != null  && states.size() > 0){
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
				}
			} else {
				Iterable<PlayerProd> iter = playerProdRepositoryDao.findAll();
				for(PlayerProd p: iter){
					logger.debug(String.format("Profile finded  %s", p.getNikName()));
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e1) {
						logger.error(String.format("Errore in attesa thread: %s", e1.getMessage()));
					}
					
					ArrayList<State> states = null;
					ArrayList<Notification> notifications = null;
					ArrayList<BagesData> someBadge = null;
					List<ChallengesData> challenges = null;
					List<ChallengesData> lastWeekChallenges = null;
					
					try {
						// WS State Invocation
						String urlWSState = "state/" + gameName + "/" + p.getSocialId();
						states = getState(urlWSState);
						// Challenges correction
						String completeState = getAllChallenges(urlWSState);
						try {
							@SuppressWarnings("rawtypes")
							List<List> challLists = challUtils.correctCustomData(completeState);
							if(challLists != null && challLists.size() == 2){
								challenges = challLists.get(0);
								lastWeekChallenges = challLists.get(1);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
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
					
					if(mailSend.compareTo("true") == 0 && playerName != null && playerName.compareTo("") != 0){
						
						try {
							if(notifications != null){
								if(states != null  && states.size() > 0){
									this.emailService.sendMailGamification(mailStartGame, playerName, states.get(0).getScore(), null, null, null, null, // health and pr point are null
											actual_week, actual_week_theme, last_week, are_chall, are_prizes, are_prizes_last_week, someBadge, 
											challenges, lastWeekChallenges, mailPrizeActualData, mailWinnersFileData, standardImages, mailto, mailRedirectUrl, Locale.ITALIAN);
								} else {
									this.emailService.sendMailGamification(mailStartGame, playerName, "0", "0", "0", null, null, 
											actual_week, actual_week_theme, last_week, are_chall, are_prizes, are_prizes_last_week, someBadge,
											challenges, lastWeekChallenges, mailPrizeActualData, mailWinnersFileData, standardImages, mailto, mailRedirectUrl, Locale.ITALIAN);
								}
							} else {
								if(states != null  && states.size() > 0){
									this.emailService.sendMailGamification(mailStartGame, playerName, states.get(0).getScore(), null, null, null, null, // health and pr point are null
											actual_week, actual_week_theme, last_week, are_chall, are_prizes, are_prizes_last_week, null, 
											challenges, lastWeekChallenges, mailPrizeActualData, mailWinnersFileData, standardImages, mailto, mailRedirectUrl, Locale.ITALIAN);
								} else {
									this.emailService.sendMailGamification(mailStartGame, playerName, "0", "0", "0", null, null, 
											actual_week, actual_week_theme, last_week, are_chall, are_prizes, are_prizes_last_week, null, 
											challenges, lastWeekChallenges, mailPrizeActualData, mailWinnersFileData, standardImages, mailto, mailRedirectUrl, Locale.ITALIAN);
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
		//}
	}
	
	@SuppressWarnings("unchecked")
	//@Scheduled(fixedRate = 2*60*1000) // Repeat once a minute
	//@Scheduled(cron="0 0 11 * * MON") 	// Repeat every Monday at 11 AM
	public synchronized void checkWinnersNotification() throws IOException {
		ArrayList<Summary> summaryMail = new ArrayList<Summary>();
		long millis = System.currentTimeMillis() - (3*24*60*60*1000);	// Delta in millis of N days: now 3 days
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
		File footer = new File(path + "mail/img/footer.png");
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
		List<WeekWinnersData> mailWinnersFileData = readWeekWinnersFile(path + "mail/conf_file/game_week_winners.csv");
		List<WeekPrizeData> mailPrizeActualData = new ArrayList<WeekPrizeData>();
		// here I have to add the new mail parameters readed from csv files
		String actual_week = "";
		String actual_week_theme = "";
		String last_week = "";
		Boolean are_chall = false;
		Boolean are_prizes = false;
		Boolean are_prizes_last_week = false;
		for(int i = 0; i < mailConfigurationFileData.size(); i++){
			WeekConfData tmpWConf = mailConfigurationFileData.get(i);
			if(tmpWConf.isActual()){
				actual_week = tmpWConf.getWeekNum();
				actual_week_theme = tmpWConf.getWeekTheme();
				last_week = Integer.toString(Integer.parseInt(actual_week) - 1);
				are_chall = tmpWConf.isChallenges();
				are_prizes = tmpWConf.isPrizes();
				are_prizes_last_week = tmpWConf.isPrizesLast();
				mailPrizeActualData = readWeekPrizesFileData(actual_week, mailPrizeFileData);
			}
		}
		if(isTest.compareTo("true") == 0){
			Iterable<Player> iter = playerRepositoryDao.findAll();
			for(Player p: iter){
				logger.debug(String.format("Profile finded  %s", p.getNikName()));
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e1) {
					logger.error(String.format("Errore in attesa thread: %s", e1.getMessage()));
				}
				
				ArrayList<State> states = null;
				ArrayList<Notification> notifications = null;
				ArrayList<BagesData> someBadge = null;
				List<ChallengesData> challenges = null;
				List<ChallengesData> lastWeekChallenges = null;
				
				try {
					// WS State Invocation
					String urlWSState = "state/" + gameName + "/" + p.getSocialId();
					states = getState(urlWSState);
					// Challenges correction
					String completeState = getAllChallenges(urlWSState);
					try {
						@SuppressWarnings("rawtypes")
						List<List> challLists = challUtils.correctCustomData(completeState);
						if(challLists != null && challLists.size() == 2){
							challenges = challLists.get(0);
							lastWeekChallenges = challLists.get(1);
						}
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
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
				
				if(mailSend.compareTo("true") == 0 && playerName!= null && playerName.compareTo("")!=0){
					try {
						if(notifications != null){
							if(states != null && states.size() > 0){
								this.emailService.sendMailGamificationForWinners(playerName, states.get(0).getScore(), null, null, null, null,		// health and pr point are null
										actual_week, actual_week_theme, last_week, are_chall, are_prizes, are_prizes_last_week, someBadge, 
										challenges, lastWeekChallenges, mailPrizeActualData, mailWinnersFileData, standardImages, mailto, mailRedirectUrl, Locale.ITALIAN);
							} else {
								this.emailService.sendMailGamificationForWinners(playerName, "0", "0", "0", null, null, 
										actual_week, actual_week_theme, last_week, are_chall, are_prizes, are_prizes_last_week, someBadge, 
										challenges, lastWeekChallenges, mailPrizeActualData, mailWinnersFileData, standardImages, mailto, mailRedirectUrl, Locale.ITALIAN);
							}
						} else {
							if(states != null  && states.size() > 0){
								this.emailService.sendMailGamificationForWinners(playerName, states.get(0).getScore(), null, null, null, null, // health and pr point are null
										actual_week, actual_week_theme, last_week, are_chall, are_prizes, are_prizes_last_week, null, 
										challenges, lastWeekChallenges, mailPrizeActualData, mailWinnersFileData, standardImages, mailto, mailRedirectUrl, Locale.ITALIAN);
							} else {
								this.emailService.sendMailGamificationForWinners(playerName, "0", "0", "0", null, null, 
										actual_week, actual_week_theme, last_week, are_chall, are_prizes, are_prizes_last_week, null, 
										challenges, lastWeekChallenges, mailPrizeActualData, mailWinnersFileData, standardImages, mailto, mailRedirectUrl, Locale.ITALIAN);
							}
						}
					} catch (MessagingException e) {
						logger.debug(String.format("Errore invio mail : %s", e.getMessage()));
					}
				} else {
					if(notifications != null){
						if(states != null && states.size() > 0){
							logger.debug(String.format("Invio mail a %s con notifica : %s e stato: %s", playerName ,notifications.toString(), states.toString()));
						} else {
							logger.debug(String.format("Invio mail a %s con notifica : %s", playerName ,notifications.toString()));
						}
					} else {
						if(states != null  && states.size() > 0){
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
			}
		} else {
			Iterable<PlayerProd> iter = playerProdRepositoryDao.findAll();
			for(PlayerProd p: iter){
				logger.debug(String.format("Profile finded  %s", p.getNikName()));
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e1) {
					logger.error(String.format("Errore in attesa thread: %s", e1.getMessage()));
				}
				
				ArrayList<State> states = null;
				ArrayList<Notification> notifications = null;
				ArrayList<BagesData> someBadge = null;
				List<ChallengesData> challenges = null;
				List<ChallengesData> lastWeekChallenges = null;
				
				try {
					// WS State Invocation
					String urlWSState = "state/" + gameName + "/" + p.getSocialId();
					states = getState(urlWSState);
					// Challenges correction
					String completeState = getAllChallenges(urlWSState);
					try {
						@SuppressWarnings("rawtypes")
						List<List> challLists = challUtils.correctCustomData(completeState);
						if(challLists != null && challLists.size() == 2){
							challenges = challLists.get(0);
							lastWeekChallenges = challLists.get(1);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
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
				
				if(mailSend.compareTo("true") == 0 && playerName!= null && playerName.compareTo("")!=0){
					
					try {
						if(notifications != null){
							if(states != null  && states.size() > 0){
								this.emailService.sendMailGamificationForWinners(playerName, states.get(0).getScore(), null, null, null, null, // health and pr point are null
										actual_week, actual_week_theme, last_week, are_chall, are_prizes, are_prizes_last_week, someBadge, 
										challenges, lastWeekChallenges, mailPrizeActualData, mailWinnersFileData, standardImages, mailto, mailRedirectUrl, Locale.ITALIAN);
							} else {
								this.emailService.sendMailGamificationForWinners(playerName, "0", "0", "0", null, null, 
										actual_week, actual_week_theme, last_week, are_chall, are_prizes, are_prizes_last_week, someBadge,
										challenges, lastWeekChallenges, mailPrizeActualData, mailWinnersFileData, standardImages, mailto, mailRedirectUrl, Locale.ITALIAN);
							}
						} else {
							if(states != null  && states.size() > 0){
								this.emailService.sendMailGamificationForWinners(playerName, states.get(0).getScore(), null, null, null, null, // health and pr point are null
										actual_week, actual_week_theme, last_week, are_chall, are_prizes, are_prizes_last_week, null, 
										challenges, lastWeekChallenges, mailPrizeActualData, mailWinnersFileData, standardImages, mailto, mailRedirectUrl, Locale.ITALIAN);
							} else {
								this.emailService.sendMailGamificationForWinners(playerName, "0", "0", "0", null, null, 
										actual_week, actual_week_theme, last_week, are_chall, are_prizes, are_prizes_last_week, null, 
										challenges, lastWeekChallenges, mailPrizeActualData, mailWinnersFileData, standardImages, mailto, mailRedirectUrl, Locale.ITALIAN);
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
		File greenBronze = new File(path + "mail/img/leaderboard/green/leaderboardGreen3.png");
		File greenSilver = new File(path + "mail/img/leaderboard/green/leaderboardGreen2.png");
		File greenGold = new File(path + "mail/img/leaderboard/green/leaderboardGreen1.png");
		
		allBadges.add(new BagesData(greenKing.getName(), FileUtils.readFileToByteArray(greenKing), "image/png", "king_week_green", "Re della Settimana - Green Leaves"));
		allBadges.add(new BagesData(green50.getName(), FileUtils.readFileToByteArray(green50), "image/png", "50_point_green", "50 Punti Green Leaves"));
		allBadges.add(new BagesData(green100.getName(), FileUtils.readFileToByteArray(green100), "image/png", "100_point_green", "100 Punti Green Leaves"));
		allBadges.add(new BagesData(green200.getName(), FileUtils.readFileToByteArray(green200), "image/png", "200_point_green", "200 Punti Green Leaves"));
		allBadges.add(new BagesData(green400.getName(), FileUtils.readFileToByteArray(green400), "image/png", "400_point_green", "400 Punti Green Leaves"));
		allBadges.add(new BagesData(green800.getName(), FileUtils.readFileToByteArray(green800), "image/png", "800_point_green", "800 Punti Green Leaves"));
		allBadges.add(new BagesData(green1500.getName(), FileUtils.readFileToByteArray(green1500), "image/png", "1500_point_green", "1500 Punti Green Leaves"));
		allBadges.add(new BagesData(green2500.getName(), FileUtils.readFileToByteArray(green2500), "image/png", "2500_point_green", "2500 Punti Green Leaves"));
		allBadges.add(new BagesData(green5000.getName(), FileUtils.readFileToByteArray(green5000), "image/png", "5000_point_green", "5000 Punti Green Leaves"));
		allBadges.add(new BagesData(greenBronze.getName(), FileUtils.readFileToByteArray(greenBronze), "image/png", "bronze-medal-green", "Medaglia di Bronzo - Green Leaves"));
		allBadges.add(new BagesData(greenSilver.getName(), FileUtils.readFileToByteArray(greenSilver), "image/png", "silver-medal-green", "Medaglia d'Argento - Green Leaves"));
		allBadges.add(new BagesData(greenGold.getName(), FileUtils.readFileToByteArray(greenGold), "image/png", "gold-medal-green", "Medaglia d'Oro - Green Leaves"));
				
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
		
		allBadges.add(new BagesData(healthKing.getName(), FileUtils.readFileToByteArray(healthKing), "image/png", "king_week_health", "Re della Settimana - Salute"));
		allBadges.add(new BagesData(health10.getName(), FileUtils.readFileToByteArray(health10), "image/png", "10_point_health", "10 Punti Salute"));
		allBadges.add(new BagesData(health25.getName(), FileUtils.readFileToByteArray(health25), "image/png", "25_point_health", "25 Punti Salute"));
		allBadges.add(new BagesData(health50.getName(), FileUtils.readFileToByteArray(health50), "image/png", "50_point_health", "50 Punti Salute"));
		allBadges.add(new BagesData(health100.getName(), FileUtils.readFileToByteArray(health100), "image/png", "100_point_health", "100 Punti Salute"));
		allBadges.add(new BagesData(health200.getName(), FileUtils.readFileToByteArray(health200), "image/png", "200_point_health", "200 Punti Salute"));
		allBadges.add(new BagesData(healthBronze.getName(), FileUtils.readFileToByteArray(healthBronze), "image/png", "bronze_medal_health", "Medaglia di Bronzo - Salute"));
		allBadges.add(new BagesData(healthSilver.getName(), FileUtils.readFileToByteArray(healthSilver), "image/png", "silver_medal_health", "Medaglia d'Argento - Salute"));
		allBadges.add(new BagesData(healthGold.getName(), FileUtils.readFileToByteArray(healthGold), "image/png", "gold_medal_health", "Medaglia d'Oro - Salute"));
		
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
		
		allBadges.add(new BagesData(prKing.getName(), FileUtils.readFileToByteArray(prKing), "image/png", "king_week_pr", "Re della Settimana - Park&Ride"));
		allBadges.add(new BagesData(pr10.getName(), FileUtils.readFileToByteArray(pr10), "image/png", "10_point_pr", "10 Punti Park&Ride"));
		allBadges.add(new BagesData(pr20.getName(), FileUtils.readFileToByteArray(pr20), "image/png", "20_point_pr", "20 Punti Park&Ride"));
		allBadges.add(new BagesData(pr50.getName(), FileUtils.readFileToByteArray(pr50), "image/png", "50_point_pr", "50 Punti Park&Ride"));
		allBadges.add(new BagesData(pr100.getName(), FileUtils.readFileToByteArray(pr100), "image/png", "100_point_pr", "100 Punti Park&Ride"));
		allBadges.add(new BagesData(pr200.getName(), FileUtils.readFileToByteArray(pr200), "image/png", "200_point_pr", "200 Punti Park&Ride"));
		allBadges.add(new BagesData(prBronze.getName(), FileUtils.readFileToByteArray(prBronze), "image/png", "bronze_medal_pr", "Medaglia di Bronzo - Park&Ride"));
		allBadges.add(new BagesData(prSilver.getName(), FileUtils.readFileToByteArray(prSilver), "image/png", "silver_medal_pr", "Medaglia d'Argento - Park&Ride"));
		allBadges.add(new BagesData(prGold.getName(), FileUtils.readFileToByteArray(prGold), "image/png", "gold_medal_pr", "Medaglia d'Oro - Park&Ride"));
		allBadges.add(new BagesData(prManifattura.getName(), FileUtils.readFileToByteArray(prManifattura), "image/png", "Manifattura_parking", "Parcheggio Manifattura - Park&Ride"));
		allBadges.add(new BagesData(prStadio.getName(), FileUtils.readFileToByteArray(prStadio), "image/png", "Stadio_parking", "Parcheggio Stadio - Park&Ride"));
		
		// files for special badges
		File specialEmotion = new File(path + "mail/img/special/emotion.png");
		File specialZeroImpact = new File(path + "mail/img/special/impatto_zero.png");
		File specialStadioPark = new File(path + "mail/img/special/special_p_quercia.png");
		File specialManifattura = new File(path + "mail/img/special/special_special_p_manifattura.png");
		File specialCentroStorico = new File(path + "mail/img/special/special_p_centro.storico.png");
		File specialParcheggioCentro = new File(path + "mail/img/special/special_p_centro.png");
		File specialPleALeoni = new File(path + "mail/img/special/special_p_p.le.a.leoni.png");
		
		allBadges.add(new BagesData(specialEmotion.getName(), FileUtils.readFileToByteArray(specialEmotion), "image/png", "e-motion", "E-Motion"));
		allBadges.add(new BagesData(specialZeroImpact.getName(), FileUtils.readFileToByteArray(specialZeroImpact), "image/png", "zero-impact", "Impatto Zero"));
		allBadges.add(new BagesData(specialStadioPark.getName(), FileUtils.readFileToByteArray(specialStadioPark), "image/png", "Stadio-park", "Parcheggio Stadio Quercia"));
		allBadges.add(new BagesData(specialManifattura.getName(), FileUtils.readFileToByteArray(specialManifattura), "image/png", "Ex Manifattura-park", "Parcheggio Ex Manifattura"));
		allBadges.add(new BagesData(specialCentroStorico.getName(), FileUtils.readFileToByteArray(specialCentroStorico), "image/png", "Centro Storico-park", "Parcheggio Centro Storico"));
		allBadges.add(new BagesData(specialParcheggioCentro.getName(), FileUtils.readFileToByteArray(specialParcheggioCentro), "image/png", "Parcheggio Centro-park", "Parcheggio Centro"));
		allBadges.add(new BagesData(specialPleALeoni.getName(), FileUtils.readFileToByteArray(specialPleALeoni), "image/png", "P.le A.Leoni-park", "Parcheggio Piazzale Leoni"));
		
		// files for bike
		File bike1 = new File(path + "mail/img/bike/bikeAficionado1.png");
		File bike5 = new File(path + "mail/img/bike/bikeAficionado5.png");
		File bike10 = new File(path + "mail/img/bike/bikeAficionado10.png");
		File bike25 = new File(path + "mail/img/bike/bikeAficionado25.png");
		File bike50 = new File(path + "mail/img/bike/bikeAficionado50.png");
		
		allBadges.add(new BagesData(bike1.getName(), FileUtils.readFileToByteArray(bike1), "image/png", "1_bike_trip", "1 Viaggio in Bici"));
		allBadges.add(new BagesData(bike5.getName(), FileUtils.readFileToByteArray(bike5), "image/png", "5_bike_trip", "5 Viaggi in Bici"));
		allBadges.add(new BagesData(bike10.getName(), FileUtils.readFileToByteArray(bike10), "image/png", "10_bike_trip", "10 Viaggi in Bici"));
		allBadges.add(new BagesData(bike25.getName(), FileUtils.readFileToByteArray(bike25), "image/png", "25_bike_trip", "25 Viaggi in Bici"));
		allBadges.add(new BagesData(bike50.getName(), FileUtils.readFileToByteArray(bike50), "image/png", "50_bike_trip", "50 Viaggi in Bici"));
		
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
				
		allBadges.add(new BagesData(bikeShareBrione.getName(), FileUtils.readFileToByteArray(bikeShareBrione), "image/png", "Brione - Rovereto_BSstation", "Parcheggio Bike Sharing Brione"));
		allBadges.add(new BagesData(bikeShareLizzana.getName(), FileUtils.readFileToByteArray(bikeShareLizzana), "image/png", "Lizzana - Rovereto_BSstation", "Parcheggio Bike Sharing Lizzana"));
		allBadges.add(new BagesData(bikeShareMarco.getName(), FileUtils.readFileToByteArray(bikeShareMarco), "image/png", "Marco - Rovereto_BSstation", "Parcheggio Bike Sharing Marco"));
		allBadges.add(new BagesData(bikeShareMunicipio.getName(), FileUtils.readFileToByteArray(bikeShareMunicipio), "image/png", "Municipio - Rovereto_BSstation", "Parcheggio Bike Sharing Municipio"));
		allBadges.add(new BagesData(bikeShareNoriglio.getName(), FileUtils.readFileToByteArray(bikeShareNoriglio), "image/png", "Noriglio - Rovereto_BSstation", "Parcheggio Bike Sharing Noriglio"));
		allBadges.add(new BagesData(bikeShareOrsi.getName(), FileUtils.readFileToByteArray(bikeShareOrsi), "image/png", "Orsi - Rovereto_BSstation", "Parcheggio Bike Sharing Piazzale Orsi"));
		allBadges.add(new BagesData(bikeShareOspedale.getName(), FileUtils.readFileToByteArray(bikeShareOspedale), "image/png", "Ospedale - Rovereto_BSstation", "Parcheggio Bike Sharing Ospedale"));
		allBadges.add(new BagesData(bikeSharePaoli.getName(), FileUtils.readFileToByteArray(bikeSharePaoli), "image/png", "Via Paoli - Rovereto_BSstation", "Parcheggio Bike Sharing Via Paoli"));
		allBadges.add(new BagesData(bikeSharePROsmini.getName(), FileUtils.readFileToByteArray(bikeSharePROsmini), "image/png", "P. Rosmini - Rovereto_BSstation", "Parcheggio Bike Sharing P. Rosmini"));
		allBadges.add(new BagesData(bikeShareQuercia.getName(), FileUtils.readFileToByteArray(bikeShareQuercia), "image/png", "Quercia - Rovereto_BSstation", "Parcheggio Bike Sharing Quercia"));
		allBadges.add(new BagesData(bikeShareSacco.getName(), FileUtils.readFileToByteArray(bikeShareSacco), "image/png", "Sacco - Rovereto_BSstation", "Parcheggio Bike Sharing Sacco"));
		allBadges.add(new BagesData(bikeShareStazione.getName(), FileUtils.readFileToByteArray(bikeShareStazione), "image/png", "Stazione FF.SS. - Rovereto_BSstation", "Parcheggio Bike Sharing Stazione FF.SS."));
		allBadges.add(new BagesData(bikeShareZonaIndustriale.getName(), FileUtils.readFileToByteArray(bikeShareZonaIndustriale), "image/png", "Zona Industriale - Rovereto_BSstation", "Parcheggio Bike Sharing Zona Industriale"));
		allBadges.add(new BagesData(bikeShareMart.getName(), FileUtils.readFileToByteArray(bikeShareMart), "image/png", "Mart - Rovereto_BSstation", "Parcheggio Bike Sharing MART"));
		
		// files for recommendation
		File recommendations3 = new File(path + "mail/img/recommendation/inviteFriends3.png");
		File recommendations5 = new File(path + "mail/img/recommendation/inviteFriends5.png");
		File recommendations10 = new File(path + "mail/img/recommendation/inviteFriends10.png");
		File recommendations25 = new File(path + "mail/img/recommendation/inviteFriends25.png");
				
		allBadges.add(new BagesData(recommendations3.getName(), FileUtils.readFileToByteArray(recommendations3), "image/png", "3_recommendations", "3 Amici Invitati"));
		allBadges.add(new BagesData(recommendations5.getName(), FileUtils.readFileToByteArray(recommendations5), "image/png", "5_recommendations", "5 Amici Invitati"));
		allBadges.add(new BagesData(recommendations10.getName(), FileUtils.readFileToByteArray(recommendations10), "image/png", "10_recommendations", "10 Amici Invitati"));
		allBadges.add(new BagesData(recommendations25.getName(), FileUtils.readFileToByteArray(recommendations25), "image/png", "25_recommendations", "25 Amici Invitati"));
				
		// files for public transport
		File publicTrans5 = new File(path + "mail/img/public_transport/publicTransportAficionado5.png");
		File publicTrans10 = new File(path + "mail/img/public_transport/publicTransportAficionado10.png");
		File publicTrans25 = new File(path + "mail/img/public_transport/publicTransportAficionado25.png");
		File publicTrans50 = new File(path + "mail/img/public_transport/publicTransportAficionado50.png");
		File publicTrans100 = new File(path + "mail/img/public_transport/publicTransportAficionado100.png");
		
		allBadges.add(new BagesData(publicTrans5.getName(), FileUtils.readFileToByteArray(publicTrans5), "image/png", "5_pt_trip", "5 Viaggi Mezzi Pubblici"));
		allBadges.add(new BagesData(publicTrans10.getName(), FileUtils.readFileToByteArray(publicTrans10), "image/png", "10_pt_trip", "10 Viaggi Mezzi Pubblici"));
		allBadges.add(new BagesData(publicTrans25.getName(), FileUtils.readFileToByteArray(publicTrans25), "image/png", "25_pt_trip", "25 Viaggi Mezzi Pubblici"));
		allBadges.add(new BagesData(publicTrans50.getName(), FileUtils.readFileToByteArray(publicTrans50), "image/png", "50_pt_trip", "50 Viaggi Mezzi Pubblici"));
		allBadges.add(new BagesData(publicTrans100.getName(), FileUtils.readFileToByteArray(publicTrans100), "image/png", "100_pt_trip", "100 Viaggi Mezzi Pubblici"));
		
		// files for zero impact
		File zeroImpact1 = new File(path + "mail/img/zero_impact/zeroImpact1.png");
		File zeroImpact5 = new File(path + "mail/img/zero_impact/zeroImpact5.png");
		File zeroImpact10 = new File(path + "mail/img/zero_impact/zeroImpact10.png");
		File zeroImpact25 = new File(path + "mail/img/zero_impact/zeroImpact25.png");
		File zeroImpact50 = new File(path + "mail/img/zero_impact/zeroImpact50.png");
				
		allBadges.add(new BagesData(zeroImpact1.getName(), FileUtils.readFileToByteArray(zeroImpact1), "image/png", "1_zero_impact_trip", "1 Viaggio Impatto Zero"));
		allBadges.add(new BagesData(zeroImpact5.getName(), FileUtils.readFileToByteArray(zeroImpact5), "image/png", "5_zero_impact_trip", "5 Viaggi Impatto Zero"));
		allBadges.add(new BagesData(zeroImpact10.getName(), FileUtils.readFileToByteArray(zeroImpact10), "image/png", "10_zero_impact_trip", "10 Viaggi Impatto Zero"));
		allBadges.add(new BagesData(zeroImpact25.getName(), FileUtils.readFileToByteArray(zeroImpact25), "image/png", "25_zero_impact_trip", "25 Viaggi Impatto Zero"));
		allBadges.add(new BagesData(zeroImpact50.getName(), FileUtils.readFileToByteArray(zeroImpact50), "image/png", "50_zero_impact_trip", "50 Viaggi Impatto Zero"));		
		
		// files for leaderboard top 3
		File firstOfWeek = new File(path + "mail/img/leaderboard/leaderboard1.png");
		File secondOfWeek = new File(path + "mail/img/leaderboard/leaderboard2.png");
		File thirdOfWeek = new File(path + "mail/img/leaderboard/leaderboard3.png");
						
		allBadges.add(new BagesData(firstOfWeek.getName(), FileUtils.readFileToByteArray(firstOfWeek), "image/png", "1st_of_the_week", "Primo della settimana"));
		allBadges.add(new BagesData(secondOfWeek.getName(), FileUtils.readFileToByteArray(secondOfWeek), "image/png", "2nd_of_the_week", "Secondo della settimana"));
		allBadges.add(new BagesData(thirdOfWeek.getName(), FileUtils.readFileToByteArray(thirdOfWeek), "image/png", "3rd_of_the_week", "Terzo della settimana"));
		
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
	
	
	/**
	 * Method used to retrieve the state of a specific user and to send the find data via mail
	 * @param urlWS: url of the ws
	 * @return state ArrayList
	 * @throws InterruptedException 
	 */
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
	}
	
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
	private ArrayList<State> chekState(String result){
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
	}
	
	private String cleanStringFieldScore(String fieldString){
		String field = fieldString.trim();
		Float score_num_f = Float.valueOf(field);
		int score_num_i = score_num_f.intValue();
		String cleanedScore = Integer.toString(score_num_i);
		return cleanedScore;
	}
	
	/**
	 * Method orderState: used to order the state array
	 * @param toOrder
	 * @return
	 */
	private ArrayList<State> orderState(ArrayList<State> toOrder){
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
	}
	
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
				String areChallenges = weekConfValues[2];
				String arePrizes = weekConfValues[3];
				String arePrizesLast= weekConfValues[4];
				String actualWeek = weekConfValues[5];
				logger.debug(String.format("Week conf file: week num %s, theme %s, challenges %s, prizes %s, prizes last %s, actual week %s", weekNum, weekTheme, areChallenges, arePrizes, arePrizesLast, actualWeek));
				// value conversion from string to boolean
				Boolean areChall = (areChallenges.compareTo("Y") == 0) ? true : false;
				Boolean arePriz = (arePrizes.compareTo("Y") == 0) ? true : false;
				Boolean arePrizLast = (arePrizesLast.compareTo("Y") == 0) ? true : false;
				Boolean isActual = (actualWeek.compareTo("Y") == 0) ? true : false;
				WeekConfData wconf = new WeekConfData(weekNum, weekTheme, areChall, arePriz, arePrizLast, isActual);
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
