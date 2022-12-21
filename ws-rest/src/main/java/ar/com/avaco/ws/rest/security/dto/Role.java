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
public class Role extends Entity<Long> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -478191277772449397L;
	private Long id;
	private String code;
	private String name;
	
	public Role() {
		
	}

	public Role(Long id, String code, String name) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	
}
