/**
 * 
 */
package ar.com.avaco.ws.rest.security.service.impl;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import ar.com.avaco.arc.sec.domain.Rol;
import ar.com.avaco.arc.sec.service.RolService;
import ar.com.avaco.ws.rest.security.dto.Role;
import ar.com.avaco.ws.rest.security.service.RoleService;
import ar.com.avaco.ws.rest.service.AbstractConvertService;

/**
 * @author avaco
 *
 */
@Transactional
@Service("roleService")
public class RoleServiceImpl extends AbstractConvertService<Role, Long, Rol> implements RoleService{
	
	public Role convertToDto(Rol permiso) {
		return new Role(permiso.getId(), permiso.getCodigo(), permiso.getNombre());
	}

	@Resource(name = "rolService")
	public void setRolService(RolService rolService) {
		this.service = rolService;
	}

	@Override
	protected Rol newEntity() {
		return new Rol();
	}

	@Override
	public Rol convertToEntity(Rol entity, Role dto) {
		entity.setNombre(dto.getName());
		entity.setCodigo(dto.getCode());
		return entity;
	}
}
