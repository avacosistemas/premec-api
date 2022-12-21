/**
 * 
 */
package ar.com.avaco.ws.rest.security.service;

import ar.com.avaco.arc.sec.domain.Usuario;
import ar.com.avaco.commons.exception.ErrorValidationException;
import ar.com.avaco.ws.rest.security.dto.UpdatePasswordDTO;
import ar.com.avaco.ws.rest.security.dto.User;
import ar.com.avaco.ws.rest.service.ConvertService;

/**
 * @author avaco
 *
 */
public interface UserService extends ConvertService<User, Long, Usuario>{
	void updatePassword(UpdatePasswordDTO resetPassword);
	void updateValidation(User user) throws ErrorValidationException;
	User saveUser(User user) throws ErrorValidationException;
	User getByUsername(String username);
}
