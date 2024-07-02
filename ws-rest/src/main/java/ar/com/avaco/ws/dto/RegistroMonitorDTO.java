package ar.com.avaco.ws.dto;

public class RegistroMonitorDTO {

	private String numero;
	private String empleado;
	private String cliente;
	private String tareasARealizar;
	private String estado;
	private String fecha;
	private String hora;

	public String getEmpleado() {
		return empleado;
	}

	public void setEmpleado(String empleado) {
		this.empleado = empleado;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public String getTareasARealizar() {
		return tareasARealizar;
	}

	public void setTareasARealizar(String tareasARealizar) {
		this.tareasARealizar = tareasARealizar;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

}