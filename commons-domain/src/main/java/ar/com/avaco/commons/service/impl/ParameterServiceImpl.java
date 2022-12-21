/**
 * 
 */
package ar.com.avaco.commons.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.com.avaco.commons.domain.Parameter;
import ar.com.avaco.commons.exception.BusinessException;
import ar.com.avaco.commons.repository.ParameterRepository;
import ar.com.avaco.commons.service.ParameterService;
import ar.com.avaco.arc.core.component.bean.service.NJBaseService;

/**
 * @author avaco
 */

@Transactional
@Service("parameterService")
public class ParameterServiceImpl extends NJBaseService<Integer, Parameter, ParameterRepository> implements ParameterService{

	
	@Resource(name = "parameterRepository")
	public void setParameterRepository(ParameterRepository parameterRepository) {
		repository = parameterRepository;
	}

	@Override
	public String findByKey(String key) throws BusinessException {
		Parameter result = getRepository().findByKeyEquals(key);
		if (result == null) {
			throw new BusinessException("Key: " + key + "not found in Parameter Table" );
		}
		return result.getValue();
	}
	
	@Override
	public Parameter save(Parameter entity) {
		return super.save(cleanDTO(entity));
	}
	
	@Override
	public Parameter update(Parameter entity) {
		return super.update(cleanDTO(entity));
	}
	
	private Parameter cleanDTO(Parameter dto) {
		Parameter entity = null;
		if(dto.getId() == null) {
			entity = new Parameter();
		}else {
			entity = super.get(dto.getId());
		}
		entity.setKey(dto.getKey());
		entity.setValue(dto.getValue());
		entity.setDescription(dto.getDescription());
		return entity;
	}

}
