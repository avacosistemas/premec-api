package ar.com.avaco.arc.sec.service.impl;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import ar.com.avaco.arc.sec.domain.Permiso;
import ar.com.avaco.arc.sec.repository.PermisoRepository;
import ar.com.avaco.arc.sec.service.PermisoService;
import ar.com.avaco.arc.core.component.bean.service.NJBaseService;

@Transactional
@Service("permisoService")
public class PermisoServiceImpl extends
	NJBaseService<Long, Permiso, PermisoRepository> implements
		PermisoService {

	@Resource(name = "permisoRepository")
	public void setPermisoRepository(PermisoRepository permisoRepository) {
		repository = permisoRepository;
	}
}