/**
 * 
 */
package ar.com.avaco.arc.sec.service;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import ar.com.avaco.arc.sec.domain.Usuario;

/**
 * @author avaco
 *
 */
public class CustomUsernamePasswordAuthentication extends
		UsernamePasswordAuthenticationToken {

	public CustomUsernamePasswordAuthentication(Object principal,
			Object credentials) {
		super(principal, credentials);
	}
	public CustomUsernamePasswordAuthentication(Object principal,
			Object credentials,
			Collection<? extends GrantedAuthority> authorities) {
		super(principal, credentials, authorities);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 8580766236646378126L;
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return (Collection<GrantedAuthority>) ((Usuario)SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal()).getAuthorities();
	}
}
