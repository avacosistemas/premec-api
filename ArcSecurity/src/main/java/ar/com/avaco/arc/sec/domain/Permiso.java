package ar.com.avaco.arc.sec.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

/**
 * Un permiso representa una accion concreta y atomica dentro de la aplicacion.
 * 
 * @author aogonzalez
 */
@Entity
@Table(name = "SEG_PERMISO")
@SequenceGenerator(name = "SEG_PERMISO_SEQ", sequenceName = "SEG_PERMISO_SEQ", allocationSize = 1)
public class Permiso extends ar.com.avaco.arc.core.domain.Entity<Long> implements GrantedAuthority {

	private static final long serialVersionUID = 64072452532029953L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEG_PERMISO_SEQ")
	@Column(name="ID_SEG_PERMISO")
	private Long id;

	/**
	 * El codigo del permiso. Por ejemplo: NOMINACION_AGREGAR.
	 */
	@Column(updatable = false, unique = true, length = 50)
	private String codigo;

	/**
	 * La descripcion del permiso. Por ejemplo: Agregar una nueva nominacion.
	 */
	@Column(length = 100)
	private String descripcion;

	/**
	 * Determina si el permiso esta activo o no. Si esta inactivo no puede ser
	 * asignado a un perfil. Y si ya se encuentra asignado, debe devolver null
	 * en el metodo getAuthority.
	 */
	@Column
	private boolean activo;

	/*
	 * Devuelve el codigo si esta activo. Sino null. (non-Javadoc)
	 * 
	 * @see org.springframework.security.core.GrantedAuthority#getAuthority()
	 */
	@Override
	public String getAuthority() {
		String ret = null;
		if (activo)
			ret = this.codigo;
		return ret;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo
	 *            the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion
	 *            the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return the activo
	 */
	public boolean isActivo() {
		return activo;
	}

	/**
	 * @param activo
	 *            the activo to set
	 */
	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	
	@Override
	public String toString() {
		return this.codigo;
	}

}