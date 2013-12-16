package eu.trentorise.game.profile.model;

/**
 *
 * @author Luca Piras
 */
public class GameProfile {
    
    protected Integer id;
    
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