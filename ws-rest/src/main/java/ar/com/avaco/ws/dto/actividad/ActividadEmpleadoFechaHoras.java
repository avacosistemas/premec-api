package ar.com.avaco.ws.dto.actividad;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ActividadEmpleadoFechaHoras {

	@JsonProperty(value = "HandledByEmployee")
	private Long handledByEmployee;

	@JsonProperty(value = "StartDate")
	private String startDate;

	@JsonProperty(value = "HorasTotales")
	private Double horasTotales;

	public Long getHandledByEmployee() {
		return handledByEmployee;
	}

	public void setHandledByEmployee(Long handledByEmployee) {
		this.handledByEmployee = handledByEmployee;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public Double getHorasTotales() {
		return horasTotales;
	}

	public void setHorasTotales(Double horasTotales) {
		this.horasTotales = horasTotales;
	}

}
