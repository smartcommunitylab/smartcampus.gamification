package eu.trentorise.smartcampus.gamification_web.models;

import java.io.Serializable;

public class UserCS implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String nome;
	private String cognome;
	
	private String sesso;
	private String dataNascita;
	private String provinciaNascita;
	private String luogoNascita;
	private String codiceFiscale;
	private String cellulare;
	private String email;
	
	private String indirizzoRes;
	private String capRes;
	private String cittaRes;
	private String provinciaRes;
	//private String cittadinanza;
	
	private String issuersdn;
	private String subjectdn;
	private String base64;
	
	
	public UserCS(){
		super();
		// TODO Auto-generated constructor stub
	}

	public UserCS(String nome, String cognome, String sesso,
			String dataNascita, String provinciaNascita, String luogoNascita, String codiceFiscale,
			String cellulare, String email, String indirizzoRes, String capRes,
			String cittaRes, String provinciaRes, String issuersdn,
			String subjectdn, String base64) {
		super();
		this.nome = nome;
		this.cognome = cognome;
		this.sesso = sesso;
		this.dataNascita = dataNascita;
		this.provinciaNascita = provinciaNascita;
		this.luogoNascita = luogoNascita;
		this.codiceFiscale = codiceFiscale;
		this.cellulare = cellulare;
		this.email = email;
		this.indirizzoRes = indirizzoRes;
		this.capRes = capRes;
		this.cittaRes = cittaRes;
		this.provinciaRes = provinciaRes;
		//this.cittadinanza = cittadinanza;
		this.issuersdn = issuersdn;
		this.subjectdn = subjectdn;
		this.base64 = base64;
	}

	public String getNome() {
		return nome;
	}

	public String getCognome() {
		return cognome;
	}

	public String getSesso() {
		return sesso;
	}

	public String getDataNascita() {
		return dataNascita;
	}

	public String getProvinciaNascita() {
		return provinciaNascita;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public String getCellulare() {
		return cellulare;
	}

	public String getEmail() {
		return email;
	}

	public String getIndirizzoRes() {
		return indirizzoRes;
	}

	public String getCapRes() {
		return capRes;
	}

	public String getCittaRes() {
		return cittaRes;
	}

	public String getProvinciaRes() {
		return provinciaRes;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public void setSesso(String sesso) {
		this.sesso = sesso;
	}

	public void setDataNascita(String dataNascita) {
		this.dataNascita = dataNascita;
	}

	public void setProvinciaNascita(String provinciaNascita) {
		this.provinciaNascita = provinciaNascita;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public void setCellulare(String cellulare) {
		this.cellulare = cellulare;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setIndirizzoRes(String indirizzoRes) {
		this.indirizzoRes = indirizzoRes;
	}

	public void setCapRes(String capRes) {
		this.capRes = capRes;
	}

	public void setCittaRes(String cittaRes) {
		this.cittaRes = cittaRes;
	}

	public void setProvinciaRes(String provinciaRes) {
		this.provinciaRes = provinciaRes;
	}

	public String getLuogoNascita() {
		return luogoNascita;
	}

	public void setLuogoNascita(String luogoNascita) {
		this.luogoNascita = luogoNascita;
	}

	public String getIssuersdn() {
		return issuersdn;
	}

	public String getSubjectdn() {
		return subjectdn;
	}

	public String getBase64() {
		return base64;
	}

	public void setIssuersdn(String issuersdn) {
		this.issuersdn = issuersdn;
	}

	public void setSubjectdn(String subjectdn) {
		this.subjectdn = subjectdn;
	}

	public void setBase64(String base64) {
		this.base64 = base64;
	}
	
}	

