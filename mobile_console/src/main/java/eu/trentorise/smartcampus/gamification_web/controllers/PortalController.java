package eu.trentorise.smartcampus.gamification_web.controllers;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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
import java.util.jar.Attributes;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import eu.trentorise.smartcampus.gamification_web.service.EmailService;

import eu.trentorise.smartcampus.aac.AACException;
import eu.trentorise.smartcampus.gamification_web.models.BagesData;
import eu.trentorise.smartcampus.gamification_web.models.MailImage;
import eu.trentorise.smartcampus.gamification_web.models.Notification;
import eu.trentorise.smartcampus.gamification_web.models.State;
import eu.trentorise.smartcampus.gamification_web.models.Summary;
//import eu.trentorise.smartcampus.gamification_web.models.SubjectDn;
import eu.trentorise.smartcampus.gamification_web.models.UserCS;
import eu.trentorise.smartcampus.gamification_web.repository.AuthPlayer;
import eu.trentorise.smartcampus.gamification_web.repository.AuthPlayerProd;
import eu.trentorise.smartcampus.gamification_web.repository.AuthPlayerProdRepositoryDao;
import eu.trentorise.smartcampus.gamification_web.repository.AuthPlayerRepositoryDao;
import eu.trentorise.smartcampus.gamification_web.repository.Player;
import eu.trentorise.smartcampus.gamification_web.repository.PlayerProd;
import eu.trentorise.smartcampus.gamification_web.repository.PlayerProdRepositoryDao;
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
	@Value("${smartcampus.isTest}")
	private String isTest;
	
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
    @Value("${gamification.useAuthorizationTable}")
    private String authorizationTable;
    
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
			model.put("user_name", user.getName());
			model.put("user_surname", user.getSurname());
			logger.info(String
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
					logger.info(String.format("Player to add: mail %s.", attribute_mail));
					if(attribute_mail != null){
						if(authorizationTable.compareTo("true") == 0){
							AuthPlayer auth_p = authPlayerRepositoryDao.findByMail(attribute_mail);
							if(auth_p != null){
								logger.info(String.format("Add player: authorised %s.", auth_p.toJSONString()));
								Player new_p = new Player(user.getUserId(), user.getUserId(), user.getName(), user.getSurname(), auth_p.getNikName(), auth_p.getMail(), null);
								playerRepositoryDao.save(new_p);
								logger.info(String.format("Add player: created player %s.", new_p.toJSONString()));
							} else {
								return new ModelAndView("redirect:/logout");	// user not allowed - logout
							}
						} else {
							// case of no authentication table and user not in user table: I add the user
							//nick = generateNick(user.getName(), user.getSurname(), user.getUserId());
							Player new_p = new Player(user.getUserId(), user.getUserId(), user.getName(), user.getSurname(), nick, attribute_mail, null);
							playerRepositoryDao.save(new_p);
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
								logger.info(String.format("Add player: created player %s.", new_p.toJSONString()));
							} else {
								return new ModelAndView("redirect:/logout");	// user not allowed - logout
							}
						} else {
							// case of no authentication table and user not in user table: I add the user
							//nick = generateNick(user.getName(), user.getSurname(), user.getUserId());
							PlayerProd new_p = new PlayerProd(user.getUserId(), user.getUserId(), user.getName(), user.getSurname(), nick, attribute_mail, null);
							playerProdRepositoryDao.save(new_p);
							logger.info(String.format("Add new player: created player %s.", new_p.toJSONString()));
						}
					}
				}
			}
			
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
				"profile.basicprofile.me,profile.accountprofile.me", null);	//"smartcampus.profile.basicprofile.me,smartcampus.profile.accountprofile.me"
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
	
	@RequestMapping(method = RequestMethod.GET, value = "/cookie_licence")
	public ModelAndView preSecureCookieLicence(HttpServletRequest request) {
		//String redirectUri = mainURL + "/check";
		logger.error(String.format("I am in cookie licence info page"));
		ModelAndView model = new ModelAndView();
		model.setViewName("cookie_licence");
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
	//@Scheduled(fixedRate = 2*60*1000) // Repeat once a minute
	//@Scheduled(cron="0 0 0/2 * * *") // Repeat every hours at 00:00 min/sec
	@Scheduled(cron="0 0 8 * * *") 		// Repeat every day at 8 AM from 1 to 8 dec
	public synchronized void checkNotification() throws IOException{
		
		ArrayList<Summary> summaryMail = new ArrayList<Summary>();
		
		long millis = System.currentTimeMillis() - (24*60*60*1000);	// Delta in millis of 24 hours //long millis = 1415660400000L; //(for test)
		String timestamp = "?timestamp=" + millis;
		//String timestamp = "";
		
		URL resource = getClass().getResource("/");
		String path = resource.getPath();
		logger.error(String.format("class path : %s", path));
		
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
		
		logger.error(String.format("Image data: path - %s length: %d", greenScore.getAbsolutePath(), greenScore.length()));
		
		//ArrayList<BagesData> allBadgeTest = getAllBadges(path);
		//try {
		//	this.emailService.sendMailGamification("NikName", "43", "32", "112", null, null, allBadgeTest, standardImages ,mailTo, Locale.ITALIAN);
		//} catch (MessagingException e1) {
		//	e1.printStackTrace();
		//}
		
			// New method
			logger.error(String.format("Check Notification task. Cycle - %d", i++));
			
			if(isTest.compareTo("true") == 0){
				Iterable<Player> iter = playerRepositoryDao.findAll();
				for(Player p: iter){
					logger.error(String.format("Profile finded  %s", p.getNikName()));
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e1) {
						logger.error(String.format("Errore in attesa thread: %s", e1.getMessage()));
					}
					
					ArrayList<State> states = null;
					ArrayList<Notification> notifications = null;
					ArrayList<BagesData> someBadge = null;
					
					try {
					// WS State Invocation
						String urlWSState = "state/" + gameName + "/" + p.getSocialId();
						states = getState(urlWSState);
						
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
					
					if(mailSend.compareTo("true") == 0){
						try {
							if(notifications != null){
								if(states != null && states.size() > 0){
									this.emailService.sendMailGamification(playerName, states.get(0).getScore(), states.get(1).getScore(), states.get(2).getScore(), null, null, someBadge, standardImages, mailto, mailRedirectUrl, Locale.ITALIAN);
								} else {
									this.emailService.sendMailGamification(playerName, "0", "0", "0", null, null, someBadge, standardImages, mailto, mailRedirectUrl, Locale.ITALIAN);
								}
							} else {
								if(states != null  && states.size() > 0){
									this.emailService.sendMailGamification(playerName, states.get(0).getScore(), states.get(1).getScore(), states.get(2).getScore(), null, null, null, standardImages, mailto, mailRedirectUrl, Locale.ITALIAN);
								} else {
									this.emailService.sendMailGamification(playerName, "0", "0", "0", null, null, null, standardImages, mailto, mailRedirectUrl, Locale.ITALIAN);
								}
							}
						} catch (MessagingException e) {
							logger.error(String.format("Errore invio mail : %s", e.getMessage()));
						}
					} else {
						if(notifications != null){
							if(states != null && states.size() > 0){
								logger.error(String.format("Invio mail a %s con notifica : %s e stato: %s", playerName ,notifications.toString(), states.toString()));
							} else {
								logger.error(String.format("Invio mail a %s con notifica : %s", playerName ,notifications.toString()));
							}
						} else {
							if(states != null  && states.size() > 0){
								logger.error(String.format("Invio mail a %s con stato: %s", playerName , states.toString()));
							} else {
								logger.error(String.format("Invio mail a %s", playerName));
							}
						}
					}
					summaryMail.add(new Summary(p.getName() + " " + p.getSurname() + ": " + p.getNikName(), (states != null) ? states.toString() : "", (notifications != null) ? notifications.toString() : ""));
				}
			} else {
				Iterable<PlayerProd> iter = playerProdRepositoryDao.findAll();
				for(PlayerProd p: iter){
					logger.error(String.format("Profile finded  %s", p.getNikName()));
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e1) {
						logger.error(String.format("Errore in attesa thread: %s", e1.getMessage()));
					}
					
					ArrayList<State> states = null;
					ArrayList<Notification> notifications = null;
					ArrayList<BagesData> someBadge = null;
					
					try {
					// WS State Invocation
						String urlWSState = "state/" + gameName + "/" + p.getSocialId();
						states = getState(urlWSState);
						
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
					
					if(mailSend.compareTo("true") == 0){
						try {
							if(notifications != null){
								if(states != null  && states.size() > 0){
									this.emailService.sendMailGamification(playerName, states.get(0).getScore(), states.get(1).getScore(), states.get(2).getScore(), null, null, someBadge, standardImages, mailto, mailRedirectUrl, Locale.ITALIAN);
								} else {
									this.emailService.sendMailGamification(playerName, "0", "0", "0", null, null, someBadge, standardImages, mailto, mailRedirectUrl, Locale.ITALIAN);
								}
							} else {
								if(states != null  && states.size() > 0){
									this.emailService.sendMailGamification(playerName, states.get(0).getScore(), states.get(1).getScore(), states.get(2).getScore(), null, null, null, standardImages, mailto, mailRedirectUrl, Locale.ITALIAN);
								} else {
									this.emailService.sendMailGamification(playerName, "0", "0", "0", null, null, null, standardImages, mailto, mailRedirectUrl, Locale.ITALIAN);
								}
							}
						} catch (MessagingException e) {
							logger.error(String.format("Errore invio mail : %s", e.getMessage()));
						}
					} else {
						if(notifications != null){
							if(states != null && states.size() > 0){
								logger.error(String.format("Invio mail a %s con notifica : %s e stato: %s", playerName ,notifications.toString(), states.toString()));
							} else {
								logger.error(String.format("Invio mail a %s con notifica : %s", playerName ,notifications.toString()));
							}
						} else {
							if(states != null && states.size() > 0){
								logger.error(String.format("Invio mail a %s con stato: %s", playerName , states.toString()));
							} else {
								logger.error(String.format("Invio mail a %s", playerName));
							}
						}
					}
					summaryMail.add(new Summary(p.getName() + " " + p.getSurname() + ": " + p.getNikName(), (states != null) ? states.toString() : "", (notifications != null) ? notifications.toString() : ""));
				}
			}
			
			// Send summary mail
			try {
				this.emailService.sendMailSummary("Mattia", "0", "0", "0", summaryMail, standardImages, mailTo, Locale.ITALIAN);
			} catch (MessagingException e) {
				logger.error(String.format("Errore invio mail notifica : %s", e.getMessage()));
			}
		//}
	}
	
	private ArrayList<BagesData> getAllBadges(String path) throws IOException {
		ArrayList<BagesData> allBadges = new ArrayList<BagesData>();
		// files for green badges
		File greenKing = new File(path + "mail/img/green/greenKingWeek.png");
		File green10 = new File(path + "mail/img/green/greenLeaves10.png");
		File green50 = new File(path + "mail/img/green/greenLeaves50.png");
		File green100 = new File(path + "mail/img/green/greenLeaves100.png");
		File green250 = new File(path + "mail/img/green/greenLeaves250.png");
		File green500 = new File(path + "mail/img/green/greenLeaves500.png");
		File green1000 = new File(path + "mail/img/green/greenLeaves1000.png");
		File greenBronze = new File(path + "mail/img/green/greenBronzeMedal.png");
		File greenSilver = new File(path + "mail/img/green/greenSilverMedal.png");
		File greenGold = new File(path + "mail/img/green/greenGoldMedal.png");
		
		allBadges.add(new BagesData(greenKing.getName(), FileUtils.readFileToByteArray(greenKing), "image/png", "king-week-green", "Re della Settimana - Green"));
		allBadges.add(new BagesData(green10.getName(), FileUtils.readFileToByteArray(green10), "image/png", "10-point-green", "10 Punti Green"));
		allBadges.add(new BagesData(green50.getName(), FileUtils.readFileToByteArray(green50), "image/png", "50-point-green", "50 Punti Green"));
		allBadges.add(new BagesData(green100.getName(), FileUtils.readFileToByteArray(green100), "image/png", "100-point-green", "100 Punti Green"));
		allBadges.add(new BagesData(green250.getName(), FileUtils.readFileToByteArray(green250), "image/png", "250-point-green", "250 Punti Green"));
		allBadges.add(new BagesData(green500.getName(), FileUtils.readFileToByteArray(green500), "image/png", "500-point-green", "500 Punti Green"));
		allBadges.add(new BagesData(green1000.getName(), FileUtils.readFileToByteArray(green1000), "image/png", "1000-point-green", "1000 Punti Green"));
		allBadges.add(new BagesData(greenBronze.getName(), FileUtils.readFileToByteArray(greenBronze), "image/png", "bronze-medal-green", "Medaglia di Bronzo - Green"));
		allBadges.add(new BagesData(greenSilver.getName(), FileUtils.readFileToByteArray(greenSilver), "image/png", "silver-medal-green", "Medaglia d'Argento - Green"));
		allBadges.add(new BagesData(greenGold.getName(), FileUtils.readFileToByteArray(greenGold), "image/png", "gold-medal-green", "Medaglia d'Oro - Green"));
				
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
		
		allBadges.add(new BagesData(healthKing.getName(), FileUtils.readFileToByteArray(healthKing), "image/png", "king-week-health", "Re della Settimana - Salute"));
		allBadges.add(new BagesData(health10.getName(), FileUtils.readFileToByteArray(health10), "image/png", "10-point-health", "10 Punti Salute"));
		allBadges.add(new BagesData(health25.getName(), FileUtils.readFileToByteArray(health25), "image/png", "25-point-health", "25 Punti Salute"));
		allBadges.add(new BagesData(health50.getName(), FileUtils.readFileToByteArray(health50), "image/png", "50-point-health", "50 Punti Salute"));
		allBadges.add(new BagesData(health100.getName(), FileUtils.readFileToByteArray(health100), "image/png", "100-point-health", "100 Punti Salute"));
		allBadges.add(new BagesData(health200.getName(), FileUtils.readFileToByteArray(health200), "image/png", "200-point-health", "200 Punti Salute"));
		allBadges.add(new BagesData(healthBronze.getName(), FileUtils.readFileToByteArray(healthBronze), "image/png", "bronze-medal-health", "Medaglia di Bronzo - Salute"));
		allBadges.add(new BagesData(healthSilver.getName(), FileUtils.readFileToByteArray(healthSilver), "image/png", "silver-medal-health", "Medaglia d'Argento - Salute"));
		allBadges.add(new BagesData(healthGold.getName(), FileUtils.readFileToByteArray(healthGold), "image/png", "gold-medal-health", "Medaglia d'Oro - Salute"));
		
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
		
		allBadges.add(new BagesData(prKing.getName(), FileUtils.readFileToByteArray(prKing), "image/png", "king-week-pr", "Re della Settimana - Park&Ride"));
		allBadges.add(new BagesData(pr10.getName(), FileUtils.readFileToByteArray(pr10), "image/png", "10-point-pr", "10 Punti Park&Ride"));
		allBadges.add(new BagesData(pr20.getName(), FileUtils.readFileToByteArray(pr20), "image/png", "20-point-pr", "20 Punti Park&Ride"));
		allBadges.add(new BagesData(pr50.getName(), FileUtils.readFileToByteArray(pr50), "image/png", "50-point-pr", "50 Punti Park&Ride"));
		allBadges.add(new BagesData(pr100.getName(), FileUtils.readFileToByteArray(pr100), "image/png", "100-point-pr", "100 Punti Park&Ride"));
		allBadges.add(new BagesData(pr200.getName(), FileUtils.readFileToByteArray(pr200), "image/png", "200-point-pr", "200 Punti Park&Ride"));
		allBadges.add(new BagesData(prBronze.getName(), FileUtils.readFileToByteArray(prBronze), "image/png", "bronze-medal-pr", "Medaglia di Bronzo - Park&Ride"));
		allBadges.add(new BagesData(prSilver.getName(), FileUtils.readFileToByteArray(prSilver), "image/png", "silver-medal-pr", "Medaglia d'Argento - Park&Ride"));
		allBadges.add(new BagesData(prGold.getName(), FileUtils.readFileToByteArray(prGold), "image/png", "gold-medal-pr", "Medaglia d'Oro - Park&Ride"));
		
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
		
		return allBadges;
	}
	
	private ArrayList<BagesData> checkCorrectBadges(ArrayList<BagesData> allB, ArrayList<Notification> notifics) throws IOException{
		ArrayList<BagesData> correctBadges = new ArrayList<BagesData>();
		
		for(int i = 0; i < allB.size(); i++){
			for(int j = 0; j < notifics.size(); j++){
				if(notifics.get(j).getBadge().compareTo(allB.get(i).getTextId()) == 0){
					logger.error(String.format("Notification check notifics: %s, badge :%s", notifics.get(j).getBadge(), allB.get(i).getTextId()));
					correctBadges.add(allB.get(i));
				}
			}
		}
		return correctBadges;
	}
	
	
	/**
	 * Method used to retrieve the state of a specific user and to send the find data via mail
	 * @param urlWS: url of the ws
	 * @return state ArrayList
	 * @throws InterruptedException 
	 */
	private ArrayList<State> getState(String urlWS) throws InterruptedException{
		
		RestTemplate restTemplate = new RestTemplate();
		logger.error("Notification WS GET " + urlWS);
		
		String result = "";
		try {
			result = restTemplate.getForObject(gamificationUrl + urlWS, String.class); //I pass the timestamp of the scheduled start time
		} catch (Exception ex){
			logger.error(String.format("Exception in proxyController get ws. Method: %s. Details: %s", urlWS, ex.getMessage()));
		}
		
		ArrayList<State> states = null;
		
		if(result != null && result.compareTo("") != 0){
			logger.error(String.format("State Result Ok: %s", result));
			states = chekState(result);	
			
		} else {
			logger.error(String.format("State Result Fail: %s", result));
		}
		
		return states;
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
		logger.error("Notification WS GET " + urlWS);
		
		String result = "";
		try {
			result = restTemplate.getForObject(gamificationUrl + urlWS + timestamp, String.class); //I pass the timestamp of the scheduled start time
		} catch (Exception ex){
			logger.error(String.format("Exception in proxyController get ws. Method: %s. Details: %s", urlWS, ex.getMessage()));
		}
		
		ArrayList<Notification> notifications = null;
		if(result != null){
			logger.error(String.format("Notification Result Ok: %s", result));
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
		logger.error(String.format("Result from WS: %s", result));
		
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
		logger.error(String.format("Result from WS: %s", result));
		
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
	
	private String cleanField(String[] fieldStrings){
		String field = fieldStrings[1].replace('"', ' ').trim();
		return field;
	}
	
	private String cleanFieldScore(String[] fieldStrings){
		String field = fieldStrings[1].replace('"', ' ').trim();
		
		Float score_num_f = Float.valueOf(field);
		int score_num_i = score_num_f.intValue();
		
		String cleanedScore = Integer.toString(score_num_i);
		return cleanedScore;
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
	
	/**
	 * Method generateNik: used to concatenate name, surname and social id to generate a univoke nik
	 * @param name: name of user
	 * @param surname: surname of user
	 * @param socialId: socialId of user from aac
	 * @return string of the generated nickname
	 */
	private String generateNick(String name, String surname, String socialId){
		String nick_n = "";
		nick_n = name.toLowerCase() + surname.toUpperCase().substring(0,1) + socialId;
		return nick_n;
	}

}
