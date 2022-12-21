package ar.com.avaco.arc.sec.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Un RolCompania representa el tipo de posicion de un usuario dentro de la
 * aplicacion. El/los rol/es de un usuario se obtienen de los perfiles que tiene
 * asignado.
 * 
 * @author aogonzalez
 * 
 */
@Entity
@Table(name = "SEG_ROL")
@SequenceGenerator(name = "SEG_ROL_SEQ", sequenceName = "SEG_ROL_SEQ", allocationSize = 1)
public class Rol extends ar.com.avaco.arc.core.domain.Entity<Long> {

	/** */
	private static final long serialVersionUID = -905096204577279657L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEG_ROL_SEQ")
	@Column(name = "ID_SEG_ROL")
	private Long id;

	/**
	 * El codigo del RolCompania. Por ejemplo: SHP.
	 */
	@Column(name = "CODIGO", updatable = false, unique = true, length = 10, nullable = false)
	private String codigo;

	/**
	 * El nombre del RolCompania. Por ejemplo: Cargador.
	 */
	@Column(name = "NOMBRE", length = 50, unique = true, nullable = false)
	private String nombre;

	/**
	 * Determina si el rol es un super rol que no requiere filtrar por compania y gasoducto.
	 */
	@Column(name = "SUPER_ROL", nullable = false)
	private boolean superRol;
	
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
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre
	 *            the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public boolean isSuperRol() {
		return superRol;
	}

	public void setSuperRol(boolean superRol) {
		this.superRol = superRol;
	}
}