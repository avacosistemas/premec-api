/**
 * 
 */
package ar.com.avaco.ws.rest.security.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import ar.com.avaco.arc.sec.domain.Acceso;
import ar.com.avaco.arc.sec.domain.Perfil;
import ar.com.avaco.arc.sec.domain.Usuario;
import ar.com.avaco.arc.sec.service.PerfilService;
import ar.com.avaco.arc.sec.service.UsuarioService;
import ar.com.avaco.service.cliente.AccesoService;
import ar.com.avaco.ws.rest.security.dto.AccesoDTO;
import ar.com.avaco.ws.rest.security.service.AccesoEPService;
import ar.com.avaco.ws.rest.service.CRUDEPBaseService;

/**
 * @author avaco
 *
 */
@Transactional
@Service("accesoEPService")
public class AccesoEPServiceImpl extends CRUDEPBaseService<Long, AccesoDTO, Acceso, AccesoService>
		implements AccesoEPService {

	private PerfilService perfilService;

	private UsuarioService usuarioService;

	@Override
	protected Acceso convertToEntity(AccesoDTO dto) {
		Acceso a = new Acceso();
		a.setId(dto.getId());
		a.setPerfil(perfilService.get(dto.getIdGrupo()));
		a.setUsuario(usuarioService.get(dto.getIdUsuario()));
		return a;
	}

	@Override
	protected AccesoDTO convertToDto(Acceso entity) {
		AccesoDTO dto = new AccesoDTO();
		dto.setId(entity.getPerfil().getId());
		dto.setIdUsuario(entity.getUsuario().getId());
		dto.setPerfilNombre(entity.getPerfil().getNombre());
		return dto;
	}

	@Override
	@Resource(name = "accesoService")
	protected void setService(AccesoService service) {
		this.service = service;
	}

	@Resource(name = "perfilService")
	public void setPerfilService(PerfilService perfilService) {
		this.perfilService = perfilService;
	}

	@Resource(name = "usuarioService")
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	@Override
	public List<AccesoDTO> list(Long usuarioId) {
		List<Acceso> list = this.service.list(usuarioId);
		List<AccesoDTO> dtos = new ArrayList<AccesoDTO>();
		list.forEach(a -> dtos.add(convertToDto(a)));
		return dtos;
	}

	@Override
	public void delete(Long id, Long idUsuario) {
		Usuario usuario = this.usuarioService.get(idUsuario);
		Perfil perfil = this.perfilService.get(id);
		usuario.getAccesos().remove(perfil);
		this.usuarioService.update(usuario);
	}

}
