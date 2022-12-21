/**
 * 
 */
package ar.com.avaco.arc.sec.service;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author avaco
 *
 */
public class CustomDaoAuthenticationProvider extends DaoAuthenticationProvider {
    
	protected Authentication createSuccessAuthentication(Object principal, Authentication authentication,
            UserDetails user) {
        // Ensure we return the original credentials the user supplied,
        // so subsequent attempts are successful even with encoded passwords.
        // Also ensure we return the original getDetails(), so that future
        // authentication events after cache expiry contain the details
		GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();
		setAuthoritiesMapper(authoritiesMapper);
        CustomUsernamePasswordAuthentication result = new CustomUsernamePasswordAuthentication(principal,
                authentication.getCredentials(),authoritiesMapper.mapAuthorities(user.getAuthorities()));
        result.setDetails(authentication.getDetails());
        return result;
    }
}
