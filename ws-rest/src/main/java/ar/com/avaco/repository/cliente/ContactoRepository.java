package ar.com.avaco.repository.cliente;

import java.util.List;

import ar.com.avaco.arc.core.component.bean.repository.NJRepository;
import ar.com.avaco.entities.cliente.Contacto;

public interface ContactoRepository extends NJRepository<Long, Contacto>, ContactoRepositoryCustom {
	
	List<Contacto> findAllByClienteId(Long idCliente);

}
