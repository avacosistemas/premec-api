/**
 * 
 */
package ar.com.avaco.commons.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author avaco
 */

@Entity
@Table(name="CONTACT_US")
@SequenceGenerator(name = "CONTACT_US_SEQ", sequenceName = "CONTACT_US_SEQ", allocationSize = 1)
public class ContactUS extends ar.com.avaco.arc.core.domain.Entity<Long> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8168268668794827990L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CONTACT_US_SEQ")
	@Column(name = "ID_CONTACT")
    private Long id;
	
	@Column(name="name", nullable=false)
    private String name;
	
	@Column(name="telephone")
    private String telephone;
	
	@Column(name="email", nullable=false)
    private String email;

	@Column(name="message", nullable=false)
	private String message;
	
	@Column(name="date", nullable=false)
	private Date date;
	
    public ContactUS() {
		super();
	}
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
}