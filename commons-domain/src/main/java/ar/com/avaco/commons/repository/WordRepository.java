/**
 * 
 */
package ar.com.avaco.commons.repository;

import ar.com.avaco.arc.core.component.bean.repository.NJRepository;
import ar.com.avaco.commons.domain.Word;

/**
 * @author avaco
 *
 */
public interface WordRepository extends NJRepository<Long, Word>, WordRepositoryCustom {

	Word findByKeyEquals(String key);

}
