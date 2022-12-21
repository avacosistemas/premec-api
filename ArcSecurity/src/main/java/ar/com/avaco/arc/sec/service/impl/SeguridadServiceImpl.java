package ar.com.avaco.arc.sec.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import ar.com.avaco.arc.sec.domain.Acceso;
import ar.com.avaco.arc.sec.domain.Usuario;
import ar.com.avaco.arc.sec.service.SeguridadService;

/**
 * @author HDimitri
 */
@Service
@Transactional
public class SeguridadServiceImpl implements SeguridadService {
		
	/** */
	private static final long serialVersionUID = -8229748635639912282L;

	public List<Acceso> getAccesosConPermiso(Usuario usuario, String permiso) {
		List<Acceso> accesosList = new ArrayList<Acceso>();
		
		for (Acceso ac : usuario.getAccesos()) {
			if (ac.getPerfil().getPermisosString().contains(permiso)) {
				accesosList.add(ac);
			}
		}
		return accesosList;
	}

	public boolean isSuperRol(Usuario usuario, String permiso, List<Acceso> accesos) {
		boolean isSuperRol = false;
		Iterator<Acceso> iter = accesos.iterator();
		while (iter.hasNext() && !isSuperRol) {
			Acceso acc = iter.next();
			isSuperRol = acc.getPerfil().getRol().isSuperRol();
		}
		return isSuperRol;
	}

	public boolean hasPermiso(Usuario usuario,String permiso){
		boolean hasPermiso = false;
		if(permiso != null && !permiso.trim().equals("")){
			for(GrantedAuthority authoritie : usuario.getAuthorities()){
				if(permiso.equals(authoritie.getAuthority())){
					hasPermiso = true;
					break;
				}
			}
		}
		return hasPermiso;
	}

	@Override
	public boolean hasRol(Usuario usuario, String permiso, String rol) {
		return usuario.hasRol(permiso, rol);
	}

	@Override
	public boolean hasRolYPermisos(Usuario usuario,List<String> permisos, String rol) {
		boolean hasRolYPermisos = true;
		Iterator<String> it = permisos.iterator();
		while(hasRolYPermisos && it.hasNext()){
			hasRolYPermisos = usuario.hasRol(it.next(), rol);
		}	
		return hasRolYPermisos;
	}
}