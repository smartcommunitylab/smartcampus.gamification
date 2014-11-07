package eu.trentorise.smartcampus.gamification_web.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PracticeController {
	
	@Autowired
	@Value("${smartcampus.isTest}")
	private String is_test;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	private static final Logger logger = Logger.getLogger(PracticeController.class);


	
	@RequestMapping(method = RequestMethod.POST, value = "/rest/pdf")
	public @ResponseBody
	String createPdf(HttpServletRequest request, @RequestBody String data) {
		String createdFile = "";
		String path = request.getSession().getServletContext().getRealPath("/pdf/");
		try {
			//logger.error("File path " + path);
			createdFile = create_file(data, path);
		} catch (Exception e) {
			logger.error("Errore in creazione PDF tmp file: " + e.getMessage());
			//e.printStackTrace();
		}
		return createdFile;
	}
	
	//@SuppressWarnings("restriction")
	public static String create_file(String data, String path) throws FileNotFoundException, UnsupportedEncodingException{
		
		byte[] decodedBytes = data.getBytes("ISO-8859-1");
		File dir = new File(path);
		dir.mkdir();
		//File file = new File(path, "scheda_plain_test.pdf");
		File file = null;
		try {
			file = File.createTempFile("scheda_", ".pdf", dir);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		FileOutputStream fop = new FileOutputStream(file);

		try {
			fop.write(decodedBytes);
			fop.flush();
			fop.close();
		} catch (IOException e) {
			logger.error("Errore in scrittura su file pdf tmp: "+ e.getMessage());
			//e.printStackTrace();
		}
		
		return file.getName();
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/rest/pdf")
	public @ResponseBody
	boolean deletePdf(HttpServletRequest request, @RequestParam String filename) {
		boolean deletion = true;
		String path = request.getSession().getServletContext().getRealPath("/pdf/");
		try {
			//logger.error("File path " + path);
			File toDel = new File(path, filename);
			if(!toDel.delete()){
				deletion = false;
				logger.error("Errore nella cancellazione del file pdf tmp " + filename);
			}
		} catch (Exception e) {
			logger.error("Eccezione in cancellazione PDF tmp file: " + e.getMessage());
			//e.printStackTrace();
		}
		return deletion;
	}
	
	// Ws used to check if I am in test or in prod
	@RequestMapping(method = RequestMethod.GET, value = "/rest/checkcf")
	public @ResponseBody
	boolean checkCF(HttpServletRequest request) {
		logger.error("Is Test: " + is_test);
		return (is_test.compareTo("true") == 0) ? true : false;
	}
	
}
