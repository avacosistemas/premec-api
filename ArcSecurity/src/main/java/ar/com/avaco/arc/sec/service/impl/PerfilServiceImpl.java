package ar.com.avaco.arc.sec.service.impl;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import ar.com.avaco.arc.sec.domain.Perfil;
import ar.com.avaco.arc.sec.repository.PerfilRepository;
import ar.com.avaco.arc.sec.service.PerfilService;
import ar.com.avaco.arc.core.component.bean.service.NJBaseService;

@Transactional
@Service("perfilService")
public class PerfilServiceImpl extends
		NJBaseService<Long, Perfil, PerfilRepository> implements
		PerfilService {

	@Resource(name = "perfilRepository")
	public void setPerfilRepository(PerfilRepository perfilRepository) {
		repository = perfilRepository;
	}
}