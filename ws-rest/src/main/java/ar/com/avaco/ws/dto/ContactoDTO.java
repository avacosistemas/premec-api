package ar.com.avaco.ws.dto;

import ar.com.avaco.entities.cliente.TipoContacto;
import ar.com.avaco.ws.rest.dto.DTOEntity;

public class ContactoDTO extends DTOEntity<Long> {

	private Long id;
	private Long idCliente;
	private TipoContacto tipo;
	private String nombre;
	private String email;
	private String telefono;

	public ContactoDTO() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public TipoContacto getTipo() {
		return tipo;
	}

	public void setTipo(TipoContacto tipo) {
		this.tipo = tipo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

}
