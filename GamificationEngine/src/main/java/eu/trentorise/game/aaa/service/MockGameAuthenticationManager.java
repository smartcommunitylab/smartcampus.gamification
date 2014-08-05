package eu.trentorise.game.aaa.service;

import eu.trentorise.game.aaa.container.IAuthenticationContainer;
import eu.trentorise.game.aaa.model.User;
import java.util.Comparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author Luca Piras
 */
@Service("mockGameAuthenticationManager")
public class MockGameAuthenticationManager implements IGameAuthenticationManager {

    public static MockGameAuthenticationManager createInstance() {
        return new MockGameAuthenticationManager();
    }
    
    @Override
    public User authenticate(IAuthenticationContainer container) throws Exception {
        //TODO: improve the authentication, now it is very basic and not safe
        //TODO: return null or throw Exception if it is not possible to 
        //authenticate that one
        
        User returnValue = null;
        
        User expectedElement = this.createUser();
        if (0 == comparator.compare(container.getUser(), expectedElement)) {
            returnValue = expectedElement;
        }
        
        return returnValue;
    }
    
    public User createUser() {
        User element = new User();
        element.setUsername("gamificationUser@gmail.com");
        element.setPassword("gamificationUserPwd");
        return element;
    }
    
    
    @Qualifier("userComparator")
    @Autowired
    protected Comparator<User> comparator;
}