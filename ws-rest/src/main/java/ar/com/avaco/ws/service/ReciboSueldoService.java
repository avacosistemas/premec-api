package ar.com.avaco.ws.service;

import java.io.IOException;
import java.util.List;

import ar.com.avaco.ws.dto.timesheet.LoteRecibosSueldoDTO;
import ar.com.avaco.ws.dto.timesheet.ReciboSueldoDTO;
import ar.com.avaco.ws.dto.timesheet.RegistroReciboPorUsuarioDTO;

public interface ReciboSueldoService {

	List<ReciboSueldoDTO> procesarRecibos(String tipo) throws IOException;

	void rechazarRecibos(LoteRecibosSueldoDTO lote);

	void aprobarRecibos(LoteRecibosSueldoDTO lote);

	List<RegistroReciboPorUsuarioDTO> listarRecibosPorUsuario();

	byte[] obtenerReciboPDF(RegistroReciboPorUsuarioDTO recibo) throws IOException;

}
