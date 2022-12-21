/**
 * 
 */
package ar.com.avaco.commons.repository.impl;

import javax.persistence.EntityManager;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import ar.com.avaco.arc.core.component.bean.repository.NJBaseRepository;
import ar.com.avaco.commons.domain.I18n;
import ar.com.avaco.commons.repository.I18nRepositoryCustom;

/**
 * 
 *
 */
@Repository("i18nRepository")
public class I18nRepositoryImpl extends NJBaseRepository<Long, I18n> implements I18nRepositoryCustom {

	public I18nRepositoryImpl(EntityManager entityManager) {
		super(I18n.class, entityManager);
	}

	@Override
	public I18n getByNameAndLang(String name, String lang) {
		Query query = getCurrentSession().createQuery("from I18n as i18n " +
		        											"where i18n.name = :name");
//		query.setParameter("lang", lang);
		query.setParameter("name", name);
		return (I18n) query.uniqueResult();
	}
}

