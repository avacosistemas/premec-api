/**
 * 
 */
package ar.com.avaco.ws.rest.security.dto;

import java.io.Serializable;
import java.util.List;

import ar.com.avaco.arc.core.domain.Entity;

/**
 * @author avaco
 *
 */
public class Profile extends Entity<Long> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1484703680205099614L;
	
	private Long id;
	private String name;
	private Role role;
	private List<Permission> permissions;
	private Boolean enabled;
	
	public Profile() {
		
	}

	public Profile(Long id, String name, Role role, List<Permission> permissions, Boolean enabled) {
		super();
		this.id = id;
		this.name = name;
		this.role = role;
		this.permissions = permissions;
		this.enabled = enabled;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
