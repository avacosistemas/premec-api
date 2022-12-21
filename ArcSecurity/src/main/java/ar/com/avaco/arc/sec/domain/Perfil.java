package ar.com.avaco.arc.sec.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * Un Perfil representa una agrupación de Permisos contenido por un RolCompania.
 * 
 * @author aogonzalez
 * 
 */
@Entity
@Table(name = "SEG_PERFIL")
@SequenceGenerator(name = "SEG_PERFIL_SEQ", sequenceName = "SEG_PERFIL_SEQ", allocationSize = 1)
public class Perfil extends ar.com.avaco.arc.core.domain.Entity<Long> {

	/** */
	private static final long serialVersionUID = -5085142355499582872L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEG_PERFIL_SEQ")
	@Column(name  = "ID_SEG_PERFIL")
	private Long id;

	/**
	 * El nombre del perfil. Por ejemplo: Visualizador de Reportes.
	 */
	@Column(unique = true, length = 50)
	private String nombre;

	/**
	 * Determina si el Perfil esta activo. Si esta inactivo, no puede asignarse
	 * a un usuario. Si ya se encuentra asignado, no deberían devolverse los
	 * permisos asociados.
	 */
	@Column
	private boolean activo;

	/**
	 * El RolCompania al cual pertenece el Perfil.
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(foreignKey = @ForeignKey(name = "SEG_ROL_ID_FK"))
	private Rol rol;

	/**
	 * Los permisos asignados al perfil.
	 */
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "SEG_PERFIL_PERMISO", 
	joinColumns = @JoinColumn(name = "ID_SEG_PERFIL", referencedColumnName = "ID_SEG_PERFIL"), 
	inverseJoinColumns = @JoinColumn(name = "ID_SEG_PERMISO", referencedColumnName = "ID_SEG_PERMISO"))
	@Fetch(FetchMode.SELECT)
	private List<Permiso> permisos;

	public List<String> getPermisosString() {
		List<String> permisosString = new ArrayList<String>();
		for (Permiso p : permisos) {
			permisosString.add(p.getCodigo());
		}
		return permisosString;
	}
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	/**
	 * @return the permisos
	 */
	public List<Permiso> getPermisos() {
		return permisos;
	}

	/**
	 * @param permisos
	 *            the permisos to set
	 */
	public void setPermisos(List<Permiso> permisos) {
		this.permisos = permisos;
	}
}