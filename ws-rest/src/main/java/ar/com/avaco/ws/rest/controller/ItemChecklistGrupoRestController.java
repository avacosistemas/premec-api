package ar.com.avaco.ws.rest.controller;

import javax.annotation.Resource;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.com.avaco.commons.exception.BusinessException;
import ar.com.avaco.ws.dto.ItemChecklistGrupoDTO;
import ar.com.avaco.ws.rest.controller.AbstractDTORestController;
import ar.com.avaco.ws.rest.dto.JSONResponse;
import ar.com.avaco.ws.service.ItemChecklistGrupoEPService;

@RestController
public class ItemChecklistGrupoRestController
		extends AbstractDTORestController<ItemChecklistGrupoDTO, Long, ItemChecklistGrupoEPService> {

	@Override
	@RequestMapping(value = "/itemChecklistGrupo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> list() {
		// TODO Auto-generated method stub
		return super.list();
	}

	@Override
	@RequestMapping(value = "/itemChecklistGrupo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> create(@RequestBody ItemChecklistGrupoDTO dto) throws BusinessException {
		// TODO Auto-generated method stub
		return super.create(dto);
	}

	@Override
	@RequestMapping(value = "/itemChecklistGrupo", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> update(Long id, @RequestBody ItemChecklistGrupoDTO dto)
			throws BusinessException {
		// TODO Auto-generated method stub
		return super.update(id, dto);
	}

	@RequestMapping(value = "/itemChecklistGrupo", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Override
	public ResponseEntity<JSONResponse> delete(@RequestParam Long id) throws BusinessException {
		// TODO Auto-generated method stub
		return super.delete(id);
	}

	@Override
	@Resource(name = "itemChecklistGrupoEPService")
	public void setService(ItemChecklistGrupoEPService service) {
		this.service = service;
	}

}
