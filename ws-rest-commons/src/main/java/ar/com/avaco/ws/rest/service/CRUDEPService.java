package ar.com.avaco.ws.rest.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import ar.com.avaco.arc.core.domain.filter.AbstractFilter;
import ar.com.avaco.commons.exception.BusinessException;
import ar.com.avaco.ws.rest.dto.DTOEntity;

public interface CRUDEPService<ID extends Serializable, DTO extends DTOEntity<ID>> {

	DTO save(DTO entity) throws BusinessException;

	DTO update(DTO entity) throws BusinessException ;

	List<DTO> save(Collection<DTO> entities);

	DTO get(ID id);

	List<DTO> list();

	void remove(ID id) throws BusinessException;

	int listCount(AbstractFilter abstractFilter);

	List<DTO> listFilter(AbstractFilter abstractFilter);

	List<DTO> listPattern(String field, String pattern);

}
