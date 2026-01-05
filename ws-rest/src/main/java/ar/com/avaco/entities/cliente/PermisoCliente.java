package ar.com.avaco.entities.cliente;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "CLI_PERMISO_CLIENTE")
public class PermisoCliente extends ar.com.avaco.arc.core.domain.Entity<Long> implements GrantedAuthority {

	private static final long serialVersionUID = 9029064534314530730L;

	@Id
	@Column(name = "ID_PERMISO")
	private Long id;

	@Column
	private String codigo;

	@Column
	private String descripcion;

	@Override
	public String getAuthority() {
		return codigo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

}
