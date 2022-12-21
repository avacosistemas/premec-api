/**
 * 
 */
package ar.com.avaco.commons.repository;

import ar.com.avaco.arc.core.component.bean.repository.NJRepository;
import ar.com.avaco.commons.domain.ContactUS;

/**
 * @author avaco
 *
 */
public interface ContactUSRepository extends NJRepository<Long,ContactUS>, ContactUSRepositoryCustom {
	
}
