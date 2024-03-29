/**
 * 
 */
package ar.com.avaco.ws.rest.security.dto;

import java.io.Serializable;
import java.util.Set;

import ar.com.avaco.arc.core.domain.Entity;

/**
 * @author avaco
 *
 */
public class User extends Entity<Long> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3712576319173757829L;
	private Long id;
	private String username;
	private String name;
	private String lastname;
	private Set<Profile> profiles;
	private String email;
	private boolean enabled;
	private String usuariosap;
	private Boolean admin;
	private String deposito;

	public User() {

	}

	public User(Long id, String username, String name, String lastname, Set<Profile> profiles, String email,
			String usuariosap, boolean enabled, Boolean admin, String deposito) {
		super();
		this.id = id;
		this.username = username;
		this.name = name;
		this.lastname = lastname;
		this.profiles = profiles;
		this.email = email;
		this.usuariosap = usuariosap;
		this.enabled = enabled;
		this.admin = admin;
		this.deposito = deposito;
	}

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<Profile> getProfiles() {
		return profiles;
	}

	public void setProfiles(Set<Profile> profiles) {
		this.profiles = profiles;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getUsuariosap() {
		return usuariosap;
	}

	public void setUsuariosap(String usuariosap) {
		this.usuariosap = usuariosap;
	}

	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	public String getDeposito() {
		return deposito;
	}

	public void setDeposito(String deposito) {
		this.deposito = deposito;
	}

}
