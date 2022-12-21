package ar.com.avaco.commons.domain;

import javax.persistence.Column;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import ar.com.avaco.arc.core.domain.Entity;

@javax.persistence.Entity
@Table(name="WORD")
@SequenceGenerator(name = "WORD_SEQ", sequenceName = "WORD_SEQ", allocationSize = 1)
public class Word extends Entity<Long> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8420587604061094816L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WORD_SEQ")
	@Column(name = "ID_WORD")
    private Long id;
	
	/**
	 * Se mapea el diccionario solo por temas de configuracion.
	 */
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "I18N_ID_FK"), nullable=false)
	private I18n i18n;
	
	@Column(name="key", nullable=false)
    private String key;
	
	@Column(name="value", nullable=false)
    private String value;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public I18n getI18n() {
		return i18n;
	}

	public void setI18n(I18n i18n) {
		this.i18n = i18n;
	}
	
}

