package ar.com.avaco.entities.cliente;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "CLI_ACCESOS_CLIENTE")
public class AccesosCliente extends ar.com.avaco.arc.core.domain.Entity<Long> {

	private static final long serialVersionUID = 7461582434202907846L;

	@Id
	@Column(name = "ID_ACCESO")
	private Long id;

	@Column
	private String codigo;

	@Column
	private String descripcion;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "CLI_ACCESO_PERMISO", 
	joinColumns = @JoinColumn(name = "ID_ACCESO", referencedColumnName = "ID_ACCESO"), 
	inverseJoinColumns = @JoinColumn(name = "ID_PERMISO", referencedColumnName = "ID_PERMISO"))
	@Fetch(FetchMode.SELECT)
	private Set<PermisoCliente> permisos;

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Set<PermisoCliente> getPermisos() {
		return permisos;
	}

	public void setPermisos(Set<PermisoCliente> permisos) {
		this.permisos = permisos;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
