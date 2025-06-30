package ar.com.avaco.repository.cliente;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import ar.com.avaco.arc.core.component.bean.repository.NJBaseRepository;
import ar.com.avaco.arc.sec.domain.Acceso;

/**
 * Repositorio de Accesos
 * 
 * @author betongo
 *
 */
@Repository("accesoRepository")
public class AccesoRepositoryImpl extends NJBaseRepository<Long, Acceso> implements AccesoRepositoryCustom {

	public AccesoRepositoryImpl(EntityManager entityManager) {
		super(Acceso.class, entityManager);
	}

}
