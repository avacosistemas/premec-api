package ar.com.avaco.commons.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "GRUPO_TIPO_ACTIVIDAD")
@SequenceGenerator(name = "GRUPO_TIPO_ACTIVIDAD_SEQ", sequenceName = "GRUPO_TIPO_ACTIVIDAD_SEQ", allocationSize = 1)
public class GrupoTipoActividad extends ar.com.avaco.arc.core.domain.Entity<Long> {

	private static final long serialVersionUID = -3260414467249766837L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GRUPO_TIPO_ACTIVIDAD_SEQ")
	@Column(name = "ID_GRUPO_TIPO_ACTIVIDAD")
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_ACTIVIDAD", nullable = false)
	private TipoActividad tipo;

	@Column(name = "TITULO", nullable = false)
	private String titulo;

	@Column(name = "ORDEN")
	private Integer orden;

	@OneToMany(mappedBy = "grupo", orphanRemoval = true)
	@LazyCollection(LazyCollectionOption.FALSE)
	@OrderBy("orden")
	private Set<ItemChecklistGrupo> items = new HashSet<ItemChecklistGrupo>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TipoActividad getTipo() {
		return tipo;
	}

	public void setTipo(TipoActividad tipo) {
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

	public Set<ItemChecklistGrupo> getItems() {
		return items;
	}

	public void setItems(Set<ItemChecklistGrupo> items) {
		this.items = items;
	}

}
