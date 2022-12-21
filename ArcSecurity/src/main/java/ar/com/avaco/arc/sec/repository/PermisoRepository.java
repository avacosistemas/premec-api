package ar.com.avaco.arc.sec.repository;

import ar.com.avaco.arc.sec.domain.Permiso;
import ar.com.avaco.arc.core.component.bean.repository.NJRepository;

public interface PermisoRepository extends NJRepository<Long, Permiso>,
		PermisoRepositoryCustom {
	
}