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
public class Permission extends Entity<Long> implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4646168645540186169L;
	private Long id;
	private String code;
	private String description;
	private Boolean enabled;
	
	public Permission() {
		
	}
	
	public Permission(Long id, String code, String description, Boolean enabled) {
		super();
		this.id = id;
		this.code = code;
		this.description = description;
		this.enabled = enabled;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
}
