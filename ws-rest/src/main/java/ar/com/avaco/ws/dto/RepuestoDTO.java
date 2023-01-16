package ar.com.avaco.ws.dto;

public class RepuestoDTO {

	String numeroArticulo;
	String descripcion;
	int cantidad;
	String nroSerie;
	
	public String getNumeroArticulo() {
		return numeroArticulo;
	}
	public void setNumeroArticulo(String numeroArticulo) {
		this.numeroArticulo = numeroArticulo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public String getNroSerie() {
		return nroSerie;
	}
	public void setNroSerie(String nroSerie) {
		this.nroSerie = nroSerie;
	}
	
}
