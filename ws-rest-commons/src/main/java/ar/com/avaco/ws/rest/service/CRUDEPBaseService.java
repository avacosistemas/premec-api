package ar.com.avaco.ws.rest.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ar.com.avaco.arc.core.component.bean.service.NJService;
import ar.com.avaco.arc.core.domain.Entity;
import ar.com.avaco.arc.core.domain.filter.AbstractFilter;
import ar.com.avaco.commons.exception.BusinessException;
import ar.com.avaco.ws.rest.dto.DTOEntity;

public abstract class CRUDEPBaseService<ID extends Serializable, DTO extends DTOEntity<ID>, T extends Entity<ID>, S extends NJService<ID, T>>
		implements CRUDEPService<ID, DTO> {

	
	
	protected S service;
	
	@Override
	public DTO save(DTO dto) throws BusinessException {
		validationSave(dto);
		T entity = convertToEntity(dto);
		entity = service.save(entity);
		return convertToDto(entity);
	}

	protected void validationSave(DTO dto) {
		// Implementar en los hijos
	}
	
	protected void validationUpdate(DTO dto) {
		// Implementar en los hijos
	}

	@Override
	public DTO update(DTO dto) throws BusinessException {
		T entity = convertToEntity(dto);
		entity = service.update(entity);
		return convertToDto(entity);
	}

	@Override
	public List<DTO> save(Collection<DTO> dtos) {
		List<T> entities = convertToEntities(dtos);
		entities = service.save(entities);
		return convertToDtos(entities);
	}

	@Override
	public DTO get(ID id) {
		T t = service.get(id);
		return convertToDto(t);
	}

	@Override
	public List<DTO> list() {
		List<T> entities = service.list();
		List<DTO> dtos = convertToDtos(entities);
		return dtos;
	}

	@Override
	public void remove(ID id) {
		service.remove(id);
	}

	@Override
	public int listCount(AbstractFilter abstractFilter) {
		return 0;
	}

	@Override
	public List<DTO> listFilter(AbstractFilter abstractFilter) {
		return convertToDtos(service.listFilter(abstractFilter));
	}

	@Override
	public List<DTO> listPattern(String field, String pattern) {
		// TODO Auto-generated method stub
		return null;
	}

	abstract protected T convertToEntity(DTO dto);

	abstract protected DTO convertToDto(T entity);

	public List<T> convertToEntities(Collection<DTO> dtos) {
		List<T> entities = new ArrayList<T>();
		for (DTO dto : dtos) {
			entities.add(convertToEntity(dto));
		}
		return entities;
	}

	public List<DTO> convertToDtos(Collection<T> entities) {
		List<DTO> dtos = new ArrayList<DTO>();
		for (T entity : entities) {
			dtos.add(convertToDto(entity));
		}
		return dtos;
	}

	protected final S getService() {
		return this.service;
	}
	
	protected abstract void setService(S service);
	
}
