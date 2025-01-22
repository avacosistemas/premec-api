package ar.com.avaco.ws.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ar.com.avaco.commons.domain.GrupoTipoActividad;
import ar.com.avaco.commons.domain.ItemChecklistGrupo;
import ar.com.avaco.commons.service.GrupoTipoActividadService;
import ar.com.avaco.commons.service.ItemCheckListGrupoService;
import ar.com.avaco.ws.dto.GrupoTipoActividadDTO;
import ar.com.avaco.ws.dto.ItemChecklistGrupoDTO;
import ar.com.avaco.ws.rest.service.CRUDEPBaseService;
import ar.com.avaco.ws.service.GrupoTipoActividadEPService;

@Service("grupoTipoActividadEPService")
public class GrupoTipoActividadEPServiceImpl
		extends CRUDEPBaseService<Long, GrupoTipoActividadDTO, GrupoTipoActividad, GrupoTipoActividadService>
		implements GrupoTipoActividadEPService {

	@Override
	protected GrupoTipoActividad convertToEntity(GrupoTipoActividadDTO dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected GrupoTipoActividadDTO convertToDto(GrupoTipoActividad entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void setService(GrupoTipoActividadService service) {
		this.service = service;
	}

	

}
