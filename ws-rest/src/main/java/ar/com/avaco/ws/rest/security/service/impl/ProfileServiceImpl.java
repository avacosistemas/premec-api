/**
 * 
 */
package ar.com.avaco.ws.rest.security.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import ar.com.avaco.arc.sec.domain.Perfil;
import ar.com.avaco.arc.sec.domain.Permiso;
import ar.com.avaco.arc.sec.domain.Rol;
import ar.com.avaco.arc.sec.service.PerfilService;
import ar.com.avaco.ws.rest.security.dto.Permission;
import ar.com.avaco.ws.rest.security.dto.Profile;
import ar.com.avaco.ws.rest.security.dto.Role;
import ar.com.avaco.ws.rest.security.service.PermissionService;
import ar.com.avaco.ws.rest.security.service.ProfileService;
import ar.com.avaco.ws.rest.security.service.RoleService;
import ar.com.avaco.ws.rest.service.AbstractConvertService;

/**
 * @author avaco
 *
 */
@Transactional
@Service("profileService")
public class ProfileServiceImpl extends AbstractConvertService<Profile, Long, Perfil> implements ProfileService{
	
	@Resource(name = "permissionService")
	private PermissionService permissionService;
	
	@Resource(name = "roleService")
	private RoleService roleService;
	
	public Profile convertToDto(Perfil perfil) {
		List<Permission> permissions = new ArrayList<Permission>();
		for(Permiso p : perfil.getPermisos()) {
			permissions.add(permissionService.convertToDto(p));
		}
		
		Role role = roleService.convertToDto(perfil.getRol());
		return new Profile(perfil.getId(), perfil.getNombre(), role, permissions, perfil.isActivo());
	}


	@Override
	protected Perfil newEntity() {
		return new Perfil();
	}

	@Override
	public Perfil convertToEntity(Perfil entity, Profile dto) {
		
		if (dto.getId() == null) {
			entity = new Perfil();
			entity.setActivo(true);
			Role role = roleService.list().stream().filter(rol -> rol.getCode().equals("ADM")).findFirst().orElse(new Role());

			Rol r = new Rol();
			r.setId(role.getId());
			
			entity.setRol(roleService.convertToEntity(r,role));
		} else {
			entity = this.service.get(dto.getId());
		}
		
		entity.setNombre(dto.getName());
		
//		//FIXME Por default tiene el rol ADM, modificar cuando se requiera
//		List<Permiso> permisos = new  ArrayList<>();
//		for(Permission permission :dto.getPermissions()) {
//			Permiso p = new Permiso();
//			p.setId(permission.getId());
//			permisos.add(permissionService.convertToEntity(p, permission));
//		}
//		entity.getPermisos().addAll(permisos);
		return entity;
	}
	
	
	@Resource(name = "perfilService")
	public void setPermisoService(PerfilService perfilService) {
		this.service = perfilService;
	}
}
