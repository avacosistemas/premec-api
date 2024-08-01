package ar.com.avaco.ws.service;

import java.util.List;

import ar.com.avaco.ws.dto.ActividadReporteDTO;
import ar.com.avaco.ws.dto.ActividadTarjetaDTO;
import ar.com.avaco.ws.dto.RegistroMonitorDTO;

public interface ActividadEPService {

	List<ActividadTarjetaDTO> getActividades(String fecha, String usuarname) throws Exception;

	List<ActividadReporteDTO> getActividadesReporte() throws Exception;

	void marcarEnviado(Long idActividad) throws Exception;

	List<RegistroMonitorDTO> getActividadesMonitor(String skip) throws Exception;
}
