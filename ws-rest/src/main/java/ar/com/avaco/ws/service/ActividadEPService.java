package ar.com.avaco.ws.service;

import java.util.List;

import ar.com.avaco.ws.dto.ActividadReporteDTO;
import ar.com.avaco.ws.dto.ActividadTarjetaDTO;

public interface ActividadEPService {

	List<ActividadTarjetaDTO> getActividades(String fecha, String usuarname) throws Exception;

	List<ActividadReporteDTO> getActividadesReporte() throws Exception;
}
