package eu.trentorise.game.profile.game.model;

import eu.trentorise.game.application.model.Application;

/**
 *
 * @author Luca Piras
 */
public class GameProfile {
    
    protected Integer id;
    
    //TODO: the game will not tied to an application in the future
    protected Application application;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    @Override
    public String toString() {
        return "GameProfile{" + "id=" + id + ", application=" + application + '}';
    }
}