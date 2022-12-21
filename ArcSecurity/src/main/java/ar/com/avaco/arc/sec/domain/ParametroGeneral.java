package ar.com.avaco.arc.sec.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "PARAMETRO_GENERAL")
@SequenceGenerator(name = "PARAMETRO_GENERAL_SEQ", sequenceName = "PARAMETRO_GENERAL_SEQ")
public class ParametroGeneral {

	public static final String DATOS_GENERADOS = "DATOS_GENERADOS";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PARAMETRO_GENERAL_SEQ")
	@Column(name = "ID_PARAMETRO_GENERAL")
	private Long id;

	private String codigo;

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}