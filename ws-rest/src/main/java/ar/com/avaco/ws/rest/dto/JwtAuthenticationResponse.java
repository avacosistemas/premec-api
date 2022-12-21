package ar.com.avaco.ws.rest.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import ar.com.avaco.ws.rest.security.dto.Permission;
import ar.com.avaco.ws.rest.security.dto.Profile;
import ar.com.avaco.ws.rest.security.dto.User;

/**
 * 
 */
public class JwtAuthenticationResponse implements Serializable {

	private static final long serialVersionUID = 1250166508152483573L;

	private final String token;

	private String name;

	private String lastname;

	private Set<Permission> permissions;

	private String email;

	public JwtAuthenticationResponse(String token) {
		this.token = token;
	}

	public JwtAuthenticationResponse(String token, User usuario) {
		this.token = token;
		this.name = usuario.getName();
		this.lastname = usuario.getLastname();
		this.email = usuario.getEmail();
		
		this.permissions=new HashSet<Permission>();
		Set<Profile> profiles=usuario.getProfiles();
		for (Profile profile : profiles) {
			this.permissions.addAll(profile.getPermissions());
		}		
	}

	public String getToken() {
		return this.token;
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

	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		permissions = permissions;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}