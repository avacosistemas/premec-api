/**
 * 
 */
package ar.com.avaco.commons.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.com.avaco.arc.core.component.bean.service.NJBaseService;
import ar.com.avaco.commons.domain.GrupoTipoActividad;
import ar.com.avaco.commons.repository.GrupoTipoActividadRepository;
import ar.com.avaco.commons.repository.ParameterRepository;
import ar.com.avaco.commons.service.GrupoTipoActividadService;

/**
 * @author avaco
 */

@Transactional
@Service("grupoTipoActividadService")
public class GrupoTipoActividadServiceImpl extends NJBaseService<Long, GrupoTipoActividad, GrupoTipoActividadRepository>
		implements GrupoTipoActividadService {

	@Resource(name = "grupoTipoActividadRepository")
	public void setRepository(GrupoTipoActividadRepository grupoTipoActividadRepository) {
		repository = grupoTipoActividadRepository;
	}

}
