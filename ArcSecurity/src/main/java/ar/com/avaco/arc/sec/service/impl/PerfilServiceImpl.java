package ar.com.avaco.arc.sec.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import ar.com.avaco.arc.sec.domain.Perfil;
import ar.com.avaco.arc.sec.domain.Permiso;
import ar.com.avaco.arc.sec.repository.PerfilRepository;
import ar.com.avaco.arc.sec.service.PerfilService;
import ar.com.avaco.arc.sec.service.PermisoService;
import ar.com.avaco.arc.core.component.bean.service.NJBaseService;

@Transactional
@Service("perfilService")
public class PerfilServiceImpl extends
		NJBaseService<Long, Perfil, PerfilRepository> implements
		PerfilService {

	private PermisoService permisoService;
	
	@Override
	public List<Permiso> listPermisosByPerfil(Long idPerfil) {
		Perfil one = this.repository.getOne(idPerfil);
		return new ArrayList<Permiso>(one.getPermisos());
	}
	
	@Resource(name = "perfilRepository")
	public void setPerfilRepository(PerfilRepository perfilRepository) {
		repository = perfilRepository;
	}

	@Override
	public void agregarPermisoAPerfil(Long idPerfil, Long idPermiso) {
		Permiso permiso = this.permisoService.get(idPermiso);
		Perfil one = this.repository.getOne(idPerfil);
		one.getPermisos().add(permiso);
		this.repository.save(one);
	}

	@Override
	public void quitarPermisoAPerfil(Long idPerfil, Long idPermiso) {
		Permiso permiso = this.permisoService.get(idPermiso);
		Perfil one = this.repository.getOne(idPerfil);
		one.getPermisos().remove(permiso);
		this.repository.save(one);
	}

	
	@Resource(name = "permisoService")
	public void setPermisoService(PermisoService permisoService) {
		this.permisoService = permisoService;
	}
}