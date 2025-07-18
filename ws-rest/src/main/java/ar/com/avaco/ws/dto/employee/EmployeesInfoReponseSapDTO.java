package ar.com.avaco.ws.dto.employee;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeesInfoReponseSapDTO {

	@JsonProperty("LastName")
	private String lastName;

	@JsonProperty("FirstName")
	private String firstName;

	@JsonProperty("U_serviciotecnico")
	private String servicioTecnico;

	@JsonProperty("U_objetivo")
	private String objetivo;

	@JsonProperty("U_horainicio")
	private String horaInicio;

	@JsonProperty("U_horafin")
	private String horaFin;

	public String getServicioTecnico() {
		return servicioTecnico;
	}

	public void setServicioTecnico(String servicioTecnico) {
		this.servicioTecnico = servicioTecnico;
	}

	public String getObjetivo() {
		return objetivo;
	}

	public void setObjetivo(String objetivo) {
		this.objetivo = objetivo;
	}

	public String getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(String horaInicio) {
		this.horaInicio = horaInicio;
	}

	public String getHoraFin() {
		return horaFin;
	}

	public void setHoraFin(String horaFin) {
		this.horaFin = horaFin;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

}
