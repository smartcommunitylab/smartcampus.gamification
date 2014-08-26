package eu.trentorise.game.profile.game.model;

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
@SequenceGenerator(name=IGameConstants.GAME_SEQUENCE_GENERATOR_NAME, sequenceName=IGameConstants.GAME_SEQUENCE_NAME)
public class Game {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO, generator=IGameConstants.GAME_SEQUENCE_GENERATOR_NAME)
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
        return "Game{" + "id=" + id + ", name=" + name + '}';
    }
}