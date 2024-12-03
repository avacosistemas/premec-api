package ar.com.avaco.ws.service;

import ar.com.avaco.ws.dto.FormularioDTO;

public interface FormularioEPService {

	void enviarFormulario(FormularioDTO formulario, String usuarioSAP) throws Exception;

	void grabarFormulario(FormularioDTO formularioDTO, String usuariosap) throws Exception;

	void enviarFormulariosFromFiles() throws Exception;

	void quitarArchivoColaEnvio(long actividadId) throws Exception;

	void validarHorasMaquina(Long parentObjectId, int horasMaquina) throws Exception;
}
