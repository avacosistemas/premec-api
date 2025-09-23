package ar.com.avaco.ws.service;

import ar.com.avaco.ws.dto.employee.liquidacion.PlantillaData;

public interface NovedadesContadorService {

	PlantillaData getRegistrosCierre(String mes, String anio);

}
