/**
 * 
 */
package ar.com.avaco.ws.rest.security.dto;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author avaco
 * The user authorised is an entite and a dto
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserAuthorised implements UserDetails{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1352581722512247397L;
	private String username;
	private boolean isAccountNoExpired;
	private boolean isAccountNonLocked;
	private boolean isCredentialsNonExpired;
	private boolean isEnabled;
	private String sistemaExterno;
	
	protected Collection<? extends GrantedAuthority> authorities;

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}
	@Override
	public String getPassword() {
		return null;
	}
	@Override
	public boolean isAccountNonExpired() {
		return this.isAccountNoExpired;
	}
	@Override
	public boolean isAccountNonLocked() {
		return this.isAccountNonLocked;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return this.isCredentialsNonExpired;
	}
	@Override
	public boolean isEnabled() {
		return this.isEnabled;
	}
	public boolean isAccountNoExpired() {
		return isAccountNoExpired;
	}
	public void setAccountNoExpired(boolean isAccountNoExpired) {
		this.isAccountNoExpired = isAccountNoExpired;
	}
	public void setAccountNonLocked(boolean isAccountNonLocked) {
		this.isAccountNonLocked = isAccountNonLocked;
	}
	public void setCredentialsNonExpired(boolean isCredentialsNonExpired) {
		this.isCredentialsNonExpired = isCredentialsNonExpired;
	}
	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	public String getSistemaExterno() {
		return sistemaExterno;
	}
	public void setSistemaExterno(String sistemaExterno) {
		this.sistemaExterno = sistemaExterno;
	}
	
	
}
