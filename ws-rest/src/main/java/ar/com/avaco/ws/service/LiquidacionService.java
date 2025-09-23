package ar.com.avaco.ws.service;

import java.io.IOException;
import java.util.List;

import ar.com.avaco.ws.dto.employee.liquidacion.FueraConvenio;
import ar.com.avaco.ws.dto.employee.liquidacion.Jornal;
import ar.com.avaco.ws.dto.employee.liquidacion.Mensual;

public interface LiquidacionService {

	void generarExcel(String periodo, List<FueraConvenio> fueraConvenio, List<Mensual> mensuales, List<Jornal> jornales)
			throws IOException;

}
