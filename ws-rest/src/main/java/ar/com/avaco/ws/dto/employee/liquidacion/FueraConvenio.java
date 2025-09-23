package ar.com.avaco.ws.dto.employee.liquidacion;

public class FueraConvenio {
	private String legajo;
	private String apellido;
	private String nombre;
	private String cuil;
	private String categoria;
	private String feriado;
	private String adelantoSueldo;
	private String prestamos;
	private String gratificaciones;
	private String novedades;
	private String ausencias;

	public String getAusencias() {
		return ausencias;
	}

	public void setAusencias(String ausencias) {
		this.ausencias = ausencias;
	}

	public String getLegajo() {
		return legajo;
	}

	public void setLegajo(String legajo) {
		this.legajo = legajo;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCuil() {
		return cuil;
	}

	public void setCuil(String cuil) {
		this.cuil = cuil;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getFeriado() {
		return feriado;
	}

	public void setFeriado(String feriado) {
		this.feriado = feriado;
	}

	public String getAdelantoSueldo() {
		return adelantoSueldo;
	}

	public void setAdelantoSueldo(String adelantoSueldo) {
		this.adelantoSueldo = adelantoSueldo;
	}

	public String getPrestamos() {
		return prestamos;
	}

	public void setPrestamos(String prestamos) {
		this.prestamos = prestamos;
	}

	public String getGratificaciones() {
		return gratificaciones;
	}

	public void setGratificaciones(String gratificaciones) {
		this.gratificaciones = gratificaciones;
	}

	public String getNovedades() {
		return novedades;
	}

	public void setNovedades(String novedades) {
		this.novedades = novedades;
	}

}
