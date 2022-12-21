/**
 * 
 */
package ar.com.avaco.ws.rest.security.service;

import ar.com.avaco.arc.sec.domain.Permiso;
import ar.com.avaco.ws.rest.security.dto.Permission;
import ar.com.avaco.ws.rest.service.ConvertService;

/**
 * @author avaco
 *
 */
public interface PermissionService extends ConvertService<Permission, Long, Permiso>{
	
}
