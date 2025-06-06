package ar.com.avaco.ws.service;

import java.io.IOException;
import java.util.List;

import ar.com.avaco.ws.dto.timesheet.ReciboSueldoDTO;
import ar.com.avaco.ws.dto.timesheet.RegistroReciboPorUsuarioDTO;

public interface ReciboSueldoService {

	void rechazarRecibos(List<ReciboSueldoDTO> lote);

	void aprobarRecibos(List<ReciboSueldoDTO> lote);

	List<RegistroReciboPorUsuarioDTO> listarRecibosPorUsuario();

	byte[] obtenerReciboPDF(RegistroReciboPorUsuarioDTO recibo) throws IOException;

	List<ReciboSueldoDTO> procesarRecibos(String tipo, byte[] archivo);

}
