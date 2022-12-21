
/**
 * 
 */
package ar.com.avaco.commons.exception;


public class BackendException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6544913921417375986L;

	public BackendException() {
		super();
	}

	public BackendException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public BackendException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public BackendException(String arg0) {
		super(arg0);
	}

	public BackendException(Throwable arg0) {
		super(arg0);
	}
}
