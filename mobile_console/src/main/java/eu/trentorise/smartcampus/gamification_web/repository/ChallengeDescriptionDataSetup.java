package eu.trentorise.smartcampus.gamification_web.repository;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import eu.trentorise.smartcampus.gamification_web.models.ChallengeDescriptionData;

@Component
public class ChallengeDescriptionDataSetup {

	@Value("classpath:/chall-conf.yml")
	private Resource resource;
	
	@PostConstruct
	public void init() throws IOException {
		Yaml yaml = new Yaml(new Constructor(ChallengeDescriptionDataSetup.class));
		ChallengeDescriptionDataSetup data = (ChallengeDescriptionDataSetup) yaml.load(resource.getInputStream());
		this.descriptions = data.descriptions;
	}

	private List<ChallengeDescriptionData> descriptions;
	private Map<String,ChallengeDescriptionData> descriptionsMap;

	public List<ChallengeDescriptionData> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(List<ChallengeDescriptionData> descriptions) {
		this.descriptions = descriptions;
	}
	
	@Override
	public String toString() {
		return "ChallengeDescriptionDataSetup [descriptions=" + descriptions + "]";
	}
	
	public ChallengeDescriptionData findChallDescByID(String id){
		if (descriptionsMap == null) {
			descriptionsMap = new HashMap<String, ChallengeDescriptionData>();
			for (ChallengeDescriptionData challdesc : descriptions) {
				descriptionsMap.put(challdesc.getId(), challdesc);
			}
		}
		return descriptionsMap.get(id);
	}

}
