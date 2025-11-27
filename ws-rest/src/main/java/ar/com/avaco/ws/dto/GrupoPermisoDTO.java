package ar.com.avaco.ws.dto;

import ar.com.avaco.arc.sec.domain.Permiso;

public class GrupoPermisoDTO {

	private Long id;

	private String codigo;

	private String descripcion;

	private boolean activo;

	private Long idGrupo;

	public GrupoPermisoDTO() {
	}

	public GrupoPermisoDTO(Permiso permiso, Long idGrupo) {
		this.id = permiso.getId();
		this.codigo = permiso.getCodigo();
		this.descripcion = permiso.getDescripcion();
		this.activo = permiso.isActivo();
		this.idGrupo = idGrupo;
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

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public Long getIdGrupo() {
		return idGrupo;
	}

	public void setIdGrupo(Long idGrupo) {
		this.idGrupo = idGrupo;
	}

}
