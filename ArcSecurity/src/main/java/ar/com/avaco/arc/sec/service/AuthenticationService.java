/**
 * 
 */
package ar.com.avaco.arc.sec.service;

import java.io.Serializable;

/**
 * The Authentication Service check application user authentication.
 * 
 * @author avaco
 *
 */
public interface AuthenticationService extends Serializable{

	/**
	 * This method check user with Authentication Manager
	 * @param user
	 * @param password
	 */
	void authenticate(String user, String password);
}
