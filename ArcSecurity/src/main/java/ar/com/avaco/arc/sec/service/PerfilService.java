package ar.com.avaco.arc.sec.service;

import java.util.List;

import ar.com.avaco.arc.core.component.bean.service.NJService;
import ar.com.avaco.arc.sec.domain.Perfil;
import ar.com.avaco.arc.sec.domain.Permiso;

/**
 * @author avaco
 */
public interface PerfilService extends NJService<Long, Perfil> {

	List<Permiso> listPermisosByPerfil(Long idPerfil);

	void agregarPermisoAPerfil(Long idPerfil, Long idPermiso);

	void quitarPermisoAPerfil(Long idPerfil, Long idPermiso);

}