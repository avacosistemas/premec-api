/**
 * 
 */
package ar.com.avaco.commons.repository;

import ar.com.avaco.commons.domain.Parameter;
import ar.com.avaco.arc.core.component.bean.repository.NJRepository;

/**
 * @author avaco
 *
 */
public interface ParameterRepository extends NJRepository<Integer, Parameter>, ParameterRepositoryCustom {

	Parameter findByKeyEquals(String key);

}
