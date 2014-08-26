package eu.trentorise.game.plugin.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import eu.trentorise.game.controller.IGameConstants;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Luca Piras
 */
//Necessary for the Test
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@SequenceGenerator(name=IGameConstants.PLUGIN_SEQUENCE_GENERATOR_NAME, sequenceName=IGameConstants.PLUGIN_SEQUENCE_NAME)
public class Plugin {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO, generator=IGameConstants.PLUGIN_SEQUENCE_GENERATOR_NAME)
    protected Integer id;
    
    @NotNull
    @Column(nullable = false)
    protected String name;
    
    protected String version;
    
    protected String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public String toString() {
        return "GamificationPlugin{" + "id=" + id + ", name=" + name + ", version=" + version + ", description=" + description + '}';
    }
}