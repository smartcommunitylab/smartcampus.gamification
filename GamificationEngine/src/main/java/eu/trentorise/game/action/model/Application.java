package eu.trentorise.game.action.model;

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
@Entity
@SequenceGenerator(name=IGameConstants.APPLICATION_SEQUENCE_GENERATOR_NAME, sequenceName=IGameConstants.APPLICATION_SEQUENCE_NAME)
public class Application {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO, generator=IGameConstants.APPLICATION_SEQUENCE_GENERATOR_NAME)
    protected Integer id;

    @NotNull
    @Column(nullable = false)
    protected String name;
    
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

    @Override
    public String toString() {
        return "Application{" + "id=" + id + ", name=" + name + '}';
    }
}