package ar.com.avaco.arc.sec.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import ar.com.avaco.arc.sec.domain.Acceso;
import ar.com.avaco.arc.sec.domain.ParametroGeneral;
import ar.com.avaco.arc.sec.domain.Perfil;
import ar.com.avaco.arc.sec.domain.Permiso;
import ar.com.avaco.arc.sec.domain.Rol;
import ar.com.avaco.arc.sec.domain.Usuario;
import ar.com.avaco.arc.sec.repository.ParametroGeneralRepository;
import ar.com.avaco.arc.sec.repository.UsuarioRepository;
import ar.com.avaco.arc.sec.service.InitialDataService;
import ar.com.avaco.arc.sec.service.PerfilService;
import ar.com.avaco.arc.sec.service.PermisoService;
import ar.com.avaco.arc.sec.service.RolService;
import ar.com.avaco.arc.sec.service.UsuarioService;

@Transactional
@Service
public class InitialDataServiceImpl implements InitialDataService {
	
	@Resource
	private UsuarioService usuarioService;
	
	private Map<String, Permiso> permisosAdmin;

	@Resource
	PermisoService permisoService;
	
	@Resource
	private RolService rolService;
	
	@Resource
	private PerfilService perfilService;
	
	@Resource
	private UsuarioRepository usuarioRepository;
	
	@Resource
	private ParametroGeneralRepository parametroGeneralRepository;

	private Usuario admin;
	private Perfil perfilAdmin;
	private Rol rolStandar;

	public void init() throws Exception {
		generarPermisos();
		generarRoles();
		generarPerfiles();
		generarUsuarios();

		parametroGeneralRepository.save(ParametroGeneral.DATOS_GENERADOS);
	}

	private void generarPerfiles() throws Exception {
		perfilAdmin = new Perfil();
		perfilAdmin.setActivo(true);
		perfilAdmin.setNombre("Administrador");
		
		List<Permiso> permisosAdminList = new ArrayList<Permiso>(permisosAdmin.values());
		perfilAdmin.setPermisos(permisosAdminList);
		perfilAdmin.setRol(rolStandar);
		perfilService.save(perfilAdmin);
	}
	
	
	
	private void generarRoles() {
		
		rolStandar = new Rol();
		rolStandar.setCodigo("Standar");
		rolStandar.setNombre("Standar");
		rolStandar.setSuperRol(true);
		
		rolService.save(rolStandar);
	}

	private void generarPermisos() {
		permisosAdmin = new HashMap<String, Permiso>();
		grabarPermiso("USUARIO_ACCESO", "Acceso ABM Usuarios");
		grabarPermiso("PERFIL_ACCESO", "Acceso ABM Perfiles");
		grabarPermiso("ROL_ACCESO", "Acceso ABM Roles");
		grabarPermiso("PERMISO_ACCESO", "Acceso ABM Permisos");
		grabarPermiso("CAMBIOPASSWORD_ACCESO", "Actualizar Contraseña");
		grabarPermiso("FAQ_ACCESO", "Acceso ABM FAQ");
		grabarPermiso("PREGUNTA_PERFIL_INVERSOR_ACCESO", "Acceso ABM Pregunta Perfil Inversor");
		grabarPermiso("PARAM_ACCESO", "Acceso ABM Parametros");
		permisoService.save(permisosAdmin.values());
	}

	private void grabarPermiso(String codigo, String descripcion) {
		Permiso p = new Permiso();
		p.setActivo(true);
		p.setCodigo(codigo);
		p.setDescripcion(descripcion);
		permisosAdmin.put(codigo, p);
	}

	private void generarUsuarios() {
		admin = new Usuario();
		admin.setApellido("Gonzalez");
		admin.setEmail("alosgonzalez@gmail.com");
		admin.setInterno(true);
		admin.setNombre("Alberto");
		admin.setUsername("admin");
		
		Acceso accesoAdmin = new Acceso();
		accesoAdmin.setPerfil(perfilAdmin);
		accesoAdmin.setUsuario(admin);
		admin.getAccesos().add(accesoAdmin);
		usuarioService.update(admin);
		
		admin.setIntentosFallidosLogin(0);
		admin.setBloqueado(false);
		admin.setPassword("$2a$10$9EmXkvYDoxA/rwi2.uxOue/I2Z2.6fzIubnhKnjFEQvCESbfzuqZm");
		admin.setRequiereCambioPassword(false);
		usuarioRepository.save(admin);
		
		
	}

	@Override
	public boolean isDatosGenerados() {
		return parametroGeneralRepository
				.getParametroGeneral(ParametroGeneral.DATOS_GENERADOS) != null;
	}
}