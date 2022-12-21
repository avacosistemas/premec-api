/**
 * 
 */
package ar.com.avaco.commons.service;

import ar.com.avaco.commons.domain.FormDef;
import ar.com.avaco.commons.exception.ErrorValidationException;

/**
 * @author avaco
 *
 */
public interface ValidationFormDefService {
	void validate(Object dto,FormDef formDef) throws ErrorValidationException;
}
