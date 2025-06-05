package ar.com.avaco.ws.dto.timesheet;

import java.util.ArrayList;
import java.util.List;

public class LoteRecibosSueldoDTO {

	private List<ReciboSueldoDTO> recibos = new ArrayList<ReciboSueldoDTO>();

	public List<ReciboSueldoDTO> getRecibos() {
		return recibos;
	}

	public void setRecibos(List<ReciboSueldoDTO> recibos) {
		this.recibos = recibos;
	}

}
