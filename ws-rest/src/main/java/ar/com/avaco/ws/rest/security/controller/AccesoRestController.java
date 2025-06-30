package ar.com.avaco.ws.rest.security.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.com.avaco.commons.exception.BusinessException;
import ar.com.avaco.ws.rest.controller.AbstractDTORestController;
import ar.com.avaco.ws.rest.dto.JSONResponse;
import ar.com.avaco.ws.rest.security.dto.AccesoDTO;
import ar.com.avaco.ws.rest.security.service.AccesoEPService;

@RestController
public class AccesoRestController extends AbstractDTORestController<AccesoDTO, Long, AccesoEPService> {

	@RequestMapping(value = "/acceso", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> list(@RequestParam Long idUsuario) {
		List<AccesoDTO> list = this.service.list(idUsuario);
		JSONResponse response = getResponseOK(list);
		return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/acceso/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> get(@PathVariable("id") Long id) throws BusinessException {
		return super.get(id);
	}

	@RequestMapping(value = "/acceso", method = RequestMethod.POST)
	public ResponseEntity<JSONResponse> create(@RequestBody AccesoDTO acceso) throws BusinessException {
		return super.create(acceso);
	}

	@RequestMapping(value = "/acceso", method = RequestMethod.PUT)
	public ResponseEntity<JSONResponse> update(@RequestBody AccesoDTO acceso) throws BusinessException {
		this.service.delete(acceso.getId(), acceso.getIdUsuario());
		JSONResponse response = getResponseOK(null);
		return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/acceso/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<JSONResponse> delete(@PathVariable("id") Long id) throws BusinessException {
		return super.delete(id);
	}

	@Override
	@Resource(name = "accesoEPService")
	public void setService(AccesoEPService service) {
		this.service = service;
	}

}