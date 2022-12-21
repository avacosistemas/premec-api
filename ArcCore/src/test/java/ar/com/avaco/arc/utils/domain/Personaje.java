package ar.com.avaco.arc.utils.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "PERSONAJE")
@SequenceGenerator(name = "PERSONAJE_SEQ", sequenceName = "PERSONAJE_SEQ", allocationSize = 1)
public class Personaje extends ar.com.avaco.arc.core.domain.Entity<Long> {
	
	/** */
	private static final long serialVersionUID = 5539335152400699532L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PERSONAJE_SEQ")
	@Column(name = "ID_PERSONA")
	private Long id;
	
	@Column(name = "NOMBRE", length = 30)
	private String nombre;

	public Personaje() {

	}
	
	public Personaje(String nombre) {
		this.nombre = nombre;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}