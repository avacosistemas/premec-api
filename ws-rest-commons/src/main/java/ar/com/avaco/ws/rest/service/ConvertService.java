/**
 * 
 */
package ar.com.avaco.ws.rest.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import ar.com.avaco.arc.core.component.bean.service.NJService;
import ar.com.avaco.arc.core.domain.Entity;
import ar.com.avaco.arc.core.domain.filter.AbstractFilter;

/**
 * @author avaco
 * @deprecated Usar {@CRUDEPService}
 *
 */
@Deprecated
public interface ConvertService<DTO extends Entity<ID>, ID extends Serializable, E extends Entity<ID>> extends NJService<ID, DTO>{
	
	DTO save(DTO dto);
	DTO update(DTO dto);
	List<DTO> save(Collection<DTO> dtos);
	DTO get(ID id);
	List<DTO> list();
	void remove(ID id);
	int listCount(AbstractFilter abstractFilter);
	List<DTO> listFilter(AbstractFilter abstractFilter);
	List<DTO> listPattern(String field, String pattern);
	DTO convertToDto(E entity);
	E convertToEntity(E entity, DTO dto);
	
}
