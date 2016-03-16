
package eu.trentorise.game.challenges.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "BadgeCollectionConcept",
    "PointConcept"
})
public class State {

    @JsonProperty("BadgeCollectionConcept")
    private List<eu.trentorise.game.challenges.rest.BadgeCollectionConcept> BadgeCollectionConcept = new ArrayList<eu.trentorise.game.challenges.rest.BadgeCollectionConcept>();
    @JsonProperty("PointConcept")
    private List<eu.trentorise.game.challenges.rest.PointConcept> PointConcept = new ArrayList<eu.trentorise.game.challenges.rest.PointConcept>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The BadgeCollectionConcept
     */
    @JsonProperty("BadgeCollectionConcept")
    public List<eu.trentorise.game.challenges.rest.BadgeCollectionConcept> getBadgeCollectionConcept() {
        return BadgeCollectionConcept;
    }

    /**
     * 
     * @param BadgeCollectionConcept
     *     The BadgeCollectionConcept
     */
    @JsonProperty("BadgeCollectionConcept")
    public void setBadgeCollectionConcept(List<eu.trentorise.game.challenges.rest.BadgeCollectionConcept> BadgeCollectionConcept) {
        this.BadgeCollectionConcept = BadgeCollectionConcept;
    }

    /**
     * 
     * @return
     *     The PointConcept
     */
    @JsonProperty("PointConcept")
    public List<eu.trentorise.game.challenges.rest.PointConcept> getPointConcept() {
        return PointConcept;
    }

    /**
     * 
     * @param PointConcept
     *     The PointConcept
     */
    @JsonProperty("PointConcept")
    public void setPointConcept(List<eu.trentorise.game.challenges.rest.PointConcept> PointConcept) {
        this.PointConcept = PointConcept;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
