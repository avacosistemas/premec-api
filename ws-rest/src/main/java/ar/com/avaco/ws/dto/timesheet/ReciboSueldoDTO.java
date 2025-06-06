package ar.com.avaco.ws.dto.timesheet;

import java.math.BigDecimal;

public class ReciboSueldoDTO {

	private int legajo;
	private String nombreCompleto;
	private String periodo;
	private BigDecimal neto;
	private String tipo;
	private String observaciones;
	private Boolean aprobado;

	public ReciboSueldoDTO() {
	}

	public ReciboSueldoDTO(int legajo, String nombreCompleto, String periodo, BigDecimal neto, String tipo) {
		super();
		this.legajo = legajo;
		this.nombreCompleto = nombreCompleto;
		this.periodo = periodo;
		this.neto = neto;
		this.tipo = tipo;
	}

	public int getLegajo() {
		return legajo;
	}

	public void setLegajo(int legajo) {
		this.legajo = legajo;
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	public BigDecimal getNeto() {
		return neto;
	}

	public void setNeto(BigDecimal neto) {
		this.neto = neto;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Boolean getAprobado() {
		return aprobado;
	}

	public void setAprobado(Boolean aprobado) {
		this.aprobado = aprobado;
	}

}
