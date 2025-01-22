package ar.com.avaco.ws.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

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
		return null;
	}

	@Override
	protected ItemChecklistGrupoDTO convertToDto(ItemChecklistGrupo entity) {
		return null;
	}

	@Override
	@Resource(name = "itemChecklistGrupoService")
	protected void setService(ItemCheckListGrupoService service) {
		this.service = service;
	}

}
