package eu.trentorise.game.profile.game.model;

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
@SequenceGenerator(name="GAME_SEQUENCE", sequenceName="game_sequence")
public class Game {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO, generator="GAME_SEQUENCE")
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