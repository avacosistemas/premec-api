package ar.com.avaco.ws.rest.service;
import ar.com.avaco.commons.exception.BusinessException;

/**
 * 
 */

/**
 * @author avaco
 *
 */
public interface FunctionBusiness<T, R>{
	R apply(T t) throws BusinessException;
}
