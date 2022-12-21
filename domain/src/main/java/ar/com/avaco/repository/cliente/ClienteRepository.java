package ar.com.avaco.repository.cliente;

import ar.com.avaco.arc.core.component.bean.repository.NJRepository;
import ar.com.avaco.entities.cliente.Cliente;

public interface ClienteRepository extends NJRepository<Long, Cliente>, ClienteRepositoryCustom {

	Cliente findByRazonSocialEqualsIgnoreCase(String razonSocial);

}
