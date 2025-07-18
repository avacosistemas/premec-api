package ar.com.avaco.ws.rest.security.dto;

public class EmpleadoSap {

	private String username;

	private String nombre;

	private String apellido;

	private String usuarioSap;

	private Long legajo;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getUsuarioSap() {
		return usuarioSap;
	}

	public void setUsuarioSap(String usuarioSap) {
		this.usuarioSap = usuarioSap;
	}

	public Long getLegajo() {
		return legajo;
	}

	public void setLegajo(Long legajo) {
		this.legajo = legajo;
	}

}
