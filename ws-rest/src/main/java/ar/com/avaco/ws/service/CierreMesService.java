package ar.com.avaco.ws.service;

import java.util.List;

import ar.com.avaco.ws.dto.actividad.RegistroPreviewEmpleadoMensualDTO;

public interface CierreMesService {

	List<RegistroPreviewEmpleadoMensualDTO> getRegistrosCierre(String mes, String anio);

	void cerrarMes(List<RegistroPreviewEmpleadoMensualDTO> cierre, String anio, String mes);

}
