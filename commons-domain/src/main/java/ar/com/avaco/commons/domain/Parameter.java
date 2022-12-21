/**
 * 
 */
package ar.com.avaco.commons.domain;

import java.io.Serializable;

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
@Table(name = "PARAMETER")
@SequenceGenerator(name = "PARAMETER_SEQ", sequenceName = "PARAMETER_SEQ", allocationSize = 1)
public class Parameter extends ar.com.avaco.arc.core.domain.Entity<Integer> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8168268668794827990L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PARAMETER_SEQ")
	@Column(name = "ID_PARAMETER")
	private Integer id;

	@Column(name = "key")
	private String key;

	@Column(name = "value")
	private String value;

	@Column(name = "description")
	private String description;
	
	public Parameter() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}