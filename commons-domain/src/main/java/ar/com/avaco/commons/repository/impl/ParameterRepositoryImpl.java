/**
 * 
 */
package ar.com.avaco.commons.repository.impl;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import ar.com.avaco.commons.domain.Parameter;
import ar.com.avaco.commons.repository.ParameterRepositoryCustom;
import ar.com.avaco.arc.core.component.bean.repository.NJBaseRepository;

/**
 * 
 *
 */
@Repository("parameterRepository")
public class ParameterRepositoryImpl extends NJBaseRepository<Integer, Parameter> implements ParameterRepositoryCustom {

	public ParameterRepositoryImpl(EntityManager entityManager) {
		super(Parameter.class, entityManager);
	}

}

