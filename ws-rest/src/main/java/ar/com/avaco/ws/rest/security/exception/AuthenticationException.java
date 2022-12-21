package ar.com.avaco.ws.rest.security.exception;

public class AuthenticationException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6752356305868357493L;

	public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}