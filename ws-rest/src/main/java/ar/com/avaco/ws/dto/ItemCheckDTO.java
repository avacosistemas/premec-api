package ar.com.avaco.ws.dto;

public class ItemCheckDTO {

	private String titulo;
	private String nombre;
	private String estado;
	private String observaciones;

	private transient boolean ok;
	private transient boolean noOK;
	private transient boolean na;

	public ItemCheckDTO() {
	}

	public ItemCheckDTO(String titulo, String nombre, String estado, String observaciones) {
		super();
		this.titulo = titulo;
		this.nombre = nombre;
		this.estado = estado;
		this.observaciones = observaciones;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public boolean isOk() {
		return this.estado.equals("Ok");
	}

	public boolean isNa() {
		return this.estado.equals("NA");
	}

	public boolean isNoOK() {
		return this.estado.equals("No Ok");
	}

}
