/**
 * 
 */
package ar.com.avaco.commons.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * @author avaco
 *
 */
public class ErrorValidationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -383811933611356052L;
	
	private Map<String, String> errors;
	
	public ErrorValidationException() {
		super();
	}

	public ErrorValidationException(String message, String field, String error) {
		super(message);
		Map<String, String> map = new HashMap<String, String>();
		map.put(field, error);
		this.errors = map;
	}

	public ErrorValidationException(Map<String, String> errors) {
		super("Se encontraron los siguientes errores");
		this.errors = errors;
	}
	
	public ErrorValidationException(String message, Map<String, String> errors) {
		super(message);
		this.errors = errors;
	}

	public Map<String, String> getErrors() {
		return errors;
	}

	public void setErrors(Map<String, String> errors) {
		this.errors = errors;
	}
	
}
