package eu.trentorise.game.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Level {
    private String name;
    private String pointConceptName;
    private List<Threshold> thresholds = new ArrayList<>();


    public Level(String name, String pointConceptName) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("name cannot be blank");
        }

        if (StringUtils.isBlank(pointConceptName)) {
            throw new IllegalArgumentException("pointConceptName cannot be blank");
        }
        this.name = name;
        this.pointConceptName = pointConceptName;
    }


    public String getPointConceptName() {
        return pointConceptName;
    }


    public List<Threshold> getThresholds() {
        return thresholds;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        Level rhs = (Level) obj;
        return new EqualsBuilder().append(name, rhs.name).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(45, 13).append(name).hashCode();
    }


    @Override
    public String toString() {
        return String.format("{name=%s, pointConceptName=%s}", name, pointConceptName);
    }


    public String getName() {
        return name;
    }

    public static class Config {
        private int choices;
        private List<String> availableModels = new ArrayList<>();

        public int getChoices() {
            return choices;
        }

        public void setChoices(int choices) {
            this.choices = choices;
        }

        public List<String> getAvailableModels() {
            return availableModels;
        }

    }

    public static class Threshold {
        private String name;
        private double value;
        private Config config;


        public Threshold(String name, double value) {
            this.name = name;
            this.value = value;
        }


        public Threshold updateValue(double newValue) {
            this.value = newValue;
            return this;
        }
        public String getName() {
            return name;
        }

        public double getValue() {
            return value;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (obj == this) {
                return true;
            }

            if (obj.getClass() != getClass()) {
                return false;
            }

            Threshold rhs = (Threshold) obj;
            return new EqualsBuilder().append(name, rhs.name).isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(43, 3).append(name).hashCode();
        }

        @Override
        public String toString() {
            return String.format("{name=%s, value=%s}", name, value);
        }


        public Config getConfig() {
            return config;
        }


        public void setConfig(Config config) {
            this.config = config;
        }
    }
}
