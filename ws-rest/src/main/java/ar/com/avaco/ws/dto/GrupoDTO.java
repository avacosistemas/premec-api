package ar.com.avaco.ws.dto;

import java.util.ArrayList;
import java.util.List;

public class GrupoDTO {

	private String titulo;

	private List<CheckListItemDTO> checklist = new ArrayList<CheckListItemDTO>();

	public GrupoDTO() {
	}

	public GrupoDTO(String titulo) {
		super();
		this.titulo = titulo;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public List<CheckListItemDTO> getChecklist() {
		return checklist;
	}

	public void setChecklist(List<CheckListItemDTO> checklist) {
		this.checklist = checklist;
	}
	
	public void agregarItem(String titulo) {
		checklist.add(new CheckListItemDTO(titulo));
	}

}
