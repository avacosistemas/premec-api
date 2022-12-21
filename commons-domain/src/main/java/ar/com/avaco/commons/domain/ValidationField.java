/**
 * 
 */
package ar.com.avaco.commons.domain;

/**
 * @author avaco
 *
 */
public class ValidationField {
	public static final String REGEX = "REGEX";
	private String key;
	private String input;
	private String messageKey;
	private String message;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getInput() {
		return input;
	}
	public void setInput(String input) {
		this.input = input;
	}
	public String getMessageKey() {
		return messageKey;
	}
	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
