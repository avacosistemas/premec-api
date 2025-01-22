package ar.com.avaco.commons.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "ITEM_CHECKLIST_GRUPO")
@SequenceGenerator(name = "ITEM_CHECKLIST_GRUPO_SEQ", sequenceName = "ITEM_CHECKLIST_GRUPO_SEQ", allocationSize = 1)
public class ItemChecklistGrupo extends ar.com.avaco.arc.core.domain.Entity<Long> {

	private static final long serialVersionUID = -3260414467249766837L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ITEM_CHECKLIST_GRUPO_SEQ")
	@Column(name = "ID_ITEM_CHECKLIST_GRUPO")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "ID_GRUPO", nullable = false)
	private GrupoTipoActividad grupo;

	@Column(name = "NOMBRE", nullable = false)
	private String nombre;

	@Column(name = "ORDEN")
	private Integer orden;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public GrupoTipoActividad getGrupo() {
		return grupo;
	}

	public void setGrupo(GrupoTipoActividad grupo) {
		this.grupo = grupo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getOrden() {
		return orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

}
