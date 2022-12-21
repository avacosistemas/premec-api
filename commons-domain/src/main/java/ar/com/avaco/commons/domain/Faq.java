/**
 * 
 */
package ar.com.avaco.commons.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author avaco
 */

@Entity
@Table(name = "FAQ")
@SequenceGenerator(name = "FAQ_SEQ", sequenceName = "FAQ_SEQ", allocationSize = 1)
public class Faq extends ar.com.avaco.arc.core.domain.Entity<Long> implements Serializable {

	public static final String CATEGORY_PRESTAMOS = "prestamos";
	public static final String CATEGORY_LEGALES = "legales";
	public static final String CATEGORY_FACTORING = "factoring";
	public static final String CATEGORY_SEGUROS = "seguros";
	public static final String CATEGORY_MCAPITALES = "mcapitales";

	public static final String SUBCATEGORY_SOLICITANTE = "solicitante";
	public static final String SUBCATEGORY_INVERSOR = "inversor";
	public static final String SUBCATEGORY_LEGALES = "legales";

	/**
	 * 
	 */
	private static final long serialVersionUID = 8168268668794827990L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FAQ_SEQ")
	@Column(name = "ID_FAQ")
	private Long id;

	@Column(name = "order_faq")
	private Integer orderFaq;

	@Column(name = "favourite")
	private Boolean favourite;

	@Column(name = "lang")
	private String lang;

	@Column(name = "category")
	private String category;

	@Column(name = "subcategory")
	private String subcategory;

	@Column(name = "question", columnDefinition = "TEXT", nullable = false)
	private String question;

	@Column(name = "answer", columnDefinition = "TEXT", nullable = false)
	private String answer;

	public Faq() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getOrderFaq() {
		return orderFaq;
	}

	public void setOrderFaq(Integer orderFaq) {
		this.orderFaq = orderFaq;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubcategory() {
		return subcategory;
	}

	public void setSubcategory(String subcategory) {
		this.subcategory = subcategory;
	}

	public Boolean getFavourite() {
		return favourite;
	}

	public void setFavourite(Boolean favourite) {
		this.favourite = favourite;
	}

}