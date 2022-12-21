package ar.com.avaco.arc.utils.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import ar.com.avaco.arc.core.component.bean.repository.NJRepository;
import ar.com.avaco.arc.utils.domain.Personaje;

public interface PersonajeRepository extends NJRepository<Long, Personaje>, PersonajeRepositoryCustom {

	List<Personaje> getByNombre(String nombre);

	int countByNombre(String nombre);

	List<Personaje> getByNombreContaining(String pattern);

	@Query("select count(e) > 0 from Personaje e where nombre = ?1")
	boolean existsByNombre(String nombre);

	void removeByNombre(String nombre);

}
