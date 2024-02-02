package ar.com.avaco.ws.dto;

public class RepuestoDTO {

	String nroArticulo;
	String descripcion;
	Double cantidad;
	String nroSerie;

	public RepuestoDTO() {
	}

	public RepuestoDTO(String nroArticulo, String descripcion, Double cantidad, String nroSerie) {
		super();
		this.nroArticulo = nroArticulo;
		this.descripcion = descripcion;
		this.cantidad = cantidad;
		this.nroSerie = nroSerie;
	}

	public String getNroArticulo() {
		return nroArticulo;
	}

	public void setNroArticulo(String nroArticulo) {
		this.nroArticulo = nroArticulo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Double getCantidad() {
		return cantidad;
	}

	public void setCantidad(Double cantidad) {
		this.cantidad = cantidad;
	}

	public String getNroSerie() {
		return nroSerie;
	}

	public void setNroSerie(String nroSerie) {
		this.nroSerie = nroSerie;
	}

}
