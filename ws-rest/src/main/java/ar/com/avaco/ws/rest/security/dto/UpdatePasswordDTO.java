/**
 * 
 */
package ar.com.avaco.ws.rest.security.dto;

import java.io.Serializable;

/**
 * @author avaco
 *
 */
public class UpdatePasswordDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4721942938138967801L;
	private String username;
	private String currentPassword;
	private String newPassword;

	public UpdatePasswordDTO() {

	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String password) {
		this.currentPassword = password;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
}
