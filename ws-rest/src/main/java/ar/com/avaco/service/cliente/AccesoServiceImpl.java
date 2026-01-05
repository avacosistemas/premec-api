package ar.com.avaco.service.cliente;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.com.avaco.arc.core.component.bean.service.NJBaseService;
import ar.com.avaco.arc.sec.domain.Acceso;
import ar.com.avaco.repository.cliente.AccesoRepository;

@Transactional
@Service("accesoService")
public class AccesoServiceImpl extends NJBaseService<Long, Acceso, AccesoRepository> implements AccesoService {

	@Resource(name = "accesoRepository")
	void setAccesoRepository(AccesoRepository accesoRepository) {
		this.repository = accesoRepository;
	}

	@Override
	public List<Acceso> list(Long usuarioId) {
		return this.getRepository().findByUsuarioId(usuarioId);
	}

}
