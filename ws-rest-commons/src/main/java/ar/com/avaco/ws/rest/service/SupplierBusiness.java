package ar.com.avaco.ws.rest.service;
import ar.com.avaco.commons.exception.BusinessException;

/**
 * 
 */

/**
 * @author avaco
 *
 */
public interface SupplierBusiness<T>{
	T get() throws BusinessException;
}
