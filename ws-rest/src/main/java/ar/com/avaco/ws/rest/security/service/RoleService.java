/**
 * 
 */
package ar.com.avaco.ws.rest.security.service;

import ar.com.avaco.arc.sec.domain.Rol;
import ar.com.avaco.ws.rest.security.dto.Role;
import ar.com.avaco.ws.rest.service.ConvertService;

/**
 * @author avaco
 *
 */
public interface RoleService extends ConvertService<Role, Long, Rol>{
	
}
