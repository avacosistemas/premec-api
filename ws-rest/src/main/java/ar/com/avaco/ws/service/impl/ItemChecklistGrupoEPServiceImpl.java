package ar.com.avaco.ws.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ar.com.avaco.commons.domain.GrupoTipoActividad;
import ar.com.avaco.commons.domain.ItemChecklistGrupo;
import ar.com.avaco.commons.service.ItemCheckListGrupoService;
import ar.com.avaco.ws.dto.ItemChecklistGrupoDTO;
import ar.com.avaco.ws.rest.service.CRUDEPBaseService;
import ar.com.avaco.ws.service.ItemChecklistGrupoEPService;

@Service("itemChecklistGrupoEPService")
public class ItemChecklistGrupoEPServiceImpl
		extends CRUDEPBaseService<Long, ItemChecklistGrupoDTO, ItemChecklistGrupo, ItemCheckListGrupoService>
		implements ItemChecklistGrupoEPService {

	@Override
	protected ItemChecklistGrupo convertToEntity(ItemChecklistGrupoDTO dto) {
		GrupoTipoActividad grupo = new GrupoTipoActividad();
		grupo.setId(dto.getId());
		ItemChecklistGrupo item = new ItemChecklistGrupo();
		item.setGrupo(grupo);
		item.setId(dto.getId());
		item.setNombre(dto.getNombre());
		item.setOrden(dto.getOrden());
		return item;
	}

	@Override
	public ItemChecklistGrupoDTO convertToDto(ItemChecklistGrupo entity) {
		ItemChecklistGrupoDTO dto = new ItemChecklistGrupoDTO();
		dto.setId(entity.getId());
		dto.setIdGrupo(entity.getGrupo().getId());
		dto.setNombre(entity.getNombre());
		dto.setOrden(entity.getOrden());
		return dto;
	}

	@Override
	@Resource(name = "itemChecklistGrupoService")
	protected void setService(ItemCheckListGrupoService service) {
		this.service = service;
	}

}
