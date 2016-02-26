package eu.trentorise.smartcampus.gamification_web.models;

import java.util.List;

public class PersonalData {

	private String age_range;
	private boolean use_transport;
	private List<String> vehicles;
	private int averagekm;
	private String nick_recommandation;

	public String getAge_range() {
		return age_range;
	}

	public void setAge_range(String age_range) {
		this.age_range = age_range;
	}

	public boolean isUse_transport() {
		return use_transport;
	}

	public List<String> getVehicles() {
		return vehicles;
	}

	public int getAveragekm() {
		return averagekm;
	}

	public String getNick_recommandation() {
		return nick_recommandation;
	}

	public void setUse_transport(boolean use_transport) {
		this.use_transport = use_transport;
	}

	public void setVehicles(List<String> vehicles) {
		this.vehicles = vehicles;
	}

	public void setAveragekm(int averagekm) {
		this.averagekm = averagekm;
	}

	public void setNick_recommandation(String nick_recommandation) {
		this.nick_recommandation = nick_recommandation;
	}

	public PersonalData() {
		super();
	}

	public PersonalData(String age_range, boolean use_transport, List<String> vehicles, int averagekm,
			String nick_recommandation) {
		super();
		this.age_range = age_range;
		this.use_transport = use_transport;
		this.vehicles = vehicles;
		this.averagekm = averagekm;
		this.nick_recommandation = nick_recommandation;
	}

	@Override
	public String toString() {
		return "PersonalData [age_range=" + age_range + ", use_transport=" + use_transport + ", vehicles=" + vehicles
				+ ", averagekm=" + averagekm + ", nick_recommandation=" + nick_recommandation + "]";
	}
	
	public String toJSONString() {
		String vehicle_strings = "[";
		String nick_recomm = null;
		if(nick_recommandation != null && nick_recommandation.compareTo("")!= 0){
			nick_recomm = nick_recommandation;
		}
		for(int i = 0; i < vehicles.size(); i++){
			vehicle_strings += "\"" + vehicles.get(i) + "\",";
		}
		if(vehicle_strings.length()>1){
			vehicle_strings = vehicle_strings.substring(0, vehicle_strings.length() - 1);
			vehicle_strings += "]";
		} else {
			vehicle_strings = null;
		}
		if(nick_recomm != null){
			return "{\"age_range\":\"" + age_range + "\", \"use_transport\":" + use_transport + ", \"vehicles\":"
					+ vehicle_strings + ", \"averagekm\":" + averagekm + ", \"nick_recommandation\":\"" + nick_recommandation
					+ "\"}";
		} else {
			return "{\"age_range\":\"" + age_range + "\", \"use_transport\":" + use_transport + ", \"vehicles\":"
					+ vehicle_strings + ", \"averagekm\":" + averagekm + "}";
		}
		
	}

}
