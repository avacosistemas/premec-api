package ar.com.avaco.ws.dto.actividad;

public class RegistroEmpleadoCierreMensualDTO {

	private Integer legajo;

	private Long usuarioSap;

	private String nombre;

	private Integer cantidadActividades;

	private Integer cantidadActividadesMB;

	private Integer cantidadActividadesB;

	private Integer cantidadActividadesR;

	private Integer cantidadActividadesM;

	private Double porcentajeValoracion;

	private Integer objetivoActividades;

	private Double cumplimientoObjetivoActividades;

	private Double salario;

	// FIXME falta completar
	private Double porcentajeEfectividad;

	public Integer getLegajo() {
		return legajo;
	}

	public void setLegajo(Integer legajo) {
		this.legajo = legajo;
	}

	public Long getUsuarioSap() {
		return usuarioSap;
	}

	public void setUsuarioSap(Long usuarioSap) {
		this.usuarioSap = usuarioSap;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getCantidadActividades() {
		return cantidadActividades;
	}

	public void setCantidadActividades(Integer cantidadActividades) {
		this.cantidadActividades = cantidadActividades;
	}

	public Integer getCantidadActividadesMB() {
		return cantidadActividadesMB;
	}

	public void setCantidadActividadesMB(Integer cantidadActividadesMB) {
		this.cantidadActividadesMB = cantidadActividadesMB;
	}

	public Integer getCantidadActividadesB() {
		return cantidadActividadesB;
	}

	public void setCantidadActividadesB(Integer cantidadActividadesB) {
		this.cantidadActividadesB = cantidadActividadesB;
	}

	public Integer getCantidadActividadesR() {
		return cantidadActividadesR;
	}

	public void setCantidadActividadesR(Integer cantidadActividadesR) {
		this.cantidadActividadesR = cantidadActividadesR;
	}

	public Integer getCantidadActividadesM() {
		return cantidadActividadesM;
	}

	public void setCantidadActividadesM(Integer cantidadActividadesM) {
		this.cantidadActividadesM = cantidadActividadesM;
	}

	public Double getPorcentajeValoracion() {
		return porcentajeValoracion;
	}

	public void setPorcentajeValoracion(Double porcentajeValoracion) {
		this.porcentajeValoracion = porcentajeValoracion;
	}

	public Integer getObjetivoActividades() {
		return objetivoActividades;
	}

	public void setObjetivoActividades(Integer objetivoActividades) {
		this.objetivoActividades = objetivoActividades;
	}

	public Double getCumplimientoObjetivoActividades() {
		return cumplimientoObjetivoActividades;
	}

	public void setCumplimientoObjetivoActividades(Double cumplimientoObjetivoActividades) {
		this.cumplimientoObjetivoActividades = cumplimientoObjetivoActividades;
	}

	public Double getSalario() {
		return salario;
	}

	public void setSalario(Double salario) {
		this.salario = salario;
	}

	public Double getPorcentajeEfectividad() {
		return porcentajeEfectividad;
	}

	public void setPorcentajeEfectividad(Double porcentajeEfectividad) {
		this.porcentajeEfectividad = porcentajeEfectividad;
	}

}
