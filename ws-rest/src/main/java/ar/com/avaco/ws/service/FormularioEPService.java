package ar.com.avaco.ws.service;

import ar.com.avaco.ws.dto.FormularioDTO;


public interface FormularioEPService {
	
	public void enviarFormulario (FormularioDTO formulario, String usuarioSAP) throws Exception;
}
