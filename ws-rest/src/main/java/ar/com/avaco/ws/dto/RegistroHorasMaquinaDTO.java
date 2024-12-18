package ar.com.avaco.ws.dto;

import java.util.Date;

import ar.com.avaco.utils.DateUtils;

public class RegistroHorasMaquinaDTO {

	private Long serviceCallId;

	private Long idActividad;

	private Date fecha;

	private Double horasMaquina;

	private Double promedio;

	private String promedioString;

	private Long contractId;

	private Integer horasContratadas;

	public Long getServiceCallId() {
		return serviceCallId;
	}

	public void setServiceCallId(Long serviceCallId) {
		this.serviceCallId = serviceCallId;
	}

	public Long getIdActividad() {
		return idActividad;
	}

	public void setIdActividad(Long idActividad) {
		this.idActividad = idActividad;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getFechaString() {
		if (this.fecha != null)
		return DateUtils.toString(this.fecha, "dd/MM/yyyy hh:mm");
		return "Promedio Mensual";
	}

	public Double getPromedio() {
		return promedio;
	}

	public void setPromedio(Double promedio) {
		this.promedio = promedio;
	}

	public String getPromedioString() {
		return promedioString;
	}

	public void setPromedioString(String promedioString) {
		this.promedioString = promedioString;
	}

	public Long getContractId() {
		return contractId;
	}

	public void setContractId(Long contractId) {
		this.contractId = contractId;
	}

	public Integer getHorasContratadas() {
		return horasContratadas;
	}

	public void setHorasContratadas(Integer horasContratadas) {
		this.horasContratadas = horasContratadas;
	}

	public Double getHorasMaquina() {
		return horasMaquina;
	}

	public void setHorasMaquina(Double horasMaquina) {
		this.horasMaquina = horasMaquina;
	}

}
