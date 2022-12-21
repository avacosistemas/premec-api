/**
 * 
 */
package ar.com.avaco.ws.rest.service;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ar.com.avaco.arc.core.component.bean.repository.NJRepository;
import ar.com.avaco.arc.core.component.bean.service.NJService;
import ar.com.avaco.arc.core.domain.Entity;
import ar.com.avaco.arc.core.domain.filter.AbstractFilter;


/**
 * @author avaco
 *
 */
@Deprecated
public abstract class AbstractConvertService<DTO extends Entity<ID>, ID extends Serializable, E extends Entity<ID>> implements NJService<ID, DTO>{

	protected NJService<ID, E> service;
	protected NJRepository<ID, E> repository;
	
	public DTO save(DTO dto) {
		if(this.service == null) {
			return convertToDto(this.repository.save(convertToEntity(newEntity(), dto)));
		}
		return convertToDto(this.service.save(convertToEntity(newEntity(), dto)));
	}

	public DTO update(DTO dto) {
		if(this.service == null) {
			E entity = this.repository.findOne(dto.getId());
			return convertToDto(this.repository.save(convertToEntity(entity, dto)));
		}
		E entity = this.service.get(dto.getId());
		return convertToDto(this.service.update(convertToEntity(entity, dto)));
	}

	public List<DTO> save(Collection<DTO> dtos) {
		if(this.service == null) {
			final List<DTO> result = new ArrayList<DTO>();
			convertToEntities(dtos).stream().forEach(e -> {
				result.add(convertToDto(this.repository.save(e)));	
			});
			return result;
		}
		return convertToDtos(this.service.save(convertToEntities(dtos)));
	}

	public DTO get(ID id) {
		if(this.service == null) {
			return convertToDto(this.repository.findOne(id));
		}
		return convertToDto(this.service.get(id));
	}

	public List<DTO> list() {
		if(this.service == null) {
			return convertToDtos(this.repository.findAll());
		}
		return convertToDtos(this.service.list());
	}

	public void remove(ID id) {
		if(this.service == null) {
			this.repository.delete(id);
		}else {			
			this.service.remove(id);
		}
	}

	public int listCount(AbstractFilter abstractFilter) {
		if(this.service == null) {
			return this.repository.listCount(abstractFilter);
		}
		return this.service.listCount(abstractFilter);
	}

	public List<DTO> listFilter(AbstractFilter abstractFilter) {
		if(this.service == null) {
			return convertToDtos(this.repository.listFilter(abstractFilter));
		}
		return convertToDtos(this.service.listFilter(abstractFilter));
	}

	public List<DTO> listPattern(String field, String pattern) {
		if(this.service == null) {
			return convertToDtos(this.repository.listPattern(field, pattern));
		}
		return convertToDtos(this.service.listPattern(field, pattern));
	}

	abstract protected E newEntity();
	abstract protected E convertToEntity(E entity, DTO dto);
	abstract protected DTO convertToDto(E entity);
	
	public List<E> convertToEntities(Collection<DTO> dtos){
		List<E> entities = new ArrayList<E>();
		for (DTO dto : dtos) {
			entities.add(convertToEntity(newEntity(),dto));
		}
		return entities;
	}
	

	public List<DTO> convertToDtos(Collection<E> entities){
		List<DTO> dtos = new ArrayList<DTO>();
		for (E entity : entities) {
			dtos.add(convertToDto(entity));
		}
		return dtos;
	}	
}
