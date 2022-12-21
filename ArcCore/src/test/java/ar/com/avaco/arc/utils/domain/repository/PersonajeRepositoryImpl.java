package ar.com.avaco.arc.utils.domain.repository;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import ar.com.avaco.arc.core.component.bean.repository.NJBaseRepository;
import ar.com.avaco.arc.utils.domain.Personaje;

@Repository
public class PersonajeRepositoryImpl extends NJBaseRepository<Long, Personaje> implements PersonajeRepositoryCustom {

	public PersonajeRepositoryImpl(EntityManager entityManager) {
		super(Personaje.class, entityManager);
	}
}