package ar.com.avaco.ws.service;

import java.io.IOException;
import java.util.List;

import ar.com.avaco.ws.dto.actividad.ActividadEmpleadoFechaHoras;
import ar.com.avaco.ws.rest.security.dto.EmpleadoFichados;

public interface NovedadesFichadoService {

	List<EmpleadoFichados> parsearExcelNovedadesFichado(byte[] archivoBytes) throws IOException;

	void enviarFichados(List<EmpleadoFichados> registros);

}
