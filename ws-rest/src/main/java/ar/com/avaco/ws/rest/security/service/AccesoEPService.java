/**
 * 
 */
package ar.com.avaco.ws.rest.security.service;

import java.util.List;

import ar.com.avaco.ws.rest.security.dto.AccesoDTO;
import ar.com.avaco.ws.rest.service.CRUDEPService;

/**
 * @author avaco
 *
 */
public interface AccesoEPService extends CRUDEPService<Long, AccesoDTO> {

	List<AccesoDTO> list(Long usuarioId);

	void delete(Long id, Long idUsuario);

}
