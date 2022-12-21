/**
 * 
 */
package ar.com.avaco.commons.service;

import ar.com.avaco.commons.domain.Parameter;
import ar.com.avaco.commons.exception.BusinessException;
import ar.com.avaco.arc.core.component.bean.service.NJService;

/**
 * 
 *
 */
public interface ParameterService extends NJService<Integer, Parameter>{

	/**
	 * Finds a parameter by its key. if not found throws {@BusinessException}
	 * @param key The key.
	 * @return a String
	 * @throws BusinessException 
	 */
	public String findByKey(String key) throws BusinessException;

}
