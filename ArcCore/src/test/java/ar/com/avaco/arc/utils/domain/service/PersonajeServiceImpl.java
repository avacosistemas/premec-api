package ar.com.avaco.arc.utils.domain.service;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import ar.com.avaco.arc.core.component.bean.service.NJBaseService;
import ar.com.avaco.arc.utils.domain.Personaje;
import ar.com.avaco.arc.utils.domain.repository.PersonajeRepository;

@Service
@Transactional
public class PersonajeServiceImpl extends NJBaseService<Long, Personaje, PersonajeRepository> implements PersonajeService {

	@Override
	public List<Personaje> getByNombre(String nombre) {
		return getRepository().getByNombre(nombre);
	}

	@Override
	public int countByNombre(String nombre) {
		return getRepository().countByNombre(nombre);
	}

	@Override
	public List<Personaje> getByNombreContaining(String pattern) {
		return getRepository().getByNombreContaining(pattern);
	}

	@Override
	public boolean existsByNombre(String nombre) {
		return getRepository().existsByNombre(nombre);
	}

	@Override
	public void removeByNombre(String nombre) {
		getRepository().removeByNombre(nombre);
	}

	@Resource
	public void setPersonaRepository(PersonajeRepository personaRepository) {
		this.repository = personaRepository;
	}
	
}