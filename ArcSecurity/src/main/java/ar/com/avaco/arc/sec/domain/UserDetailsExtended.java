
package ar.com.avaco.arc.sec.domain;

import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailsExtended extends UserDetails {

	Date getFechaAltaPassword();
	
	boolean isBloqueado();
	
	Integer getIntentosFallidosLogin();
	
	Long getId();
	
}
