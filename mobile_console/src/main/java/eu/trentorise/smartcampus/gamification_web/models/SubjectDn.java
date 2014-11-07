package eu.trentorise.smartcampus.gamification_web.models;

import java.io.Serializable;

public class SubjectDn implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String cn;
	private String ou;
	private String o;
	private String c;
	
	public SubjectDn(String cn, String ou, String o, String c) {
		super();
		this.cn = cn;
		this.ou = ou;
		this.o = o;
		this.c = c;
	}
	
	public SubjectDn(String fullAtt){
		String attr[] = fullAtt.split(",");
		if(attr.length == 4){
			this.setCn(attr[0].substring(3));
			this.setOu(attr[1].substring(3));
			this.setO(attr[2].substring(3));
			this.setC(attr[3].substring(3));
		}
	}
	
	public String getCn() {
		return cn;
	}
	public String getOu() {
		return ou;
	}
	public String getO() {
		return o;
	}
	public String getC() {
		return c;
	}
	public void setCn(String cn) {
		this.cn = cn;
	}
	public void setOu(String ou) {
		this.ou = ou;
	}
	public void setO(String o) {
		this.o = o;
	}
	public void setC(String c) {
		this.c = c;
	}

	
	
}
