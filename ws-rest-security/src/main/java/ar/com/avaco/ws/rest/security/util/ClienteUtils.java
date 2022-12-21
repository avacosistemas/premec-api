package ar.com.avaco.ws.rest.security.util;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;

import ar.com.avaco.ws.rest.security.dto.UserAuthorisedDTO;

public class ClienteUtils {

	public static Long getClienteLogueadoId() {
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (!(principal instanceof UserAuthorisedDTO)) {
			throw new BadCredentialsException("Cliente no logueado");
		}
		
		UserAuthorisedDTO user = (UserAuthorisedDTO) principal;
		return user.getId();

	}

}
