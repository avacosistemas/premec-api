package ar.com.avaco.ws.rest.security.service.impl;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ar.com.avaco.ws.rest.security.dto.UserAuthorised;

@Service(value="cacheUserDetailsService")
public class CacheUserDetailsService implements UserDetailsService {

    private List<UserAuthorised> userAuthorizeds;
    
    public CacheUserDetailsService() {
    	this.userAuthorizeds = new ArrayList<UserAuthorised>(); 
	}
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	UserAuthorised user = this.userAuthorizeds.stream()
    								.filter(p -> p.getUsername().equals(username)).findAny().orElse(null);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        }
        return user;
    }

    public void addUserToCache(UserAuthorised userAuthorised) {
    	this.userAuthorizeds.add(userAuthorised);
    }
	
}