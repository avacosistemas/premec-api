/**
 * 
 */
package ar.com.avaco.arc.sec.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.com.avaco.arc.sec.domain.Usuario;
import ar.com.avaco.arc.sec.exception.NuclearJSecurityException;
import ar.com.avaco.arc.sec.service.AuthenticationService;
import ar.com.avaco.arc.sec.service.UsuarioService;

/**
 * The Authentication Service Implements authentication with authentication
 * manager by spring security. .
 * 
 * @author avaco
 * 
 */
@Service
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8691028753433563888L;
	
	/**
	 * The user invalid constant property
	 */
	public static final String USER_INVALID_PROP = "user.invalid";

	/**
	 * Bad credential message
	 */
	public static final String BAD_CREDENTIALS = "Bad credentials";
	/**
	 * The authenticationmanager
	 */
	@Autowired
	private AuthenticationManager authenticationManager = null;

	
	/**
	 * The usuarioService
	 */
	@Autowired
	private UsuarioService usuarioService = null;

	@Override
	public void authenticate(String user, String password) {

		try {

			Authentication request = new UsernamePasswordAuthenticationToken(
					user, password);
			Authentication result = authenticationManager.authenticate(request);
			SecurityContextHolder.getContext().setAuthentication(result);

		} catch (CredentialsExpiredException e) {
			throw new NuclearJSecurityException("password.expired", e);
		} catch (BadCredentialsException e) {
			if (usuarioService.isUserExists(user) ) {
				Usuario usuario = (Usuario) usuarioService.loadUserByUsername(user);
				usuario.incrementarIntentoFallido();
				usuarioService.update(usuario);
			}
			throw new NuclearJSecurityException("user.invalid", e);
		} catch (AuthenticationException e) {
			throw new NuclearJSecurityException(USER_INVALID_PROP,e);
		}

	}

	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}

	public void setAuthenticationManager(
			AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

}
