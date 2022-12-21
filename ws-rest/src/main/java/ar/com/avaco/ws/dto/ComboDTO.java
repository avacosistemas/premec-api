package ar.com.avaco.ws.dto;

public class ComboDTO {

	private String nombre;
	private String codigo;

	public ComboDTO() {
	}
	
	public ComboDTO(String nombre, String codigo) {
		super();
		this.nombre = nombre;
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

}
