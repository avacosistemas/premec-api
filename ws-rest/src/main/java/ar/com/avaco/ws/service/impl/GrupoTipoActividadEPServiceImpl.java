package ar.com.avaco.ws.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ar.com.avaco.commons.domain.GrupoTipoActividad;
import ar.com.avaco.commons.domain.TipoActividad;
import ar.com.avaco.commons.service.GrupoTipoActividadService;
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
		GrupoTipoActividad grupo = new GrupoTipoActividad();
		grupo.setId(dto.getId());
		grupo.setOrden(dto.getOrden());
		grupo.setTipo(TipoActividad.valueOf(dto.getTipo()));
		grupo.setTitulo(dto.getTitulo());
		return grupo;
	}

	@Override
	protected GrupoTipoActividadDTO convertToDto(GrupoTipoActividad entity) {
		GrupoTipoActividadDTO dto = new GrupoTipoActividadDTO();
		dto.setId(entity.getId());
		dto.setOrden(entity.getOrden());
		dto.setTipo(entity.getTipo().name());
		dto.setTitulo(entity.getTitulo());

		entity.getItems().forEach(x -> {
			ItemChecklistGrupoDTO gdto = new ItemChecklistGrupoDTO();
			gdto.setId(x.getId());
			gdto.setIdGrupo(x.getGrupo().getId());
			gdto.setNombre(x.getNombre());
			gdto.setOrden(x.getOrden());
			dto.getItems().add(gdto);
		});

		return dto;

	}

	@Override
	@Resource(name = "grupoTipoActividadService")
	protected void setService(GrupoTipoActividadService service) {
		this.service = service;
	}

}
