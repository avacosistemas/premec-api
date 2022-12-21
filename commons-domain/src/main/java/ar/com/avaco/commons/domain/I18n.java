/**
 * 
 */
package ar.com.avaco.commons.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 * @author avaco
 */

@Entity
@Table(name="I18N")
@SequenceGenerator(name = "I18N_SEQ", sequenceName = "I18N_SEQ", allocationSize = 1)
public class I18n extends ar.com.avaco.arc.core.domain.Entity<Long> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8168268668794827990L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "I18N_SEQ")
	@Column(name = "ID_I18N")
    private Long id;
	
	@Column(name="lang")
    private String lang;
	
	@Column(name="description")
	private String description;
	
	@OneToMany(mappedBy = "i18n", orphanRemoval = true, cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<Word> words = new HashSet<Word>(); 
	
    public I18n() {
		super();
	}
    
	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Word> getWords() {
		return words;
	}

	public void setWords(Set<Word> words) {
		this.words = words;
	}
	
}