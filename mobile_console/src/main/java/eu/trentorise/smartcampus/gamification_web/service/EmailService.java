package eu.trentorise.smartcampus.gamification_web.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import eu.trentorise.smartcampus.gamification_web.models.Notification;
import eu.trentorise.smartcampus.gamification_web.models.Summary;

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
        logger.error("Mail Sent - OK");

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
            final String recipientName, final String point_green, final String point_health, final String point_pr, final String badge,
            final String position, 
            final ArrayList<BagesData> badges,
            final ArrayList<MailImage> standardImages,
            final String recipientEmail, final Locale locale)
            throws MessagingException {
        
    	logger.error(String.format("Gamification Mail Prepare for %s - OK", recipientName));
    	
        // Prepare the evaluation context
        final Context ctx = new Context(locale);
        ctx.setVariable("name", recipientName);
        ctx.setVariable("g_point", point_green);
        ctx.setVariable("h_point", point_health);
        ctx.setVariable("p_point", point_pr);
        ctx.setVariable("n_badge", badge);
        ctx.setVariable("n_badges", badges);
        ctx.setVariable("u_position", position);
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
        message.setSubject("Gamification - Notifica");
        message.setFrom(mailFrom);
        message.setTo(recipientEmail);

        // Create the HTML body using Thymeleaf
        final String htmlContent = this.templateEngine.process("email-gamification.html", ctx);
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
        
        if(badges != null){
        	// Add the inline images for badges
	        for(int i = 0; i < badges.size(); i++){
	        	final InputStreamSource tmp = new ByteArrayResource(badges.get(i).getImageByte());
	            message.addInline(badges.get(i).getImageName(), tmp, badges.get(i).getImageType());
	        }
        }
        
        // Send mail
        this.mailSender.send(mimeMessage);
        logger.error(String.format("Gamification Mail Sent to %s - OK", recipientName));
        
    }
    
    public void sendMailSummary(
            final String recipientName, final String point_green, final String point_health, final String point_pr, 
            final ArrayList<Summary> summary,
            final ArrayList<MailImage> standardImages,
            final String recipientEmail, final Locale locale)
            throws MessagingException {
        
    	logger.error(String.format("Gamification Mail Prepare for %s - OK", recipientName));
    	
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
        message.setSubject("Gamification - Notifica");
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
        logger.error(String.format("Gamification Mail Sent to %s - OK", recipientName));
        
    }


}