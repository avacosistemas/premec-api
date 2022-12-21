package ar.com.avaco.arc.utils.domain.service;

import java.util.List;

import ar.com.avaco.arc.core.component.bean.service.NJService;
import ar.com.avaco.arc.utils.domain.Personaje;

public interface PersonajeService extends NJService<Long, Personaje> {
	
	List<Personaje> getByNombre(String nombre);
	
	int countByNombre(String nombre);
	
	List<Personaje> getByNombreContaining(String pattern);
	
	boolean existsByNombre(String nombre);
	
	void removeByNombre(String nombre);
}