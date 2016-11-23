package eu.trentorise.smartcampus.gamification_web.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import eu.trentorise.smartcampus.gamification_web.models.BagesData;
import eu.trentorise.smartcampus.gamification_web.models.MailImage;
import eu.trentorise.smartcampus.gamification_web.models.Summary;
import eu.trentorise.smartcampus.gamification_web.models.WeekPrizeData;
import eu.trentorise.smartcampus.gamification_web.models.WeekWinnersData;
import eu.trentorise.smartcampus.gamification_web.models.status.ChallengesData;

@Service
public class EmailService {

    @Autowired 
    private JavaMailSender mailSender;
    
    @Autowired 
    private TemplateEngine templateEngine;
    
    @Autowired
    @Value("${gamification.mail.from}")
    private String mailFrom;
    
    private static final Logger logger = Logger.getLogger(EmailService.class);

    /* 
     * Send HTML mail (simple) 
     */
    public void sendSimpleMail(
            final String recipientName, final String recipientEmail, final Locale locale) 
            throws MessagingException {
        
        // Prepare the evaluation context
        final Context ctx = new Context(locale);
        ctx.setVariable("name", recipientName);
        ctx.setVariable("subscriptionDate", new Date());
        ctx.setVariable("hobbies", Arrays.asList("Cinema", "Sports", "Music"));
        
        // Prepare message using a Spring helper
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
        message.setSubject("Example HTML email (simple)");
        message.setFrom(mailFrom);
        message.setTo(recipientEmail);

        // Create the HTML body using Thymeleaf
        final String htmlContent = this.templateEngine.process("email-simple.html", ctx);
        message.setText(htmlContent, true /* isHtml */);
        
        // Send email
        this.mailSender.send(mimeMessage);
        logger.debug("Mail Sent - OK");

    }

    
    /* 
     * Send HTML mail with attachment. 
     */
    public void sendMailWithAttachment(
            final String recipientName, final String recipientEmail, final String attachmentFileName, 
            final byte[] attachmentBytes, final String attachmentContentType, final Locale locale) 
            throws MessagingException {
        
        // Prepare the evaluation context
        final Context ctx = new Context(locale);
        ctx.setVariable("name", recipientName);
        ctx.setVariable("subscriptionDate", new Date());
        ctx.setVariable("hobbies", Arrays.asList("Cinema", "Sports", "Music"));
        
        // Prepare message using a Spring helper
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper message = 
                new MimeMessageHelper(mimeMessage, true /* multipart */, "UTF-8");
        message.setSubject("Example HTML email with attachment");
        message.setFrom(mailFrom);
        message.setTo(recipientEmail);

        // Create the HTML body using Thymeleaf
        final String htmlContent = this.templateEngine.process("email-withattachment.html", ctx);
        message.setText(htmlContent, true /* isHtml */);
        
        // Add the attachment
        final InputStreamSource attachmentSource = new ByteArrayResource(attachmentBytes);
        message.addAttachment(
                attachmentFileName, attachmentSource, attachmentContentType);
        
        // Send mail
        this.mailSender.send(mimeMessage);
        
    }

    /* 
     * Send HTML mail with inline image
     */
    public void sendMailWithInline(
            final String recipientName, final String recipientEmail, final String imageResourceName, 
            final byte[] imageBytes, final String imageContentType, final Locale locale)
            throws MessagingException {
        
        // Prepare the evaluation context
        final Context ctx = new Context(locale);
        ctx.setVariable("name", recipientName);
        ctx.setVariable("subscriptionDate", new Date());
        ctx.setVariable("hobbies", Arrays.asList("Cinema", "Sports", "Music"));
        ctx.setVariable("imageResourceName", imageResourceName); // so that we can reference it from HTML
        
        // Prepare message using a Spring helper
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper message = 
                new MimeMessageHelper(mimeMessage, true /* multipart */, "UTF-8");
        message.setSubject("Example HTML email with inline image");
        message.setFrom(mailFrom);
        message.setTo(recipientEmail);

        // Create the HTML body using Thymeleaf
        final String htmlContent = this.templateEngine.process("email-inlineimage.html", ctx);
        message.setText(htmlContent, true /* isHtml */);
        
        // Add the inline image, referenced from the HTML code as "cid:${imageResourceName}"
        final InputStreamSource imageSource = new ByteArrayResource(imageBytes);
        message.addInline(imageResourceName, imageSource, imageContentType);
        
        // Send mail
        this.mailSender.send(mimeMessage);
        
    }
    
    public void sendMailGamification(
    		final String firstGameMail, final String recipientName, final String point_green, final String point_health, final String point_pr, final String badge,
            final String position, final String week_number, final String week_theme, final String last_week_number, final Boolean are_challenges, final Boolean are_prizes, final Boolean are_last_week_prizes, 
            final ArrayList<BagesData> badges,
            final List<ChallengesData> challenges,
            final List<ChallengesData> last_week_challenges,
            final List<WeekPrizeData> prizes,
            final List<WeekWinnersData> winners,
            final List<MailImage> standardImages,
            final String recipientEmail, final String greengame_url, String surveyLink, String unsubscribtionLink, final Locale locale)
            throws MessagingException {
        
    	logger.debug(String.format("Gamification Mail Prepare for %s - OK", recipientName));
    	
    	// Correct the winners:
    	List<WeekWinnersData> last_week_winners = new ArrayList<WeekWinnersData>();
    	for(int i = 0; i < winners.size(); i++){
    		if(winners.get(i).getWeekNum().compareTo(last_week_number) == 0){
    			last_week_winners.add(winners.get(i));
    		}
    	}
    	// Correct the win challenges
    	List<ChallengesData> winChallenges = new ArrayList<ChallengesData>();
    	if(last_week_challenges != null){
	    	for(int i = 0; i < last_week_challenges.size(); i++){
	    		if(last_week_challenges.get(i).getSuccess()){
	    			winChallenges.add(last_week_challenges.get(i));
	    		}
	    	}
    	}
    	
    	String challengesStartingTime = "";
    	String challengesEndingTime = "";
    	String challengesStartingDate = "";
    	String challengesEndingDate = "";
    	Date ch_startTime = null;
    	Date ch_endTime = null;
    	SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    	if(challenges != null && challenges.size() > 0){
    		long startTime = challenges.get(0).getStartDate();
    		long endTime = challenges.get(0).getEndDate();
    		ch_startTime = new Date(startTime);
    		ch_endTime = new Date(endTime);
    		String challStartingAll = dt.format(ch_startTime);
    		String challEndingAll = dt.format(ch_endTime);
    		String[] completeStart = challStartingAll.split(" ");
    		String[] completeEnd = challEndingAll.split(" ");
    		challengesStartingDate = completeStart[0];
    		challengesStartingTime = completeStart[1];
    		challengesEndingDate = completeEnd[0];
    		challengesEndingTime = completeEnd[1];
    	}
    	
    	boolean isLastWeek = false;
    	//if(week_theme.compareTo("Last") == 0){
    	if(week_theme.compareTo("Park & Ride") == 0){
    		isLastWeek = true;
    	}
    	
        // Prepare the evaluation context
        final Context ctx = new Context(locale);
        ctx.setVariable("name", recipientName);
        ctx.setVariable("g_point", point_green);
        ctx.setVariable("h_point", point_health);
        ctx.setVariable("p_point", point_pr);
        ctx.setVariable("n_badge", badge);
        ctx.setVariable("n_badges", badges);
        ctx.setVariable("next_week_num", week_number);
        ctx.setVariable("next_week_theme", week_theme);
        ctx.setVariable("show_last_week", isLastWeek);
        ctx.setVariable("week_num", last_week_number);
        ctx.setVariable("n_challenges", challenges);
        ctx.setVariable("n_lw_challenges", last_week_challenges);
        ctx.setVariable("n_lw_win_challenges", winChallenges);
        ctx.setVariable("chs_start_date", challengesStartingDate);
        ctx.setVariable("chs_start_time", challengesStartingTime);
        ctx.setVariable("chs_end_date", challengesEndingDate);
        ctx.setVariable("chs_end_time", challengesEndingTime);
        ctx.setVariable("n_prizes", prizes);
        ctx.setVariable("are_prizes", are_prizes);
        ctx.setVariable("are_prizes_last", are_last_week_prizes);
        ctx.setVariable("are_challenges", are_challenges);
        ctx.setVariable("n_winners", last_week_winners);
        ctx.setVariable("u_position", position);
        ctx.setVariable("greengame_url", greengame_url);
        ctx.setVariable("unsubscribtionLink", unsubscribtionLink);
        ctx.setVariable("surveyLink", surveyLink);
        ctx.setVariable("imageRNFoglie03", standardImages.get(0).getImageName()); // so that we can reference it from HTML
        ctx.setVariable("imageRNFoglie04", standardImages.get(1).getImageName()); // so that we can reference it from HTML
        ctx.setVariable("imageRNGreenScore", standardImages.get(2).getImageName()); // so that we can reference it from HTML
        ctx.setVariable("imageRNHealthScore", standardImages.get(3).getImageName()); // so that we can reference it from HTML
        ctx.setVariable("imageRNPrScore", standardImages.get(4).getImageName()); // so that we can reference it from HTML
        ctx.setVariable("imageResourceName", standardImages.get(5).getImageName()); // so that we can reference it from HTML
        
        // Prepare message using a Spring helper
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper message = 
                new MimeMessageHelper(mimeMessage, true /* multipart */, "UTF-8");
        message.setSubject("Play&Go - Notifica");
        message.setFrom(mailFrom);
        message.setTo(recipientEmail);

        // Create the HTML body using Thymeleaf
        boolean isFirstMail = Boolean.parseBoolean(firstGameMail);
        String htmlContent = "";
        if(isFirstMail){
        	htmlContent = (locale == Locale.ITALIAN) ? this.templateEngine.process("email-gamification2016-startgame-tn.html", ctx) : this.templateEngine.process("email-gamification2016-startgame-tn-eng.html", ctx);
        } else {
        	htmlContent = (locale == Locale.ITALIAN) ? this.templateEngine.process("email-gamification2016-tn.html", ctx) : this.templateEngine.process("email-gamification2016-tn-eng.html", ctx);
        }
        message.setText(htmlContent, true /* isHtml */);
        
        // Add the inline titles image, referenced from the HTML code as "cid:${imageResourceName}"
        final InputStreamSource imageSourceFoglia03 = new ByteArrayResource(standardImages.get(0).getImageByte());
        message.addInline(standardImages.get(0).getImageName(), imageSourceFoglia03, standardImages.get(0).getImageType());
        final InputStreamSource imageSourceFoglia04 = new ByteArrayResource(standardImages.get(1).getImageByte());
        message.addInline(standardImages.get(1).getImageName(), imageSourceFoglia04, standardImages.get(1).getImageType());
        
        // Add the inline score image, referenced from the HTML code as "cid:${imageResourceName}"
        final InputStreamSource imageSourceGreen = new ByteArrayResource(standardImages.get(2).getImageByte());
        message.addInline(standardImages.get(2).getImageName(), imageSourceGreen, standardImages.get(2).getImageType());
        /*final InputStreamSource imageSourceHealth = new ByteArrayResource(standardImages.get(3).getImageByte());
        message.addInline(standardImages.get(3).getImageName(), imageSourceHealth, standardImages.get(3).getImageType());
        final InputStreamSource imageSourcePr = new ByteArrayResource(standardImages.get(4).getImageByte());
        message.addInline(standardImages.get(4).getImageName(), imageSourcePr, standardImages.get(4).getImageType());*/
        
        // Add the inline footer image, referenced from the HTML code as "cid:${imageResourceName}"
        final InputStreamSource imageSourceFooter = new ByteArrayResource(standardImages.get(5).getImageByte());
        message.addInline(standardImages.get(5).getImageName(), imageSourceFooter, standardImages.get(5).getImageType());
        
        if(badges != null){
        	// Add the inline images for badges
	        for(int i = 0; i < badges.size(); i++){
	        	final InputStreamSource tmp = new ByteArrayResource(badges.get(i).getImageByte());
	            message.addInline(badges.get(i).getImageName(), tmp, badges.get(i).getImageType());
	        }
        }
        
        // Send mail
        this.mailSender.send(mimeMessage);
        logger.info(String.format("Gamification Mail Sent to %s - OK", recipientName));
        
    }
    
    public void sendMailGamificationForWinners(
            final String recipientName, final String point_green, final String point_health, final String point_pr, final String badge,
            final String position, final String week_number, final String week_theme, final String last_week_number, final Boolean are_challenges, final Boolean are_prizes, final Boolean are_last_week_prizes, 
            final ArrayList<BagesData> badges,
            final List<ChallengesData> challenges,
            final List<ChallengesData> last_week_challenges,
            final List<WeekPrizeData> prizes,
            final List<WeekWinnersData> winners,
            final List<MailImage> standardImages,
            final String recipientEmail, final String greengame_url, String unsubscribtionLink, final Locale locale)
            throws MessagingException {
        
    	logger.debug(String.format("Gamification Mail Prepare for %s - OK", recipientName));
    	
    	// Correct the winners:
    	List<WeekWinnersData> last_week_winners = new ArrayList<WeekWinnersData>();
    	for(int i = 0; i < winners.size(); i++){
    		if(winners.get(i).getWeekNum().compareTo(last_week_number) == 0){
    			last_week_winners.add(winners.get(i));
    		}
    	}
    	// Correct the win challenges
    	List<ChallengesData> winChallenges = new ArrayList<ChallengesData>();
    	for(int i = 0; i < last_week_challenges.size(); i++){
    		if(last_week_challenges.get(i).getSuccess()){
    			winChallenges.add(last_week_challenges.get(i));
    		}
    	}
    	
    	String challengesStartingTime = "";
    	String challengesEndingTime = "";
    	String challengesStartingDate = "";
    	String challengesEndingDate = "";
    	Date ch_startTime = null;
    	Date ch_endTime = null;
    	SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    	if(challenges != null && challenges.size() > 0){
    		long startTime = challenges.get(0).getStartDate();
    		long endTime = challenges.get(0).getEndDate();
    		ch_startTime = new Date(startTime);
    		ch_endTime = new Date(endTime);
    		String challStartingAll = dt.format(ch_startTime);
    		String challEndingAll = dt.format(ch_endTime);
    		String[] completeStart = challStartingAll.split(" ");
    		String[] completeEnd = challEndingAll.split(" ");
    		challengesStartingDate = completeStart[0];
    		challengesStartingTime = completeStart[1];
    		challengesEndingDate = completeEnd[0];
    		challengesEndingTime = completeEnd[1];
    	}
    	
        // Prepare the evaluation context
        final Context ctx = new Context(locale);
        ctx.setVariable("name", recipientName);
        ctx.setVariable("g_point", point_green);
        ctx.setVariable("h_point", point_health);
        ctx.setVariable("p_point", point_pr);
        ctx.setVariable("n_badge", badge);
        ctx.setVariable("n_badges", badges);
        ctx.setVariable("next_week_num", week_number);
        ctx.setVariable("next_week_theme", week_theme);
        ctx.setVariable("week_num", last_week_number);
        ctx.setVariable("n_challenges", challenges);
        ctx.setVariable("n_lw_challenges", last_week_challenges);
        ctx.setVariable("n_lw_win_challenges", winChallenges);
        ctx.setVariable("chs_start_date", challengesStartingDate);
        ctx.setVariable("chs_start_time", challengesStartingTime);
        ctx.setVariable("chs_end_date", challengesEndingDate);
        ctx.setVariable("chs_end_time", challengesEndingTime);
        ctx.setVariable("n_prizes", prizes);
        ctx.setVariable("are_prizes", are_prizes);
        ctx.setVariable("are_prizes_last", are_last_week_prizes);
        ctx.setVariable("are_challenges", are_challenges);
        ctx.setVariable("n_winners", last_week_winners);
        ctx.setVariable("u_position", position);
        ctx.setVariable("greengame_url", greengame_url);
        ctx.setVariable("unsubscribtionLink", unsubscribtionLink);
        ctx.setVariable("imageRNFoglie03", standardImages.get(0).getImageName()); // so that we can reference it from HTML
        ctx.setVariable("imageRNFoglie04", standardImages.get(1).getImageName()); // so that we can reference it from HTML
        ctx.setVariable("imageRNGreenScore", standardImages.get(2).getImageName()); // so that we can reference it from HTML
        ctx.setVariable("imageRNHealthScore", standardImages.get(3).getImageName()); // so that we can reference it from HTML
        ctx.setVariable("imageRNPrScore", standardImages.get(4).getImageName()); // so that we can reference it from HTML
        ctx.setVariable("imageResourceName", standardImages.get(5).getImageName()); // so that we can reference it from HTML
        
        // Prepare message using a Spring helper
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper message = 
                new MimeMessageHelper(mimeMessage, true /* multipart */, "UTF-8");
        message.setSubject("Play&Go - Notifica"); //Vincitori
        message.setFrom(mailFrom);
        message.setTo(recipientEmail);

        // Create the HTML body using Thymeleaf
        final String htmlContent = (locale == Locale.ITALIAN) ? this.templateEngine.process("email-gamification2016-winners-tn.html", ctx) : this.templateEngine.process("email-gamification2016-winners-tn-eng.html", ctx);
        message.setText(htmlContent, true /* isHtml */);
        
        // Add the inline titles image, referenced from the HTML code as "cid:${imageResourceName}"
        final InputStreamSource imageSourceFoglia03 = new ByteArrayResource(standardImages.get(0).getImageByte());
        message.addInline(standardImages.get(0).getImageName(), imageSourceFoglia03, standardImages.get(0).getImageType());
        final InputStreamSource imageSourceFoglia04 = new ByteArrayResource(standardImages.get(1).getImageByte());
        message.addInline(standardImages.get(1).getImageName(), imageSourceFoglia04, standardImages.get(1).getImageType());
        
        // Add the inline score image, referenced from the HTML code as "cid:${imageResourceName}"
        /*final InputStreamSource imageSourceGreen = new ByteArrayResource(standardImages.get(2).getImageByte());
        message.addInline(standardImages.get(2).getImageName(), imageSourceGreen, standardImages.get(2).getImageType());*/
        /*final InputStreamSource imageSourceHealth = new ByteArrayResource(standardImages.get(3).getImageByte());
        message.addInline(standardImages.get(3).getImageName(), imageSourceHealth, standardImages.get(3).getImageType());
        final InputStreamSource imageSourcePr = new ByteArrayResource(standardImages.get(4).getImageByte());
        message.addInline(standardImages.get(4).getImageName(), imageSourcePr, standardImages.get(4).getImageType());*/
        
        // Add the inline footer image, referenced from the HTML code as "cid:${imageResourceName}"
        final InputStreamSource imageSourceFooter = new ByteArrayResource(standardImages.get(5).getImageByte());
        message.addInline(standardImages.get(5).getImageName(), imageSourceFooter, standardImages.get(5).getImageType());
        
        if(badges != null){
        	// Add the inline images for badges
	        for(int i = 0; i < badges.size(); i++){
	        	final InputStreamSource tmp = new ByteArrayResource(badges.get(i).getImageByte());
	            message.addInline(badges.get(i).getImageName(), tmp, badges.get(i).getImageType());
	        }
        }
        
        // Send mail
        this.mailSender.send(mimeMessage);
        logger.info(String.format("Gamification Mail Sent to %s - OK", recipientName));
        
    }
    
    public void sendMailSummary(
            final String recipientName, final String point_green, final String point_health, final String point_pr, 
            final ArrayList<Summary> summary,
            final ArrayList<MailImage> standardImages,
            final String recipientEmail, final Locale locale)
            throws MessagingException {
        
    	logger.debug(String.format("Gamification Mail Prepare for %s - OK", recipientName));
    	
        // Prepare the evaluation context
        final Context ctx = new Context(locale);
        ctx.setVariable("name", recipientName);
        ctx.setVariable("g_point", point_green);
        ctx.setVariable("h_point", point_health);
        ctx.setVariable("p_point", point_pr);
        ctx.setVariable("n_summ", summary);
        ctx.setVariable("imageRNFoglie03", standardImages.get(0).getImageName()); // so that we can reference it from HTML
        ctx.setVariable("imageRNFoglie04", standardImages.get(1).getImageName()); // so that we can reference it from HTML
        ctx.setVariable("imageRNGreenScore", standardImages.get(2).getImageName()); // so that we can reference it from HTML
        ctx.setVariable("imageRNHealthScore", standardImages.get(3).getImageName()); // so that we can reference it from HTML
        ctx.setVariable("imageRNPrScore", standardImages.get(4).getImageName()); // so that we can reference it from HTML
        ctx.setVariable("imageResourceName", standardImages.get(5).getImageName()); // so that we can reference it from HTML
        
        // Prepare message using a Spring helper
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper message = 
                new MimeMessageHelper(mimeMessage, true /* multipart */, "UTF-8");
        message.setSubject("Play&Go - Riepilogo");
        message.setFrom(mailFrom);
        message.setTo(recipientEmail);

        // Create the HTML body using Thymeleaf
        final String htmlContent = this.templateEngine.process("email-gamification-summary.html", ctx);
        message.setText(htmlContent, true /* isHtml */);
        
        // Add the inline titles image, referenced from the HTML code as "cid:${imageResourceName}"
        final InputStreamSource imageSourceFoglia03 = new ByteArrayResource(standardImages.get(0).getImageByte());
        message.addInline(standardImages.get(0).getImageName(), imageSourceFoglia03, standardImages.get(0).getImageType());
        final InputStreamSource imageSourceFoglia04 = new ByteArrayResource(standardImages.get(1).getImageByte());
        message.addInline(standardImages.get(1).getImageName(), imageSourceFoglia04, standardImages.get(1).getImageType());
        
        // Add the inline score image, referenced from the HTML code as "cid:${imageResourceName}"
        final InputStreamSource imageSourceGreen = new ByteArrayResource(standardImages.get(2).getImageByte());
        message.addInline(standardImages.get(2).getImageName(), imageSourceGreen, standardImages.get(2).getImageType());
        final InputStreamSource imageSourceHealth = new ByteArrayResource(standardImages.get(3).getImageByte());
        message.addInline(standardImages.get(3).getImageName(), imageSourceHealth, standardImages.get(3).getImageType());
        final InputStreamSource imageSourcePr = new ByteArrayResource(standardImages.get(4).getImageByte());
        message.addInline(standardImages.get(4).getImageName(), imageSourcePr, standardImages.get(4).getImageType());
        
        // Add the inline footer image, referenced from the HTML code as "cid:${imageResourceName}"
        final InputStreamSource imageSourceFooter = new ByteArrayResource(standardImages.get(5).getImageByte());
        message.addInline(standardImages.get(5).getImageName(), imageSourceFooter, standardImages.get(5).getImageType());
        
        
        // Send mail
        this.mailSender.send(mimeMessage);
        logger.info(String.format("Gamification Mail Sent to %s - OK", recipientName));
        
    }


}