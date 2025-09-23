package ar.com.avaco.ws.dto.timesheet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistroCierreDTO {

	@JsonProperty(value = "U_cumplimientoobjetivo")
	public String cumplimientoObjetivo;

	@JsonProperty(value = "U_viaticos")
	public String viaticos;

	@JsonProperty(value = "U_adelanto")
	public String adelanto;

	@JsonProperty(value = "U_prestamo")
	public String prestamo;

	@JsonProperty(value = "U_premio")
	public String premio;

	@JsonProperty(value = "U_porcentajevaloracion")
	public String porcentajeValoracion;

	@JsonProperty(value = "U_porcentajeefectividad")
	public String porcentajeEfectividad;

	@JsonProperty(value = "U_cantidadactividades")
	public String cantidadActividades;

	@JsonProperty(value = "U_salario")
	public String salario;

	public String getCumplimientoObjetivo() {
		return cumplimientoObjetivo;
	}

	public void setCumplimientoObjetivo(String cumplimientoObjetivo) {
		this.cumplimientoObjetivo = cumplimientoObjetivo;
	}

	public String getViaticos() {
		return viaticos;
	}

	public void setViaticos(String viaticos) {
		this.viaticos = viaticos;
	}

	public String getAdelanto() {
		return adelanto;
	}

	public void setAdelanto(String adelanto) {
		this.adelanto = adelanto;
	}

	public String getPrestamo() {
		return prestamo;
	}

	public void setPrestamo(String prestamo) {
		this.prestamo = prestamo;
	}

	public String getPremio() {
		return premio;
	}

	public void setPremio(String premio) {
		this.premio = premio;
	}

	public String getPorcentajeValoracion() {
		return porcentajeValoracion;
	}

	public void setPorcentajeValoracion(String porcentajeValoracion) {
		this.porcentajeValoracion = porcentajeValoracion;
	}

	public String getPorcentajeEfectividad() {
		return porcentajeEfectividad;
	}

	public void setPorcentajeEfectividad(String porcentajeEfectividad) {
		this.porcentajeEfectividad = porcentajeEfectividad;
	}

	public String getCantidadActividades() {
		return cantidadActividades;
	}

	public void setCantidadActividades(String cantidadActividades) {
		this.cantidadActividades = cantidadActividades;
	}

	public String getSalario() {
		return salario;
	}

	public void setSalario(String salario) {
		this.salario = salario;
	}

}
