package ar.com.avaco.ws.rest.security.controller;

import java.util.Objects;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ar.com.avaco.arc.sec.domain.Usuario;
import ar.com.avaco.ws.rest.security.dto.JwtAuthenticationRequest;
import ar.com.avaco.ws.rest.security.dto.JwtAuthenticationResponse;
import ar.com.avaco.ws.rest.security.dto.User;
import ar.com.avaco.ws.rest.security.dto.UserAuthorised;
import ar.com.avaco.ws.rest.security.exception.AuthenticationException;
import ar.com.avaco.ws.rest.security.service.UserService;
import ar.com.avaco.ws.rest.security.service.impl.JwtUserDetailsService;
import ar.com.avaco.ws.rest.security.util.JwtTokenUtil;

@RestController
public class AuthenticationRestController {

	@Value("${jwt.header}")
	private String tokenHeader;

	@Resource(name = "authenticationManager")
	private AuthenticationManager authenticationManager;

	@Resource(name = "jwtTokenUtil")
	private JwtTokenUtil jwtTokenUtil;

	@Resource(name = "jwtUserDetailsService")
	private JwtUserDetailsService userDetailsService;

	@Resource(name = "userService")
	private UserService userService;

	@RequestMapping(value = "/auth", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest)
			throws AuthenticationException {
		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		// Reload password post-security so we can generate the token
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		final String token = jwtTokenUtil.generateToken(userDetails);

		User usuario = userService.getByUsername(userDetails.getUsername());

		// Return the token and user datas
		return ResponseEntity.ok(new JwtAuthenticationResponse(token, usuario));
	}

	@RequestMapping(value = "/authAdmin", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationTokenAdmin(@RequestBody JwtAuthenticationRequest authenticationRequest)
			throws AuthenticationException {
		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		// Reload password post-security so we can generate the token
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		final String token = jwtTokenUtil.generateToken(userDetails);

		User usuario = userService.getByUsername(userDetails.getUsername());
		if (!usuario.getAdmin().booleanValue()) {
			throw new AuthenticationException("El usuario " + authenticationRequest.getUsername() + " no es admin",
					null);
		}
		// Return the token and user datas
		return ResponseEntity.ok(new JwtAuthenticationResponse(token, usuario));
	}

	@RequestMapping(value = "/refresh", method = RequestMethod.POST)
	public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
		String authToken = request.getHeader(tokenHeader);
		final String token = authToken.substring(7);
		String username = jwtTokenUtil.getUsernameFromToken(token);
		Usuario user = (Usuario) userDetailsService.loadUserByUsername(username);

		User usuario = userService.getByUsername(username);

		if (jwtTokenUtil.canTokenBeRefreshed(token, user.getFechaAltaPassword())) {
			String refreshedToken = jwtTokenUtil.refreshToken(token);
			return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken, usuario));
		} else {
			return ResponseEntity.badRequest().body(null);
		}
	}

	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public ResponseEntity<?> userAuthenticationToken(HttpServletRequest request) {
		String authToken = request.getHeader(tokenHeader);
		final String token = authToken.substring(7);
		String username = jwtTokenUtil.getUsernameFromToken(token);
		Usuario user = (Usuario) userDetailsService.loadUserByUsername(username);
		UserAuthorised userAutho = new UserAuthorised();
		userAutho.setUsername(user.getUsername());
		userAutho.setAuthorities(user.getAuthorities());
		userAutho.setAccountNoExpired(user.isAccountNonExpired());
		userAutho.setAccountNonLocked(user.isAccountNonLocked());
		userAutho.setCredentialsNonExpired(user.isCredentialsNonExpired());
		userAutho.setEnabled(user.isEnabled());
		return ResponseEntity.ok(userAutho);
	}

	@ExceptionHandler({ AuthenticationException.class })
	public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
	}

	/**
	 * Authenticates the user. If something is wrong, an
	 * {@link AuthenticationException} will be thrown
	 */
	private void authenticate(String username, String password) {
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new AuthenticationException("User is disabled!", e);
		} catch (BadCredentialsException e) {
			throw new AuthenticationException("Bad credentials!", e);
		}
	}

	
//	public void setJwtTokenUtilManager(JwtTokenUtil jwtTokenUtil) {
//		this.jwtTokenUtil = jwtTokenUtil;
//	}
//
//	
//	public void setJwtUserDetailsServiceManager(UserDetailsService userDetailsService) {
//		this.userDetailsService = userDetailsService;
//	}
//
//
//	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
//		this.authenticationManager = authenticationManager;
//	}
//
//
//	public void setUserServiceManager(UserService userService) {
//		this.userService = userService;
//	}

}