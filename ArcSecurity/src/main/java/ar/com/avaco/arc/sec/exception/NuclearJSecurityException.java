package ar.com.avaco.arc.sec.exception;

public class NuclearJSecurityException extends RuntimeException {

	private static final long serialVersionUID = -8310769991335810592L;

	public NuclearJSecurityException(String message, Throwable t) {
		super(message, t);
	}

	public NuclearJSecurityException(String message) {
		super(message);
	}
}