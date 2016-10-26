package eu.trentorise.smartcampus.gamification_web.security;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import eu.trentorise.smartcampus.gamification_web.models.ConsoleUserData;

@Component
public class ConsoleUserDetailsService  implements UserDetailsService{

	@Autowired
	private UserSetup userSetup;
	
	private static final Logger logger = Logger.getLogger(ConsoleUserDetailsService.class);
    private static final Integer role = 1;
    
    private org.springframework.security.core.userdetails.User userdetails;
	
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
        
        ConsoleUserData myUser = userSetup.findUserByUsername(username);
        if(myUser != null){
        	logger.info(String.format("username : %s", myUser.getUsername()));
        	logger.info(String.format("pwd : %s", myUser.getPassword()));
            
            userdetails = new User(myUser.getUsername(), 
            				myUser.getPassword(),
     			   			enabled,
     			   			accountNonExpired,
     			   			credentialsNonExpired,
     			   			accountNonLocked,
     			   			getAuthorities(role));
        }
        return userdetails;
    }

    public List<GrantedAuthority> getAuthorities(Integer role) {
        List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
        if (role.intValue() == 1) {
            authList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        } else if (role.intValue() == 2) {
            authList.add(new SimpleGrantedAuthority("ROLE_CAMPAIGN"));
        }
        System.out.println(authList);
        return authList;
    }
    
    public ConsoleUserData getUserDetails(String username){
    	return userSetup.findUserByUsername(username);
    }

}
