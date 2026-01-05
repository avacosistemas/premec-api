package ar.com.avaco.exception;

public class AulaVirtualException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4230608683674955098L;

	private Integer messsageKey;

	private String message;

	public AulaVirtualException(Integer messsageKey, String message) {
		super();
		this.messsageKey = messsageKey;
		this.message = message;
	}

	public Integer getMesssageKey() {
		return messsageKey;
	}

	public void setMesssageKey(Integer messsageKey) {
		this.messsageKey = messsageKey;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
