/**
 * 
 */
package ar.com.avaco.ws.rest.security.dto;

import java.io.Serializable;

import ar.com.avaco.arc.core.domain.Entity;

/**
 * @author avaco
 *
 */
public class Access extends Entity<Long> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4094190051306425499L;
	private Long id;
	private User user;
	private Profile profile;
	
	public Access() {
		
	}

	public Access(Long id, User user, Profile profile) {
		super();
		this.id = id;
		this.user = user;
		this.profile = profile;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

}
