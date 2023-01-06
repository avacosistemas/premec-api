/**
 * 
 */
package ar.com.avaco.ws.rest.security.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import ar.com.avaco.arc.sec.domain.Acceso;
import ar.com.avaco.arc.sec.domain.Perfil;
import ar.com.avaco.arc.sec.domain.Usuario;
import ar.com.avaco.arc.sec.service.UsuarioService;
import ar.com.avaco.commons.exception.ErrorValidationException;
import ar.com.avaco.ws.rest.security.dto.Profile;
import ar.com.avaco.ws.rest.security.dto.UpdatePasswordDTO;
import ar.com.avaco.ws.rest.security.dto.User;
import ar.com.avaco.ws.rest.security.service.ProfileService;
import ar.com.avaco.ws.rest.security.service.UserService;
import ar.com.avaco.ws.rest.service.AbstractConvertService;

/**
 * @author avaco
 *
 */
@Transactional
@Service("userService")
public class UserServiceImpl extends AbstractConvertService<User, Long, Usuario> implements UserService {
	
	@Resource(name = "profileService")
	private ProfileService profileService;
    
	private UsuarioService usuarioService;
	
	public User convertToDto(Usuario usuario) {
		Set<Profile> profiles = new HashSet<>();
		if(usuario.getAccesos() != null) {			
			usuario.getAccesos().stream().forEach(e -> {
				profiles.add(profileService.convertToDto(e.getPerfil()));
			});
		}
		return new User(usuario.getId(),usuario.getUsername(), usuario.getNombre(), usuario.getApellido(), profiles, usuario.getEmail(), usuario.getUsuariosap(), usuario.isEnabled());
	}


	@Override
	protected Usuario newEntity() {
		return new Usuario();
	}

	@Override
	public Usuario convertToEntity(Usuario entity, User dto) {
		entity.setApellido(dto.getLastname());
		entity.setEmail(dto.getEmail());
		entity.setNombre(dto.getName());
		entity.setUsername(dto.getUsername());
		entity.setBloqueado(!dto.isEnabled());
		entity.setUsuariosap(dto.getUsuariosap());
		if(dto.getProfiles() != null) {			
			Set<Acceso> accesos = new HashSet<>();
			dto.getProfiles().stream().forEach(e -> {
				Acceso acceso = new Acceso();
				Perfil p = new Perfil();
				p.setId(e.getId());
				acceso.setPerfil(profileService.convertToEntity(p, e));
				acceso.setUsuario(entity);
				accesos.add(acceso);
			});
			entity.setAccesos(accesos);
		}
		return entity;
	}
	
	
	@Resource(name = "usuarioService")
	public void setUsuarioService(UsuarioService usuarioService) {
		this.service = usuarioService;
		this.usuarioService = usuarioService;
	}

	@Override
	public User saveUser(User user) {
		return convertToDto(usuarioService.save(convertToEntity(newEntity(), user)));
	}

	public UsuarioService getService() {
		return (UsuarioService) this.service;
	}

	@Override
	public void updatePassword(UpdatePasswordDTO resetPassword) {
		String username = resetPassword.getUsername();
		Usuario loadUserByUsername = (Usuario) getService().loadUserByUsername(username);
		getService().updatePassword(loadUserByUsername, resetPassword.getPassword(), resetPassword.getNewPassword());
	}


	@Override
	public void updateValidation(User user) throws ErrorValidationException {
		Map<String, String> errores = new HashMap<>();
		//Se valida que el nombre de usuario sea distinto de los existentes
		Usuario usuario = this.service.get(user.getId());
		
		if(!usuario.getUsername().equals(user.getUsername()) && 
				getService().isUserExists(user.getUsername())){
			errores.put("username", "Este nombre de usuario ya existe");
		}
		
		if(!usuario.getEmail().equals(user.getEmail()) &&
				getService().isUserExistWithEmail(user.getEmail())){
			errores.put("email", "Este email ya se encuentra registrado");
		}
		
		if(!errores.isEmpty()) {
			throw new ErrorValidationException("Se han encontrado errores de negocio en el objeto analizado", errores);
		}
	}


	@Override
	public User getByUsername(String username) {
		return convertToDto(getService().findByUsername(username));
	}
	
}
