package ar.com.avaco.ws.rest.security.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.com.avaco.arc.sec.domain.Permiso;
import ar.com.avaco.arc.sec.service.PerfilService;
import ar.com.avaco.ws.rest.dto.JSONResponse;
import ar.com.avaco.ws.rest.security.dto.GrupoPermisoDTO;

@RestController
public class GrupoPermisoRestController {

	private PerfilService perfilService;

	@RequestMapping(value = "/grupoPermiso", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> list(@RequestParam Long idGrupo) {
		List<Permiso> listPermisosByPerfil = this.perfilService.listPermisosByPerfil(idGrupo);
		JSONResponse response = new JSONResponse();
		response.setData(listPermisosByPerfil);
		return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/grupoPermiso", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> insertar(@RequestBody GrupoPermisoDTO dto) {
		this.perfilService.agregarPermisoAPerfil(dto.getIdGrupo(), dto.getIdPermiso());
		JSONResponse response = new JSONResponse();
		return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/grupoPermiso", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> quitar(@RequestBody GrupoPermisoDTO dto) {
		this.perfilService.quitarPermisoAPerfil(dto.getIdGrupo(), dto.getId());
		JSONResponse response = new JSONResponse();
		return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
	}

	@Resource(name = "perfilService")
	public void setPerfilService(PerfilService perfilService) {
		this.perfilService = perfilService;
	}

}