package ar.com.avaco.ws.rest.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.com.avaco.arc.core.domain.filter.AbstractFilter;
import ar.com.avaco.arc.sec.domain.Usuario;
import ar.com.avaco.commons.exception.BusinessException;
import ar.com.avaco.ws.dto.ItemChecklistGrupoDTO;
import ar.com.avaco.ws.dto.RepuestoDepositoDTO;
import ar.com.avaco.ws.rest.controller.AbstractDTORestController;
import ar.com.avaco.ws.rest.dto.JSONResponse;
import ar.com.avaco.ws.service.ItemChecklistGrupoEPService;
import ar.com.avaco.ws.service.filter.ItemChecklistGrupoFilter;

@RestController
public class ItemChecklistGrupoRestController
		extends AbstractDTORestController<ItemChecklistGrupoDTO, Long, ItemChecklistGrupoEPService> {

	@RequestMapping(value = "/itemChecklistGrupo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> list(ItemChecklistGrupoFilter filter) {

		JSONResponse response = new JSONResponse();

		try {
			response.setData(this.service.listFilter(filter));
			response.setStatus(JSONResponse.OK);
		} catch (Exception e) {
			response.setStatus(JSONResponse.ERROR);
			response.setData(e.getLocalizedMessage());
			e.printStackTrace();
		}
		return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
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
		return super.update(dto.getId(), dto);
	}

	@RequestMapping(value = "/itemChecklistGrupo/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Override
	public ResponseEntity<JSONResponse> delete(@PathVariable Long id) throws BusinessException {
		// TODO Auto-generated method stub
		return super.delete(id);
	}

	@Override
	@Resource(name = "itemChecklistGrupoEPService")
	public void setService(ItemChecklistGrupoEPService service) {
		this.service = service;
	}

}
