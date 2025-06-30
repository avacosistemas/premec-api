package ar.com.avaco.repository.cliente;

import java.util.List;

import ar.com.avaco.arc.core.component.bean.repository.NJRepository;
import ar.com.avaco.arc.sec.domain.Acceso;

public interface AccesoRepository extends NJRepository<Long, Acceso>, AccesoRepositoryCustom {

	List<Acceso> findByUsuarioId(Long usuarioId);

}
