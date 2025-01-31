package ar.com.avaco.ws.rest.controller;

import javax.annotation.Resource;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.com.avaco.commons.exception.BusinessException;
import ar.com.avaco.ws.dto.GrupoTipoActividadDTO;
import ar.com.avaco.ws.rest.dto.JSONResponse;
import ar.com.avaco.ws.service.GrupoTipoActividadEPService;

@RestController
public class GrupoTipoActividadRestController
		extends AbstractDTORestController<GrupoTipoActividadDTO, Long, GrupoTipoActividadEPService> {

	@Override
	@RequestMapping(value = "/grupoTipoActividad", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> list() {
		// TODO Auto-generated method stub
		return super.list();
	}

	@Override
	@RequestMapping(value = "/grupoTipoActividad", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> create(@RequestBody  GrupoTipoActividadDTO dto) throws BusinessException {
		// TODO Auto-generated method stub
		return super.create(dto);
	}

	@Override
	@RequestMapping(value = "/grupoTipoActividad", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> update(Long id, @RequestBody GrupoTipoActividadDTO dto) throws BusinessException {
		// TODO Auto-generated method stub
		return super.update(dto.getId(), dto);
	}

	@Override
	@RequestMapping(value = "/grupoTipoActividad/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> delete(@PathVariable Long id) throws BusinessException {
		// TODO Auto-generated method stub
		return super.delete(id);
	}

	@Override
	@Resource(name = "grupoTipoActividadEPService")
	public void setService(GrupoTipoActividadEPService service) {
		this.service = service;
	}

}
