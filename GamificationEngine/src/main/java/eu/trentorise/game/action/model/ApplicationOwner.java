package eu.trentorise.game.action.model;

/**
 *
 * @author Luca Piras
 */
public class ApplicationOwner {
    
    //TODO: will be an email
    protected String username;
    
    protected String name;
    protected String surname;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public String toString() {
        return "ApplicationOwner{" + "username=" + username + ", name=" + name + ", surname=" + surname + '}';
    }
}