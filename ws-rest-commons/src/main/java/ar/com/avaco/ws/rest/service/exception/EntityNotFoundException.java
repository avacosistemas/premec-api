/**
 * 
 */
package ar.com.avaco.ws.rest.service.exception;

import ar.com.avaco.commons.exception.BusinessException;

/**
 * @author avaco
 *
 */
public class EntityNotFoundException extends BusinessException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -383811933611356052L;

	public EntityNotFoundException() {
		super();
	}

	public EntityNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public EntityNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public EntityNotFoundException(String message) {
		super(message);
	}

	public EntityNotFoundException(Throwable cause) {
		super(cause);
	}
}
