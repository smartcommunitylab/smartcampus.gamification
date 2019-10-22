package eu.trentorise.game.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import eu.trentorise.game.model.core.TimeInterval;

public class Settings {


    private Map<String, String> statisticsConfig = new HashMap<String, String>();
    private ChallengeSettings challengeSettings = new ChallengeSettings();

    public static class ChallengeSettings {
        private ChallengeDisclosure disclosure = new ChallengeDisclosure();

        public static class ChallengeDisclosure {
            private Date startDate;
            private TimeInterval frequency;

            public Date getStartDate() {
                return startDate;
            }

            public void setStartDate(Date startDate) {
                this.startDate = startDate;
            }

            public TimeInterval getFrequency() {
                return frequency;
            }

            public void setFrequency(TimeInterval frequency) {
                this.frequency = frequency;
            }



        }

        public ChallengeDisclosure getDisclosure() {
            return disclosure;
        }

        public void setDisclosure(ChallengeDisclosure disclosure) {
            this.disclosure = disclosure;
        }
    }



	public Map<String, String> getStatisticsConfig() {
		return statisticsConfig;
	}

	public void setStatisticsConfig(Map<String, String> statisticsConfig) {
		this.statisticsConfig = statisticsConfig;
	}

    public ChallengeSettings getChallengeSettings() {
        return challengeSettings;
    }

    public void setChallengeSettings(ChallengeSettings challengeSettings) {
        this.challengeSettings = challengeSettings;
    }

}