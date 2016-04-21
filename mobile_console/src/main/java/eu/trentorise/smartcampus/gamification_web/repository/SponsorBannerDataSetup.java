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

import eu.trentorise.smartcampus.gamification_web.models.SponsorBannerData;

@Component
public class SponsorBannerDataSetup {

	@Value("classpath:/sponsor-conf.yml")
	private Resource resource;
	
	@PostConstruct
	public void init() throws IOException {
		Yaml yaml = new Yaml(new Constructor(SponsorBannerDataSetup.class));
		SponsorBannerDataSetup data = (SponsorBannerDataSetup) yaml.load(resource.getInputStream());
		this.sponsors = data.sponsors;
	}

	private List<SponsorBannerData> sponsors;
	private Map<String,SponsorBannerData> sponsorsMap;

	public List<SponsorBannerData> getSponsors() {
		return sponsors;
	}

	public void setSponsors(List<SponsorBannerData> sponsors) {
		this.sponsors = sponsors;
	}
	
	@Override
	public String toString() {
		return "SponsorBannerDataSetup [sponsors=" + sponsors + "]";
	}
	
	public SponsorBannerData findSponsorByID(String id){
		if (sponsorsMap == null) {
			sponsorsMap = new HashMap<String, SponsorBannerData>();
			for (SponsorBannerData sponsor : sponsors) {
				sponsorsMap.put(sponsor.getId(), sponsor);
			}
		}
		return sponsorsMap.get(id);
	}

}
