/**
 * 
 */
package ar.com.avaco.ws.rest.security.service.impl;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import ar.com.avaco.arc.sec.domain.Permiso;
import ar.com.avaco.arc.sec.service.PermisoService;
import ar.com.avaco.ws.rest.security.dto.Permission;
import ar.com.avaco.ws.rest.security.service.PermissionService;
import ar.com.avaco.ws.rest.service.AbstractConvertService;

/**
 * @author avaco
 *
 */
@Transactional
@Service("permissionService")
public class PermissionServiceImpl extends AbstractConvertService<Permission, Long, Permiso> implements PermissionService{
	
	public Permission convertToDto(Permiso permiso) {
		return new Permission(permiso.getId(), permiso.getCodigo(), permiso.getDescripcion(), permiso.getAuthority() != null );
	}
	
	@Resource(name = "permisoService")
	public void setPermisoService(PermisoService permisoService) {
		this.service = permisoService;
	}

	@Override
	protected Permiso newEntity() {
		return new Permiso();
	}

	@Override
	public Permiso convertToEntity(Permiso entity, Permission dto) {
		entity.setActivo(dto.getEnabled());
		entity.setCodigo(dto.getCode());
		entity.setDescripcion(dto.getDescription());
		return entity;
	}

}
