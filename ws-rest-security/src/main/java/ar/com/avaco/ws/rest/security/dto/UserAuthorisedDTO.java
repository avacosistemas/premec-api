/**
 * 
 */
package ar.com.avaco.ws.rest.security.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ar.com.avaco.arc.sec.domain.UserDetailsExtended;

/**
 * @author avaco
 * The user authorised is an entite and a dto
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserAuthorisedDTO implements Serializable, UserDetailsExtended {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1352581722512247397L;
	private Long id;
	private String username;
	private boolean isAccountNoExpired;
	private boolean isAccountNonLocked;
	private boolean isCredentialsNonExpired;
	private boolean isEnabled;
	private List<? extends Authority> authorities;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public boolean isAccountNoExpired() {
		return isAccountNoExpired;
	}
	public void setAccountNoExpired(boolean isAccountNoExpired) {
		this.isAccountNoExpired = isAccountNoExpired;
	}
	public boolean isAccountNonLocked() {
		return isAccountNonLocked;
	}
	public void setAccountNonLocked(boolean isAccountNonLocked) {
		this.isAccountNonLocked = isAccountNonLocked;
	}
	public boolean isCredentialsNonExpired() {
		return isCredentialsNonExpired;
	}
	public void setCredentialsNonExpired(boolean isCredentialsNonExpired) {
		this.isCredentialsNonExpired = isCredentialsNonExpired;
	}
	public boolean isEnabled() {
		return isEnabled;
	}
	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public List<? extends Authority> getAuthorities() {
		return authorities;
	}
	public void setAuthorities(List<? extends Authority> authorities) {
		this.authorities = authorities;
	}
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public Date getFechaAltaPassword() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean isBloqueado() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public Integer getIntentosFallidosLogin() {
		// TODO Auto-generated method stub
		return null;
	}	
}
