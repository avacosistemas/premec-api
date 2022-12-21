/**
 * 
 */
package ar.com.avaco.ws.rest.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ar.com.avaco.arc.core.component.bean.service.NJService;
import ar.com.avaco.arc.core.domain.Entity;
import ar.com.avaco.arc.core.domain.filter.AbstractFilter;
import ar.com.avaco.ws.rest.dto.DTOEntity;

/**
 * Intento de mejora.
 * @author beto
 *
 */
public abstract class AbstractDTOConvertService<DTO extends DTOEntity<ID>, ID extends Serializable, E extends Entity<ID>> {

	protected NJService<ID, E> service;
	
	public DTO save(DTO dto) {
		return convertToDto(this.service.save(convertToEntity(newEntity(), dto)));
	}

	public DTO update(DTO dto) {
		E entity = this.service.get(dto.getId());
		return convertToDto(this.service.update(convertToEntity(entity, dto)));
	}

	public List<DTO> save(Collection<DTO> dtos) {
		return convertToDtos(this.service.save(convertToEntities(dtos)));
	}

	public DTO get(ID id) {
		return convertToDto(this.service.get(id));
	}

	public List<DTO> list() {
		return convertToDtos(this.service.list());
	}

	public void remove(ID id) {
		this.service.remove(id);
	}

	public int listCount(AbstractFilter abstractFilter) {
		return this.service.listCount(abstractFilter);
	}

	public List<DTO> listFilter(AbstractFilter abstractFilter) {
		return convertToDtos(this.service.listFilter(abstractFilter));
	}

	public List<DTO> listPattern(String field, String pattern) {
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
