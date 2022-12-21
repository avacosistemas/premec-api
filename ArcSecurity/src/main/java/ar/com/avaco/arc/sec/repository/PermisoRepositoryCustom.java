package ar.com.avaco.arc.sec.repository;

import ar.com.avaco.arc.sec.domain.Permiso;

public interface PermisoRepositoryCustom {	
	Permiso getPermisoPorCodigo(String codigo);
}