package ar.com.avaco.arc.sec.repository;

import ar.com.avaco.arc.sec.domain.Rol;
import ar.com.avaco.arc.core.component.bean.repository.NJRepository;

public interface RolRepository extends NJRepository<Long, Rol>,
		RolRepositoryCustom {

}