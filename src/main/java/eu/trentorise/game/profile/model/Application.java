package eu.trentorise.game.profile.model;

/**
 *
 * @author Luca Piras
 */
public class Application {
    
    protected Integer id;
    
    protected ApplicationOwner owner;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ApplicationOwner getOwner() {
        return owner;
    }

    public void setOwner(ApplicationOwner owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Application{" + "id=" + id + ", owner=" + owner + '}';
    }
}