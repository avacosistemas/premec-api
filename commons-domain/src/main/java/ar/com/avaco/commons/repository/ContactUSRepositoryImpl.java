/**
 * 
 */
package ar.com.avaco.commons.repository;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import ar.com.avaco.arc.core.component.bean.repository.NJBaseRepository;
import ar.com.avaco.commons.domain.ContactUS;

/**
 * 
 *
 */
@Repository("contactUSRepository")
public class ContactUSRepositoryImpl extends NJBaseRepository<Long, ContactUS> implements ContactUSRepositoryCustom {

	public ContactUSRepositoryImpl(EntityManager entityManager) {
		super(ContactUS.class, entityManager);
	}
}

