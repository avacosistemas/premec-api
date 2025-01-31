package ar.com.avaco.ws.dto;

import java.util.ArrayList;
import java.util.List;

import ar.com.avaco.ws.rest.dto.DTOEntity;

public class GrupoTipoActividadDTO extends DTOEntity<Long> {

	private Long id;

	private String tipo;

	private String titulo;

	private Integer orden;

	private String tipoString;

	private String tituloTipo;

	private List<ItemChecklistGrupoDTO> items = new ArrayList<ItemChecklistGrupoDTO>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Integer getOrden() {
		return orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	public List<ItemChecklistGrupoDTO> getItems() {
		return items;
	}

	public void setItems(List<ItemChecklistGrupoDTO> items) {
		this.items = items;
	}

	public String getTipoString() {
		return tipoString;
	}

	public void setTipoString(String tipoString) {
		this.tipoString = tipoString;
	}

	public String getTituloTipo() {
		return tituloTipo;
	}

	public void setTituloTipo(String tituloTipo) {
		this.tituloTipo = tituloTipo;
	}

}
